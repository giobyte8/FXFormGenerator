package org.fxformgenerator.samples;

import javafx.application.Application;
import javafx.stage.Stage;
import org.fxformgenerator.core.FFGLayout;
import org.fxformgenerator.core.FXFormGenerator;
import org.fxformgenerator.samples.models.User;

/**
 * Created by giovanni on 4/7/16.
 */
public class CustomLabelsForm extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.show();

        User user = new User();
        FXFormGenerator
                .forModel(user)
                .assignFieldLabel("fullName", "Nombre completo")
                .assignFieldLabel("username", "Nombre de usuario")
                .assignFormLayout(FFGLayout.SINGLE_COLUMN)
                .showAsDialog(
                        "New user",
                        "Create new user account"
                );
    }
}
