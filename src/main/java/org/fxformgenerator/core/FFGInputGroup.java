package org.fxformgenerator.core;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
            fieldTF.textProperty().addListener((observable, oldValue, newValue) -> {
                this.updateFieldValue(newValue);
            });

            fieldTF.setText(getCurrentValue().toString());
            return fieldTF;
        }

        return null;
    }

    /**
     * Invokes to the setter method over #propDesc
     * with given param value
     *
     * @param newValue value to pass to setter method as parameter
     */
    private void updateFieldValue(String newValue) {
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
