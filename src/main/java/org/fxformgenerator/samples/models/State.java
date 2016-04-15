package org.fxformgenerator.samples.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by giovanni on 4/14/16.
 */
public class State {

    private StringProperty name = new SimpleStringProperty();
    private IntegerProperty population = new SimpleIntegerProperty();

    public State(String name, int population) {
        this.name.set(name);
        this.population.set(population);
    }

    @Override
    public String toString() {
        return name.get();
    }


    ////////////////////////////////////////////////////////////////////////////

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getPopulation() {
        return population.get();
    }

    public IntegerProperty populationProperty() {
        return population;
    }

    public void setPopulation(int population) {
        this.population.set(population);
    }
}
