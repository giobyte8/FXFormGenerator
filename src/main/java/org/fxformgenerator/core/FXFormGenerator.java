package org.fxformgenerator.core;

/**
 * Created by giovanni on 4/6/16.
 */
public class FXFormGenerator {

    public static FormBuilder forModel(Object model) {
        return new FormBuilder(model);
    }
}
