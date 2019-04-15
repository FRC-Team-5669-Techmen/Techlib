package edu.boscotech.techlib.validator;

import edu.boscotech.techlib.configparser.ConfigElement;

public class IntValidator implements IElementValidator {
    @Override
    public boolean validate(ConfigElement element) throws ValidatorError {
        try {
            double value = Double.parseDouble(element.getValue());
            if (Math.round(value) != value) {
                throw new ValidatorError(element, "value must be an integer, got " + value + " instead");
            }
        } catch (Exception e) {
            throw new ValidatorError(element, "value must be a number, got \"" + element.getValue() + "\" instead");
        }
        return true;
    }
}