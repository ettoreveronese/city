package citylogic.model.entity;

// ── Park ──────────────────────────────────────────────────────────────────────
class Park extends Entity {
    public Park(int x, int y) { super(x, y, BuildingType.PARK); }
}

// ── PowerPlant ────────────────────────────────────────────────────────────────
class PowerPlant extends Entity {
    public PowerPlant(int x, int y) { super(x, y, BuildingType.POWER_PLANT); }
}

// ── Road ──────────────────────────────────────────────────────────────────────
class Road extends Entity {
    public Road(int x, int y) { super(x, y, BuildingType.ROAD); }
}

// ── FireStation ───────────────────────────────────────────────────────────────
class FireStation extends Entity {
    public FireStation(int x, int y) { super(x, y, BuildingType.FIRE_STATION); }
}

// ── Hospital ──────────────────────────────────────────────────────────────────
class Hospital extends Entity {
    public Hospital(int x, int y) { super(x, y, BuildingType.HOSPITAL); }
}

// ── Residential ───────────────────────────────────────────────────────────────
class Residential extends Entity {
    public Residential(int x, int y) { super(x, y, BuildingType.RESIDENTIAL); }
}

// ── Industrial ────────────────────────────────────────────────────────────────
class Industrial extends Entity {
    public Industrial(int x, int y) { super(x, y, BuildingType.INDUSTRIAL); }
}
