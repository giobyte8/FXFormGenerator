package org.fxformgenerator.samples;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by giovanni on 5/2/16.
 */
public class Artist {

    private StringProperty publicName = new SimpleStringProperty();
    private StringProperty realName = new SimpleStringProperty();

    private IntegerProperty age = new SimpleIntegerProperty();

    ////////////////////////////////////////////////////////////////////////////
    // Getter and setters (With validation constraints)

    @NotNull(message = "El nombre publico es requerido")
    @Size(min = 2, max = 10, message = "Debe tener entre 2 y 10 caracteres")
    public String getPublicName() {
        return publicName.get();
    }

    public StringProperty publicNameProperty() {
        return publicName;
    }

    public void setPublicName(String publicName) {
        this.publicName.set(publicName);
    }

    @Size(min = 2, max = 10, message = "Debe tener entre 2 y 50 caracteres")
    public String getRealName() {
        return realName.get();
    }

    public StringProperty realNameProperty() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName.set(realName);
    }

    @NotNull(message = "Ingrese la edad del artista")
    @Min(value = 18, message = "Al menos debe contar con 18 años")
    public int getAge() {
        return age.get();
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    public void setAge(int age) {
        this.age.set(age);
    }
}
