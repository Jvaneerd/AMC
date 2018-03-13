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
public class Variable extends Formula {

    private final String name;
    private final FormulaType scope;

    public Variable(String name, FormulaType scope) {
        super(FormulaType.VARIABLE);
        this.name = name;
        this.scope = scope;
    }

    public String getName() {
        return name;
    }

    public FormulaType getScope() {
        return scope;
    }

    @Override
    public Set<Variable> getVariables() {
        Set<Variable> vars = new HashSet<>();
        vars.add(this);
        return vars;
    }

    @Override
    protected Set<Variable> getOpenVariables() {
        return getVariables();
    }
    
    @Override
    public String toString(){
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Variable)) {
            return false;
        }
        Variable otherMyClass = (Variable) other;
        return otherMyClass.name.equals(this.name);
    }

}
