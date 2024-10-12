package allsheetsmanager;

import sheetmanager.SheetManager;
import sheetmanager.SheetManagerImpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AllSheetsManagerImpl {




    private final List<SheetManager> sheetsDataList;


    public AllSheetsManagerImpl() {
        this.sheetsDataList = new ArrayList<>();
    }

    public synchronized void addSheet(InputStream fileContent, String fileName) throws Exception {

        for (SheetManager sheetManager : this.sheetsDataList) {

            String sheetTitle = sheetManager.getCurrentSheetState().getCurrentSheet().getTitle();
            if(fileName.equals(sheetTitle)) {
                throw new Exception("The file is already exist");
            }
        }

        SheetManager newSheetManager = new SheetManagerImpl();
        newSheetManager.loadsheetFromStream(fileContent,fileName);

        sheetsDataList.add(newSheetManager);
    }

}
