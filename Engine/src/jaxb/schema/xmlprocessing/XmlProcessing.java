package jaxb.schema.xmlprocessing;

import jakarta.xml.bind.JAXBException;
import jaxb.schema.generated.STLSheet;

public interface XmlProcessing {
    STLSheet parseAndValidateXml(String xmlPath) throws FileDataException, JAXBException;
    //void loadVersionToFile(String FilePath) throws JAXBException;
}
