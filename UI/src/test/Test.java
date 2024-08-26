package test;

import dto.DTOSheetImpl;
import manager.UIManagerImpl;
import sheet.Sheet;
import sheet.SheetImpl;
import sheet.coordinate.CoordinateImpl;
import sheet.coordinate.Coordinate;

import javax.swing.*;


public class Test {
    public static void main(String[] args) {
        String title = "Test1";
        int numOfRows = 5;
        int numOfCols = 6;

        Sheet sheet = new SheetImpl(title, numOfRows, numOfCols, 5, 5);
        Coordinate coordinate1 = new CoordinateImpl('A', 1);
        Coordinate coordinate2 = new CoordinateImpl('B', 2);
        Coordinate coordinate3 = new CoordinateImpl('A', 2);
        Coordinate coordinate4 = new CoordinateImpl('B', 1);
        sheet.setCell(coordinate1, "1");
        sheet.setCell(coordinate2, "{ref,A1}");
        sheet.setCell(coordinate3, "{Plus,2,3}");
        DTOSheetImpl sheetDTO = new DTOSheetImpl(sheet);
        UIManagerImpl.printSheetToConsole(sheetDTO);



    }
}
