package edu.boscotech.techlib.validator;

import edu.boscotech.techlib.configparser.ConfigElement;

public class CompoundValidator implements IElementValidator {
    private IElementValidator[] m_children;

    public CompoundValidator(IElementValidator... validators) {
        m_children = validators;
    }

    @Override
    public boolean validate(ConfigElement element) throws ValidatorError {
        for (IElementValidator child : m_children) {
            if (!child.validate(element)) return false;
        }
        return true;
    }
}