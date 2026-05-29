import com.cityproject.model.factory.BuildingFactory;
import com.cityproject.model.type.BuildingType;

public class TestFactory {
    public static void main(String[] args) {
        BuildingType type = BuildingFactory.getInstance().getBuildingType("CONDO");
        System.out.println("CONDO unlockLevel: " + (type != null ? type.getUnlockLevel() : "null"));
    }
}
