/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.formula;

/**
 *
 * @author s163360
 */
public class MuFormula extends FixpointFormula {

    public MuFormula(RecursionVariable variable, Formula formula) {
        super(FormulaType.MU, variable, formula);
    }

    @Override
    public String toString() {
        return "mu " + variable.toString() + "." + formula.toString();
    }

}
