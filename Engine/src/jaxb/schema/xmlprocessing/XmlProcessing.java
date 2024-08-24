package jaxb.schema.xmlprocessing;

import jakarta.xml.bind.JAXBException;
import jaxb.schema.generated.STLSheet;

public interface XmlProcessing {
    STLSheet parseAndValidateXml(String xmlPath) throws JAXBException, FileDataException.InvalidRowCountException, FileDataException.InvalidColumnCountException, FileDataException.InvalidColumnWidthException, FileDataException.InvalidRowHeightException;
    void loadVersionToFile(String FilePath) throws JAXBException;
}
