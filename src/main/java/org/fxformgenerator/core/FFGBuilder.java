package org.fxformgenerator.core;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;

import javax.validation.ConstraintViolation;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by giovanni on 4/5/16.
 */
public class FFGBuilder {
    private FFGFieldsAssembler assembler = new FFGFieldsAssembler();

    /** POJO To edit in this form */
    private Object model;

    /** Custom labels to use in form fields */
    private Map<String, String> fieldLabels = new HashMap<>();

    /**
     * Stores a list with maps representing multiple options
     * for POJO properties
     */
    private Map<String, ObservableList<Object>> fieldsValues = new HashMap();

    /**
     * A list with custom editors for model properties
     */
    private Map<String, Node> fieldsEditors = new HashMap<>();

    /**
     * Contains each form's fields in corresponding order to be
     * displayed in form.
     */
    private List<String> orderedFields = new ArrayList<>();

    /** Name of fields that will be ignored on form creation */
    private List<String> excludedFields = new ArrayList<>();

    /** Stores each validation message */
    private Map<String, String> validationMessages = new HashMap<>();

    private boolean useValidation = false;

    /** If true, form is generated with labels instead of editor nodes */
    private boolean readOnlyMode = false;


    public FFGBuilder(Object model) {
        this.model = model;
    }


    ////////////////////////////////////////////////////////////////////////////
    // User customization methods

    /**
     * Assign a custom editor for a given model property. FXFormGenerator
     * only will render the node in the place for field editor.            <br/>
     * You're responsible of Node behavior and binding node value
     * changes to model property value.
     *
     * @param field  Field to edit in custom editor node
     * @param editor Node to render in place for field editor
     * @return A FFGBuilder with the assigned field editors
     */
    public FFGBuilder assignFieldEditor(String field, Node editor) {
        this.fieldsEditors.put(field, editor);
        return this;
    }

    /**
     * Assign a custom editor label to a field
     *
     * @param field Property name such as is declared in your POJO
     * @param fieldLabel The custom label you wish to show in form for this field
     *
     * @return
     */
    public FFGBuilder assignFieldLabel(String field, String fieldLabel) {
        fieldLabels.put(field, fieldLabel);
        return this;
    }

    /**
     * Assign custom labels for a bunch of fields
     * @param labels A {@link Map} with <fieldName, fieldLabel> entries
     *
     * @return
     */
    public FFGBuilder assignFieldLabels(Map<String, String> labels) {
        for (Map.Entry<String, String> entry : labels.entrySet()) {
            fieldLabels.put(entry.getKey(), entry.getValue());
        }

        return this;
    }

    public FFGBuilder enableReadOnlyMode() {
        this.readOnlyMode = true;
        return this;
    }

    /**
     * Enable model validation through Hibernate Validator.
     * On 'Show as dialog', the model will be validated when user clicks
     * on save button, if model does not pass validation the dialog will be
     * rendered with the validation error messages.
     *
     * @return A references to FFGBuilder with validation enabled
     */
    public FFGBuilder enableValidation() {
        this.useValidation = true;
        return this;
    }

    /**
     * Excludes a field from form.
     * FXFormGenerator will not generate form input for this field
     *
     * @param field Field's name to be excluded from generated form
     * @return
     */
    public FFGBuilder excludeField(String field) {
        if (!this.excludedFields.contains(field)) {
            this.excludedFields.add(field);
        }

        return this;
    }

    /**
     * Excludes a bunch of fields from form
     * FXFormGenerator will not generate form inputs for those fields.
     *
     * @param fields Field's names to be excluded from generated form
     * @return
     */
    public FFGBuilder excludeFields(String... fields) {
        for (String field : fields) {
            if (!this.excludedFields.contains(field)) {
                this.excludedFields.add(field);
            }
        }

        return this;
    }

    /**
     * Sets the order to display fields into form.  <br/>
     * NOTE: Fields not included in param <code>orderedFields</code> will be
     * added after ordered fields.
     *
     * @param orderedFields A list with field's names in corresponding sequence
     *                      to be displayed in form.
     * @return
     */
    public FFGBuilder assignFieldsOrder(String... orderedFields) {
        this.orderedFields.clear();
        this.orderedFields.addAll(Arrays.asList(orderedFields));

        return this;
    }

    /**
     * Assigns a list of possible options for a given field (Usually an object
     * type field) In this case, FFG will create a ChoiceBox for this property
     * with given options.
     *
     * @param field
     * @param options
     * @return
     */
    public FFGBuilder assignFieldOptions(String field, ObservableList<Object> options) {
        this.fieldsValues.put(field, options);
        return this;
    }

    public FFGBuilder assignFormLayout(int formLayout) {
        assembler.assignFormLayout(formLayout);
        return this;
    }


    ////////////////////////////////////////////////////////////////////////////
    // Builder methods

    public VBox build() {
        List<FFGInputGroup> inputGroups = new ArrayList<>();

        // Recover all properties to be added to form
        for (PropertyDescriptor pDesc : getFormProperties()) {
            FFGInputGroup ig = new FFGInputGroup(model, pDesc);

            if (fieldsEditors.containsKey(pDesc.getName())) {
                ig.setCustomEditor(fieldsEditors.get(pDesc.getName()));
            }

            if (fieldLabels.containsKey(pDesc.getName())) {
                ig.setEditorLBText(fieldLabels.get(pDesc.getName()));
            }

            if (fieldsValues.containsKey(pDesc.getName())) {
                ig.setFieldValues(fieldsValues.get(pDesc.getName()));
            }

            if (validationMessages.containsKey(pDesc.getName())) {
                ig.setValidationMessage(validationMessages.get(pDesc.getName()));
            }

            ig.setReadOnly(readOnlyMode);
            inputGroups.add(ig);
        }

        // Assembly all form nodes into a single VBox parent
        return assembler.assembly(inputGroups);
    }

    /**
     * If validation is enabled, check model for validation errors
     * and constructs form showing them.
     *
     * @return
     */
    public VBox buildWithValidationMessages() {
        if (useValidation) {
            validateModel();
            retrieveConstraintViolationMessages();
        }

        return build();
    }

    /**
     * Iterates over each property inside model object and check that:     <br/>
     * - Property is not included in excluded fields                       <br/>
     * - Property's accessors and setters methods are available.           <br/>
     * - ...                                                               <br/>
     *
     * @return Properties to be included into form in corresponding order.
     */
    private List<PropertyDescriptor> getFormProperties() {
        List<PropertyDescriptor> orderedFormProperties = new ArrayList<>();
        List<PropertyDescriptor> formProperties = new ArrayList<>();

        try {
            PropertyDescriptor[] propDescriptors = Introspector
                    .getBeanInfo(model.getClass(), Object.class)
                    .getPropertyDescriptors();

            for (PropertyDescriptor pDesc : propDescriptors) {
                if (!excludedFields.contains(pDesc.getName())) {
                    if (pDesc.getReadMethod() != null && pDesc.getWriteMethod() != null) {
                        formProperties.add(pDesc);
                    }
                    else {
                        System.err.println("Setter and getter methods not available");
                    }
                }
            }

            // Put form properties in corresponding order
            for (String fieldName : orderedFields) {
                orderedFormProperties.addAll(
                        formProperties
                                .stream()
                                .filter(pDesc -> fieldName.equals(pDesc.getName()))
                                .collect(Collectors.toList())
                );
            }

            // Add not ordered properties after ordered properties
            formProperties
                    .stream()
                    .filter(pDesc -> !orderedFormProperties.contains(pDesc))
                    .forEach(orderedFormProperties::add);
        }
        catch (IntrospectionException e) {
            System.err.println("Introspection error");
            System.err.println(e.getMessage());
        }

        return orderedFormProperties;
    }

    public void showAsDialog(Consumer consumer) {
        String dTitle = "Create new " + model.getClass().getName();
        this.showAsDialog(dTitle, consumer);
    }

    public void showAsDialog(String dTitle, Consumer consumer) {
        this.showAsDialog(dTitle, null, consumer);
    }

    public void showAsDialog(String dTitle, String dHeaderText, Consumer consumer) {
        this.showAsDialog(dTitle, dHeaderText, "Save", "Cancel", consumer);
    }

    /**
     * Build and shows the form as a {@link Dialog}
     *
     * @param dTitle Passed directly to {@link Dialog#setTitle(String)}
     * @param dHeaderText Passed directly to {@link Dialog#setHeaderText(String)}
     * @param okBtnText Text to put inside ok button
     * @param cancelBtnText Text to put inside cancel button
     * @param consumer Consumer to be called when ok button is pressed
     */
    public void showAsDialog(String dTitle, String dHeaderText, String okBtnText,
                             String cancelBtnText, Consumer consumer) {

        Dialog dialog = new Dialog();
        dialog.setTitle(dTitle);
        dialog.setHeaderText(dHeaderText);
        dialog.getDialogPane().setContent(this.build());

        if (this.readOnlyMode) {
            ButtonType okBtnType = new ButtonType(okBtnText, ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(okBtnType);
            dialog.showAndWait();
        }
        else {
            ButtonType okBtnType = new ButtonType(okBtnText, ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelBtnType = new ButtonType(cancelBtnText, ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(okBtnType, cancelBtnType);

            dialog.showAndWait().ifPresent(dialogBtn -> {
                if (dialogBtn == okBtnType) {
                    if (!this.useValidation || this.validateModel()) {
                        consumer.accept(dialogBtn);
                    }
                    else {
                        retrieveConstraintViolationMessages();
                        this.showAsDialog(dTitle, dHeaderText, okBtnText, cancelBtnText, consumer);
                    }
                }
            });
        }
    }

    private boolean validateModel() {
        return FFGModelValidator.isModelValid(model);
    }

    /**
     * Puts each validation messages into {@link #validationMessages}
     * using the property name as key and message as value.
     *
     * NOTE: Validation messages will appear into form on next rendering/showAsDialog
     * call.
     */
    private void retrieveConstraintViolationMessages() {
        validationMessages.clear();
        Set<ConstraintViolation<Object>> violations =
                FFGModelValidator.getViolatedConstraints(model);

        for (ConstraintViolation<Object> cViolation : violations) {
            String propertyName = cViolation.getPropertyPath().iterator().next().getName();
            String validationMessage = cViolation.getMessage();

            validationMessages.put(propertyName, validationMessage);
        }
    }

}
