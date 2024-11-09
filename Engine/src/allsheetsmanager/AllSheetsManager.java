package allsheetsmanager;

import sheetmanager.SheetManager;
import sheetmanager.SheetManagerImpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllSheetsManager {

    private final Map<String,SheetManager> allSheetsMap;

    public AllSheetsManager() {
        this.allSheetsMap = new HashMap<>();
    }

    public synchronized String addSheet(InputStream fileContent, String fileName,String owner) throws Exception {


        SheetManager newSheetManager = new SheetManagerImpl();
        newSheetManager.loadsheetFromStream(fileContent,fileName);
        newSheetManager.setOwner(owner);

        String sheetTitle = newSheetManager.getTilte();
        if(allSheetsMap.containsKey(sheetTitle)) {
            throw new Exception("The file is already exist");
        }
        allSheetsMap.put(sheetTitle,newSheetManager);

        return sheetTitle;
    }

    public synchronized SheetManager getSheet(String fileName) {
        return allSheetsMap.get(fileName);
    }

    public List<String> getAllSheets() {
        return new ArrayList<>(allSheetsMap.keySet());
    }

    public Map<String,SheetManager> getAllSheetsManager(){
        return allSheetsMap;
    }
}
