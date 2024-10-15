package JsonSerializer;

import com.google.gson.Gson;
import dto.DTOSheet;
import dto.DTOSheetImpl;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


public class JsonSerializer {


    public String convertDtoToJson(DTOSheet dtoSheet) {
        Gson gson = GsonUtil.createGsonWithInstanceCreators();
        return gson.toJson(dtoSheet);
    }

    public DTOSheet convertJsonToDto(String json) {
        Gson gson = GsonUtil.createGsonWithInstanceCreators();
        Type dtoSheetType = new TypeToken<DTOSheetImpl>() {}.getType();
        DTOSheet dtoSheet = gson.fromJson(json, dtoSheetType);
        JsonDTOCellValueUpdater jsonDTOCellValueUpdater = new JsonDTOCellValueUpdater();
        jsonDTOCellValueUpdater.updateDTOCellValue(dtoSheet,json);
        return dtoSheet;
    }


}


