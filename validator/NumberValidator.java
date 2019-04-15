package edu.boscotech.techlib.validator;

import edu.boscotech.techlib.configparser.ConfigElement;

public class NumberValidator implements IElementValidator {
    @Override
    public boolean validate(ConfigElement element) throws ValidatorError {
        try {
            Double.parseDouble(element.getValue());
        } catch (Exception e) {
            throw new ValidatorError(element, "value must be a number, got \"" + element.getValue() + "\" instead");
        }
        return true;
    }
}