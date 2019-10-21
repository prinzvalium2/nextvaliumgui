package de.prinzvalium.nextvaliumgui.nextcolony.galaxymap;


public class GalaxyMapKey {

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private final int x;
    private final int y;

    public GalaxyMapKey(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GalaxyMapKey)) return false;
        GalaxyMapKey key = (GalaxyMapKey) o;
        return x == key.x && y == key.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
