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
        
        plotter = new PointPlotter(GradientMaker.make(Color.GREEN, Color.BLACK)); //weight 0: green; weight 1: black
        
    }
    
}

```
##How it looks like
![Ex1](http://melanke.github.com/GoogleMapTypeGenerator/readmefiles/ex1.png)
![Ex2](http://melanke.github.com/GoogleMapTypeGenerator/readmefiles/ex2.png)
![Ex3](http://melanke.github.com/GoogleMapTypeGenerator/readmefiles/ex3.png)
![Ex4](http://melanke.github.com/GoogleMapTypeGenerator/readmefiles/ex4.png)
![Ex5](http://melanke.github.com/GoogleMapTypeGenerator/readmefiles/ex5.png)