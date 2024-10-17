package component.selectedSheetView.subcomponent.left;

import dto.DTOSheet;

public interface SheetCallback {
    void onSheetReceived(DTOSheet dtoSheet);
    void onError(String error);
}
