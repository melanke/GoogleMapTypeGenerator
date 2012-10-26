GoogleMapTypeGenerator
==================

Generates images to overlay google maps, it is usefull when you need to plot a large amount of data to a map

##Implementing a MapGenerator

```java
import java.util.LinkedList;
import java.util.List;
import mlk.mapgenerator.draw.PointPlotter;
import mlk.mapgenerator.scructure.Location;
import mlk.mapgenerator.scructure.PointInMap;
import mlk.mapgenerator.main.MapGenerator;

public class MyCustomMapGenerator extends MapGenerator{

    

    @Override
    public List<PointInMap> getPointsInArea(double minLat, double minLng, double maxLat, double maxLng) {
        //here you should return the points in the area specified in params
        //this method will be invoked many times with different coords
        
        List<PointInMap> response = new LinkedList<PointInMap>();
        
        float weight = 0.5F; //0 to 1 represents the color of the point
        
        response.add(new PointInMap(new Location(-21.534847,-43.914001), weight));
        
        return response;
    }

    @Override
    public String getFolder() {
        return "path/of/folder/to/save"; //ends without bar
    }
    
    
    //for better performance tell the total area
    

    @Override
    public double getMinLng() {
        return -21.534847; 
    }

    @Override
    public double getMaxLng() {
        return -21.534847;
    }

    @Override
    public double getMinLat() {
        return -43.914001;
    }

    @Override
    public double getMaxLat() {
        return -43.914001;
    }
    
    
    //constructor (I put it in the end because it is not necessary)
    
    public MyCustomMapGenerator() {
        //only necessary if you want customize your plotter
        
        plotter = new PointPlotter();
        
        //in the next update will be a better wat to custom the colors
        //for now, use the default RGB color model (TYPE_INT_ARGB)
        
        int[] opaqueColors = {
        -16764468, //royal blue
        -16762676,
        -16761140,
        -16759348,
        -16757812,
        -16756020,
        -16754484,
        -16752948,
        -16751156,
        -16749620,
        -16747828,
        -16746292,
        -16744500,
        -16742964,
        -16741172,
        -16739636,
        -16738100,
        -16736308,
        -16734772,
        -16732980,
        -16731444,
        -16729652,
        -16728116,
        -16726324,
        -16724788,
        -16724794,
        -16724801,
        -16724807,
        -16724814,
        -16724820 //dark blue
    };
    
    int[] semitransparentColors = {
        2013278668, //royal blue
        2013280460,
        2013281996,
        2013283788,
        2013285324,
        2013287116,
        2013288652,
        2013290188,
        2013291980,
        2013293516,
        2013295308,
        2013296844,
        2013298636,
        2013300172,
        2013301964,
        2013303500,
        2013305036,
        2013306828,
        2013308364,
        2013310156,
        2013311692,
        2013313484,
        2013315020,
        2013316812,
        2013318348,
        2013318342,
        2013318335,
        2013318329,
        2013318322,
        2013318316 //dark blue
    };
        
        plotter.setOpaqueColors(opaqueColors);
        
        plotter.setSemitransparentColors(semitransparentColors); 
        
    }
    
}

```
##How it looks like
![Ex1](https://github.com/melanke/GoogleMapTypeGenerator/blob/master/readmefiles/ex1.png)
![Ex2](https://github.com/melanke/GoogleMapTypeGenerator/blob/master/readmefiles/ex2.png)
![Ex3](https://github.com/melanke/GoogleMapTypeGenerator/blob/master/readmefiles/ex3.png)
![Ex4](https://github.com/melanke/GoogleMapTypeGenerator/blob/master/readmefiles/ex4.png)
![Ex5](https://github.com/melanke/GoogleMapTypeGenerator/blob/master/readmefiles/ex5.png)