package jaxb.schema.xmlprocessing;

import jakarta.xml.bind.JAXBException;
import jaxb.schema.generated.STLCell;
import jaxb.schema.generated.STLCells;
import jaxb.schema.generated.STLSheet;

import java.util.List;

public interface XmlProcessing {
    STLSheet parseAndValidateXml(String xmlPath) throws FileDataException, JAXBException;
    List<STLCell> getTopologicalSortOrThrowCircularReferenceException(STLCells listOfCells) throws FileDataException.CircularReferenceException;
    //void loadVersionToFile(String FilePath) throws JAXBException;
}
