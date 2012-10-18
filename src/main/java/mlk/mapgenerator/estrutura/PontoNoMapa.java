

package mlk.mapgenerator.estrutura;


/**
 *
 * @author gillopesbueno
 */
public class PontoNoMapa {

    private Localidade localidade;
    private float peso;

    public PontoNoMapa() {
    }

    public PontoNoMapa(Localidade localidade, float peso) {
        this.localidade = localidade;
        this.peso = peso;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public Localidade getLocalidade() {
        return localidade;
    }

    public void setLocalidade(Localidade localidade) {
        this.localidade = localidade;
    }

}
