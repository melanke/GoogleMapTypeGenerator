

package mlk.mapgenerator.scructure;

/**
 *
 * @author gillopesbueno aka melanke
 * 
 * represents a square in the map
 * each tile is in a position and zoom, so gmaps only use this image when the 
 * user is looking at this exacly position and zoom
 */
public class Tile {

    private int x, y, zoom;

    public Tile(int x, int y, int zoom) {
        this.x = x;
        this.y = y;
        this.zoom = zoom;
    }

    

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZoom() {
        return zoom;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tile other = (Tile) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.zoom != other.zoom) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this.x;
        hash = 31 * hash + this.y;
        hash = 31 * hash + this.zoom;
        return hash;
    }

    

    

    

}
