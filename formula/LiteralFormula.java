package edu.boscotech.techlib.formula;

class LiteralFormula extends Formula {
    private double mValue;

    public LiteralFormula(double value) {
        mValue = value;
    }

    @Override
    protected double computeValue() {
        return mValue;
    }
}