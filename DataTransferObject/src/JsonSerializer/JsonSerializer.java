package JsonSerializer;

import com.google.gson.Gson;
import dto.DTOSheet;
import dto.DTOSheetImpl;
import com.google.gson.reflect.TypeToken;
import dto.DTOSheetInfo;

import java.lang.reflect.Type;
import java.util.List;


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

    public String convertDTOSheetInfoListToJson(List<DTOSheetInfo> listOfDtoSheetInfo) {
        Gson gson = GsonUtil.createGsonWithInstanceCreators();
        return gson.toJson(listOfDtoSheetInfo);
    }

    public List<DTOSheetInfo> convertJsonToDtoInfoList(String json) {
        Gson gson = GsonUtil.createGsonWithInstanceCreators();
        Type dtoSheetType = new TypeToken<List<DTOSheetInfo>>() {}.getType();
        return gson.fromJson(json, dtoSheetType);
    }

    public String convertDTOSheetInfoToJson(DTOSheetInfo dtoSheetInfo) {
        Gson gson = GsonUtil.createGsonWithInstanceCreators();
        return gson.toJson(dtoSheetInfo);
    }

    public DTOSheetInfo convertJsonToDTOSheetInfo(String json) {
        Gson gson = GsonUtil.createGsonWithInstanceCreators();
        Type dtoSheetType = new TypeToken<DTOSheetInfo>() {}.getType();
        return gson.fromJson(json, dtoSheetType);
    }


}


