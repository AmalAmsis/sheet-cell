package jaxb.schema.xmlprocessing;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jaxb.schema.generated.STLSheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class XmlProcessingImpl /*implements XmlProcessing*/ {

    //the package path of the generated classes
    private final String JAXB_XML_STL_PACKAGE_NAME = "jaxb.schema.generated";

    /**
     * This function reads an XML file and converts it into a Java object (STLSheet)
     * using JAXB's unmarshal process.
     * If the function returns null, it means that the file was not found or an error occurred.
     *
     * @param inputPath the path to the XML file
     * @return STLSheet object representing the XML content, or null if an error occurs
     */
    //@Override
    public STLSheet fromXmlFileToXmlFile(String inputPath) throws JAXBException {

        STLSheet stlSheet = null;
        try {
            InputStream inputStream = new FileInputStream(inputPath);
            stlSheet = deserializeFrom(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace(); //to do
            //WE NEED TO RETURN TO THE UI A MESSAGE OF THE EXCEPTION
            return null;
        } catch (JAXBException e) {
            e.printStackTrace();
            //WE NEED TO RETURN TO THE UI A MESSAGE OF THE EXCEPTION
            return null;
        }
        return stlSheet;
    }

    /**
     * This function serializes an STLSheet object into an XML file at the given path.
     *
     * @param xmlFilePath the path where the XML file will be saved
     * @param stlSheet    the STLSheet object to be serialized
     * @throws JAXBException if an error occurs during marshalling
     */
    void fromObjectToXmlFile(String xmlFilePath, STLSheet stlSheet) throws JAXBException{

        try {
            File file = new File(xmlFilePath);
            JAXBContext jaxbContext = JAXBContext.newInstance(STLSheet.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(stlSheet, file);
        }
        catch (JAXBException e) {
            e.printStackTrace();
            //WE NEED TO RETURN TO THE UI A MESSAGE OF THE EXCEPTION

        }

    }

    /**
     * This function takes an InputStream representing an XML file and converts it into an STLSheet object.
     *
     * @param xmlFile the InputStream of the XML file
     * @return STLSheet object representing the XML content
     * @throws JAXBException if an error occurs during unmarshalling
     */
    public STLSheet deserializeFrom(InputStream xmlFile) throws JAXBException {
        //Create JAXB context for the generated classes
        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_XML_STL_PACKAGE_NAME);
        //Create an Unmarshaller to convert XML to Java objects
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        //Read the XML file and convert it to a Java object
        return (STLSheet) jaxbUnmarshaller.unmarshal(xmlFile);
    }

    public STLSheet validate(String xml) throws JAXBException, FileDataException.InvalidRowCountException, FileDataException.InvalidColumnCountException, FileDataException.InvalidColumnWidthException, FileDataException.InvalidRowHeightException {
        //1.the file is a xml file -->
        isXmlFile(xml);
        //2.file load STLSheet = fromXmlFileToXmlFile(inputPath)
        STLSheet stlSheet = fromXmlFileToXmlFile(xml);
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
        //7.The cells that define the use of the function are directed to the cells that contain information that
        // corresponds to the arguments of the function (Maybe we can test this as well as the creation of our objects)
        return stlSheet;
    }

    public void isXmlFile(String file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }

        if (!file.toLowerCase().endsWith(".xml")) {
            throw new IllegalArgumentException("File does not have a .xml extension");
        }
    }

    public void numOfRowsValidation(int rowCount ) throws FileDataException.InvalidRowCountException {
        if (rowCount  < 1 || rowCount  >50) {
            throw new FileDataException.InvalidRowCountException(rowCount );
        }
    }

    public void numOfColumnsValidation(int columnCount) throws FileDataException.InvalidColumnCountException {
        if (columnCount < 1 || columnCount > 20) {
            throw new FileDataException.InvalidColumnCountException(columnCount);
        }
    }

    public void columnWidthValidation(int columnWidth) throws FileDataException.InvalidColumnWidthException {
        if (columnWidth <= 0) {
            throw new FileDataException.InvalidColumnWidthException(columnWidth);
        }
    }

    public void rowHeightValidation(int rowHeight) throws FileDataException.InvalidRowHeightException {
        if (rowHeight <= 0) {
            throw new FileDataException.InvalidRowHeightException(rowHeight);
        }
    }



}