package org.fxformgenerator.core;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by giovanni on 4/6/16.
 */
public class FBInputGroup {

    private Object model;
    private String label;

    private Method getterMethod;
    private Method setterMethod;

    public FBInputGroup(Object model, PropertyDescriptor propDesc) {
        this.model = model;
        this.label = propDesc.getName();

        this.getterMethod = propDesc.getReadMethod();
        this.setterMethod = propDesc.getWriteMethod();
    }

    public VBox getAsVBox() {
        VBox vBox = new VBox(3);

        Label fieldLB    = new Label(label);
        Node fieldEditor = this.getEditor();

        vBox.getChildren().addAll(fieldLB, fieldEditor);
        return vBox;
    }

    public Node getEditor() {
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

    private void updateFieldValue(String newValue) {
        try {
            setterMethod.invoke(model, newValue);
        }
        catch (IllegalAccessException | InvocationTargetException e) {}
    }

    private Object getCurrentValue() {
        try {
            Object value = getterMethod.invoke(model);
            return value != null ? value : "";
        }
        catch (InvocationTargetException | IllegalAccessException e) {
            return "";
        }
    }
}
