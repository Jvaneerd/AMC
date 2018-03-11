/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelChecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.formula.FixpointFormula;
import model.formula.Formula;
import model.formula.FormulaType;
import model.formula.LogicFormula;
import model.formula.ModalFormula;
import model.formula.MuFormula;
import model.formula.NuFormula;
import model.formula.RecursionVariable;
import model.lts.LTS;
import model.lts.Node;

/**
 *
 * @author s163360
 */
public class EmersonLeiAlgorithm extends BasicAlgorithm {

    private Map<RecursionVariable, List<FixpointFormula>> variableScopes;
    
    @Override
    protected Set<Node> recursiveCheckFormula(LTS lts, Formula formula) {
        switch (formula.getType()) {
            case MU:
                return checkMuFormula(lts, (MuFormula) formula);
            case NU:
                return checkNuFormula(lts, (NuFormula) formula);
            default:
                return super.recursiveCheckFormula(lts, formula);
        }
    }

    @Override
    protected void initializeVariables(LTS lts, Formula formula) {
        variableScopes = new HashMap<>();
        variableAssignments = new HashMap<>();
        Set<RecursionVariable> foo = formula.getRecursionVariables();
        foo.forEach((var) -> {
            FormulaType t = var.getScope();
            if(null != t) switch (t) {
                case MU:
                    variableAssignments.put(var.getName(), new HashSet<>());
                    break;
                case NU:
                    variableAssignments.put(var.getName(), new HashSet<>(lts.getNodes()));
                    break;
                case FREE:
                    throw new UnsupportedOperationException("Free variables not supported yet.");
                default:
                    break;
            }
            List<FixpointFormula> varScope = new ArrayList<>();
            if(!findVariable(formula, var, varScope)) throw new UnsupportedOperationException("Free variables not supported yet.");
            else variableScopes.put(var, varScope);
        });
    }
    
    private boolean findVariable(Formula f, RecursionVariable r, List<FixpointFormula> s) {
        switch(f.getType()) {
            case VARIABLE: 
                return f==r;
            case LOGIC:
                return findVariable(((LogicFormula) f).getLhs(), r, s) || findVariable(((LogicFormula) f).getRhs(), r, s);
            case MU:
                MuFormula mf = (MuFormula) f;
                if(mf.getVariable() == r) return true;
                else {
                    boolean found = findVariable(mf.getFormula(), r, s);
                    if(found) s.add(mf);
                    return found;
                }
            case NU:
                NuFormula nf = (NuFormula) f;
                if(nf.getVariable() == r) return true;
                else {
                    boolean found = findVariable(nf.getFormula(), r, s);
                    if(found) s.add(nf);
                    return found;
                }
            case TRUE:
                return false;
            case FALSE:
                return false;
            case BOX:
                return findVariable(((ModalFormula) f).getFormula(), r, s);
            case DIAMOND:
                return findVariable(((ModalFormula) f).getFormula(), r, s);
            default:
                return false;
        }
    }

    @Override
    protected Set<Node> checkMuFormula(LTS lts, MuFormula formula) {
        RecursionVariable var = formula.getVariable();
        Set<Node> oldSolution = variableAssignments.get(var.getName());
        variableAssignments.put(var.getName(), recursiveCheckFormula(lts, formula.getFormula()));
        
        while (!oldSolution.equals(variableAssignments.get(var.getName()))) {
            oldSolution = variableAssignments.get(var.getName());
            variableAssignments.put(var.getName(), recursiveCheckFormula(lts, formula.getFormula()));
        }
        return variableAssignments.get(var.getName());
    }

    @Override
    protected Set<Node> checkNuFormula(LTS lts, NuFormula formula) {
        RecursionVariable var = formula.getVariable();
        Set<Node> oldSolution = variableAssignments.get(var.getName());
        variableAssignments.put(var.getName(), recursiveCheckFormula(lts, formula.getFormula()));
        
        while (!oldSolution.equals(variableAssignments.get(var.getName()))) {
            oldSolution = variableAssignments.get(var.getName());
            variableAssignments.put(var.getName(), recursiveCheckFormula(lts, formula.getFormula()));
        }
        return variableAssignments.get(var.getName());
    }

    
}
