/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.formula;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author s163360
 */
public class RecursionVariable extends Formula {

    private final String name;
    private FormulaType scope;

    public RecursionVariable(String name, FormulaType scope) {
        super(FormulaType.VARIABLE);
        this.name = name;
        if(scope == null) this.scope = FormulaType.FREE;
        else this.scope = scope;
    }
    
    public String getName() {
        return name;
    }
    
    public FormulaType getScope() {
        return scope;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Set<RecursionVariable> getRecursionVariables() {
        return new HashSet<>();
    }

}
