

package mlk.mapgenerator.medicoes.gerarImagens.logica;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import mlk.mapgenerator.desenho.PlotadorDePontos;
import mlk.mapgenerator.estrutura.GerenciadorDeTilePorMap;
import mlk.mapgenerator.estrutura.PontoNoMapa;
import mlk.mapgenerator.estrutura.Tile;
import mlk.mapgenerator.util.GoogleTileUtils;

/**
 *
 * @author gillopesbueno
 */
public class ProcessoDeCriacaoDeTile {
    
    private List<PontoNoMapa> pontos = new LinkedList<PontoNoMapa>();
    private String diretorio = "";
    private double minLng = -73.982078;
    private double maxLng = -32.378691;
    private double minLat = -33.751077;
    private double maxLat = 5.268177;
    
    private PlotadorDePontos plotter = new PlotadorDePontos();

    public List<PontoNoMapa> getPontos() {
        return pontos;
    }

    public void setPontos(List<PontoNoMapa> pontos) {
        this.pontos = pontos;
    }

    public String getDiretorio() {
        return diretorio;
    }

    public void setDiretorio(String diretorio) {
        this.diretorio = diretorio;
    }

    public double getMinLng() {
        return minLng;
    }

    public void setMinLng(double minLng) {
        this.minLng = minLng;
    }

    public double getMaxLng() {
        return maxLng;
    }

    public void setMaxLng(double maxLng) {
        this.maxLng = maxLng;
    }

    public double getMinLat() {
        return minLat;
    }

    public void setMinLat(double minLat) {
        this.minLat = minLat;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(double maxLat) {
        this.maxLat = maxLat;
    }
    
    

    /**
     * cria os tiles dos testes de acordo com suas localidades, desenha em
     * imagens e salva
     * @param tileType tipo de tile que pode ser "concentra‹o" (pontos mais 
     * escuros quanto mais testes na regi‹o) ou "qualidade" (pontos mais escuros
     * quanto melhor a qualidade do tcp download na regi‹o)
     * @param quadrante qual quadrante ser‡ desenhado
     */
    public void criarTiles(){
        int minZoom = 0;
        
        criarTilesPara6ZoonsEDividir(minZoom, minLat, minLng, maxLat, maxLng);
    }
    
    private void criarTilesPara6ZoonsEDividir(int zoom, double minLat, double minLng, double maxLat, double maxLng){
            int passo = 6;
            
            boolean criarTiles = criarTiles(zoom, zoom+passo, minLat, minLng, maxLat, maxLng);
            
            if(!criarTiles)
                return;
            
            double meioLat = (maxLat - minLat)/2 + minLat;
            double meioLng = (maxLng - minLng)/2 + minLng;
            
            int[] minTileCoords = GoogleTileUtils.getTileCoord(GoogleTileUtils.getGoogleAerial(meioLat, meioLng, zoom+passo+1));
            int ultimoTileX = minTileCoords[0];
            int ultimoTileY = minTileCoords[1];
        
            double[] ultimoTileLatLonBounds = GoogleTileUtils.getTileLatLonBounds(ultimoTileX, ultimoTileY, zoom+passo+1);
            
            meioLat = ultimoTileLatLonBounds[2];
            meioLat = ultimoTileLatLonBounds[3];
            
            criarTilesPara6ZoonsEDividir(zoom+7, minLat, minLng, meioLat, meioLng);    //  [*| ]
                                                                                                //  [ | ]
            
            criarTilesPara6ZoonsEDividir(zoom+7, meioLat, minLng, maxLat, meioLng);    //  [ | ]
                                                                                                //  [*| ]
            
            criarTilesPara6ZoonsEDividir(zoom+7, minLat, meioLng, meioLat, maxLng);    //  [ |*]
                                                                                                //  [ | ]
            
            criarTilesPara6ZoonsEDividir(zoom+7, meioLat, meioLng, maxLat, maxLng);    //  [ | ]
                                                                                                //  [ |*]
            
    }
    
    private boolean criarTiles(int minZoom, int maxZoom, double minLat, double minLng, double maxLat, double maxLng){
        
        if(minZoom>16)
            return false;
        
        maxZoom = Math.min(maxZoom, 16);
        
        if(pontos.isEmpty())
            return false;
        
        Map<Tile, List<PontoNoMapa>> pontosPorTile = GerenciadorDeTilePorMap.obterMaps(minZoom, maxZoom, pontos); //obtem o mapa de tiles relacionado a lista de pontos dentro do tile
        
        plotter.desenharTiles(pontosPorTile, diretorio); //desenha os tiles do mapa com seus respectivos pontos
        
        return true;
        
    }

}
