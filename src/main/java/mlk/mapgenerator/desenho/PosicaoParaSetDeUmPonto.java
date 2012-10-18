/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mlk.mapgenerator.desenho;

/**
 *
 * @author gillopesbueno
 */
class PosicaoParaSetDeUmPonto implements Comparable<PosicaoParaSetDeUmPonto>{
        public int x;
        public int y;

        public PosicaoParaSetDeUmPonto(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        

        public int compareTo(PosicaoParaSetDeUmPonto o) {
            return o.hashCode();
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 37 * hash + this.x;
            hash = 37 * hash + this.y;
            return hash;
        }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PosicaoParaSetDeUmPonto other = (PosicaoParaSetDeUmPonto) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }
        
        
    }