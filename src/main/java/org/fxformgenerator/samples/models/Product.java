package org.fxformgenerator.samples.models;

import javafx.beans.property.*;

import java.util.Date;

/**
 * Created by giovanni on 4/9/16.
 */
public class Product {

    private StringProperty uniqueCode = new SimpleStringProperty();
    private StringProperty productName = new SimpleStringProperty();
    private DoubleProperty unitPrice = new SimpleDoubleProperty();
    private FloatProperty taxPercent = new SimpleFloatProperty();

    private Date expirationDate = new Date();
    private StringProperty provider = new SimpleStringProperty();
    private BooleanProperty adultsOnly = new SimpleBooleanProperty();
    private StringProperty originCountry = new SimpleStringProperty();


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

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getProvider() {
        return provider.get();
    }

    public StringProperty providerProperty() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider.set(provider);
    }

    public boolean getAdultsOnly() {
        return adultsOnly.get();
    }

    public BooleanProperty adultsOnlyProperty() {
        return adultsOnly;
    }

    public void setAdultsOnly(boolean adultsOnly) {
        this.adultsOnly.set(adultsOnly);
    }

    public String getOriginCountry() {
        return originCountry.get();
    }

    public StringProperty originCountryProperty() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry.set(originCountry);
    }
}
