/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mlk.mapgenerator.draw;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author gillopesbueno aka melanke
 * 
 * represents one pixel contained in a point plotted
 */
class PixelInTile {
    
    private List<Float> weight = new LinkedList<Float>();
    private boolean opaque = true;

    public PixelInTile() {
    }

    public boolean isOpaque() {
        return opaque;
    }

    public Float getWeight() {
        float retorno = 0;
        for(Float p : weight){
            retorno += p;
        }
        return retorno/weight.size();
    }
    
    public void addWeight(Float peso, boolean opaco){
        this.weight.add(peso);
        this.opaque &= opaco;
    }
    
    
}
