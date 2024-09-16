package component.subcomponent.popup.viewonlysheet;

import component.subcomponent.sheet.UIModelSheet;

public class UIModelViewOnlySheet extends UIModelSheet {

    @Override
    public UIModelViewOnlySheet copyModel() {
        UIModelViewOnlySheet newModel = new UIModelViewOnlySheet();
        copyCellsTo(newModel);
        return newModel;
    }

}
