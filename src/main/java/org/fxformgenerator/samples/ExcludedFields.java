package org.fxformgenerator.samples;

import javafx.application.Application;
import javafx.stage.Stage;
import org.fxformgenerator.core.FXFormGenerator;
import org.fxformgenerator.samples.models.Address;

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

        Address address = new Address();
        FXFormGenerator
                .forModel(address)
                .excludeFields("id", "postalCode")
                .showAsDialog(
                        "New user",
                        "Create new user account"
                );
    }
}
