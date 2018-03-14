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
public class FalseLiteral extends Formula {

    public FalseLiteral() {
        super(FormulaType.FALSE);
    }

    @Override
    public String toString() {
        return "false";
    }

    @Override
    public Set<Variable> getVariables() {
       return new HashSet<>();
    }

    @Override
    protected Set<Variable> getOpenVariables() {
        return new HashSet<>();
    }
}
