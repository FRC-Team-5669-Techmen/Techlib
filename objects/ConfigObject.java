package edu.boscotech.techlib.objects;

import edu.boscotech.techlib.TechlibRobot;
import edu.boscotech.techlib.configparser.AbstractConfiggable;
import edu.boscotech.techlib.configparser.ConfigElement;
import edu.boscotech.techlib.configparser.IConfiggable;

@AbstractConfiggable
public abstract class ConfigObject implements IConfiggable {
    protected abstract void setup(TechlibRobot robot, 
        ConfigElement element);

    @Override
    public void setupWrapper(TechlibRobot robot, ConfigElement element) {
        setup(robot, element);
    }
}