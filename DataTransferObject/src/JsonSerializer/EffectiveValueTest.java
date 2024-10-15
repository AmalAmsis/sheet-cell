package JsonSerializer;

import com.google.gson.Gson;
import jakarta.xml.bind.JAXBException;
import sheetmanager.sheet.effectivevalue.CellType;
import sheetmanager.sheet.effectivevalue.EffectiveValue;
import sheetmanager.sheet.effectivevalue.EffectiveValueImpl;

import java.io.FileNotFoundException;

public class EffectiveValueTest {

    public static void main(String[] args) throws FileNotFoundException, JAXBException {
        EffectiveValue effectiveValue = new EffectiveValueImpl(CellType.NUMERIC, 123.45);
        Gson gson = GsonUtil.createGsonWithInstanceCreators();
        String json = gson.toJson(effectiveValue);
        System.out.println("Serialized: " + json);

        EffectiveValue deserializedValue = gson.fromJson(json, EffectiveValue.class);
        System.out.println("Deserialized: " + deserializedValue);

    }
}
