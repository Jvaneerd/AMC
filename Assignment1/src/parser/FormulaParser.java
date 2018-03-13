/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.util.HashSet;
import java.util.Set;
import model.formula.BoxFormula;
import model.formula.DiamondFormula;
import model.formula.FalseLiteral;
import model.formula.Formula;
import model.formula.FormulaType;
import model.formula.LogicFormula;
import model.formula.LogicOperator;
import model.formula.MuFormula;
import model.formula.NuFormula;
import model.formula.TrueLiteral;
import model.formula.Variable;

/**
 *
 * @author s163360
 */
public class FormulaParser {

    private String formula;
    private int parseIndex;
    private Set<Variable> boundVariables;

    public Formula parse(String formula) throws ParseException {
        this.formula = formula;
        this.parseIndex = 0;
        this.boundVariables = new HashSet<>();

        Formula f = parseFormula(null);

        if (parseIndex != formula.length()) {
            throw new ParseException();
        } else {
            return f;
        }
    }

    private Formula parseFormula(FormulaType scope) throws ParseException {
        skipWhitespace();
        String currentChar = formula.substring(parseIndex, parseIndex + 1);

        Formula f;
        if (currentChar.matches("t")) {
            f = parseTrueLiteral();
        } else if (currentChar.matches("f")) {
            f = parseFalseLiteral();
        } else if (currentChar.matches("[A-Z]")) {
            f = parseVariable();
        } else if (currentChar.matches("\\(")) {
            f = parseLogicFormula(scope);
        } else if (currentChar.matches("m")) {
            f = parseMuFormula(scope);
        } else if (currentChar.matches("n")) {
            f = parseNuFormula(scope);
        } else if (currentChar.matches("\\<")) {
            f = parseDiamondFormula(scope);
        } else if (currentChar.matches("\\[")) {
            f = parseBoxFormula(scope);
        } else {
            throw new ParseException("Cannot parse formula starting with: " + currentChar);
        }

        skipWhitespace();
        return f;

    }

    private void skipWhitespace() {
        while (parseIndex < formula.length() && formula.substring(parseIndex, parseIndex + 1).matches("[ \n\r\t\f%]")) {
            parseIndex = formula.substring(parseIndex, parseIndex + 1).equals("%") ? formula.indexOf("\n", parseIndex) : parseIndex + 1;
        }
    }

    private void expect(String expect) throws ParseException {
        if (formula.substring(parseIndex, parseIndex + expect.length()).equals(expect)) {
            parseIndex += expect.length();
        } else {
            throw new ParseException("Expected " + expect + " but got: " + formula.substring(parseIndex, parseIndex + expect.length()));
        }
    }

    private Formula parseTrueLiteral() throws ParseException {
        expect("true");
        return new TrueLiteral();
    }

    private Formula parseFalseLiteral() throws ParseException {
        expect("false");
        return new FalseLiteral();
    }

    private Variable parseBoundVariable(FormulaType scope) throws ParseException {
        String name = formula.substring(parseIndex, parseIndex + 1);
        parseIndex++;
        return new Variable(name, scope);
    }

    private Variable parseVariable() throws ParseException {
        String name = formula.substring(parseIndex, parseIndex + 1);
        parseIndex++;
        for (Variable var : boundVariables) {
            if (var.getName().equals(name)) {
                return var;
            }
        }
        return new Variable(name, FormulaType.FREE);
    }

    private Formula parseLogicFormula(FormulaType scope) throws ParseException {
        expect("(");

        Formula lhs = parseFormula(scope);

        LogicOperator operator;

        String currentChar = formula.substring(parseIndex, parseIndex + 1);
        if (currentChar.matches("[|&]")) {
            operator = parseOperator();
        } else {
            throw new ParseException("Expected logic operator but got: " + currentChar);
        }
        skipWhitespace();

        Formula rhs = parseFormula(scope);
        expect(")");

        return new LogicFormula(operator, lhs, rhs);

    }

    private LogicOperator parseOperator() throws ParseException {
        if (formula.substring(parseIndex, parseIndex + 1).equals("&")) {
            expect("&&");
            return LogicOperator.AND;
        } else {
            expect("||");
            return LogicOperator.OR;
        }
    }

    private Formula parseMuFormula(FormulaType scope) throws ParseException {
        expect("mu ");
        skipWhitespace();

        Variable variable;
        String currentChar = formula.substring(parseIndex, parseIndex + 1);
        if (currentChar.matches("[A-Z]")) {
            variable = parseBoundVariable(FormulaType.MU);
            boundVariables.add(variable);
        } else {
            throw new ParseException("Expected variable name but got: " + currentChar);
        }

        expect(".");

        Formula f = parseFormula(FormulaType.MU);
        boundVariables.remove(variable);
        return new MuFormula(variable, f, scope);
    }

    private Formula parseNuFormula(FormulaType scope) throws ParseException {
        expect("nu ");
        skipWhitespace();

        Variable variable;
        String currentChar = formula.substring(parseIndex, parseIndex + 1);
        if (currentChar.matches("[A-Z]")) {
            variable = parseBoundVariable(FormulaType.NU);
            boundVariables.add(variable);
        } else {
            throw new ParseException("Expected variable name but got: " + currentChar);
        }

        expect(".");

        Formula f = parseFormula(FormulaType.NU);
        boundVariables.remove(variable);
        return new NuFormula(variable, f, scope);
    }

    private Formula parseDiamondFormula(FormulaType scope) throws ParseException {
        expect("<");
        skipWhitespace();

        String action = "";
        String currentChar = formula.substring(parseIndex, parseIndex + 1);
        if (currentChar.matches("[a-z]")) {
            action = parseActionName();
        } else {
            throw new ParseException("Expected action name but got: " + currentChar);
        }

        expect(">");

        Formula f = parseFormula(scope);
        return new DiamondFormula(action, f);
    }

    private Formula parseBoxFormula(FormulaType scope) throws ParseException {
        expect("[");
        skipWhitespace();

        String action = "";
        String currentChar = formula.substring(parseIndex, parseIndex + 1);
        if (currentChar.matches("[a-z]")) {
            action = parseActionName();
        } else {
            throw new ParseException("Expected action name but got: " + currentChar);
        }

        expect("]");

        Formula f = parseFormula(scope);
        return new BoxFormula(action, f);
    }

    private String parseActionName() throws ParseException {
        String action = "";

        while (formula.substring(parseIndex, parseIndex + 1).matches("[a-z]")) {
            action += formula.substring(parseIndex, parseIndex + 1);
            parseIndex++;
        }
        return action;
    }

}
