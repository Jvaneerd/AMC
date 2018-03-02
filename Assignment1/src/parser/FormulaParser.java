/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import model.formula.BoxFormula;
import model.formula.DiamondFormula;
import model.formula.FalseLiteral;
import model.formula.Formula;
import model.formula.LogicFormula;
import model.formula.LogicOperator;
import model.formula.MuFormula;
import model.formula.NuFormula;
import model.formula.RecursionVariable;
import model.formula.TrueLiteral;

/**
 *
 * @author s163360
 */
public class FormulaParser {

    private String formula;
    private int parseIndex;

    public Formula parse(String formula) throws ParseException {
        this.formula = formula;
        this.parseIndex = 0;

        Formula f = parseFormula();

        if (parseIndex != formula.length()) {
            throw new ParseException();
        } else {
            return f;
        }
    }

    private Formula parseFormula() throws ParseException {
        skipWhitespace();
        String currentChar = formula.substring(parseIndex, parseIndex + 1);

        Formula f;
        if (currentChar.matches("t")) {
            f = parseTrueLiteral();
        } else if (currentChar.matches("f")) {
            f = parseFalseLiteral();
        } else if (currentChar.matches("[A-Z]")) {
            f = parseRecursionVariable();
        } else if (currentChar.matches("\\(")) {
            f = parseLogicFormula();
        } else if (currentChar.matches("m")) {
            f = parseMuFormula();
        } else if (currentChar.matches("n")) {
            f = parseNuFormula();
        } else if (currentChar.matches("\\<")) {
            f = parseDiamondFormula();
        } else if (currentChar.matches("\\[")) {
            f = parseBoxFormula();
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

    private Formula parseRecursionVariable() throws ParseException {
        String name = formula.substring(parseIndex, parseIndex + 1);
        parseIndex++;
        return new RecursionVariable(name);
    }

    private Formula parseLogicFormula() throws ParseException {
        expect("(");

        Formula lhs = parseFormula();

        LogicOperator operator;

        String currentChar = formula.substring(parseIndex, parseIndex + 1);
        if (currentChar.matches("[|&]")) {
            operator = parseOperator();
        } else {
            throw new ParseException("Expected logic operator but got: " + currentChar);
        }
        skipWhitespace();

        Formula rhs = parseFormula();
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

    private Formula parseMuFormula() throws ParseException {
        expect("mu ");
        skipWhitespace();

        Formula variable;
        String currentChar = formula.substring(parseIndex, parseIndex + 1);
        if (currentChar.matches("[A-Z]")) {
            variable = parseRecursionVariable();
        } else {
            throw new ParseException("Expected variable name but got: " + currentChar);
        }

        expect(".");

        Formula f = parseFormula();
        return new MuFormula(variable, f);
    }

    private Formula parseNuFormula() throws ParseException {
        expect("nu ");
        skipWhitespace();

        Formula variable;
        String currentChar = formula.substring(parseIndex, parseIndex + 1);
        if (currentChar.matches("[A-Z]")) {
            variable = parseRecursionVariable();
        } else {
            throw new ParseException("Expected variable name but got: " + currentChar);
        }

        expect(".");

        Formula f = parseFormula();
        return new NuFormula(variable, f);
    }

    private Formula parseDiamondFormula() throws ParseException {
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

        Formula f = parseFormula();
        return new DiamondFormula(action, f);
    }

    private Formula parseBoxFormula() throws ParseException {
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

        Formula f = parseFormula();
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
