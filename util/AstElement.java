package edu.boscotech.techlib.util;

import java.util.ArrayList;
import java.util.List;

public class AstElement {
    public String value = "";
    public List<AstElement> children = new ArrayList<>();

    public AstElement() { }

    public AstElement(String value) {
        this.value = value;
    }

    public void add(AstElement child) {
        children.add(child);
    }

    /**
     * Returns last child of this AstElement.
     * @see pop
     */
    public AstElement peek() {
        return children.get(children.size() - 1);
    }

    /**
     * Returns last child of this AstElement, and removes it from the list of
     * children.
     * @see peek
     */
    public AstElement pop() {
        AstElement last = peek();
        children.remove(children.size() - 1);
        return last;
    }

    @Override
    public String toString() {
        String result = value;
        if (children.size() > 0) {
            result = "{" + result;
            if (!value.isEmpty()) result += ": ";
            for (AstElement child : children) {
                result += child.toString();
                result += ", ";
            }
            result = result.substring(0, result.length() - 2);
            result += "}";
        }
        return result;
    }
}