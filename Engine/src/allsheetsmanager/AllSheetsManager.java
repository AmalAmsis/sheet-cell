package allsheetsmanager;

import sheetmanager.SheetManager;
import sheetmanager.SheetManagerImpl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class AllSheetsManager {

    private final Map<String,SheetManager> allSheetsMap;

    public AllSheetsManager() {
        this.allSheetsMap = new HashMap<>();
    }

    public synchronized void addSheet(InputStream fileContent, String fileName) throws Exception {

        if(allSheetsMap.containsKey(fileName)) {
            throw new Exception("The file is already exist");
        }
        SheetManager newSheetManager = new SheetManagerImpl();
        newSheetManager.loadsheetFromStream(fileContent,fileName);

        allSheetsMap.put(fileName,newSheetManager);
    }

}
