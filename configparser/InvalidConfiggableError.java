package edu.boscotech.techlib.configparser;

public class InvalidConfiggableError extends Error {
    private InvalidConfiggableError(String message) {
        super(message);
    }

    public static InvalidConfiggableError
        newMissingAnnotationError(Class<?> subclass) {
        String message = "Error attempting to load a configgable type!\n";
        message += "(A configgable type is a type that can be referenced in ";
        message += "the robot.cfg file.)\n";
        message += "The type that was being loaded was: ";
        message += subclass.getName() + "\n";
        message += "ERROR: Because the type inherits either directly or ";
        message += "implicitly from IConfiggable, it must be annotated with ";
        message += "either @RealConfiggable or @AbstractConfiggable, but ";
        message += "neither is present. Use the first for types that can be ";
        message += "referenced in robot.cfg and the second for types that ";
        message += "cannot (usually an abstract class.)\n";
        return new InvalidConfiggableError(message);
    }

    public static InvalidConfiggableError 
        newInvalidSuperclassError(Class<?> subclass) {
        String message = "Error attempting to load a configgable type!\n";
        message += "(A configgable type is a type that can be referenced in ";
        message += "the robot.cfg file.)\n";
        message += "The type that was being loaded was: ";
        message += subclass.getName() + "\n";
        message += "ERROR: The type must extend ConfigObject, Component, ";
        message += "BetterSubsystem, or Controller, either directly or ";
        message += "indirectly.\n";
        return new InvalidConfiggableError(message);
    }

    public static InvalidConfiggableError 
        newMissingValidatorError(Class<?> subclass) {
        String message = "Error attempting to load a configgable type!\n";
        message += "(A configgable type is a type that can be referenced in ";
        message += "the robot.cfg file.)\n";
        message += "The type that was being loaded was: ";
        message += subclass.getName() + "\n";
        message += "ERROR: The type must have a static method named ";
        message += "'createValidator' which takes no arguments and returns ";
        message += "a non-null instance of an object implementing ";
        message += "IElementValidator.\n";
        return new InvalidConfiggableError(message);
    }

    public static InvalidConfiggableError
        newOverlappingNameError(Class<?> newClass, Class<?> overlappedClass) {
        String message = "Error attempting to load a configgable type!\n";
        message += "(A configgable type is a type that can be referenced in ";
        message += "the robot.cfg file.)\n";
        message += "The type that was being loaded was: ";
        message += newClass.getName() + "\n";
        message += "ERROR: The ConfigName given for this class (which is the ";
        message += "name used to reference this type in the robot.cfg file), ";
        message += "conflicts with the name of another type that was already ";
        message += "loaded. The class for the other type is '";
        message += overlappedClass.getName() + "'. Either rename or delete ";
        message += "one of the types to resolve the error.";
        return new InvalidConfiggableError(message);
    }

    public static InvalidConfiggableError
        newDefaultConstructorError(Class<?> clas) {
        String message = "Error attempting to load a configgable type!\n";
        message += "(A configgable type is a type that can be referenced in ";
        message += "the robot.cfg file.)\n";
        message += "The type that was being loaded was: ";
        message += clas.getName() + "\n";
        message += "ERROR: Could not find a public constructor that takes no ";
        message += "arguments! Any configgable type must define a constructor ";
        message += "which takes no arguments. Initialization is done in the ";
        message += "Init method instead of inside the constructor.";
        return new InvalidConfiggableError(message);
    }
}