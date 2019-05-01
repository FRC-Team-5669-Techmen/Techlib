package edu.boscotech.techlib.validator;

import java.util.List;

import edu.boscotech.techlib.configparser.ConfigElement;

public class CompoundValidator implements IElementValidator {
    private IElementValidator[] mChildren;

    public CompoundValidator(IElementValidator... validators) {
        mChildren = validators;
    }

    public CompoundValidator(List<IElementValidator> validators) {
        mChildren = new IElementValidator[validators.size()];
        for (int i = 0; i < validators.size(); i++) {
            mChildren[i] = validators.get(i);
        }
    }

    @Override
    public boolean validate(ConfigElement element) throws ValidatorError {
        for (IElementValidator child : mChildren) {
            if (!child.validate(element)) return false;
        }
        return true;
    }
}