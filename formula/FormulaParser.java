package edu.boscotech.techlib.formula;

import java.util.List;
import java.util.Stack;

import edu.boscotech.techlib.util.AstElement;

public class FormulaParser {
    private enum SearchFor {
        EXPRESSION_OR_UNARY_OPERATOR,
        EXPRESSION_ONLY,
        BINARY_OPERATOR_ONLY,
        CFU_FROM,
        CFU_CHOICE_EXPRESSION,
        CFU_COMMA_OR_USING,
        CFU_USING_EXPRESSION
    }

    private static Formula treeToFormula(AstElement tree) {
        ParentFormula result;
        if (tree.value == "ADD") result = new AddFormula();
        else if (tree.value == "SUBTRACT") result = new SubtractFormula();
        else if (tree.value == "MULTIPLY") result = new MultiplyFormula();
        else if (tree.value == "DIVIDE") result = new DivideFormula();
        else {
            if (tree.children.size() == 0) {
                return new LiteralFormula(Double.parseDouble(tree.value));
            } else {
                throw new Error("No such operation: " + tree.value);
            }
        }
        
        for (AstElement child : tree.children) {
            result.addChild(treeToFormula(child));
        }

        return result;
    }

    // TODO: Add syntax errors
    private static AstElement treeify(AstElement flat) {
        AstElement previous, result = flat, buffer;
        int shoveCounter;

        previous = result;
        result = new AstElement();
        shoveCounter = 0;
        for (AstElement child : previous.children) {
            if (shoveCounter > 0) {
                result.peek().add(child);
                shoveCounter--;
            } else if (child.value.equals("*")) {
                AstElement piece = new AstElement("MULTIPLY");
                piece.add(result.pop());
                result.add(piece);
                shoveCounter = 1;
            } else if (child.value.equals("/")) {
                AstElement piece = new AstElement("DIVIDE");
                piece.add(result.pop());
                result.add(piece);
                shoveCounter = 1;
            } else {
                result.add(child);
            }
        }

        previous = result;
        result = new AstElement();
        shoveCounter = 0;
        for (AstElement child : previous.children) {
            if (shoveCounter > 0) {
                result.peek().add(child);
                shoveCounter--;
            } else if (child.value.equals("+")) {
                AstElement piece = new AstElement("ADD");
                piece.add(result.pop());
                result.add(piece);
                shoveCounter = 1;
            } else if (child.value.equals("-")) {
                AstElement piece = new AstElement("SUBTRACT");
                piece.add(result.pop());
                result.add(piece);
                shoveCounter = 1;
            } else {
                result.add(child);
            }
        }

        assert result.children.size() == 1;
        return result.children.get(0);
    }

    enum TokenizerMode {
        DEFAULT,
        FLOAT, // Reading a floating point number.
        EXPONENT, // Exponent at the end of a float, after the 'e'.

    }

    // TODO: Add syntax errors.
    public static Formula parse(String text) {
        String buffer = "";
        Stack<AstElement> stack = new Stack<>();
        AstElement flat = new AstElement();
        for (char c : text.toCharArray()) {
            if (c <= ' ') {
                if (!buffer.isEmpty()) {
                    flat.add(new AstElement(buffer));
                    buffer = "";
                }
            } else if (
                c >= '0' && c <= '9' 
                || c >= 'A' && c <= 'Z' 
                || c >= 'a' && c <= 'z' 
                || c == '.'
            ) {
                buffer += c;
            } else if (c == '(') {
                if (!buffer.isEmpty()) {
                    flat.add(new AstElement(buffer));
                    buffer = "";
                }
                stack.push(flat);
                flat = new AstElement();
            } else if (c == ')') {
                if (!buffer.isEmpty()) {
                    flat.add(new AstElement(buffer));
                    buffer = "";
                }
                AstElement tree = treeify(flat);
                flat = stack.pop();
                flat.add(tree);
            } else {
                if (!buffer.isEmpty()) {
                    flat.add(new AstElement(buffer));
                    buffer = "";
                }
                flat.add(new AstElement("" + c));
            }
        }
        if (!buffer.isEmpty()) {
            flat.add(new AstElement(buffer));
        }
        return treeToFormula(treeify(flat));
    }

    public static void main(String[] args) {
        double result = parse("(1 + 2) * 3 / 4").getValue();
        System.out.println(result);
    }
}