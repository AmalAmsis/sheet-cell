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
}

}
