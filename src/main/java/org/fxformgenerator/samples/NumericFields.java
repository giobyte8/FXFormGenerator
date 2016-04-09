package org.fxformgenerator.samples;

import javafx.application.Application;
import javafx.stage.Stage;
import org.fxformgenerator.core.FXFormGenerator;
import org.fxformgenerator.samples.models.Product;

/**
 * Created by giovanni on 4/9/16.
 */
public class NumericFields extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.show();

        Product product = new Product();
        FXFormGenerator
                .forModel(product)
                .assignFieldLabel("uniqueCode", "SKU")
                .assignFieldsOrder("uniqueCode", "productName", "unitPrice")
                .showAsDialog(
                        "New product",
                        "Create new product"
                );
    }
}
