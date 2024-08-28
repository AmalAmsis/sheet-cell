package test;

import dto.DTOSheetImpl;
import manager.UIManager;
import manager.UIManagerImpl;
import sheet.Sheet;
import sheet.SheetImpl;
import sheet.coordinate.CoordinateImpl;
import sheet.coordinate.Coordinate;

import javax.swing.*;


public class Test {
    public static void main(String[] args) {

//        UIManager manager = new UIManagerImpl();
//        manager.printMenu();
//        String title = "Test1";
//        int numOfRows = 5;
//        int numOfCols = 6;
//
//        Sheet sheet = new SheetImpl(title, numOfRows, numOfCols, 5, 5);
//        Coordinate coordinate1 = new CoordinateImpl('A', 1);
//        Coordinate coordinate2 = new CoordinateImpl('B', 2);
//        Coordinate coordinate3 = new CoordinateImpl('A', 2);
//        Coordinate coordinate4 = new CoordinateImpl('B', 1);
//        sheet.setCell(coordinate1, "1");
//        sheet.setCell(coordinate2, "{ref,A1}");
//        sheet.setCell(coordinate3, "{Plus,2,3}");
//        DTOSheetImpl sheetDTO = new DTOSheetImpl(sheet);
//        UIManagerImpl.printSheetToConsole(sheetDTO);

        //בדיקת טעינת קובץ

        String filePath1 = "C:/Users/Amal Amsis/Desktop/AmalA/Computer Science/JAVA/sheet-cell/Engine/src/resources/basic.xml";
        String filePath2 = "C:/Users/Amal Amsis/Desktop/AmalA/Computer Science/JAVA/sheet-cell/Engine/src/resources/error-2.xml";
        String filePath3 = "C:/Users/Amal Amsis/Desktop/AmalA/Computer Science/JAVA/sheet-cell/Engine/src/resources/error-4.xml";
        String filePath4 = "C:\\Users\\Yarden Daniel\\Desktop\\computer science\\year 2\\semester 3\\java\\projects\\sheet_cell\\sheet-cell\\Engine\\src\\resources\\insurance.xml";
        String filePath5 = "C:\\Users\\Yarden Daniel\\Desktop\\computer science\\year 2\\semester 3\\java\\projects\\sheet_cell\\sheet-cell\\Engine\\src\\resources\\notExist.xml";
        String filePath6 = "C:\\Users\\Yarden Daniel\\Desktop\\computer science\\year 2\\semester 3\\java\\projects\\sheet_cell\\sheet-cell\\Engine\\src\\resources\\notXmlFile.xl";
        String filePath8 = "C:/Users/Yarden Daniel/Desktop/computer science/year 2/semester 3/java/projects/sheet_cell/sheet-cell/Engine/src/resources/basic2.xml";

        String YfilePath1 = "C:/Users/Yarden Daniel/Desktop/computer science/year 2/semester 3/java/projects/sheet_cell/sheet-cell/Engine/src/resources/basic2.xml";
        String YfilePath2 = "C:/Users/Yarden Daniel/Desktop/computer science/year 2/semester 3/java/projects/sheet_cell/sheet-cell/Engine/src/resources/basic.xml";
    }
}


//        System.out.println("Test 2 : load basic.xml");
//        manager.loadXmlFile(YfilePath1);
//        manager.displaySheet();
//
////        System.out.println("Test 3 : load error-2.xml");
////        manager.loadXmlFile(filePath2);
////        manager.displaySheet();
//
//
//        System.out.println("Test 3 : load error-4.xml");
//        manager.loadXmlFile(YfilePath2);
//        manager.displaySheet();
//
//    }
//}
