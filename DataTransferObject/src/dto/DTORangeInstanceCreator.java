package dto;
import com.google.gson.InstanceCreator;
import java.lang.reflect.Type;

public class DTORangeInstanceCreator implements InstanceCreator<DTORange> {

    @Override
    public DTORange createInstance(Type type) {
        return new DTORangeImpl();
    }
}
