package citylogic.model;

/**
 * Modello passivo della città: contiene CityGrid e CityState.
 * Non contiene logica di simulazione.
 */
public class CityModel {

    private final CityGrid  grid;
    private final CityState state;

    public CityModel(CityGrid grid, CityState state) {
        this.grid  = grid;
        this.state = state;
    }

    public void applyDelta(StateDelta delta) {
        state.applyDelta(delta);
    }

    public CityGrid  getGrid()  { return grid; }
    public CityState getState() { return state; }
}
