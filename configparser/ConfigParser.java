package edu.boscotech.techlib.configparser;

import java.util.ArrayList;
import java.util.List;

class ConfigParser {
    enum Mode {
        INDENT, NAME, VALUE
    };

//    private static String strip(String string) {
//        String buffer = "", output = "";
//        boolean first = true;
//        for (char c : string.toCharArray()) {
//            if (c >= '\0' && c <= ' ') {
//                if (first) {
//                    first = false;
//                } else {
//                    // Only add in whitespace that is after the first character
//                    // and is followed by more characters.
//                    output += buffer;
//                }
//                buffer = "";
//                output += c;
//            } else {
//                buffer += c;
//            }
//        }
//        return output;
//    }

    private static List<String> splitIntoSymbols(String string) {
        String buffer = "";
        List<String> symbols = new ArrayList<>();
        for (char c : string.toCharArray()) {
            if (c >= '\0' && c <= ' ' && !buffer.isEmpty()) {
                symbols.add(buffer);
                buffer = "";
            } else {
                buffer += c;
            }
        }
        if (!buffer.isEmpty()) {
            symbols.add(buffer);
        }
        return symbols;
    }

    public static ConfigElement parse(String configData) {
        List<ConfigElement> stack = new ArrayList<>();
        ConfigElement currentObject = new ConfigElement("root", null, null);
        ConfigElement lastAddedObject = null;
        Mode mode = Mode.INDENT;
        int indentAmount = 0, newIndentAmount = 0;
        List<Integer> indentStack = new ArrayList<>();
        indentStack.add(indentAmount);
        String nameBuffer = "";
        String valueBuffer = "";
        for (int i = 0; i < configData.length(); i++) {
            char c = configData.charAt(i);

            if (c == '\r') continue;

            if (c == '\n') {
                if (mode == Mode.VALUE) {
                    List<String> words = splitIntoSymbols(nameBuffer);
                    String name = words.get(0);
                    String type = "";
                    // TODO: Add syntax errors.
                    if (words.size() > 1 && words.get(1).equals("is")) { // User specified a type.
                        type = words.get(2);
                    }
                    String value = valueBuffer.trim();
                    lastAddedObject = new ConfigElement(name, type, value);
                    currentObject.addChild(lastAddedObject);
                }
                nameBuffer = "";
                valueBuffer = "";
                mode = Mode.INDENT;
                continue;
            }

            if (mode == Mode.INDENT) {
                if (c == '\t' || c == ' ') {
                    newIndentAmount++;
                } else {
                    if (newIndentAmount > indentAmount) {
                        indentStack.add(newIndentAmount);
                        stack.add(currentObject);
                        currentObject = lastAddedObject;
                        lastAddedObject = null;
                    } else if (newIndentAmount < indentAmount) {
                        // TODO: Add syntax error for mismatched indentation.
                        while (newIndentAmount != indentStack.get(indentStack.size() - 1)) {
                            indentStack.remove(indentStack.size() - 1);
                            lastAddedObject = currentObject;
                            currentObject = stack.get(stack.size() - 1);
                            stack.remove(stack.size() - 1);
                        }
                    }
                    indentAmount = newIndentAmount;
                    newIndentAmount = 0;
                    mode = Mode.NAME;
                }
            }
            
            // Not using else-if allows for one mode to transition to the next
            // and have the character get processed by the next mode.
            if (mode == Mode.NAME) {
                if (c == ':') {
                    mode = Mode.VALUE;
                    continue; // Don't parse the : with VALUE mode.
                } else {
                    nameBuffer += c;
                }
            }
            
            if (mode == Mode.VALUE) {
                valueBuffer += c;
            }
        }

        if (stack.size() > 0) {
            return stack.get(0);
        } else {
            return currentObject;
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting!");
        parse("a:\n\tb: 10\n\tc: laksjd fdsa\n\td is thing:\n\t\tprop: 13\nb is obj2:\n\tfield :what").print();
        System.out.println("Done!");
    }
}
