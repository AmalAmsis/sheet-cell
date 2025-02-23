package JsonSerializer;

import dto.DTOSheet;
import jakarta.xml.bind.JAXBException;
import sheetmanager.SheetManager;
import sheetmanager.SheetManagerImpl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;



public class jasonTest {

    public static void main(String[] args) throws FileNotFoundException, JAXBException {

        String filePath = "C:/Users/Yarden Daniel/Downloads/grades (1) (2).xml";
        SheetManager sheetManager = new SheetManagerImpl();
        String fileName = "grades.xml";

        try {
            InputStream inputStream = new FileInputStream(filePath);
            sheetManager.loadsheetFromStream( inputStream,  fileName);

            DTOSheet dtoSheet =  sheetManager.displaySheet();

            JsonSerializer jsonSerializer = new JsonSerializer();
            String json = jsonSerializer.convertDtoToJson(dtoSheet);

            System.out.println(json);

            DTOSheet dtoSheet2 =  jsonSerializer.convertJsonToDto(json);

            JsonDTOCellValueUpdater jsonDTOCellValueUpdater = new JsonDTOCellValueUpdater();
            jsonDTOCellValueUpdater.updateDTOCellValue(dtoSheet2,json);

            //printSheetToConsole(dtoSheet2);


        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("The specified file was not found. Please check the file path and ensure that the file exists.");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
