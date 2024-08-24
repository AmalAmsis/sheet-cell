import expression.impl.math.Plus;
import expression.impl.primitive.NumericExpression;
import sheet.coordinate.Coordinate;
import sheet.coordinate.CoordinateImpl;

public class Main {
public static void main(String[] args) {
   // Sheet sheet = new Sheet();
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
    System.out.println(p.evaluate());
}

}
