package edu.boscotech.techlib.validator;

import edu.boscotech.techlib.configparser.ConfigElement;

public class MinValidator implements IElementValidator {
    private double mMin;

    public MinValidator(double min) {
        mMin = min;
    }

    @Override
    public boolean validate(ConfigElement element) throws ValidatorError {
        try {
            double value = Double.parseDouble(element.getValue());
            if (value < mMin) {
                throw new ValidatorError(element, "value must be at least " + mMin + ", got " + value + " instead");
            }
        } catch (Exception e) {
            throw new ValidatorError(element, "value must be a number, got \"" + element.getValue() + "\" instead");
        }
        return true;
    }
}