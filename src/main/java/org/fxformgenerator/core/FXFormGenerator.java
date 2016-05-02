package org.fxformgenerator.core;

/**
 * Created by giovanni on 4/6/16.
 *
 * @version 0.1
 * On date: May 05, 2016
 */
public class FXFormGenerator {

    public static FFGBuilder forModel(Object model) {
        return new FFGBuilder(model);
    }
}
