package edu.boscotech.techlib.configparser;

import java.util.ArrayList;
import java.util.List;

public class ConfigElement {
    private String m_name, m_type, m_value;
    private List<ConfigElement> m_children = new ArrayList<>();

    public ConfigElement(String name, String type, String value) {
        m_name = name;
        m_type = type;
        m_value = value;
    }

    /**
     * @return the name
     */
    public String getName() {
        return m_name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return m_type;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return m_value;
    }

    public void addChild(ConfigElement child) {
        m_children.add(child);
    }

    public List<ConfigElement> getChildren() {
        return m_children;
    }

    public ConfigElement getChild(String name) {
        for (ConfigElement child : m_children) {
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

    public String getStringOrDefault(String defaultValue, String... path) {
        ConfigElement element = getPath(path);
        return element == null ? defaultValue : element.asString();
    }

    public double getNumberOrDefault(double defaultValue, String... path) {
        ConfigElement element = getPath(path);
        return element == null ? defaultValue : element.asNumber();
    }

    public String asString() {
        return m_value;
    }

    public double asNumber() {
        return Double.parseDouble(m_value);
    }

    public String toString() {
        return toStringInternal(0);
    }

    private String toStringInternal(int indentation) {
        String result = "";
        for (int i = 0; i < indentation; i++) {
            result += '\t';
        }
        result += "name(" + m_name + ") type(" + m_type + ") value(" + m_value + ")\n";
        for (ConfigElement child : m_children) {
            result += child.toStringInternal(indentation + 1);
        }
        return result;
    }
}