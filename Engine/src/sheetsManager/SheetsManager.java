package sheetsManager;

import engine.Engine;
import engine.EngineImpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SheetsManager {

    private final List<Engine> sheetsDataList;


    public SheetsManager() {
        this.sheetsDataList = new ArrayList<>();
    }

    public synchronized void addSheet(InputStream fileContent, String fileName) throws Exception {

        for (Engine engine : this.sheetsDataList) {

            String sheetTitle = engine.getCurrentSheetState().getCurrentSheet().getTitle();
            if(fileName.equals(sheetTitle)) {
                throw new Exception("The file is already exist");
            }
        }

        Engine newEngine = new EngineImpl();
        newEngine.loadsheetFromStream(fileContent,fileName);

        sheetsDataList.add(newEngine);
    }

}
