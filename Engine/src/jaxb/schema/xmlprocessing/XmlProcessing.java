package jaxb.schema.xmlprocessing;

import jakarta.xml.bind.JAXBException;
import jaxb.schema.generated.STLSheet;

public interface XmlProcessing {
    STLSheet validate(String xml);
    void fromObjectToXmlFile(String xmlFilePath, STLSheet stlSheet) throws JAXBException;
    STLSheet fromXmlFileToXmlFile(String xmlFile) throws JAXBException;
}
