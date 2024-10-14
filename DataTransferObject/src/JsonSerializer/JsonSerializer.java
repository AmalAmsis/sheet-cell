package JsonSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.DTOSheet;
import dto.DTOSheetImpl;

public class JsonSerializer {


    public String convertDtoToJson(DTOSheet dtoSheet) {
        Gson gson = GsonUtil.createGsonWithInstanceCreators();
        return gson.toJson(dtoSheet);
    }

    public DTOSheet convertJsonToDto(String json) {
        Gson gson = GsonUtil.createGsonWithInstanceCreators();
        return gson.fromJson(json, DTOSheetImpl.class);
    }


}


