package dto;

public class DTOSheetInfo {
    private String sheetName;
    private int numRows;
    private int numCols;
    public String uploadBy;

    public DTOSheetInfo(String sheetName, int numRows, int numCols,String uploadBy) {
        this.sheetName = sheetName;
        this.numRows = numRows;
        this.numCols = numCols;
        this.uploadBy = uploadBy;
    }

    public String getSheetName() {
        return sheetName;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public String getUploadBy() {
        return uploadBy;
    }
}
