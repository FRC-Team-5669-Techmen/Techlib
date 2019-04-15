package edu.boscotech.techlib.validator;

import edu.boscotech.techlib.configparser.ConfigElement;

public class EnumValidator implements IElementValidator {
    private String[] m_values;

    public EnumValidator(String... values) {
        m_values = values;
    }

    @Override
    public boolean validate(ConfigElement element) throws ValidatorError {
        for (String value : m_values) {
            if (element.getValue() == value) return true;
        }
        String error = "value must be one of ";
        for (String value : m_values) {
            error += value + ", ";
        }
        error += "got " + element.getValue() + " instead";
        throw new ValidatorError(element, error);
    }
}