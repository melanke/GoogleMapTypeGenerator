

package mlk.mapgenerator.scructure;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import mlk.mapgenerator.util.GoogleTileUtils;

/**
 *
 * @author gillopesbueno aka melanke
 * 
 * group points in tiles
 */
public abstract class TileForMapManager {

    /**
     * create a map grouping points in its relative tile
     * @param minZoom
     * @param maxZoom
     * @param points
     * @return 
     */
    public static Map<Tile, List<PointInMap>> getMaps(int minZoom, int maxZoom, List<PointInMap> points){
        Map<Tile, List<PointInMap>> maps = new HashMap<Tile, List<PointInMap>>();
        for(int i = minZoom; i<=maxZoom; i++){
            maps.putAll(getMapsOfZoom(i, points));
        }
        return maps;
    }

    /**
     * create a map grouping points in its relative tile of the current zoom
     * @param zoom
     * @param points
     * @return 
     */
    private static Map<Tile, List<PointInMap>> getMapsOfZoom(int zoom, List<PointInMap> points){
        Map<Tile, List<PointInMap>> maps = new HashMap<Tile, List<PointInMap>>();
        for(PointInMap p : points){
            Tile tileDoPonto = getTileOfPonto(p, zoom);

            getOrCreatePoints(maps, tileDoPonto).add(p); 
        }
        return maps;
    }

    /**
     * create a tile where the point should be in current zoom
     * @param p
     * @param zoom
     * @return novo tile
     */
    private static Tile getTileOfPonto(PointInMap p, int zoom){
        int[] tileCoord = GoogleTileUtils.getTileCoord(
                                GoogleTileUtils.getGoogleAerial(
                                    p.getLocation().getLat(),
                                    p.getLocation().getLon(),
                                    zoom
                                )
                            );

        return new Tile(tileCoord[0], tileCoord[1], zoom);
    }

    /**
     * get the points of the tile, if this tile dont exist create it
     * @param maps
     * @param tile
     * @return
     */
    private static List<PointInMap> getOrCreatePoints(Map<Tile, List<PointInMap>> maps, Tile tile){
        List<PointInMap> i = maps.get(tile);
        if(i==null){
            i = new LinkedList<PointInMap>();
            maps.put(tile, i);
        }
        return i;
    }

}
