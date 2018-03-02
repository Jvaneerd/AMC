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
public class FalseLiteral extends Formula {

    public FalseLiteral() {
        super(FormulaType.TRUE);
    }

    @Override
    public String toString() {
        return "false";
    }
}
