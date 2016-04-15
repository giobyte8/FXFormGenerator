package org.fxformgenerator.samples;

import javafx.application.Application;
import javafx.stage.Stage;
import org.fxformgenerator.core.FXFormGenerator;
import org.fxformgenerator.samples.models.User;

/**
 * Created by giovanni on 4/6/16.
 */
public class SimpleForm extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.show();
        primaryStage.hide();

        User user = new User();
        FXFormGenerator
                .forModel(user)
                .showAsDialog(o -> {
                    System.out.println("User object updated with form data");
                });
    }
}
