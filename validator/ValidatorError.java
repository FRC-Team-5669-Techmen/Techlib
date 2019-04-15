package edu.boscotech.techlib.validator;

import edu.boscotech.techlib.configparser.ConfigElement;

public class ValidatorError extends Exception {
    static final long serialVersionUID = 1l;
    public ValidatorError(ConfigElement element, String error) {
        super("\nValidation error: " + error + "\nOn element:\n" + element.toString());
    }
}