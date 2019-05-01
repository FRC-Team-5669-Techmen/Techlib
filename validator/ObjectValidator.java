package edu.boscotech.techlib.validator;

import java.util.ArrayList;
import java.util.List;

import edu.boscotech.techlib.configparser.ConfigElement;

public class ObjectValidator implements IElementValidator {
    private class ObjectField {
        public String mFieldName;
        public IElementValidator mValidator;
        public boolean mOptional;
    }

    private List<ObjectField> mFields = new ArrayList<>();

    public ObjectValidator() { }

    public void addField(String name, IElementValidator validator, 
        boolean optional) {
        ObjectField field = new ObjectField();
        field.mFieldName = name;
        field.mValidator = validator;
        field.mOptional = optional;
        mFields.add(field);
    }

    public void addField(String name, IElementValidator validator) {
        addField(name, validator, false);
    }

    public void addOptionalField(String name, IElementValidator validator) {
        addField(name, validator, true);
    }

    @Override
    public boolean validate(ConfigElement element) throws ValidatorError {
        if (!element.getValue().isEmpty()) {
            throw new ValidatorError(element, "supposed to be an object, not a "
                + "value. Delete everything after the colon");
        }
        for (ObjectField field : mFields) {
            ConfigElement realField = element.getChild(field.mFieldName);
            if (realField == null && !field.mOptional) {
                throw new ValidatorError(element, "requires a child element "
                    + "called '" + field.mFieldName + "', but no such element "
                    + "was provided");
            } else if (field.mOptional && realField == null) {
                continue; // It's optional, so it being null is not a problem.
            } else if (realField != null && field.mValidator != null) {
                field.mValidator.validate(realField);
            }
        }
        return false;
    }
}