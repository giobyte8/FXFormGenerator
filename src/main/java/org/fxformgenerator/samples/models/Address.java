package org.fxformgenerator.samples.models;

import javafx.beans.property.*;

/**
 * Created by giovanni on 4/8/16.
 */
public class Address {

    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty street = new SimpleStringProperty();
    private SimpleIntegerProperty postalCode = new SimpleIntegerProperty();
    private StringProperty city = new SimpleStringProperty();
    private StringProperty state = new SimpleStringProperty();

    private BooleanProperty billingAddress = new SimpleBooleanProperty(true);


    ////////////////////////////////////////////////////////////////////////////


    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getStreet() {
        return street.get();
    }

    public StringProperty streetProperty() {
        return street;
    }

    public void setStreet(String street) {
        this.street.set(street);
    }

    public int getPostalCode() {
        return postalCode.get();
    }

    public SimpleIntegerProperty postalCodeProperty() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode.set(postalCode);
    }

    public String getCity() {
        return city.get();
    }

    public StringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getState() {
        return state.get();
    }

    public StringProperty stateProperty() {
        return state;
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public boolean getBillingAddress() {
        return billingAddress.get();
    }

    public BooleanProperty billingAddressProperty() {
        return billingAddress;
    }

    public void setBillingAddress(boolean billingAddress) {
        this.billingAddress.set(billingAddress);
    }
}
