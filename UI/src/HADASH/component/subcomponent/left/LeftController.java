package HADASH.component.subcomponent.left;

import HADASH.component.main.app.AppController;

public class LeftController {

    private AppController appController;

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void initialize() {
        appController = new AppController();
    }

}
