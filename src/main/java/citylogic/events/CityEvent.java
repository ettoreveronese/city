package citylogic.events;

import citylogic.model.CityModel;

/**
 * Classe base per tutti gli eventi del gioco.
 * Ogni evento viene pubblicato su un Channel e sa applicarsi al modello.
 */
public abstract class CityEvent {

    private final long timestamp;
    private final Channel channel;

    protected CityEvent(Channel channel) {
        this.timestamp = System.currentTimeMillis();
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Applica l'effetto dell'evento sul modello della città.
     */
    public abstract void apply(CityModel model);
}
