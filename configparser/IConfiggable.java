package edu.boscotech.techlib.configparser;

import edu.boscotech.techlib.TechlibRobot;
import edu.boscotech.techlib.validator.IElementValidator;

public interface IConfiggable {
    public void setupWrapper(TechlibRobot robot, ConfigElement element);
    public IElementValidator createValidatorWrapper();
}