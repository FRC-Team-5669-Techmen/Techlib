package edu.boscotech.techlib.formula;

class DivideFormula extends ParentFormula {
    @Override
    protected double computeValue() {
        double result = getChildren().get(0).getValue();
        for (int i = 1; i < getChildren().size(); i++) {
            result /= getChildren().get(i).getValue();
        }
        return result;
    }
}