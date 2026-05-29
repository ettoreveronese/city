import com.cityproject.model.CityState;
import com.cityproject.model.Infrastructure;
import com.cityproject.model.type.BuildingType;
import com.cityproject.engine.RoadConnectionManager;

public class TestDisconnect {
    public static void main(String[] args) {
        CityState city = new CityState(10, 10, 1000);
        
        BuildingType rootType = new BuildingType("ROOT_ROAD", "Root", 0);
        Infrastructure root = new Infrastructure("r1", rootType, 5, 5);
        city.addBuilding(root);
        city.getCell(5, 5).setStructure(root);
        
        BuildingType roadType = new BuildingType("ROAD", "Road", 0);
        Infrastructure road = new Infrastructure("r2", roadType, 5, 6);
        city.addBuilding(road);
        city.getCell(5, 6).setStructure(road);
        
        BuildingType cottageType = new BuildingType("COTTAGE", "Cottage", 0);
        Infrastructure cottage = new Infrastructure("c1", cottageType, 5, 7);
        city.addBuilding(cottage);
        city.getCell(5, 7).setStructure(cottage);
        
        RoadConnectionManager rcm = new RoadConnectionManager(city);
        rcm.checkConnections();
        System.out.println("Before delete: " + cottage.isActive());
        
        // delete road
        city.removeBuilding(road);
        city.getCell(5, 6).setStructure(null);
        
        rcm.checkConnections();
        System.out.println("After delete: " + cottage.isActive());
    }
}
