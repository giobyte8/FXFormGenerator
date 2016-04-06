package org.fxformgenerator.core;

import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by giovanni on 4/5/16.
 */
public class FormBuilder {

    private Object model;
    private List<FBInputGroup> inputGroups = new ArrayList<>();
    private List<Node> formNodes = new ArrayList<>();

    public FormBuilder(Object model) {
        this.model = model;
    }

    public List<Node> constructForm() {
        this.constructStringFields();
        return formNodes;
    }

    public void showAsDialog() {
        this.constructStringFields();

        // Construct dialog content
        VBox form = new VBox(3);
        for(Node formNode : formNodes) {
            form.getChildren().add(formNode);
        }

        Dialog dialog = new Dialog();
        dialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.CANCEL,
                ButtonType.OK
        );

        dialog.getDialogPane().setContent(form);
        dialog.showAndWait();
    }

    private void constructStringFields() {
        try {
            PropertyDescriptor[] propDescriptors = Introspector
                    .getBeanInfo(model.getClass(), Object.class)
                    .getPropertyDescriptors();

            inputGroups.clear();
            formNodes.clear();

            for (PropertyDescriptor pDesc : propDescriptors) {
                if (pDesc.getReadMethod() != null && pDesc.getWriteMethod() != null) {
                    FBInputGroup fbInputGroup = new FBInputGroup(model, pDesc);
                    inputGroups.add(fbInputGroup);
                    formNodes.add(fbInputGroup.getAsVBox());
                }
            }
        }
        catch (IntrospectionException e) {}
    }
}
