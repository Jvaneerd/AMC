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
public class BoxFormula extends ModalFormula {

    public BoxFormula(String action, Formula formula) {
        super(FormulaType.BOX, action, formula);
    }

    @Override
    public String toString() {
        return "[" + action + "]" + formula.toString();
    }

}
