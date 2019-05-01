package edu.boscotech.techlib.configparser;

import java.util.ArrayList;
import java.util.List;

public class ConfigElement {
    private String mName, mType, mValue;
    private List<ConfigElement> mChildren = new ArrayList<>();

    public ConfigElement(String name, String type, String value) {
        mName = name;
        mType = type;
        mValue = value;
    }

    /**
     * @return the name
     */
    public String getName() {
        return mName;
    }

    /**
     * @return the type
     */
    public String getType() {
        return mType;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return mValue;
    }

    public void addChild(ConfigElement child) {
        mChildren.add(child);
    }

    public List<ConfigElement> getChildren() {
        return mChildren;
    }

    public ConfigElement getChild(String name) {
        for (ConfigElement child : mChildren) {
            if (child.getName() == name) {
                return child;
            }
        }
        return null;
    }

    public ConfigElement getPath(String... path) {
        ConfigElement current = this;
        for (String name : path) {
            current = current.getChild(name);
            if (current == null) return null;
        }
        return current;
    }

    public String getString(String... path) {
        return getPath(path).asString();
    }

    public double getNumber(String... path) {
        return getPath(path).asNumber();
    }

    public boolean getBoolean(String... path) {
        return getPath(path).asBoolean();
    }

    public String getStringOrDefault(String defaultValue, String... path) {
        ConfigElement element = getPath(path);
        return element == null ? defaultValue : element.asString();
    }

    public double getNumberOrDefault(double defaultValue, String... path) {
        ConfigElement element = getPath(path);
        return element == null ? defaultValue : element.asNumber();
    }

    public boolean getBooleanOrDefault(boolean defaultValue, String... path) {
        ConfigElement element = getPath(path);
        return element == null ? defaultValue : element.asBoolean();
    }

    public String asString() {
        return mValue;
    }

    public double asNumber() {
        return Double.parseDouble(mValue);
    }

    public boolean asBoolean() {
        return mValue == "true";
    }

    public String toString() {
        return toStringInternal(0);
    }

    private String toStringInternal(int indentation) {
        String result = "";
        for (int i = 0; i < indentation; i++) {
            result += '\t';
        }
        result += "name(" + mName + ") type(" + mType + ") value(" + mValue + ")\n";
        for (ConfigElement child : mChildren) {
            result += child.toStringInternal(indentation + 1);
        }
        return result;
    }
}