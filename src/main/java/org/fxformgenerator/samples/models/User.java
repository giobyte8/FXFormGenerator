package org.fxformgenerator.samples.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by giovanni on 4/6/16.
 */
public class User {

    private StringProperty username = new SimpleStringProperty();
    private StringProperty fullName = new SimpleStringProperty();


    ///////////////////////////////////////////////////////////////////////////

    public String getUsername() {
        return username.get();
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getFullName() {
        return fullName.get();
    }

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }
}
