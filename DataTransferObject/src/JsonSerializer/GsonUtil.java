package JsonSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.DTOCell;
import dto.DTOCellInstanceCreator;
import dto.DTORange;
import dto.DTORangeInstanceCreator;
import sheetmanager.sheet.effectivevalue.EffectiveValue;

public class GsonUtil {

    public static Gson createGsonWithInstanceCreators() {
        return new GsonBuilder()
                .registerTypeAdapter(DTOCell.class, new DTOCellInstanceCreator())  // רישום InstanceCreator עבור DTOCell
                .registerTypeAdapter(DTORange.class, new DTORangeInstanceCreator())  // רישום InstanceCreator עבור DTORange
                .registerTypeAdapter(EffectiveValue.class, new EffectiveValueAdapter()) // Add TypeAdapter for EffectiveValue
                .create();
    }
}
