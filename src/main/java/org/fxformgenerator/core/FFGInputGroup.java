package org.fxformgenerator.core;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.ZoneId;
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
    private PropertyDescriptor propDesc;

    private double minMaxEditorWidth = 220.0;


    /**
     * Constructs a new input group using the <code>editorLB</code> param
     * as label text for editor's label.
     *
     * NOTE: Please be sure that #propDesc param has accessible setter and
     * getter methods.
     *
     * @param model POJO owner of property to edit on this input group.
     * @param editorLB display text for editor's label
     * @param propDesc Property descriptor of edited property
     */
    public FFGInputGroup(Object model, String editorLB, PropertyDescriptor propDesc) {
        this.model = model;
        this.editorLB = editorLB;
        this.propDesc = propDesc;
    }

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
        Node fieldEditor = this.constructEditor();

        vBox.getChildren().addAll(fieldLB, fieldEditor);
        return vBox;
    }

    /**
     * Constructs a binded editor based on #propDesc type
     * TextField for types: [String | ]
     * CheckBox for types: [boolean ]
     *
     * @return The generated input node
     */
    public Node constructEditor() {
        Method getterMethod = propDesc.getReadMethod();

        if (getterMethod.getReturnType() == String.class) {
            TextField fieldTF = new TextField();
            fieldTF.textProperty().addListener((obs, oldV, newV) -> {
                this.updateFieldValue(newV);
            });

            fieldTF.setText(getCurrentValue().toString());
            fieldTF.setMinWidth(minMaxEditorWidth);
            return fieldTF;
        }
        else if (getterMethod.getReturnType() == boolean.class) {
            CheckBox fieldCB = new CheckBox();
            fieldCB.selectedProperty().addListener((observable, oldValue, newValue) -> {
                this.updateFieldValue(newValue);
            });

            fieldCB.setSelected((boolean) getCurrentValue());
            fieldCB.setMinWidth(minMaxEditorWidth);
            return fieldCB;
        }
        else if (getterMethod.getReturnType() == int.class) {
            Spinner fieldSP = new Spinner(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                    Integer.MIN_VALUE,
                    Integer.MAX_VALUE,
                    (int) getCurrentValue()
            ));

            // TODO Implement pure integer editable field
            // fieldSP.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0));
            // fieldSP.setEditable(true);

            fieldSP.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                this.updateFieldValue(Integer.parseInt(newValue));
            });

            fieldSP.setMinWidth(minMaxEditorWidth);
            return fieldSP;
        }
        else if (getterMethod.getReturnType() == float.class) {
            Spinner fieldSP = new Spinner(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                    Float.MIN_VALUE,
                    Float.MAX_VALUE,
                    (float) getCurrentValue()
            ));

            // TODO Implement pure float editable field
            // fieldSP.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0));
            // fieldSP.setEditable(true);

            fieldSP.getEditor().textProperty().addListener((obs, oldV, newV) -> {
                this.updateFieldValue(Float.parseFloat(newV));
            });

            fieldSP.setMinWidth(minMaxEditorWidth);
            return fieldSP;
        }
        else if (getterMethod.getReturnType() == double.class) {
            Spinner fieldSP = new Spinner(new SpinnerValueFactory.DoubleSpinnerValueFactory(
                    Double.MIN_VALUE,
                    Double.MAX_VALUE,
                    (double) getCurrentValue()
            ));

            // TODO Implement pure double editable field
            // fieldSP.getEditor().setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 0));
            // fieldSP.setEditable(true);

            fieldSP.getEditor().textProperty().addListener((obs, oldV, newV) -> {
                this.updateFieldValue(Double.parseDouble(newV));
            });

            fieldSP.setMinWidth(minMaxEditorWidth);
            return fieldSP;
        }
        else if (getterMethod.getReturnType() == Date.class) {
            DatePicker fieldDP = new DatePicker();
            fieldDP.getEditor().setEditable(false);

            LocalDate date = ((Date) getCurrentValue()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            fieldDP.setValue(date);

            fieldDP.setMinWidth(minMaxEditorWidth);
            fieldDP.setMaxWidth(minMaxEditorWidth);
            return fieldDP;
        }

        return null;
    }

    /**
     * Invokes to the setter method over #propDesc
     * with given param value
     *
     * @param newValue value to pass to setter method as parameter
     */
    private void updateFieldValue(Object newValue) {
        try {
            propDesc.getWriteMethod().invoke(model, newValue);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            System.err.println("Can not invoke setter method");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Invokes the getter method over #propDesc and returns
     * its value or empty string ("") if value is null
     *
     * @return
     */
    private Object getCurrentValue() {
        try {
            Object value = propDesc.getReadMethod().invoke(model);
            return value != null ? value : "";
        }
        catch (InvocationTargetException | IllegalAccessException e) {
            System.err.println("Can not invoke getter method");
            return "";
        }
    }
}
