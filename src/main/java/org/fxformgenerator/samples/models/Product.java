package org.fxformgenerator.samples.models;

import javafx.beans.property.*;

/**
 * Created by giovanni on 4/9/16.
 */
public class Product {

    private StringProperty uniqueCode = new SimpleStringProperty();
    private StringProperty productName = new SimpleStringProperty();
    private DoubleProperty unitPrice = new SimpleDoubleProperty();
    private FloatProperty taxPercent = new SimpleFloatProperty();


    ////////////////////////////////////////////////////////////////////////////

    public String getUniqueCode() {
        return uniqueCode.get();
    }

    public StringProperty uniqueCodeProperty() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode.set(uniqueCode);
    }

    public String getProductName() {
        return productName.get();
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public double getUnitPrice() {
        return unitPrice.get();
    }

    public DoubleProperty unitPriceProperty() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice.set(unitPrice);
    }

    public float getTaxPercent() {
        return taxPercent.get();
    }

    public FloatProperty taxPercentProperty() {
        return taxPercent;
    }

    public void setTaxPercent(float taxPercent) {
        this.taxPercent.set(taxPercent);
    }
}
