package org.fxformgenerator.samples.models;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by giovanni on 5/3/16.
 */
public class Employee {

    @NotNull(message = "El nombre es requerido")
    private String fullName;

    @NotNull(message = "Ingrese una biografia breve del empleado")
    @Size(min = 25, max = 350, message = "Deben ser entre 25 y 250 caracteres")
    private String bio;

    @Min(value = 18, message = "Debe tener al menos 19 años para trabajar")
    private int age;

    @Min(value = 20000, message = "El salario minimo mensual es de $20000")
    private double salary;

    @AssertTrue(message = "El empleado debe estar activo")
    private boolean active;

    @Min(value = 3, message = "El minimo aceptado es 3")
    private int repeatTestInMonths;


    ////////////////////////////////////////////////////////////////////////////
    // Getter and setters

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getRepeatTestInMonths() {
        return repeatTestInMonths;
    }

    public void setRepeatTestInMonths(int repeatTestInMonths) {
        this.repeatTestInMonths = repeatTestInMonths;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
