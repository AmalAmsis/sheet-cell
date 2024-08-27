package expression;

import expression.exception.InvalidOperationNameException;
import expression.impl.primitive.BooleanExpression;
import expression.impl.primitive.NumericExpression;
import expression.impl.primitive.StringExpression;
import sheet.SheetDataRetriever;
import sheet.coordinate.Coordinate;
import sheet.effectivevalue.CellType;
import sheet.effectivevalue.EffectiveValue;
import sheet.effectivevalue.EffectiveValueImpl;


import java.util.ArrayList;
import java.util.List;


public class ExpressionEvaluator {

    /**
     * Evaluates a given str string and returns its EffectiveValue.
     * The str can be a numeric value, a boolean value, a function, or a string.
     *
     * @param str the string representation of the str to evaluate
     * @return the evaluated EffectiveValue
     */
    public static EffectiveValue evaluate(String str, SheetDataRetriever sheet, Coordinate targetCoordinate , boolean isArgument) {

        // Handle empty or whitespace-only strings
        if (str == null || str.trim().isEmpty()) {
            return new EffectiveValueImpl(CellType.EMPTY, str);
        }

        if(!isArgument)
        {
            str = str.trim();
        }


        // Handle numeric values
        if (isNumeric(str)) {
            return new NumericExpression(Double.parseDouble(str)).evaluate();
        }

        // Handle boolean values
        if (str.toUpperCase().equals("TRUE")) {
            return new BooleanExpression(true).evaluate();
        } else if (str.toUpperCase().equals("FALSE")) {
            return new BooleanExpression(false).evaluate();
        }

        // Handle function expressions
        String strWithoutWhiteSpaces = str.trim();
        if (strWithoutWhiteSpaces.startsWith("{") && strWithoutWhiteSpaces.endsWith("}")) {
            strWithoutWhiteSpaces = strWithoutWhiteSpaces.substring(1, strWithoutWhiteSpaces.length() - 1).trim();
            String[] parts = splitByComma(strWithoutWhiteSpaces);
            String operationName = parts[0].trim().toUpperCase();


            Operation operation = Operation.fromString(operationName);
            if (parts.length - 1 != operation.getNumberOfArguments()) {
                throw new IllegalArgumentException("Incorrect number of arguments for operation: " + operationName + ", expected " + operation.getNumberOfArguments() + ", got " + (parts.length-1));
            }

            Expression[] expressions = new Expression[operation.getNumberOfArguments()];
            for (int i = 0; i < expressions.length; i++) {
                expressions[i] = parseExpression(parts[i + 1], sheet, targetCoordinate);
            }

            return operation.eval(sheet, targetCoordinate, expressions);

        }

        // If none of the conditions match, treat as a string str
        return new StringExpression(str).evaluate();
    }

    /**
     * Checks if a given string represents a numeric value.
     *
     * @param str the string to check
     * @return true if the string is numeric, false otherwise
     */
    private static boolean isNumeric(String str) {
        // Trim the string and check if it is different from the original string
        // str = "    5" is not a double
        if (!str.trim().equals(str)) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Splits an expression string by commas while respecting nested braces.
     *
     * @param expression the expression string to split
     * @return an array of strings split by commas
     */
    private static String[] splitByComma(String expression) {
        List<String> parts = new ArrayList<>();
        StringBuilder currentPart = new StringBuilder();
        int bracesLevel = 0;

        for (char c : expression.toCharArray()) {
            if (c == '{') {
                bracesLevel++;
            } else if (c == '}') {
                bracesLevel--;
            }

            if (c == ',' && bracesLevel == 0) {
                parts.add(currentPart.toString());
                currentPart.setLength(0);
            } else {
                currentPart.append(c);
            }
        }

        if (currentPart.length() > 0) {
            parts.add(currentPart.toString());
        }

        return parts.toArray(new String[0]);
    }

    /**
     * Parses a string expression into an Expression object.
     * This method is called recursively to handle nested functions within arguments.
     *
     * @param str the string expression to parse
     * @return the corresponding Expression object
     */
    private static Expression parseExpression(String str, SheetDataRetriever sheet, Coordinate targetCoordinate) {
        boolean isArgument = true;
        EffectiveValue effectiveValue = evaluate(str, sheet, targetCoordinate , isArgument);
        return convertEffectiveValueToExpression(effectiveValue);
    }

    /**
     * Converts an EffectiveValue to its corresponding Expression object.
     *
     * @param value the EffectiveValue to convert
     * @return the corresponding Expression object
     */
    private static Expression convertEffectiveValueToExpression(EffectiveValue value) {
        CellType cellType = value.getCellType();
        switch (cellType) {
            case BOOLEAN:
                return new BooleanExpression(value.extractValueWithExpectation(Boolean.class));
            case NUMERIC:
                return new NumericExpression(value.extractValueWithExpectation(Double.class));
            case STRING:
                return new StringExpression(value.extractValueWithExpectation(String.class));
            default:
                throw new IllegalArgumentException("Unexpected cell type: " + cellType);
        }
    }
}
