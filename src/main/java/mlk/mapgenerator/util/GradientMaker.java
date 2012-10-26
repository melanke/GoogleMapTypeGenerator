/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mlk.mapgenerator.util;

import java.awt.Color;

/**
 *
 * @author gillopesbueno
 * 
 * create an array of colors making a gradient between to colors
 */
public class GradientMaker {
    
    public static Color[] make(Color from, Color to){
      int steps = 30;
      Color[] result = new Color[steps];
      
      float redFrom = from.getRed();
      float greenFrom = from.getGreen();
      float blueFrom = from.getBlue();
      float alphaFrom = from.getAlpha();
      
      float redTo = to.getRed();
      float greenTo = to.getGreen();
      float blueTo = to.getBlue();
      float alphaTo = to.getAlpha();
      
      float redstep = (redTo - redFrom)/(steps*1f);
      float greenstep = (greenTo - greenFrom)/(steps*1f);
      float bluestep = (blueTo - blueFrom)/(steps*1f);
      float alphastep = (alphaTo - alphaFrom)/(steps*1f);
      
      result[0] = from;
      
      for(int i = 1; i<steps-1; i++){
          int red = (int) (redFrom + (redstep * i));
          int green = (int) (greenFrom + (greenstep * i));
          int blue = (int) (blueFrom + (bluestep * i));
          int alpha = (int) (alphaFrom + (alphastep * i));
          result[i] = new Color(red, green, blue, alpha);
      }
     
      result[steps-1] = to;
      
      return result;
  }
    
}
