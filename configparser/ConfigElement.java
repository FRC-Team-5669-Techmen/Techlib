package edu.boscotech.techlib.configparser;

import java.util.ArrayList;
import java.util.List;

public class ConfigElement {
    private String name, type, value;
    private List<ConfigElement> subValues = new ArrayList<>();

    public ConfigElement(String name, String type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    public void addChild(ConfigElement child) {
        subValues.add(child);
    }

    public List<ConfigElement> getChildren() {
        return subValues;
    }

    public void print() {
        print(0);
    }

    public void print(int indentation) {
        for (int i = 0; i < indentation; i++) {
            System.out.print('\t');
        }
        System.out.println("name(" + name + ") type(" + type + ") value(" + value + ")");
        for (ConfigElement subValue : subValues) {
            subValue.print(indentation + 1);
        }
    }
}