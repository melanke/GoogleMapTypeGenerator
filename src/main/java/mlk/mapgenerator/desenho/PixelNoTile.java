/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mlk.mapgenerator.desenho;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author gillopesbueno
 */
class PixelNoTile {
    
    private List<Float> pesos = new LinkedList<Float>();
    private boolean opaco = true;

    public PixelNoTile() {
    }

    public boolean isOpaco() {
        return opaco;
    }

    public Float getPeso() {
        float retorno = 0;
        for(Float p : pesos){
            retorno += p;
        }
        return retorno/pesos.size();
    }
    
    public void addPeso(Float peso, boolean opaco){
        this.pesos.add(peso);
        this.opaco &= opaco;
    }
    
    
}
