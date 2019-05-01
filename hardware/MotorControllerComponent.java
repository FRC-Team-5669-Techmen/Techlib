package edu.boscotech.techlib.hardware;

import edu.boscotech.techlib.TechlibRobot;
import edu.boscotech.techlib.configparser.AbstractConfiggable;
import edu.boscotech.techlib.configparser.ConfigElement;
import edu.boscotech.techlib.validator.ObjectValidator;
import edu.boscotech.techlib.validator.ValidatorFactory;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

@AbstractConfiggable
public abstract class MotorControllerComponent extends Component {
    private double mScale, mLimit, mCountsPerUnit;
    private boolean mReversed;
    private double mCurrentSpeed = 0.0;

    protected MotorControllerComponent(String smartDashboardTypeName) {
        super(smartDashboardTypeName);
    }

    protected abstract void extraSetup(TechlibRobot robot, 
        ConfigElement config);
    protected abstract ObjectValidator extraCreateValidator();
    protected abstract void setRawMotorSpeed(double speed);

    @Override
    protected final void setup(TechlibRobot robot, ConfigElement configTree) {
        mScale = configTree.getNumberOrDefault(1.0, "scale");
        mLimit = configTree.getNumberOrDefault(1.0, "limit");
        mCountsPerUnit = configTree.getNumberOrDefault(1.0, "countsPerUnit");
        mReversed = configTree.getBooleanOrDefault(false, "reversed");
        extraSetup(robot, configTree);
    }

    @Override
    protected ObjectValidator createValidator() {
        ObjectValidator validator = extraCreateValidator();
        validator.addOptionalField(
            "scale", ValidatorFactory.numberRange(0.0, 1.0)
        );
        validator.addOptionalField(
            "limit", ValidatorFactory.numberRange(0.0, 1.0)
        );
        validator.addOptionalField(
            "countsPerUnit", ValidatorFactory.numberRange(0.0, null)
        );
        validator.addOptionalField(
            "reversed", ValidatorFactory.boolean_()
        );
        return validator;
    }

    @Override
    protected final void periodic() {
        setRawMotorSpeed(mCurrentSpeed);
    }

    @Override
    protected final void enterSafeState() {
        setSpeed(0.0);
    }

    @Override
    protected final void addNetworkData(SendableBuilder builder) {
        builder.addDoubleProperty("speed", this::getSpeed, this::setSpeed);
        builder.addDoubleProperty("actualSpeed", this::getActualSpeed, this::setSpeed);
        builder.addBooleanProperty("reversed", this::isReversed, this::setReversed);
    }

    public final void setSpeed(double speed) {
        mCurrentSpeed = speed;
        setRawMotorSpeed(speed);
    }

    public final double getSpeed() {
        return mCurrentSpeed;
    }

    private double getActualSpeed() {
        double actualSpeed = mCurrentSpeed * (mReversed ? -1 : 1) * mScale;
        if (Math.abs(actualSpeed) > mLimit) {
            actualSpeed = mLimit * (actualSpeed > 0 ? 1 : -1);
        }
        return actualSpeed;
    }

    private void setReversed(boolean reversed) {
        mReversed = reversed;
    }

    public final boolean isReversed() {
        return mReversed;
    }
}