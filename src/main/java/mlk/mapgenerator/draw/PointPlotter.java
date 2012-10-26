package mlk.mapgenerator.draw;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import mlk.mapgenerator.scructure.PointInMap;
import mlk.mapgenerator.scructure.Tile;
import mlk.mapgenerator.util.GoogleTileUtils;
import mlk.mapgenerator.util.GradientMaker;

/**
 *
 * @author gillopesbueno aka melanke
 * 
 * print the points
 */
public class PointPlotter {
    
    private static int W = 256, H = 256;
    private static int MAX_SIZE = 65536;

    private Color[] colors;

    public PointPlotter() {
        colors = GradientMaker.make(Color.YELLOW, Color.RED);
    }

    public PointPlotter(Color[] colors) {
        this.colors = colors;
    }
    

    /**
     * draw a image representing a tile, all points of this tile are draw, the
     * tile zoom modifies the point size, the weight of the point modifies its 
     * color and the position and zoom of the tile modifies the name of the file
     * @param map tiles with its points
     * @param folder folder where the images will be saved
     */
    public void drawTiles(Map<Tile, List<PointInMap>> map, String folder) {
        Set<Tile> tiles = map.keySet();
        for (Tile t : tiles) {
            
            PixelInTile[] pixels = new PixelInTile[MAX_SIZE];

            for (int offsetY = -1; offsetY < 2; offsetY++) {
                for (int offsetX = -1; offsetX < 2; offsetX++) {
                    populateArrayForPoints(map, t, pixels, offsetX, offsetY);
                }
            }

            int[] pixelsI = convertObjectArrayToIntArray(pixels);

            draw(pixelsI, t, folder);
        }

    }

    /**
     * put the pixels in the image and save it
     * @param pixelsI
     * @param t
     * @param folder 
     */
    private static void draw(int[] pixelsI, Tile t, String folder) {
        try {
            BufferedImage image = ImageIO.read(new File("emptyTile.png"));     //read an empty image of 256x256 px

            image.setRGB(0, 0, W, H, pixelsI, 0, W); //put the pixels in the image

            //create the folder if it dont exist
            File newFolder = new File(folder);
            if (!newFolder.exists()) {
                newFolder.mkdirs();
            }

            //save in folder
            ImageIO.write(image, "PNG", new File(folder + "/" + t.getX() + "x" + t.getY() + "-" + t.getZoom() + ".png"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void populateArrayForPoints(Map<Tile, List<PointInMap>> map, Tile t, PixelInTile[] pixels, int offsetX, int offsetY) {
        List<PointInMap> currentPoints = map.get(new Tile(t.getX() + offsetX, t.getY() + offsetY, t.getZoom()));
        
        if (currentPoints != null && !currentPoints.isEmpty()) {
            for (PointInMap p : currentPoints) {                                //for each point of the tile
                populateArrayForAPoint(p, t.getZoom(), pixels, offsetX * 256, offsetY * 256);
            }
        }
    }

    /**
     * put in the array the pixels of the draw of each point
     * @param p
     * @param zoom
     * @param pixels 
     */
    private static void populateArrayForAPoint(PointInMap p, int zoom, PixelInTile[] pixels, int offsetX, int offsetY) {
        int coord[] = GoogleTileUtils.getPixelCoordinate(
                p.getLocation().getLat(), //something about World Coordinate and Mercator
                p.getLocation().getLon(),
                zoom);

        int x = coord[0] % 256 + offsetX;                                                //x e y Mod 256 (tile size)
        int y = coord[1] % 256 + offsetY;

        if (zoom < 4) {
            populatePixels(x, y, p.getWeight(), true, pixels);
        } else if (zoom < 6) {
            populatePoint3p3(x, y, p.getWeight(), true, pixels);
        } else if (zoom < 12) {
            populateBorderPoint7p7(x, y, p.getWeight(), true, pixels);
            populateFillPoint7p7(x, y, p.getWeight(), false, pixels);
        } else {
            int prop = (int) (Math.pow(zoom, 5) * 0.00004);
            populateBorderBigger(x, y, prop, p.getWeight(), true, pixels);
            populateFillBigger(x, y, prop, p.getWeight(), false, pixels);
        }
    }

    private static void populatePoint3p3(int x, int y, float weight, boolean opaque, PixelInTile[] pixels) {

        int[][] points = {
            {x - 1, y - 1}, {x, y - 1}, {x + 1, y - 1},
            {x - 1, y},     {x, y},     {x + 1, y},
            {x - 1, y + 1}, {x, y + 1}, {x + 1, y + 1}
        };

        populatePixels(points, weight, opaque, pixels);
    }

    private static void populateFillPoint7p7(int x, int y, float weight, boolean opaque, PixelInTile[] pixels) {

        int[][] points = {
            /*{x - 2, y - 2},*/ {x - 1, y - 2}, {x, y - 2}, {x + 1, y - 2},//{x + 2, y - 2},
            {x - 2, y - 1},     {x - 1, y - 1}, {x, y - 1}, {x + 1, y - 1}, {x + 2, y - 1},
            {x - 2, y},         {x - 1, y},     {x, y},     {x + 1, y},     {x + 2, y},
            {x - 2, y + 1},     {x - 1, y + 1}, {x, y + 1}, {x + 1, y + 1}, {x + 2, y + 1},
            /*{x - 2, y + 2},*/ {x - 1, y + 2}, {x, y + 2}, {x + 1, y + 2},//{x + 2, y + 2}
        };

        populatePixels(points, weight, opaque, pixels);

    }

    private static void populateBorderPoint7p7(int x, int y, float weight, boolean opaque, PixelInTile[] pixels) {

        int[][] points = {
            /*{x-3, y-3}, {x-2, y-3}, */{x - 1, y - 3}, {x, y - 3}, {x + 1, y - 3}, //{x+2,y-3},  {x+3,y-3},
            /*{x-3, y-2}, */ {x - 2, y - 2},/*{x - 1, y - 2}, {x, y - 2}, {x + 1, y - 2},*/ {x + 2, y - 2}, //{x+3,y-2},
            {x - 3, y - 1},/*{x - 2, y - 1}, {x - 1, y - 1}, {x, y - 1}, {x + 1, y - 1}, {x + 2, y - 1},*/  {x + 3, y - 1},
            {x - 3, y},/* {x - 2, y},     {x - 1, y},     {x, y},     {x + 1, y},     {x + 2, y},    */     {x + 3, y},
            {x - 3, y + 1},/* {x - 2, y + 1}, {x - 1, y + 1}, {x, y + 1}, {x + 1, y + 1}, {x + 2, y + 1},*/ {x + 3, y + 1},
            /*{x-3, y+2},*/ {x - 2, y + 2},/*{x - 1, y + 2}, {x, y + 2}, {x + 1, y + 2},*/ {x + 2, y + 2}, //{x+3,y+2},
            /*{x-3, y+3}, {x-2, y+3},*/ {x - 1, y + 3}, {x, y + 3}, {x + 1, y + 3}//,  {x+2,y+3},  {x+3,y+3}
        };

        populatePixels(points, weight, opaque, pixels);

    }

    private static void populateBorderBigger(int x, int y, int radius, float weight, boolean opaque, PixelInTile[] pixels) {
        
        
        int x_;
        int y_;
        HashSet <PositionForSetOfAPoint> points = new HashSet<PositionForSetOfAPoint>(1440);

        //contorna
        for (double alpha = 0; alpha <= 45; alpha = alpha + 0.5) {
            x_ = (int) (radius * Math.cos((alpha * Math.PI) / 180.));
            y_ = (int) (radius * Math.sin((alpha * Math.PI) / 180.));

            populateIfInsideTile(x + x_, y + y_, points);
            populateIfInsideTile(x + x_, y - y_, points);
            populateIfInsideTile(x - x_, y + y_, points);
            populateIfInsideTile(x - x_, y - y_, points);
            populateIfInsideTile(x + y_, y + x_, points);
            populateIfInsideTile(x + y_, y - x_, points);
            populateIfInsideTile(x - y_, y + x_, points);
            populateIfInsideTile(x - y_, y - x_, points);
        }

        populatePixels(points, weight, opaque, pixels);
    }

    private static void populateIfInsideTile(int x, int y, Set<PositionForSetOfAPoint> points) {
        
        if((x >= 0) && (x <= 255) && (y >= 0) && (y <= 255)){
            points.add(new PositionForSetOfAPoint(x, y));
        }
    }

    private static void populateFillBigger(int x, int y, int radius, float weight, boolean opaque, PixelInTile[] pixels) {

        int x_;
        int y_;
        HashSet<PositionForSetOfAPoint> points = new HashSet<PositionForSetOfAPoint>(1440*radius);

        for (int i = radius - 1; i > 0; i--) {
            for (double alpha = 0; alpha <= 45; alpha = alpha + 0.5) {
                x_ = (int) (i * Math.cos((alpha * Math.PI) / 180.));
                y_ = (int) (i * Math.sin((alpha * Math.PI) / 180.));

                populateIfInsideTile(x + x_, y + y_, points);
                populateIfInsideTile(x + x_, y - y_, points);
                populateIfInsideTile(x - x_, y + y_, points);
                populateIfInsideTile(x - x_, y - y_, points);
                populateIfInsideTile(x + y_, y + x_, points);
                populateIfInsideTile(x + y_, y - x_, points);
                populateIfInsideTile(x - y_, y + x_, points);
                populateIfInsideTile(x - y_, y - x_, points);
            }
        }

        populatePixels(points, weight, opaque, pixels);
    }

    private static void populatePixels(int[][] points, float weight, boolean opaque, PixelInTile[] pixels) {
        for (int i = 0; i < points.length; i++) {
            populatePixels(points[i][0], points[i][1], weight, opaque, pixels);
        }
    }

    private static void populatePixels(Set<PositionForSetOfAPoint> points, float weight, boolean opaque, PixelInTile[] pixels) {
        for (PositionForSetOfAPoint point : points) {
            populatePixels(point.x, point.y, weight, opaque, pixels);
        }
    }

    private static void populatePixels(int x, int y, float weight, boolean opaque, PixelInTile[] pixels) {
        if ((x >= 0) && (x <= 255) && (y >= 0) && (y <= 255) && (W * y + x >= 0) && (W * y + x + 1 <= MAX_SIZE) && (x != 0 || y != 0)) {
            if (pixels[W * y + x] == null) {
                pixels[W * y + x] = new PixelInTile();
            }
            pixels[W * y + x].addWeight(weight, opaque);
        }
    }

    private int[] convertObjectArrayToIntArray(PixelInTile[] pixels) {
        int[] response = new int[MAX_SIZE];

        for (int i = 0; i < MAX_SIZE; i++) {
            if (pixels[i] == null) {
                continue;
            }

            int indexColorPoint = (int) (Math.max(Math.min(pixels[i].getWeight(), 1), 0)*colors.length-1);
            Color colorBase = colors[indexColorPoint];
            
            if (pixels[i].isOpaque()) {
                response[i] = colorBase.getRGB();
            } else {
                int alpha = colorBase.getAlpha()/2;
                
                Color semi = new Color(
                        colorBase.getRed(), 
                        colorBase.getGreen(),
                        colorBase.getBlue(),
                        alpha);
                
                response[i] = semi.getRGB();
            }
        }

        return response;
    }
}
