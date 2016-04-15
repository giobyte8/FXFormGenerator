package org.fxformgenerator.samples;

import javafx.application.Application;
import javafx.stage.Stage;
import org.fxformgenerator.core.FFGLayout;
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
        primaryStage.hide();

        Product product = new Product();
        FXFormGenerator
                .forModel(product)
                .assignFieldLabel("uniqueCode", "SKU")
                .assignFieldLabel("adultsOnly", "Only for adults?")
                .assignFieldLabel("originCountry", "Imported from country")
                .assignFieldsOrder(
                        "uniqueCode",
                        "productName",
                        "unitPrice",
                        "taxPercent",
                        "expirationDate",
                        "provider",
                        "adultsOnly",
                        "originCountry")
                .assignFormLayout(FFGLayout.MULTIPLE_FULLWIDTHROWS_COLUMNS)
                .showAsDialog(o -> {
                    System.out.println("Saving product");
                });
    }
}
