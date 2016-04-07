package org.fxformgenerator.core;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by giovanni on 4/5/16.
 */
public class FFGBuilder {

    /** POJO To edit in this form */
    private Object model;

    /** Custom labels to use in form fields */
    private Map<String, String> fieldLabels = new HashMap<>();


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


    ////////////////////////////////////////////////////////////////////////////
    // Builder methods

    public VBox build() {
        VBox formContainer = new VBox(3);
        List<FFGInputGroup> inputGroups = new ArrayList<>();

        try {
            PropertyDescriptor[] propDescriptors = Introspector
                    .getBeanInfo(model.getClass(), Object.class)
                    .getPropertyDescriptors();

            for (PropertyDescriptor pDesc : propDescriptors) {
                if (pDesc.getReadMethod() != null && pDesc.getWriteMethod() != null) {

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
            }
        }
        catch (IntrospectionException e) {
            System.err.println("Introspection error");
            System.err.println(e.getMessage());
        }

        // Assembly all form nodes into a single VBox parent
        for (FFGInputGroup inputGroup : inputGroups) {
            formContainer.getChildren().add(inputGroup.buildAsVBox());
        }

        return formContainer;
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
