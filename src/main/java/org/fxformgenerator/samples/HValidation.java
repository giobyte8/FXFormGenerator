package org.fxformgenerator.samples;

import javafx.application.Application;
import javafx.stage.Stage;
import org.fxformgenerator.core.FXFormGenerator;

/**
 * Created by giovanni on 5/2/16.
 */
public class HValidation extends Application {

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.show();
        primaryStage.hide();

        FXFormGenerator
                .forModel(new Artist())
                .enableValidation()
                .showAsDialog(o -> {

                });
    }
}
