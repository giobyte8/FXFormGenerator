package org.fxformgenerator.core;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Created by giovanni on 5/2/16.
 */
public class FFGModelValidator {
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = validatorFactory.getValidator();

    public static boolean isModelValid(Object object) {
        return (validator.validate(object).size() == 0);
    }

    public static Set<ConstraintViolation<Object>> getViolatedConstraints(Object object) {
        return validator.validate(object);
    }
}
