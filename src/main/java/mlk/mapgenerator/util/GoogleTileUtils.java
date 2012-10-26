package mlk.mapgenerator.util;
/*
 * Originally written by Andrew Rowbottom 
 * Modified by gillopesbueno aka melanke
 * Released freely into the public domain, use it how you want, don't blame me.
 * No warranty for this code is taken in any way. 
 *
 * A utility class to assist in encoding and decoding google tile references
 *
 * For reasons of my own longitude is treated as being between -180 and +180
 * and internally latitude is treated as being from -1 to +1 and then converted to a mercator projection
 * before return.
 *
 * All rectangles are sorted so the width and height are +ve
 */
public abstract class GoogleTileUtils {

    private static final int TILE_SIZE = 256;
    private static final double INITIAL_RESOLUTION = 156543.03392804097, ORIGIN_SHIFT = 20037508.342789244;

    /**
     * get a hash that represents coords of the tile in google aerial
     * @param lat
     * @param lon
     * @param zoom
     * @return
     */
    public static String getGoogleAerial(double lat, double lon, int zoom) {

        // first convert the lat lon to transverse mercator coordintes.
        if (lon > 180) {
            lon -= 360;
        }

        lon /= 180;

        // convert latitude to a range -1..+1
        lat = Math.log(Math.tan((Math.PI / 4) + (0.5 * Math.PI * lat / 180))) / Math.PI;

        double tLat = -1;
        double tLon = -1;
        double lonWidth = 2;
        double latHeight = 2;

        StringBuffer keyholeString = new StringBuffer("t");

        for (int i = 0; i < zoom; i++) {
            lonWidth /= 2;
            latHeight /= 2;

            if ((tLat + latHeight) > lat) {
                if ((tLon + lonWidth) > lon) {
                    keyholeString.append('t');
                } else {
                    tLon += lonWidth;
                    keyholeString.append('s');
                }
            } else {
                tLat += latHeight;

                if ((tLon + lonWidth) > lon) {
                    keyholeString.append('q');
                } else {
                    tLon += lonWidth;
                    keyholeString.append('r');
                }
            }
        }

        return keyholeString.toString();
    }

    /**
     * get the coords of the tile from google aerial format
     * @param inGoogleAerial
     * @return {column, line, zoom}
     */
    public static int[] getTileCoord(String inGoogleAerial) {
        // Return column, row, zoom for Google Aerial tile string.
        String rowS = "";
        String colS = "";
        for (int i = 0; i < inGoogleAerial.length(); i++) {
            switch (inGoogleAerial.charAt(i)) {
                case 't':
                    rowS += '0';
                    colS += '0';
                    break;
                case 's':
                    rowS += '0';
                    colS += '1';
                    break;
                case 'q':
                    rowS += '1';
                    colS += '0';
                    break;
                case 'r':
                    rowS += '1';
                    colS += '1';
                    break;
            }
        }
        int row = Integer.parseInt(rowS, 2);
        int col = Integer.parseInt(colS, 2);
        int zoom = inGoogleAerial.length() - 1;
        row = (int) Math.pow(2, zoom) - row - 1;

        int[] retorno = {col, row, zoom};
        return retorno;
    }

    /**
     * get point position inside the tile
     * @param lat lat of the point
     * @param lon lng of the point
     * @param zoom tile zoom
     * @return {x, y}
     */
    public static int[] getPixelCoordinate(double lat, double lon, int zoom) {
        double tiles = Math.pow(2, zoom);
        double circ = TILE_SIZE * tiles;
        double radius = circ / (2 * Math.PI);
        double falseEast = -1 * circ / 2;
        double falseNorth = circ / 2;
        double x = (radius * Math.toRadians(lon)) - falseEast;
        double y = ((radius / 2 * Math.log((1 + Math.sin(Math.toRadians(lat))) / (1 - Math.sin(Math.toRadians(lat))))) - falseNorth) * -1;
        return new int[]{(int) x, (int) y};
    }

    /**
     * lat lng boundary of the tile
     * @param tx tile x
     * @param ty tile y
     * @param zoom
     * @return {min lat, min lng, max lat, max lng}
     */
    public static double[] getTileLatLngBounds(int tx, int ty, int zoom) {

        
        double[] bounds = getTileBounds(tx, ty, zoom);
        
        double[] minLatLng = getMetersToLatLng(bounds[0], bounds[1]);
        
        double[] maxLatLng = getMetersToLatLng(bounds[2], bounds[3]);
        
        double[] response = {minLatLng[0], minLatLng[1], maxLatLng[0], maxLatLng[1]};
        
        return response;
   }

    private static double[] getTileBounds(int tx, int ty, int zoom) {

        double[] minxy = getPixelsToMeters(tx * TILE_SIZE, ty * TILE_SIZE, zoom);
        
        double[] maxxy = getPixelsToMeters((tx + 1) * TILE_SIZE, (ty + 1) * TILE_SIZE, zoom);
        
        double[] response = {minxy[0], minxy[1], maxxy[0], maxxy[1]};
        return response;
    }

    private static double[] getMetersToLatLng(double mx, double my) {

        double lng = (mx / ORIGIN_SHIFT) * 180.0;

        double lat = (my / ORIGIN_SHIFT) * 180.0;
        
        lat = 180 / Math.PI * (2 * Math.atan(Math.exp(lat * Math.PI / 180.0)) - Math.PI / 2.0);
        
        double[] response = {lat, lng};
        return response;
    }

    private static double[] getPixelsToMeters(int px, int py, int zoom) {

        double res = getResolution(zoom);
        
        double mx = px * res - ORIGIN_SHIFT;
        
        double my = py * res - ORIGIN_SHIFT;
        
        double[] response = {mx, my};
        
        return response;
    }

    private static double getResolution(int zoom) {
        return INITIAL_RESOLUTION / Math.pow(2, zoom);
    }
}
