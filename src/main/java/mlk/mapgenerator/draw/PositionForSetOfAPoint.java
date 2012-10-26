/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mlk.mapgenerator.draw;

/**
 *
 * @author gillopesbueno aka melanke
 * 
 * a comparable for a position x and y
 */
class PositionForSetOfAPoint implements Comparable<PositionForSetOfAPoint>{
        public int x;
        public int y;

        public PositionForSetOfAPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        

        public int compareTo(PositionForSetOfAPoint o) {
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
        final PositionForSetOfAPoint other = (PositionForSetOfAPoint) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }
        
        
    }