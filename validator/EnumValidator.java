package edu.boscotech.techlib.validator;

import edu.boscotech.techlib.configparser.ConfigElement;

public class EnumValidator implements IElementValidator {
    private String[] mValues;

    public EnumValidator(String... values) {
        mValues = values;
    }

    @Override
    public boolean validate(ConfigElement element) throws ValidatorError {
        for (String value : mValues) {
            if (element.getValue() == value) return true;
        }
        String error = "value must be one of ";
        for (String value : mValues) {
            error += value + ", ";
        }
        error += "got " + element.getValue() + " instead";
        throw new ValidatorError(element, error);
    }
}