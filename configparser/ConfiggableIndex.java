package edu.boscotech.techlib.configparser;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.reflections.Reflections;

import edu.boscotech.techlib.hardware.Component;
import edu.boscotech.techlib.objects.ConfigObject;
import edu.boscotech.techlib.subsystems.BetterSubsystem;
import edu.boscotech.techlib.subsystems.Controller;
import edu.boscotech.techlib.validator.IElementValidator;
import junit.framework.AssertionFailedError;

public class ConfiggableIndex {
    private static ConfiggableIndex sInstance = new ConfiggableIndex();
    private Map<String, Class<?>> 
        mObjects = new HashMap<>(),
        mComponents = new HashMap<>(), 
        mSubsystems = new HashMap<>(),
        mControllers = new HashMap<>(),
        mAllTypes = new HashMap<>();
    private Map<String, IElementValidator> mValidators = new HashMap<>();
    private boolean mIndexBuilt = false;

    private ConfiggableIndex() { }

    public static ConfiggableIndex getInstance() {
        return sInstance;
    }

    public void buildIndex() {
        if (mIndexBuilt) return;
        Reflections searcher = new Reflections("");
        for (Class<?> clas : searcher.getSubTypesOf(IConfiggable.class)) {
            boolean problem = true;
            for (Annotation annotation : clas.getAnnotations()) {
                if (
                    annotation.annotationType().equals(AbstractConfiggable.class)
                    || annotation.annotationType().equals(RealConfiggable.class)
                ) {
                    problem = false;
                    break;
                }
            }
            if (problem) {
                throw InvalidConfiggableError.newMissingAnnotationError(clas);
            }
        }
        for (Class<?> clas : searcher.getTypesAnnotatedWith(RealConfiggable.class)) {
            String configTypeName 
                = clas.getAnnotation(RealConfiggable.class).value();
            Class<?> superclass = clas.getSuperclass();
            while (!superclass.equals(Object.class) 
                && !superclass.equals(Component.class)
                && !superclass.equals(BetterSubsystem.class)
                && !superclass.equals(Controller.class)) {
                superclass = superclass.getSuperclass();
            }
            if (superclass.equals(ConfigObject.class)) {
                mObjects.put(configTypeName, clas);
            } else if (superclass.equals(Component.class)) {
                mComponents.put(configTypeName, clas);
            } else if (superclass.equals(BetterSubsystem.class)) {
                mSubsystems.put(configTypeName, clas);
            } else if (superclass.equals(Controller.class)) {
                mControllers.put(configTypeName, clas);
            } else {
                throw InvalidConfiggableError.newInvalidSuperclassError(clas);
            }
            if (mAllTypes.containsKey(configTypeName)) {
                throw InvalidConfiggableError.newOverlappingNameError(
                    clas, mAllTypes.get(configTypeName)
                );
            }
            mAllTypes.put(configTypeName, clas);
            try {
                mValidators.put(
                    configTypeName, 
                    ((IConfiggable) clas.getConstructor().newInstance())
                        .createValidatorWrapper()
                );
            } catch (Exception e) {
                throw InvalidConfiggableError.newDefaultConstructorError(clas);
            }
        }
        mIndexBuilt = true;
    }

    public IElementValidator getValidator(String typeName) {
        return mValidators.get(typeName);
    }

    public Object newInstanceOf(String typeName) throws ClassNotFoundException {
        if (!mAllTypes.containsKey(typeName)) {
            throw new ClassNotFoundException(
                "No such configgable type named " + typeName
            );
        }
        try {
            return mAllTypes.get(typeName).getConstructor().newInstance();
        } catch (Exception e) {
            // The default constructor of every class is tested when the index
            // is built. If we get here, something has gone very wrong.
            throw new AssertionFailedError("We should not get here.");
        }
    }

    public ConfigObject newInstanceOfObject(String typeName) 
        throws ClassNotFoundException {
        if (!mObjects.containsKey(typeName)) {
            throw new ClassNotFoundException(
                "No such configgable type named " + typeName
            );
        }
        try {
            return (ConfigObject) 
                mObjects.get(typeName).getConstructor().newInstance();
        } catch (Exception e) {
            // The default constructor of every class is tested when the index
            // is built. If we get here, something has gone very wrong.
            throw new AssertionFailedError("We should not get here.");
        }
    }

    public Component newInstanceOfComponent(String typeName) 
        throws ClassNotFoundException {
        if (!mComponents.containsKey(typeName)) {
            throw new ClassNotFoundException(
                "No such configgable type named " + typeName
            );
        }
        try {
            return (Component) 
                mComponents.get(typeName).getConstructor().newInstance();
        } catch (Exception e) {
            // The default constructor of every class is tested when the index
            // is built. If we get here, something has gone very wrong.
            throw new AssertionFailedError("We should not get here.");
        }
    }

    public BetterSubsystem newInstanceOfSubsystem(String typeName) 
        throws ClassNotFoundException {
        if (!mSubsystems.containsKey(typeName)) {
            throw new ClassNotFoundException(
                "No such configgable type named " + typeName
            );
        }
        try {
            return (BetterSubsystem) 
                mSubsystems.get(typeName).getConstructor().newInstance();
        } catch (Exception e) {
            // The default constructor of every class is tested when the index
            // is built. If we get here, something has gone very wrong.
            throw new AssertionFailedError("We should not get here.");
        }
    }

    public Controller newInstanceOfController(String typeName) 
        throws ClassNotFoundException {
        if (!mControllers.containsKey(typeName)) {
            throw new ClassNotFoundException(
                "No such configgable type named " + typeName
            );
        }
        try {
            return (Controller) 
                mControllers.get(typeName).getConstructor().newInstance();
        } catch (Exception e) {
            // The default constructor of every class is tested when the index
            // is built. If we get here, something has gone very wrong.
            throw new AssertionFailedError("We should not get here.");
        }
    }

    public static void main(String[] args) {
        System.out.println("Start!");
        getInstance().buildIndex();
        System.out.println("Done!");
    }
}