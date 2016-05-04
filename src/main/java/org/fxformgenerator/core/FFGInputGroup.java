package org.fxformgenerator.core;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.fxformgenerator.core.readonly.AbstractValueFormatter;
import org.fxformgenerator.core.readonly.DefaultValueFormatter;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Date;

/**
 * A Form input group represents an element that contains
 * a label, an editor and an error label.
 *
 * Created by giovanni on 4/6/16.
 */
public class FFGInputGroup {

    private Object model;
    private String editorLB;
    private String validationMessage;
    private Node customEditor;
    private PropertyDescriptor propDesc;

    /** If true, node editor will be a label with property current value */
    private boolean readOnly;

    private ObservableList<Object> fieldValues;

    private double minMaxEditorWidth = 220.0;

    private AbstractValueFormatter valueFormatter = new DefaultValueFormatter();


    /**
     * Constructs a new input group, if field referenced by <code>propDesc</code>
     * param is annotated with @FFGLabel uses its value as the display text
     * for editor's label, otherwise uses the property name converted to human
     * readable format.
     *
     * NOTE: Please be sure that #propDesc param has accessible setter and
     * getter methods
     *
     * @param model POJO owner of property to edit on this input group.
     * @param propDesc Property descriptor of edited field
     */
    public FFGInputGroup(Object model, PropertyDescriptor propDesc) {
        this.model = model;
        this.propDesc = propDesc;

        // TODO Check if propDesc in model has annotation FFGLabel
        // ...

        this.editorLB = FFGUtils.camelCaseToHumanReadable(propDesc.getName());
    }

    /**
     * Assembly this input group into a {@link VBox} containing elements:
     * Editor label,
     * Editor input (TextField, DatePicker, ChoiceBox etc)
     * Editor error label
     *
     * @return a {@link VBox} containing the input group elements
     */
    public VBox buildAsVBox() {
        VBox vBox = new VBox(3);
        vBox.setPadding(new Insets(10.0, 0, 0, 0));

        Label fieldLB    = new Label(editorLB);
        Node fieldEditor = this.readOnly
                ? constructValueLabel()
                : constructEditor();

        vBox.getChildren().addAll(fieldLB, fieldEditor);

        // Add bold style to label if necessary
        if (this.readOnly) {
            fieldLB.setStyle("-fx-font-weight: bold");
        }

        // Add validation message if necessary
        if (this.validationMessage != null && this.validationMessage.length() > 0) {

            Label errorLB = new Label(validationMessage);
            errorLB.setStyle("-fx-text-fill: #E74C3C");
            errorLB.setMaxWidth(minMaxEditorWidth);
            errorLB.setWrapText(true);
            vBox.getChildren().add(errorLB);
        }

        return vBox;
    }

    /**
     * Constructs a linked editor based on #propDesc type
     * TextField for types: [String | ]
     * CheckBox for types: [boolean ]
     *
     * @return The generated input node
     */
    public Node constructEditor() {
        Method getterMethod = propDesc.getReadMethod();

        if (customEditor != null) {
            if (customEditor instanceof ChoiceBox) {
                ((ChoiceBox) customEditor).setMinWidth(minMaxEditorWidth);
                ((ChoiceBox) customEditor).setMaxWidth(minMaxEditorWidth);
            }
            else if (customEditor instanceof TextArea) {
                ((TextArea) customEditor).setMinWidth(minMaxEditorWidth);
                ((TextArea) customEditor).setMaxWidth(minMaxEditorWidth);
            }
            else if (customEditor instanceof TextField) {
                ((TextField) customEditor).setMinWidth(minMaxEditorWidth);
                ((TextField) customEditor).setMaxWidth(minMaxEditorWidth);
            }
            else if (customEditor instanceof Spinner) {
                ((Spinner) customEditor).setMinWidth(minMaxEditorWidth);
                ((Spinner) customEditor).setMaxWidth(minMaxEditorWidth);
            }

            return customEditor;
        }
        else if (fieldValues != null && fieldValues.size() > 0) {
            ChoiceBox<Object> fieldCB = new ChoiceBox();
            fieldCB.setItems(this.fieldValues);

            fieldCB.getSelectionModel()
                    .selectedItemProperty()
                    .addListener((obs, oldV, newV) -> {
                this.updateFieldValue(newV);
            });

            fieldCB.getSelectionModel().select(getCurrentFieldValue(null));
            fieldCB.setMinWidth(minMaxEditorWidth);
            fieldCB.setMaxWidth(minMaxEditorWidth);
            return fieldCB;
        }
        else if (getterMethod.getReturnType() == String.class) {
            TextField fieldTF = new TextField();
            fieldTF.textProperty().addListener((obs, oldV, newV) -> {
                this.updateFieldValue(newV);
            });

            fieldTF.setText(getCurrentFieldValue("").toString());
            fieldTF.setMinWidth(minMaxEditorWidth);
            fieldTF.setMaxWidth(minMaxEditorWidth);
            return fieldTF;
        }
        else if (getterMethod.getReturnType() == boolean.class) {
            CheckBox fieldCB = new CheckBox();
            fieldCB.selectedProperty().addListener((observable, oldValue, newValue) -> {
                this.updateFieldValue(newValue);
            });

            fieldCB.setSelected((boolean) getCurrentFieldValue(false));
            fieldCB.setMinWidth(minMaxEditorWidth);
            fieldCB.setMaxWidth(minMaxEditorWidth);
            return fieldCB;
        }
        else if (getterMethod.getReturnType() == int.class) {
            Spinner fieldSP = new Spinner(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                    Integer.MIN_VALUE,
                    Integer.MAX_VALUE,
                    (int) getCurrentFieldValue(0)
            ));

            // TODO Implement pure integer editable field
            // fieldSP.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0));
            fieldSP.setEditable(true);

            fieldSP.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                if (FFGUtils.isInteger(newValue)) {
                    this.updateFieldValue(Integer.parseInt(newValue));
                }
                else {
                    this.updateFieldValue(0);
                }
            });

            fieldSP.setMinWidth(minMaxEditorWidth);
            fieldSP.setMaxWidth(minMaxEditorWidth);
            return fieldSP;
        }
        else if (getterMethod.getReturnType() == float.class) {
            Spinner fieldSP = new Spinner(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                    Float.MIN_VALUE,
                    Float.MAX_VALUE,
                    (float) getCurrentFieldValue(0.0)
            ));

            // TODO Implement pure float editable field
            // fieldSP.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0));
            fieldSP.setEditable(true);

            fieldSP.getEditor().textProperty().addListener((obs, oldV, newV) -> {
                if (FFGUtils.isFloat(newV)) {
                    this.updateFieldValue(Float.parseFloat(newV));
                }
                else {
                    this.updateFieldValue(0);
                }
            });

            fieldSP.setMinWidth(minMaxEditorWidth);
            fieldSP.setMaxWidth(minMaxEditorWidth);
            return fieldSP;
        }
        else if (getterMethod.getReturnType() == double.class) {
            Spinner fieldSP = new Spinner(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                    Double.MIN_VALUE,
                    Double.MAX_VALUE,
                    (double) getCurrentFieldValue(0.0)
            ));

            // TODO Implement pure double editable field
            // fieldSP.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0));
            fieldSP.setEditable(true);

            fieldSP.getEditor().textProperty().addListener((obs, oldV, newV) -> {
                if (FFGUtils.isDouble(newV)) {
                    this.updateFieldValue(Double.parseDouble(newV));
                }
                else {
                    this.updateFieldValue(0);
                }
            });

            fieldSP.setMinWidth(minMaxEditorWidth);
            fieldSP.setMaxWidth(minMaxEditorWidth);
            return fieldSP;
        }
        else if (getterMethod.getReturnType() == Date.class) {
            DatePicker fieldDP = new DatePicker();
            fieldDP.setEditable(false);

            LocalDate date = FFGUtils.toLocalDate((Date) getCurrentFieldValue(new Date()));
            fieldDP.setValue(date);

            fieldDP.valueProperty().addListener((obs, oldV, newV) -> {
                if (newV != null) {
                    Date nDate = FFGUtils.toDate(newV);
                    updateFieldValue(nDate);
                }
                else {
                    updateFieldValue(null);
                }
            });

            fieldDP.setMinWidth(minMaxEditorWidth);
            fieldDP.setMaxWidth(minMaxEditorWidth);
            return fieldDP;
        }
        else if (getterMethod.getReturnType() == LocalDate.class) {
            DatePicker fieldDP = new DatePicker();
            fieldDP.setEditable(false);

            fieldDP.setValue((LocalDate) getCurrentFieldValue(LocalDate.now()));
            fieldDP.valueProperty().addListener((obs, oldV, newV) -> {
                updateFieldValue(newV);
            });

            fieldDP.setMinWidth(minMaxEditorWidth);
            fieldDP.setMaxWidth(minMaxEditorWidth);
            return fieldDP;
        }

        return null;
    }

    /**
     * TODO Add support to use custom user formatters
     * Creates a label with the current property value
     *
     * @return The generated label with min/max width assigned and word wrap
     *         enabled.
     */
    public Label constructValueLabel() {
        Method getterMethod = propDesc.getReadMethod();
        String labelValue;

        if (getterMethod.getReturnType() == boolean.class) {
            labelValue = valueFormatter.format((boolean) getCurrentFieldValue(false));
        }
        else if (getterMethod.getReturnType() == Date.class) {
            labelValue = valueFormatter.format((Date) getCurrentFieldValue(new Date()));
        }
        else {
            labelValue = getCurrentFieldValue("").toString();
        }

        Label readOnlyLB = new Label(labelValue);
        readOnlyLB.setMinWidth(minMaxEditorWidth);
        readOnlyLB.setMaxWidth(minMaxEditorWidth);
        readOnlyLB.setWrapText(true);

        return readOnlyLB;
    }


    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Invokes to the setter method over #propDesc
     * with given param value
     *
     * @param newValue value to pass to setter method as parameter
     */
    private void updateFieldValue(Object newValue) {
        try {
            if (isWrapperType(propDesc.getPropertyType())) {
                propDesc.getWriteMethod().invoke(model, newValue);
            }
            else {
                propDesc.getWriteMethod().invoke(
                        model,
                        propDesc.getPropertyType().cast(newValue)
                );
            }
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            System.err.println("Can not invoke setter method");
            System.err.println(e.getMessage());
        }
    }

    private boolean isWrapperType(Class<?> clazz) {
        return clazz.equals(Boolean.class)    ||
                clazz.equals(boolean.class)   ||
                clazz.equals(Character.class) ||
                clazz.equals(char.class)      ||
                clazz.equals(Short.class)     ||
                clazz.equals(short.class)     ||
                clazz.equals(Integer.class)   ||
                clazz.equals(int.class)       ||
                clazz.equals(Long.class)      ||
                clazz.equals(long.class)      ||
                clazz.equals(Double.class)    ||
                clazz.equals(double.class)    ||
                clazz.equals(Float.class)     ||
                clazz.equals(float.class);
    }

    /**
     * Invokes the getter method over #propDesc and returns
     * its value or empty string ("") if value is null
     *
     * @return
     */
    private Object getCurrentFieldValue(Object defaultValue) {
        try {
            Object value = propDesc.getReadMethod().invoke(model);
            return value != null ? value : defaultValue;
        }
        catch (InvocationTargetException | IllegalAccessException e) {
            System.err.println("Can not invoke getter method");
            return defaultValue;
        }
    }

    public double getMinMaxEditorWidth() {
        return minMaxEditorWidth;
    }

    public void setMinMaxEditorWidth(double minMaxEditorWidth) {
        this.minMaxEditorWidth = minMaxEditorWidth;
    }

    public void setEditorLBText(String text) {
        this.editorLB = text;
    }

    public void setFieldValues(ObservableList<Object> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }

    public Node getCustomEditor() {
        return customEditor;
    }

    public void setCustomEditor(Node customEditor) {
        this.customEditor = customEditor;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public AbstractValueFormatter getValueFormatter() {
        return valueFormatter;
    }

    public void setValueFormatter(AbstractValueFormatter valueFormatter) {
        this.valueFormatter = valueFormatter;
    }
}
