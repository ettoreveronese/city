package city.model;

public class CityModel {

    private int gridSize = 60;  // grid size
    
    private final CityGrid grid;
    private final CityState state;

    // standard constructor 
    public CityModel() {
        this.grid = new CityGrid(gridSize, gridSize); 
        this.state = new CityState();    // initial state with default values
    }
    
    // load from file constructor
    public CityModel(CityGrid grid, CityState state) {
        this.grid = grid;
        this.state = state;
    }

    // returns city grid
    // nota: valuta di restituire una vista in sola lettura o un'interfaccia immutabile verso la view.
    public CityGrid getGrid() {
        return this.grid;
    }
    
    // returns city state
    // nota: viene restituito l'oggetto interno originale per permettere ai listener 
    // di effettuare aggiornamenti di logica, ma la view dovrebbe trattarlo come passivo.
    public CityState getState() {
        return this.state;
    }

    // apply variation to city parameters
    // called from various listeners every tick
    // delta contains the difference to add to the parameters
    public synchronized void applyDelta(StateDelta delta) {
        if (delta == null) {
            return;
        }

        this.state.setBudget(this.state.getBudget() + delta.getDeltaBudget());
        this.state.setPopulation(this.state.getPopulation() + delta.getDeltaPopulation());
        this.state.setPollution(this.state.getPollution() + delta.getDeltaPollution());
        this.state.setHappiness(this.state.getHappiness() + delta.getDeltaHappiness());
        this.state.setHealth(this.state.getHealth() + delta.getDeltaHealth());
        this.state.setEnergy(this.state.getEnergy() + delta.getDeltaEnergy());

        // Vincola le metriche a intervalli validi (es. la felicità tra 0 e 100, budget anche negativo)
        this.state.updateMetrics();
    }
}
