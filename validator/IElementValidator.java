package edu.boscotech.techlib.validator;

import edu.boscotech.techlib.configparser.ConfigElement;

public interface IElementValidator {
    public boolean validate(ConfigElement element) throws ValidatorError;
}