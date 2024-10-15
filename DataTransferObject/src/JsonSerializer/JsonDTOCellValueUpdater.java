package JsonSerializer;
import com.google.gson.*;
import dto.*;
import sheetmanager.sheet.effectivevalue.EffectiveValue;
import sheetmanager.sheet.effectivevalue.EffectiveValueImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonDTOCellValueUpdater {

    // השיטה שמשתמשת ב-DTOSheet שעבר מ-Gson ומעדכנת את EffectiveValue לכל תא
    public void updateDTOCellValue(DTOSheet dtoSheet, String json) {
        // פרסר JSON
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(json); // `json` is your JSON string
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject board = jsonObject.getAsJsonObject("board");

        for (Map.Entry<String, JsonElement> entry : board.entrySet()) {

            String key = entry.getKey();
            JsonElement cellElement  = entry.getValue();

            DTOCell dtoCell = dtoSheet.getCells().get(key);
            if (dtoCell != null) {
                // Extract the JSON object for the current cell

                JsonObject cellJsonObject = cellElement.getAsJsonObject();

                String cellId = cellJsonObject.get("cellId").getAsString();
                JsonObject coordinateJson = cellJsonObject.getAsJsonObject("coordinate");
                String originalValue = cellJsonObject.get("originalValue").getAsString();
                int lastModifiedVersion = cellJsonObject.get("lastModifiedVersion").getAsInt();
                int row = coordinateJson.get("row").getAsInt();
                String col = coordinateJson.get("col").getAsString();
                DTOCoordinate coordinate = new DTOCoordinateImpl(row,col.charAt(0));

                JsonObject effectiveValueJson = cellJsonObject.getAsJsonObject("effectiveValue");

                // Extract the effectiveValue JSON from the current cell
                EffectiveValue effectiveValue = createEffectiveValueFromJson(effectiveValueJson.toString());

// Extract dependsOn array
                JsonArray dependsOnArray = cellJsonObject.getAsJsonArray("dependsOn");
                List<DTOCoordinate> dependsOn = new ArrayList<>();
                for (JsonElement dependElement : dependsOnArray) {
                    JsonObject dependCoord = dependElement.getAsJsonObject();
                    int dependRow = dependCoord.get("row").getAsInt();
                    String dependCol = dependCoord.get("col").getAsString();
                    dependsOn.add(new DTOCoordinateImpl(dependRow,dependCol.charAt(0)));
                }

// Extract influencingOn array
                JsonArray influencingOnArray = cellJsonObject.getAsJsonArray("influencingOn");
                List<DTOCoordinate> influencingOn = new ArrayList<>();
                for (JsonElement influenceElement : influencingOnArray) {
                    JsonObject influenceCoord = influenceElement.getAsJsonObject();
                    int influenceRow = influenceCoord.get("row").getAsInt();
                    String influenceCol = influenceCoord.get("col").getAsString();
                    influencingOn.add(new DTOCoordinateImpl(influenceRow,influenceCol.charAt(0)));
                }

                DTOCell newDtoCell = new DTOCellImpl(cellId,coordinate,effectiveValue,originalValue,lastModifiedVersion,dependsOn,influencingOn);

                dtoSheet.getCells().put(cellId, newDtoCell);
            }
        }
    }

    // הפונקציה שיוצרת EffectiveValue מתוך JSON קטן
    private EffectiveValue createEffectiveValueFromJson(String json) {
        // דה-סיריאליזציה של ה-EffectiveValue באמצעות Gson
        return new Gson().fromJson(json, EffectiveValueImpl.class);
    }
}
