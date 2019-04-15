package edu.boscotech.techlib.validator;

import edu.boscotech.techlib.configparser.ConfigElement;

public class MinValidator implements IElementValidator {
    private double m_min;

    public MinValidator(double min) {
        m_min = min;
    }

    @Override
    public boolean validate(ConfigElement element) throws ValidatorError {
        try {
            double value = Double.parseDouble(element.getValue());
            if (value < m_min) {
                throw new ValidatorError(element, "value must be at least " + m_min + ", got " + value + " instead");
            }
        } catch (Exception e) {
            throw new ValidatorError(element, "value must be a number, got \"" + element.getValue() + "\" instead");
        }
        return true;
    }
}