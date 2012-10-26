

package mlk.mapgenerator.scructure;


/**
 *
 * @author gillopesbueno aka melanke
 * 
 * represents the point in the map, the marker, with its position and weight.
 * The weight tells how the color will be.
 */
public class PointInMap {

    private Location location;
    private float weight;

    public PointInMap() {
    }

    public PointInMap(Location location, float weight) {
        this.location = location;
        this.weight = weight;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
