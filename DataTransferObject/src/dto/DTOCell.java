package dto;

import java.util.List;

public interface DTOCell {

    //getters
    int getRow();
    int getCol();
    Object getEffectiveValue();
    String getOriginalValue();
    int getLastModifiedVersion();
    List<DTOCell> getDependsOn();
    List<DTOCell> getInfluencingOn();

    //setter
    void setRow(int row);
    void setCol(int col);
    void setEffectiveValue(Object o);
    void setOriginalValue(String o);
    void setLastModifiedVersion(int version);
    void addDTOCellToDependsOn(DTOCell dtoCell);
    void addDTOCellToInfluencingOn(DTOCell dtoCell);


    void addDTOCellToDependsOn();
}
