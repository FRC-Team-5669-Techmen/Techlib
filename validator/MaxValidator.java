package edu.boscotech.techlib.validator;

import edu.boscotech.techlib.configparser.ConfigElement;

public class MaxValidator implements IElementValidator {
    private double mMax;

    public MaxValidator(double max) {
        mMax = max;
    }

    @Override
    public boolean validate(ConfigElement element) throws ValidatorError {
        try {
            double value = Double.parseDouble(element.getValue());
            if (value > mMax) {
                throw new ValidatorError(element, "value must be at most " + mMax + ", got " + value + " instead");
            }
        } catch (Exception e) {
            throw new ValidatorError(element, "value must be a number, got \"" + element.getValue() + "\" instead");
        }
        return true;
    }
}