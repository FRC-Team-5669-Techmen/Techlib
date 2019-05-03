package edu.boscotech.techlib.formula;

import java.util.ArrayList;
import java.util.List;

abstract class ParentFormula extends Formula {
    private List<Formula> mChildren = new ArrayList<>();

    protected final List<Formula> getChildren() { return mChildren; }

    public final void addChild(Formula child) { mChildren.add(child); }
}