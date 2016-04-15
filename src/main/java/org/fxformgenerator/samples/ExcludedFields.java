package org.fxformgenerator.samples;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import org.fxformgenerator.core.FXFormGenerator;
import org.fxformgenerator.samples.models.Address;
import org.fxformgenerator.samples.models.State;

/**
 * Created by giovanni on 4/8/16.
 */
public class ExcludedFields extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.show();

        ObservableList<Object> availableStates = FXCollections.observableArrayList();
        availableStates.add(new State("Sonora", 17000000));
        availableStates.add(new State("Yucatan", 42000));

        ObservableList<Object> availablePCodes = FXCollections.observableArrayList();
        availablePCodes.add(50500);
        availablePCodes.add(40290);
        availablePCodes.add(50200);

        Address address = new Address();
        FXFormGenerator
                .forModel(address)
                .excludeFields("id")
                .assignFieldOptions("state", availableStates)
                .assignFieldOptions("postalCode", availablePCodes)
                .showAsDialog(
                        "New address",
                        "Register user address"
                );
    }
}
