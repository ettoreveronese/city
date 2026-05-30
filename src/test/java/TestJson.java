import com.cityproject.model.factory.BuildingFactory;
import com.cityproject.model.type.BuildingType;
import com.cityproject.model.components.PollutionComponent;

public class TestJson {
    public static void main(String[] args) {
        BuildingType t = BuildingFactory.getInstance().getBuildingType("FOOD");
        PollutionComponent p = (PollutionComponent) t.getBaseComponents().get(PollutionComponent.class);
        System.out.println("FOOD pollution: " + p.getPollutionGenerated());
    }
}
