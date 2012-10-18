package mlk.mapgenerator.desenho;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import mlk.mapgenerator.estrutura.PontoNoMapa;
import mlk.mapgenerator.estrutura.Tile;
import mlk.mapgenerator.util.GoogleTileUtils;

/**
 *
 * @author gillopesbueno
 */
public class PlotadorDePontos {
    
    private static int w = 256, h = 256;
    private static int tamanhoMaximo = 65536;

    private int[] coresSolidas = {
        -16764468, //azul royal
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
        -16724820 //verde
    };
    
    private int[] coresTransparentes = {
        2013278668, //azul royal
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
        2013318316 //verde
    };

    public int[] getCoresSolidas() {
        return coresSolidas;
    }

    public void setCoresSolidas(int[] coresSolidas) {
        this.coresSolidas = coresSolidas;
    }

    public int[] getCoresTransparentes() {
        return coresTransparentes;
    }

    public void setCoresTransparentes(int[] coresTransparentes) {
        this.coresTransparentes = coresTransparentes;
    }
    
    
    
    

    /**
     * desenha uma imagem que representa um tile, todos os pontos deste tile são
     * desenhados, o tamanho do ponto vai de acordo com o zoom do tile, a corDoPonto do
     * tile vai de acordo com o peso do ponto, o diretorio que é salvo a imagem
     * vai de acordo com o tipo de mapa e o nome da imagem vai de acordo com a
     * posição e zoom do tile
     * @param diretorio em qual diretorio deve ser salvo (deve ser o tipo de mapa)
     */
    public void desenharTiles(Map<Tile, List<PontoNoMapa>> map, String diretorio) {
        Set<Tile> tiles = map.keySet();
        for (Tile t : tiles) {

            int x = 0;                                                              //posicao do ponto no tile
            int y = 0;
            
            PixelNoTile[] pixels = new PixelNoTile[tamanhoMaximo];

            for (int desvioY = -1; desvioY < 2; desvioY++) {
                for (int desvioX = -1; desvioX < 2; desvioX++) {
                    popularArrayParaOsPontos(map, t, pixels, desvioX, desvioY);
                }
            }

            int[] pixelsI = transformarArrayObjParaArrayInt(pixels);

            desenhar(pixelsI, t, diretorio);
        }

    }

    private static void desenhar(int[] pixelsI, Tile t, String diretorio) {
        try {
            BufferedImage imagem = ImageIO.read(new File("emptyTile.png"));     //le uma imagem transparente com o tamanho de 256x256

            imagem.setRGB(0, 0, w, h, pixelsI, 0, w); //coloca os pixels na imagem

            //cria diretorio se nao existe
            File novaPasta = new File(diretorio);
            if (!novaPasta.exists()) {
                novaPasta.mkdirs();
            }

            //salva no diretorio
            ImageIO.write(imagem, "PNG", new File(diretorio + "/" + t.getX() + "x" + t.getY() + "-" + t.getZoom() + ".png"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void popularArrayParaOsPontos(Map<Tile, List<PontoNoMapa>> map, Tile t, PixelNoTile[] pixels, int desvioX, int desvioY) {
        List<PontoNoMapa> pontosAtuais = map.get(new Tile(t.getX() + desvioX, t.getY() + desvioY, t.getZoom()));
        
        if (pontosAtuais != null && !pontosAtuais.isEmpty()) {
            for (PontoNoMapa p : pontosAtuais) {                                          //percorDoPontore todos os pontos do tile
                popularArrayParaOPonto(p, t.getZoom(), pixels, desvioX * 256, desvioY * 256);
            }
        }
    }

    /**
     * coloca no array os pixels do desenho de cada ponto
     * @param p
     * @param zoom
     * @param pixels 
     */
    private static void popularArrayParaOPonto(PontoNoMapa p, int zoom, PixelNoTile[] pixels, int desvioX, int desvioY) {
        int coord[] = GoogleTileUtils.getPixelCoordinate( //obtem a coordenada do ponto dentro do tile de acorDoPontodo com a lat/lng
                p.getLocalidade().getLat(), //tem a ver com World Coordinate e Mercator
                p.getLocalidade().getLon(),
                zoom);

        int x = coord[0] % 256 + desvioX;                                                //x e y Mod 256 (tamanho do tile)
        int y = coord[1] % 256 + desvioY;

        if (zoom < 4) {
            popularPixel(x, y, p.getPeso(), true, pixels);
        } else if (zoom < 6) {
            popularPonto3p3(x, y, p.getPeso(), true, pixels);
        } else if (zoom < 12) {
            popularContornoPonto7p7(x, y, p.getPeso(), true, pixels);
            popularPreenchimentoPonto7p7(x, y, p.getPeso(), false, pixels);
        } else {
            int prop = (int) (Math.pow(zoom, 5) * 0.00004);
            popularContornoMaior(x, y, prop, p.getPeso(), true, pixels);
            popularPreenchimentoMaior(x, y, prop, p.getPeso(), false, pixels);
        }
    }

    private static void popularPonto3p3(int x, int y, float peso, boolean opaco, PixelNoTile[] pixels) {

        int[][] pontos = {
            {x - 1, y - 1}, {x, y - 1}, {x + 1, y - 1},
            {x - 1, y}, {x, y}, {x + 1, y},
            {x - 1, y + 1}, {x, y + 1}, {x + 1, y + 1}
        };

        popularPixels(pontos, peso, opaco, pixels);
    }

    private static void popularPreenchimentoPonto7p7(int x, int y, float peso, boolean opaco, PixelNoTile[] pixels) {

        int[][] pontos = {
            /*{x - 2, y - 2},*/{x - 1, y - 2}, {x, y - 2}, {x + 1, y - 2},//{x + 2, y - 2},
            {x - 2, y - 1}, {x - 1, y - 1}, {x, y - 1}, {x + 1, y - 1}, {x + 2, y - 1},
            {x - 2, y}, {x - 1, y}, {x, y}, {x + 1, y}, {x + 2, y},
            {x - 2, y + 1}, {x - 1, y + 1}, {x, y + 1}, {x + 1, y + 1}, {x + 2, y + 1},
            /*{x - 2, y + 2},*/ {x - 1, y + 2}, {x, y + 2}, {x + 1, y + 2},//{x + 2, y + 2}
        };

        popularPixels(pontos, peso, opaco, pixels);

    }

    private static void popularContornoPonto7p7(int x, int y, float peso, boolean opaco, PixelNoTile[] pixels) {

        int[][] pontos = {
            /*{x-3, y-3}, {x-2, y-3}, */{x - 1, y - 3}, {x, y - 3}, {x + 1, y - 3}, //{x+2,y-3},  {x+3,y-3},
            /*{x-3, y-2}, */ {x - 2, y - 2},/*{x - 1, y - 2}, {x, y - 2}, {x + 1, y - 2},*/ {x + 2, y - 2}, //{x+3,y-2},
            {x - 3, y - 1},/*{x - 2, y - 1}, {x - 1, y - 1}, {x, y - 1}, {x + 1, y - 1}, {x + 2, y - 1},*/ {x + 3, y - 1},
            {x - 3, y},/* {x - 2, y},     {x - 1, y},     {x, y},     {x + 1, y},     {x + 2, y},    */ {x + 3, y},
            {x - 3, y + 1},/* {x - 2, y + 1}, {x - 1, y + 1}, {x, y + 1}, {x + 1, y + 1}, {x + 2, y + 1},*/ {x + 3, y + 1},
            /*{x-3, y+2},*/ {x - 2, y + 2},/*{x - 1, y + 2}, {x, y + 2}, {x + 1, y + 2},*/ {x + 2, y + 2}, //{x+3,y+2},
            /*{x-3, y+3}, {x-2, y+3},*/ {x - 1, y + 3}, {x, y + 3}, {x + 1, y + 3}//,  {x+2,y+3},  {x+3,y+3}
        };

        popularPixels(pontos, peso, opaco, pixels);

    }

    private static void popularContornoMaior(int x, int y, int raio, float peso, boolean opaco, PixelNoTile[] pixels) {
        
        
        int x_ = 0;
        int y_ = raio;
        HashSet <PosicaoParaSetDeUmPonto> pontos = new HashSet<PosicaoParaSetDeUmPonto>(1440);

        //contorna
        for (double alfa = 0; alfa <= 45; alfa = alfa + 0.5) {
            x_ = (int) (raio * Math.cos((alfa * Math.PI) / 180.));
            y_ = (int) (raio * Math.sin((alfa * Math.PI) / 180.));

            populaSeDentroDoTile(x + x_, y + y_, pontos);
            populaSeDentroDoTile(x + x_, y - y_, pontos);
            populaSeDentroDoTile(x - x_, y + y_, pontos);
            populaSeDentroDoTile(x - x_, y - y_, pontos);
            populaSeDentroDoTile(x + y_, y + x_, pontos);
            populaSeDentroDoTile(x + y_, y - x_, pontos);
            populaSeDentroDoTile(x - y_, y + x_, pontos);
            populaSeDentroDoTile(x - y_, y - x_, pontos);
        }

        popularPixels(pontos, peso, opaco, pixels);
    }

    private static void populaSeDentroDoTile(int x, int y, Set<PosicaoParaSetDeUmPonto> pontos) {
        
        if((x >= 0) && (x <= 255) && (y >= 0) && (y <= 255)){
            pontos.add(new PosicaoParaSetDeUmPonto(x, y));
        }
    }

    private static void popularPreenchimentoMaior(int x, int y, int raio, float peso, boolean opaco, PixelNoTile[] pixels) {

        int x_ = 0;
        int y_ = raio;
        HashSet<PosicaoParaSetDeUmPonto> pontos = new HashSet<PosicaoParaSetDeUmPonto>(1440*raio);

        for (int i = raio - 1; i > 0; i--) {
            for (double alfa = 0; alfa <= 45; alfa = alfa + 0.5) {
                x_ = (int) (i * Math.cos((alfa * Math.PI) / 180.));
                y_ = (int) (i * Math.sin((alfa * Math.PI) / 180.));

                populaSeDentroDoTile(x + x_, y + y_, pontos);
                populaSeDentroDoTile(x + x_, y - y_, pontos);
                populaSeDentroDoTile(x - x_, y + y_, pontos);
                populaSeDentroDoTile(x - x_, y - y_, pontos);
                populaSeDentroDoTile(x + y_, y + x_, pontos);
                populaSeDentroDoTile(x + y_, y - x_, pontos);
                populaSeDentroDoTile(x - y_, y + x_, pontos);
                populaSeDentroDoTile(x - y_, y - x_, pontos);
            }
        }

        popularPixels(pontos, peso, opaco, pixels);
    }

    private static void popularPixels(int[][] pontos, float peso, boolean opaco, PixelNoTile[] pixels) {
        for (int i = 0; i < pontos.length; i++) {
            popularPixel(pontos[i][0], pontos[i][1], peso, opaco, pixels);
        }
    }

    private static void popularPixels(Set<PosicaoParaSetDeUmPonto> pontos, float peso, boolean opaco, PixelNoTile[] pixels) {
        for (PosicaoParaSetDeUmPonto ponto : pontos) {
            popularPixel(ponto.x, ponto.y, peso, opaco, pixels);
        }
    }

    private static void popularPixel(int x, int y, float peso, boolean opaco, PixelNoTile[] pixels) {
        if ((x >= 0) && (x <= 255) && (y >= 0) && (y <= 255) && (w * y + x >= 0) && (w * y + x + 1 <= tamanhoMaximo) && (x != 0 || y != 0)) {
            if (pixels[w * y + x] == null) {
                pixels[w * y + x] = new PixelNoTile();
            }
            pixels[w * y + x].addPeso(peso, opaco);
        }
    }

    private int[] transformarArrayObjParaArrayInt(PixelNoTile[] pixels) {
        int[] retorno = new int[tamanhoMaximo];

        for (int i = 0; i < tamanhoMaximo; i++) {
            if (pixels[i] == null) {
                continue;
            }

            int indicecorDoPonto = Math.max(Math.min(((int) (pixels[i].getPeso() * coresSolidas.length)), 30) - 1, 0);

            if (pixels[i].isOpaco()) {
                retorno[i] = coresSolidas[indicecorDoPonto];
            } else {
                retorno[i] = coresTransparentes[indicecorDoPonto];
            }
        }

        return retorno;
    }
}
