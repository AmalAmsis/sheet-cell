import expression.ExpressionEvaluator;
import expression.impl.math.Plus;
import expression.impl.primitive.NumericExpression;
import expression.impl.primitive.StringExpression;
import expression.impl.str.Concat;
import sheet.Sheet;
import sheet.SheetImpl;
import sheet.coordinate.Coordinate;
import sheet.coordinate.CoordinateImpl;
import sheet.effectivevalue.CellType;
import sheet.effectivevalue.EffectiveValue;
import sheet.effectivevalue.EffectiveValueImpl;

public class Main {
    public static void main(String[] args) {

        String title = "Test1";
        int numOfRows = 5;
        int numOfCols = 6;

        Sheet sheet = new SheetImpl(title, numOfRows, numOfCols, 10, 10);
        Coordinate coordinate = new CoordinateImpl('A', 1);

        // Valid expressions
       String expression0 = "   5";
       EffectiveValue result0 = ExpressionEvaluator.evaluate(expression0, sheet, coordinate, false);
       System.out.println("Result of " + expression0 + " = " + result0.getValue());

        try {
            String expression1 = "{PLUS, 4,5}";
            EffectiveValue result1 = ExpressionEvaluator.evaluate(expression1, sheet, coordinate, false);
            System.out.println("Result of " + expression1 + " = " + result1.getValue());
        }catch (IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
        try {
            String expression2 = "{MINUS, {PLUS,4,5}, {POW,2,3}}";
            EffectiveValue result2 = ExpressionEvaluator.evaluate(expression2, sheet, coordinate, false);
            System.out.println("Result of " + expression2 + " = " + result2.getValue());
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }

        String expression3 = "TRUE";
        EffectiveValue result3 = ExpressionEvaluator.evaluate(expression3, sheet, coordinate, false);
        System.out.println("Result of " + expression3 + " = " + result3.getValue());

        String expression4 = "12345";
        EffectiveValue result4 = ExpressionEvaluator.evaluate(expression4, sheet, coordinate, false);
        System.out.println("Result of " + expression4 + " = " + result4.getValue());

        String expression5 = "{CONCAT, Hello ,    World}";
        EffectiveValue result5 = ExpressionEvaluator.evaluate(expression5, sheet, coordinate, false);
        System.out.println("Result of " + expression5 + " = " + result5.getValue());

        // Invalid expressions
        try {
            String expression6 = "{NON_EXISTENT_OPERATION,4,5}";
            EffectiveValue result6 = ExpressionEvaluator.evaluate(expression6, sheet, coordinate, false);
            System.out.println("Result of " + expression6 + " = " + result6.getValue());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid operation caught: " + e.getMessage());
        }

        try {
            String expression7 = "{PLUS, 4}";
            EffectiveValue result7 = ExpressionEvaluator.evaluate(expression7, sheet, coordinate, false);
            System.out.println("Result of " + expression7 + " = " + result7.getValue());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid argument count caught: " + e.getMessage());
        }

        // Test with a string that should be interpreted as a string
        String expression8 = "   This is a test string";
        EffectiveValue result8 = ExpressionEvaluator.evaluate(expression8, sheet, coordinate, false);
        System.out.println("Result of " + expression8 + " = " + result8.getValue());

        // Test with a string that does not start with '{' and is not a number or boolean
        String expression9 = "JustAnotherString";
        EffectiveValue result9 = ExpressionEvaluator.evaluate(expression9, sheet, coordinate, false);
        System.out.println("Result of " + expression9 + " = " + result9.getValue());

        // Test with a floating-point number
        String expression10 = "3.14159";
        EffectiveValue result10 = ExpressionEvaluator.evaluate(expression10, sheet, coordinate, false);
        System.out.println("Result of " + expression10 + " = " + result10.getValue());

        // Test with a boolean false
        String expression11 = "FALSE";
        EffectiveValue result11 = ExpressionEvaluator.evaluate(expression11, sheet, coordinate, false);
        System.out.println("Result of " + expression11 + " = " + result11.getValue());
    }
}


//public class Main {
//     public static void main(String[] args) {
//       // Sheet sheet = new Sheet();
//        /*
//        String test ="/n/t";
//        Coordinate coordinate = new CoordinateImpl('A', 1);
//        Coordinate coordinate1 = new CoordinateImpl('B', 2);
//
//        System.out.println(coordinate.toString());
//        System.out.println(coordinate.hashCode());
//
//        System.out.println(coordinate1.toString());
//        System.out.println(coordinate1.hashCode());
//
//        NumericExpression num1 = new NumericExpression(Double.NaN);
//        NumericExpression num2 = new NumericExpression(5);
//        Plus p = new Plus(num1, num2);
//        System.out.println(p.evaluate().getValue());
//
//         */
//
//        // בדיקה לעבודה עם REF ומעגלים
//
//        StringExpression str1 = new StringExpression("HELLO");
//        StringExpression str2 = new StringExpression("    World");
//        Concat c = new Concat(str1, str2);
//        System.out.println(c.evaluate().getValue());
//
//        try {
//           String title = "Test1";
//           int numOfRows = 5;
//           int numOfCols = 6;
//           Coordinate coordinate1 = new CoordinateImpl('A', 1);
//           Coordinate coordinate2 = new CoordinateImpl('B', 2);
//           Coordinate coordinate3 = new CoordinateImpl('A', 2);
//           Coordinate coordinate4 = new CoordinateImpl('B', 1);
//           Sheet sheet = new SheetImpl(title, numOfRows, numOfCols, 10, 10);
//           sheet.setCell(coordinate1, "1");
//           sheet.setCell(coordinate2, "{ref,A1}");
//           sheet.setCell(coordinate3, "{ref,B2}");
//           sheet.setCell(coordinate1, "{ref,A2}");
//
//        }catch (Exception e)
//        {
//           System.out.println(e.getMessage());
//        }
//
//        EffectiveValue effectiveValue1 = new EffectiveValueImpl(CellType.NUMERIC, 500000.987);
//        EffectiveValue effectiveValue2 = new EffectiveValueImpl(CellType.NUMERIC, 1562566);
//        EffectiveValue effectiveValue3 = new EffectiveValueImpl(CellType.NUMERIC, Double.NaN);
//        EffectiveValue effectiveValue4 = new EffectiveValueImpl(CellType.NUMERIC, 700);
//        EffectiveValue effectiveValue5 = new EffectiveValueImpl(CellType.NUMERIC, 0.65);
//        EffectiveValue effectiveValue6 = new EffectiveValueImpl(CellType.STRING, "hello");
//        EffectiveValue effectiveValue7 = new EffectiveValueImpl(CellType.BOOLEAN, true);
//
//        System.out.println(effectiveValue1);
//        System.out.println(effectiveValue2);
//        System.out.println(effectiveValue3);
//        System.out.println(effectiveValue4);
//        System.out.println(effectiveValue5);
//        System.out.println(effectiveValue6);
//        System.out.println(effectiveValue7);
//
//
//     }
//}
