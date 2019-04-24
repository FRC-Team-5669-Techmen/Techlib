package edu.boscotech.techlib.validator;

import edu.boscotech.techlib.configparser.ConfigElement;

public class CompoundValidator implements IElementValidator {
    private IElementValidator[] mChildren;

    public CompoundValidator(IElementValidator... validators) {
        mChildren = validators;
    }

    @Override
    public boolean validate(ConfigElement element) throws ValidatorError {
        for (IElementValidator child : mChildren) {
            if (!child.validate(element)) return false;
        }
        return true;
    }
}