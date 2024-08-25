import expression.impl.math.Plus;
import expression.impl.primitive.NumericExpression;
import sheet.Sheet;
import sheet.SheetImpl;
import sheet.coordinate.Coordinate;
import sheet.coordinate.CoordinateImpl;

public class Main {
     public static void main(String[] args) {
       // Sheet sheet = new Sheet();
        /*
        String test ="/n/t";
        Coordinate coordinate = new CoordinateImpl('A', 1);
        Coordinate coordinate1 = new CoordinateImpl('B', 2);

        System.out.println(coordinate.toString());
        System.out.println(coordinate.hashCode());

        System.out.println(coordinate1.toString());
        System.out.println(coordinate1.hashCode());

        NumericExpression num1 = new NumericExpression(Double.NaN);
        NumericExpression num2 = new NumericExpression(5);
        Plus p = new Plus(num1, num2);
        System.out.println(p.evaluate().getValue());

         */

        // בדיקה לעבודה עם REF ומעגלים

        try {
           String title = "Test1";
           int numOfRows = 5;
           int numOfCols = 6;
           Coordinate coordinate1 = new CoordinateImpl('A', 1);
           Coordinate coordinate2 = new CoordinateImpl('B', 2);
           Coordinate coordinate3 = new CoordinateImpl('A', 2);
           Coordinate coordinate4 = new CoordinateImpl('B', 1);
           Sheet sheet = new SheetImpl(title, numOfRows, numOfCols, 10, 10);
           sheet.setCell(coordinate1, "1");
           sheet.setCell(coordinate2, "{ref , A1}");
           sheet.setCell(coordinate3, "{ref , B2}");
           sheet.setCell(coordinate1, "{ref , A2}");

        }catch (IllegalArgumentException e)
        {
           System.out.println(e);
        }

     }

}
