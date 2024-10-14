package dto;

import com.google.gson.InstanceCreator;
import java.lang.reflect.Type;

public class DTOCellInstanceCreator implements InstanceCreator<DTOCell> {

    @Override
    public DTOCell createInstance(Type type) {
        return new DTOCellImpl();
    }
}
