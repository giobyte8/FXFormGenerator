package org.fxformgenerator.core;

/**
 * Created by giovanni on 4/6/16.
 */
public class FXFormGenerator {

    public static FFGBuilder forModel(Object model) {
        return new FFGBuilder(model);
    }
}
