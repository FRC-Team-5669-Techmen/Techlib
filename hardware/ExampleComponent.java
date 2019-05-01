package edu.boscotech.techlib.hardware;

import edu.boscotech.techlib.TechlibRobot;
import edu.boscotech.techlib.configparser.ConfigElement;
import edu.boscotech.techlib.configparser.RealConfiggable;
import edu.boscotech.techlib.validator.EnumValidator;
import edu.boscotech.techlib.validator.NumberValidator;
import edu.boscotech.techlib.validator.ObjectValidator;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

@RealConfiggable("Example")
public class ExampleComponent extends Component {
    String mStringProperty;
    double mNumberProperty;
    String mDefaultEnumProperty;

    public ExampleComponent() {
        super("Example");
    }

    @Override
    protected void setup(TechlibRobot robot, ConfigElement configTree) {
        mStringProperty = configTree.getString("stringProperty");
        mNumberProperty = configTree.getNumber("numberProperty");
        mDefaultEnumProperty 
            = configTree.getStringOrDefault("default", "defaultEnumProperty");
        System.out.println("ExampleComponent '" + getName() + "'");
        System.out.println("stringProperty: " + mStringProperty);
        System.out.println("numberProperty: " + mNumberProperty);
        System.out.println("defaultEnumProperty: " + mDefaultEnumProperty);
    }

    @Override
    protected ObjectValidator createValidator() {
        ObjectValidator validator = new ObjectValidator();
        validator.addField("stringProperty", null);
        validator.addField("numberProperty", new NumberValidator());
        validator.addField("defaultEnumProperty", 
            new EnumValidator("default", "one", "two", "three")
        );
        return validator;
    }

    @Override
    protected void periodic() {
        System.out.println("Example periodic.");
    }

    @Override
    protected void enterSafeState() {
        System.out.println("Example component is safe now!");
    }

    @Override
    protected void addNetworkData(SendableBuilder builder) {
        builder.addDoubleProperty("number", this::getNumber, this::setNumber);
    }

    private double getNumber() {
        return mNumberProperty;
    }

    private void setNumber(double number) {
        mNumberProperty = number;
    }
}