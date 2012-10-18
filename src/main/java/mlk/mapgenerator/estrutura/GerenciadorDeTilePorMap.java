

package mlk.mapgenerator.estrutura;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import mlk.mapgenerator.util.GoogleTileUtils;

/**
 *
 * @author gillopesbueno
 */
public abstract class GerenciadorDeTilePorMap {

    /**
     * cria um mapa com um tile ligado a uma sublista da lista de pontos, essa
     * sublista contem apenas os pontos que devem estar no tile
     * @param zoomMaximo cada tile tem um zoom, sera criado tiles de zoom 0 ao
     * zoomMaximo informado
     * @param lista lista de pontos no mapa
     * @return mapa de tile por pontos
     */
    public static Map<Tile, List<PontoNoMapa>> obterMaps(int zoomMinimo, int zoomMaximo, List<PontoNoMapa> lista){
        Map<Tile, List<PontoNoMapa>> maps = new HashMap<Tile, List<PontoNoMapa>>();
        for(int i = zoomMinimo; i<=zoomMaximo; i++){
            maps.putAll(obterMapsDoZoom(i, lista));
        }
        return maps;
    }

    /**
     * cria um mapa com um tile ligado a uma sublista da lista de pontos, essa
     * sublista contem apenas os pontos que devem estar no tile. Apenas para tile
     * do zoom informado
     * @param zoom zoom do tile
     * @param lista de todos os pontos
     * @return mapa com tiles do zoom informado e respectivos pontos
     */
    private static Map<Tile, List<PontoNoMapa>> obterMapsDoZoom(int zoom, List<PontoNoMapa> lista){
        Map<Tile, List<PontoNoMapa>> maps = new HashMap<Tile, List<PontoNoMapa>>();
        for(PontoNoMapa p : lista){
            Tile tileDoPonto = obterTileDoPonto(p, zoom); //cria um tile onde deveria estar o ponto

            obterOuCriarPontos(maps, tileDoPonto).add(p); //verifica se tem um tile igual no mapa, se não coloca esse tile no mapa, e depois coloca o ponto no tile
        }
        return maps;
    }

    /**
     * cria um tile de onde deveria estar o ponto em determinado zoom
     * @param p
     * @param zoom
     * @return novo tile
     */
    private static Tile obterTileDoPonto(PontoNoMapa p, int zoom){
        int[] tileCoord = GoogleTileUtils.getTileCoord(
                                GoogleTileUtils.getGoogleAerial(
                                    p.getLocalidade().getLat(),
                                    p.getLocalidade().getLon(),
                                    zoom
                                )
                            );

        return new Tile(tileCoord[0], tileCoord[1], zoom);
    }

    /**
     * obtem os pontos do tile, se não tiver lista de pontos associada ao tile cria uma nova
     * @param maps mapa de pontos por tile
     * @param tile
     * @return lista de pontos do tile
     */
    private static List<PontoNoMapa> obterOuCriarPontos(Map<Tile, List<PontoNoMapa>> maps, Tile tile){
        List<PontoNoMapa> i = maps.get(tile);
        if(i==null){
            i = new LinkedList<PontoNoMapa>();
            maps.put(tile, i);
        }
        return i;
    }

}
