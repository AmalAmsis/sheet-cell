package uploadfilemanager;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import uploadfilemanager.jaxb.generated.STLCell;
import uploadfilemanager.jaxb.generated.STLCells;
import uploadfilemanager.jaxb.generated.STLLayout;
import uploadfilemanager.jaxb.generated.STLSheet;
import uploadfilemanager.jaxb.xmlprocessing.CellGraphValidator;
import uploadfilemanager.jaxb.xmlprocessing.FileDataException;
import sheetmanager.sheet.range.RangeManager;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class LoadFile {

    //todo - check it
    private final String JAXB_XML_STL_PACKAGE_NAME = "uploadfilemanager.jaxb.generated";

    public STLSheet parseAndValidatefile(InputStream inputStream, String fileName) throws FileDataException, JAXBException, FileNotFoundException {
        //1.is the file a xml file
        isXmlFile(fileName);
        //2.file load STLSheet = fromXmlFileToXmlFile(inputPath)
        STLSheet stlSheet = fromInputStreamToSTLSheet(inputStream);
        if(stlSheet == null){
            throw new IllegalStateException("Failed to process the XML file. The file may be corrupted or improperly formatted. Please check the file.");
        }
        //3.rowsValidation
        int rowCount = stlSheet.getSTLLayout().getRows();
        numOfRowsValidation(rowCount);
        //4.columnsValidation
        int columnCount = stlSheet.getSTLLayout().getColumns();
        numOfColumnsValidation(columnCount);
        //5.columnWidthValidation
        int columnWidth = stlSheet.getSTLLayout().getSTLSize().getColumnWidthUnits();
        columnWidthValidation(columnWidth);
        //6.owHeightValidation
        int rowHeight = stlSheet.getSTLLayout().getSTLSize().getRowsHeightUnits();
        rowHeightValidation(rowHeight);
        //7. Checking that all the cells are within the borders of the sheet
        CellOutOfBoundsValidation(stlSheet.getSTLCells(), stlSheet.getSTLLayout());
        return stlSheet;
    }

    public STLSheet fromInputStreamToSTLSheet(InputStream inputStream) throws JAXBException {

        STLSheet stlSheet = null;
        try {
            stlSheet = deserializeFrom(inputStream);
        } catch (JAXBException e) {
            throw new JAXBException("Failed to process the XML file. \nThe file may be corrupted or improperly formatted. \nPlease check the file.");
        }
        return stlSheet;
    }

    public STLSheet deserializeFrom(InputStream inputXmlFilePath) throws JAXBException {
        //Create JAXB context for the generated classes
        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_XML_STL_PACKAGE_NAME);
        //Create an Unmarshaller to convert XML to Java objects
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        //Read the XML file and convert it to a Java object
        return (STLSheet) jaxbUnmarshaller.unmarshal(inputXmlFilePath);
    }

    public void isXmlFile(String file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("The file path you provided is either empty or missing. Please provide a valid file path.");
        }

        if (!file.toLowerCase().endsWith(".xml")) {
            throw new IllegalArgumentException("The provided file is not an XML file. \nPlease provide a file with the '.xml' extension.");
        }
    }

    /**
     * Validates the number of rows in the sheet.
     * @param rowCount the number of rows to validate.
     * @throws FileDataException.InvalidRowCountException if the number of rows is invalid.
     */
    public void numOfRowsValidation(int rowCount) throws FileDataException.InvalidRowCountException {
        if (rowCount  < 1 || rowCount  >50) {
            throw new FileDataException.InvalidRowCountException(rowCount);
        }
    }

    /**
     * Validates the number of columns in the sheet.
     * @param columnCount the number of columns to validate.
     * @throws FileDataException.InvalidColumnCountException if the number of columns is invalid.*/
    public void numOfColumnsValidation(int columnCount) throws FileDataException.InvalidColumnCountException {
        if (columnCount < 1 || columnCount > 20) {
            throw new FileDataException.InvalidColumnCountException(columnCount);
        }
    }

    /**
     * Validates the width of the columns in the sheet.
     * @param columnWidth the column width to validate.
     * @throws FileDataException.InvalidColumnWidthException if the column width is invalid.*/
    public void columnWidthValidation(int columnWidth) throws FileDataException.InvalidColumnWidthException {
        if (columnWidth <= 0) {
            throw new FileDataException.InvalidColumnWidthException(columnWidth);
        }
    }

    /**
     * Validates the height of the rows in the sheet.
     * @param rowHeight the row height to validate.
     * @throws FileDataException.InvalidRowHeightException if the row height is invalid.*/
    public void rowHeightValidation(int rowHeight) throws FileDataException.InvalidRowHeightException {
        if (rowHeight <= 0) {
            throw new FileDataException.InvalidRowHeightException(rowHeight);
        }
    }

    /**
     * Validates that all cells in the sheet are within the defined boundaries.
     * @param listOfCells the STLCells object containing the cells to validate.
     * @param stlLayout the layout of the sheet to validate against.
     * @throws FileDataException.CellOutOfBoundsException if any cell is out of bounds.*/
    public void CellOutOfBoundsValidation(STLCells listOfCells, STLLayout stlLayout) throws FileDataException.CellOutOfBoundsException {
        if (listOfCells == null){
            return;
        }
        for(STLCell stlCell: listOfCells.getSTLCell()){
            isCellOutOfBounds(stlCell,stlLayout);

        }
    }

    /**
     * Checks if a specific cell is out of bounds based on the sheet layout.
     * @param stlCell the STLCell to validate.
     * @param stlLayout the layout of the sheet.
     * @throws FileDataException.CellOutOfBoundsException if the cell is out of bounds.*/
    public void isCellOutOfBounds(STLCell stlCell, STLLayout stlLayout) throws FileDataException.CellOutOfBoundsException {
        String column = stlCell.getColumn().toUpperCase();
        int row = stlCell.getRow();

        if (column.length() != 1 || column.charAt(0) < 'A' || column.charAt(0) > getLastColLetter(stlLayout.getColumns())) {
            throw new FileDataException.CellOutOfBoundsException();
        }

        if (row <1 || row > stlLayout.getRows()) {
            throw new FileDataException.CellOutOfBoundsException();
        }


    }

    public List<STLCell> getTopologicalSortOrThrowCircularReferenceException(STLCells listOfCells, RangeManager rangeManager) throws FileDataException.CircularReferenceException {
        CellGraphValidator cellGraphValidator = new CellGraphValidator(listOfCells, rangeManager);
        return cellGraphValidator.topologicalSort();
    }


    /**
     * Returns the letter of the last column based on the number of columns.
     * @param numOfCols the number of columns in the sheet.
     * @return a char representing the last column letter.*/
    public char getLastColLetter(int numOfCols){
        return (char) ('A' + numOfCols - 1);
    }
}
