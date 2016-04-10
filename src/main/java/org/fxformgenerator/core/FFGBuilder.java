package org.fxformgenerator.core;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by giovanni on 4/5/16.
 */
public class FFGBuilder {

    /** POJO To edit in this form */
    private Object model;

    /** Custom labels to use in form fields */
    private Map<String, String> fieldLabels = new HashMap<>();

    /**
     * Contains each form's fields in corresponding order to be
     * displayed in form.
     */
    private List<String> orderedFields = new ArrayList<>();

    /** Name of fields that will be ignored on form creation */
    private List<String> excludedFields = new ArrayList<>();

    private int formLayout = FFGLayout.MULTIPLE_COLUMNS;


    public FFGBuilder(Object model) {
        this.model = model;
    }


    ////////////////////////////////////////////////////////////////////////////
    // User customization methods

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
     * Sets the order to display fields into form.
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


    ////////////////////////////////////////////////////////////////////////////
    // Builder methods

    public VBox build() {
        VBox formContainer = new VBox(3);
        List<FFGInputGroup> inputGroups = new ArrayList<>();

        // Recover all properties to be added to form
        for (PropertyDescriptor pDesc : getFormProperties()) {
            if (fieldLabels.containsKey(pDesc.getName())) {
                inputGroups.add(new FFGInputGroup(
                        model,
                        fieldLabels.get(pDesc.getName()),
                        pDesc
                ));
            }
            else {
                inputGroups.add(new FFGInputGroup(model, pDesc));
            }
        }

        // Assembly all form nodes into a single VBox parent
        switch (this.formLayout) {
            case FFGLayout.MULTIPLE_COLUMNS:
                if (inputGroups.size() <= 6) {
                    for (int i = 0; i < inputGroups.size(); i+=2) {
                        VBox ig1 = inputGroups.get(i).buildAsVBox();
                        VBox ig2 = null;

                        if (i+1 < inputGroups.size()) {
                            ig2 = inputGroups.get(i+1).buildAsVBox();
                        }

                        HBox hBox = new HBox(10);
                        hBox.getChildren().add(ig1);
                        if (ig2 != null) {
                            hBox.getChildren().add(ig2);
                        }

                        formContainer.getChildren().add(hBox);
                    }
                }
                break;
            default:
                for (FFGInputGroup inputGroup : inputGroups) {
                    formContainer.getChildren().add(inputGroup.buildAsVBox());
                }
                break;
        }

        return formContainer;
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

    /**
     * Build ans shows the form as a {@link Dialog}
     * TODO Add support to custom callback parameter
     *
     * @param dTitle Passed directly to {@link Dialog#setTitle(String)}
     * @param dHeaderText Passed directly to {@link Dialog#setHeaderText(String)}
     */
    public void showAsDialog(String dTitle, String dHeaderText) {
        VBox formContainer = this.build();

        Dialog dialog = new Dialog();
        dialog.setTitle(dTitle);
        dialog.setHeaderText(dHeaderText);
        dialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.CANCEL,
                ButtonType.OK
        );

        dialog.getDialogPane().setContent(formContainer);
        dialog.showAndWait();
    }
}
