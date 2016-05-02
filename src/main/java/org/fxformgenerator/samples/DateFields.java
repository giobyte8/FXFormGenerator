package org.fxformgenerator.samples;

import javafx.application.Application;
import javafx.stage.Stage;
import org.fxformgenerator.core.FXFormGenerator;
import org.fxformgenerator.samples.models.Billing;

/**
 * Created by TEA on 02/05/2016.
 */
public class DateFields extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.show();
        primaryStage.hide();

        Billing billing = new Billing();
        FXFormGenerator
                .forModel(billing)
                .excludeFields("id", "paid")
                .showAsDialog("Create billing", o -> {
                    System.out.println("New billing created");
                });

    }
}