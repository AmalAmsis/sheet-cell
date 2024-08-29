package jaxb.schema.xmlprocessing;

import jakarta.xml.bind.JAXBException;
import jaxb.schema.generated.STLCell;
import jaxb.schema.generated.STLCells;
import jaxb.schema.generated.STLSheet;
import java.util.List;

/**
 * XmlProcessing defines methods for processing and validating XML data related to spreadsheets.
 * This interface provides functions for parsing and validating XML files,
 * as well as for performing topological sorting on spreadsheet cells to detect circular references.
 */
public interface XmlProcessing {

    /** Parses and validates an XML file containing spreadsheet data.
     * The method ensures that the XML conforms to the expected structure and content rules.
     * @param xmlPath the file path of the XML document to parse and validate.
     * @return an STLSheet object representing the parsed and validated spreadsheet.
     * @throws FileDataException if the XML data is invalid or cannot be parsed.
     * @throws JAXBException if there is an issue with the XML binding process. */
    STLSheet parseAndValidateXml(String xmlPath) throws FileDataException, JAXBException;

    /** Performs a topological sort on a list of cells to ensure there are no circular references.
     * If a circular reference is detected, an exception is thrown.
     * @param listOfCells the STLCells object containing the list of cells to sort.
     * @return a List of STLCell objects sorted in topological order.
     * @throws FileDataException.CircularReferenceException if a circular reference is detected among the cells. */
    List<STLCell> getTopologicalSortOrThrowCircularReferenceException(STLCells listOfCells) throws FileDataException.CircularReferenceException;
}
