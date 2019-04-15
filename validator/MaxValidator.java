package edu.boscotech.techlib.validator;

import edu.boscotech.techlib.configparser.ConfigElement;

public class MaxValidator implements IElementValidator {
    private double m_max;

    public MaxValidator(double max) {
        m_max = max;
    }

    @Override
    public boolean validate(ConfigElement element) throws ValidatorError {
        try {
            double value = Double.parseDouble(element.getValue());
            if (value > m_max) {
                throw new ValidatorError(element, "value must be at most " + m_max + ", got " + value + " instead");
            }
        } catch (Exception e) {
            throw new ValidatorError(element, "value must be a number, got \"" + element.getValue() + "\" instead");
        }
        return true;
    }
}