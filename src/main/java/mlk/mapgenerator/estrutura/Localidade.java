

package mlk.mapgenerator.estrutura;

/**
 *
 * @author gillopesbueno
 */
public class Localidade {

    private double lat, lon;


    public Localidade(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

}
