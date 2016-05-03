package org.fxformgenerator.samples;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.fxformgenerator.core.FXFormGenerator;
import org.fxformgenerator.samples.models.Employee;

/**
 * Created by giovanni on 5/3/16.
 */
public class CustomOptions extends Application {

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.show();
        primaryStage.hide();
        Employee employee = new Employee();

        //
        // Custom possible options for repeat in months property
        ObservableList<String> repeatInMonthsOptions = FXCollections.observableArrayList();
        repeatInMonthsOptions.add("Cada mes");
        repeatInMonthsOptions.add("Cada 3 meses");
        repeatInMonthsOptions.add("Cada 6 meses");

        //
        // Create a custom Choice box with given options
        ChoiceBox repeatInMonthsCB = new ChoiceBox();
        repeatInMonthsCB.setItems(repeatInMonthsOptions);

        //
        // Bind choice box changes to model values
        repeatInMonthsCB
                .getSelectionModel()
                .selectedIndexProperty()
                .addListener((obs, oldV, newV) -> {
                    switch (newV.intValue()) {
                        case 0:
                            employee.setRepeatTestInMonths(1);
                            break;
                        case 1:
                            employee.setRepeatTestInMonths(3);
                            break;
                        case 2:
                            employee.setRepeatTestInMonths(6);
                            break;
                    }
                });

        //
        // Put current model value as default selected option
        repeatInMonthsCB.getSelectionModel().select(
                employee.getRepeatTestInMonths() == 0
                    ? 0
                    : employee.getRepeatTestInMonths() == 3
                        ? 1
                        : 2
        );

        //
        // Custom editor for bio property
        TextArea bioTA = new TextArea();
        bioTA.setPrefRowCount(4);
        bioTA.textProperty().addListener((obs, oldV, newV) -> {
            employee.setBio(newV);
        });

        FXFormGenerator
                .forModel(employee)
                .enableValidation()
                .assignFieldEditor("repeatTestInMonths", repeatInMonthsCB)
                .assignFieldEditor("bio", bioTA)
                .showAsDialog(
                        o -> {

                            //
                            // Display input values in read only form
                            FXFormGenerator
                                    .forModel(employee)
                                    .enableReadOnlyMode()
                                    .showAsDialog(p -> {

                                    });
                        }
                );
    }
}
