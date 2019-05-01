package edu.boscotech.techlib.validator;

import java.util.ArrayList;
import java.util.List;

public class ValidatorFactory {
    public static CompoundValidator numberRange(Double min, Double max) {
        List<IElementValidator> validators = new ArrayList<>();
        validators.add(new NumberValidator());
        if (min != null) {
            validators.add(new MinValidator(min));
        }
        if (max != null) {
            validators.add(new MaxValidator(max));
        }
        return new CompoundValidator(validators);
    }

    public static EnumValidator boolean_() {
        return new EnumValidator("true", "false");
    }
}