package edu.boscotech.techlib.formula;

class AddFormula extends ParentFormula {
    @Override
    protected double computeValue() {
        double result = 0.0;
        for (Formula child : getChildren()) {
            result += child.getValue();
        }
        return result;
    }
}