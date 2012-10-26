

package mlk.mapgenerator.main;

import java.util.List;
import java.util.Map;
import mlk.mapgenerator.draw.PointPlotter;
import mlk.mapgenerator.scructure.PointInMap;
import mlk.mapgenerator.scructure.Tile;
import mlk.mapgenerator.scructure.TileForMapManager;
import mlk.mapgenerator.util.GoogleTileUtils;

/**
 *
 * @author gillopesbueno aka melanke
 * 
 * create the tiles and do the magic
 */
public abstract class MapGenerator {
    
    /**
     * populate the points in required area
     * @param minLat
     * @param minLng
     * @param maxLat
     * @param maxLng
     * @return list of all points in this area
     */
    public abstract List<PointInMap> getPointsInArea(double minLat, double minLng, double maxLat, double maxLng);

    /**
     * tell what folder will contain the images
     * @return path of the folder
     */
    public abstract String getFolder();

    /**
     * total min lng to increase performance
     * @return 
     */
    public abstract double getMinLng();

    /**
     * total max lng to increase performance
     * @return 
     */
    public abstract double getMaxLng();

    /**
     * total min lat to increase performance
     * @return 
     */
    public abstract double getMinLat();

    /**
     * total max lat to increase performance
     * @return 
     */
    public abstract double getMaxLat();
    
    public PointPlotter plotter = new PointPlotter();
    
    /**
     * do the magic
     */
    public final void run(){
        int minZoom = 0;
        
        createTilesFor6ZoonsAndDivide(minZoom, getMinLat(), getMinLng(), getMaxLat(), getMaxLng());
    }
    
    private void createTilesFor6ZoonsAndDivide(int zoom, double minLat, double minLng, double maxLat, double maxLng){
            int step = 6;
            
            boolean createTiles = createTiles(zoom, zoom+step, minLat, minLng, maxLat, maxLng);
            
            if(!createTiles)
                return;
            
            double middleLat = (maxLat - minLat)/2 + minLat;
            double middleLng = (maxLng - minLng)/2 + minLng;
            
            int[] minTileCoords = GoogleTileUtils.getTileCoord(GoogleTileUtils.getGoogleAerial(middleLat, middleLng, zoom+step+1));
            int lastLileX = minTileCoords[0];
            int lastTileY = minTileCoords[1];
        
            double[] lastTileLatLonBounds = GoogleTileUtils.getTileLatLngBounds(lastLileX, lastTileY, zoom+step+1);
            
            middleLat = lastTileLatLonBounds[2];
            middleLng = lastTileLatLonBounds[3];
            
            createTilesFor6ZoonsAndDivide(zoom+7, minLat, minLng, middleLat, middleLng);    //  [*| ]
                                                                                            //  [ | ]
            
            createTilesFor6ZoonsAndDivide(zoom+7, middleLat, minLng, maxLat, middleLng);    //  [ | ]
                                                                                            //  [*| ]
            
            createTilesFor6ZoonsAndDivide(zoom+7, minLat, middleLng, middleLat, maxLng);    //  [ |*]
                                                                                            //  [ | ]
            
            createTilesFor6ZoonsAndDivide(zoom+7, middleLat, middleLng, maxLat, maxLng);    //  [ | ]
                                                                                            //  [ |*]
            
    }
    
    private boolean createTiles(int minZoom, int maxZoom, double minLat, double minLng, double maxLat, double maxLng){
        
        if(minZoom>16)
            return false;
        
        maxZoom = Math.min(maxZoom, 16);
        
        List<PointInMap> points = getPointsInArea(minLat, minLng, maxLat, maxLng);
        
        if(points.isEmpty())
            return false;
        
        Map<Tile, List<PointInMap>> pontosPorTile = TileForMapManager.getMaps(minZoom, maxZoom, points);
        
        plotter.drawTiles(pontosPorTile, getFolder());
        
        return true;
        
    }

}
