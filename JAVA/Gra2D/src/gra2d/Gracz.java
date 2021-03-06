package gra2d;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Gracz {
    private static final double PREDKOSC = 0.80;
    private int x;
    private int y;
    private static BufferedImage obrazPrawo;
    private static BufferedImage obrazLewo;
    private static BufferedImage obrazGora;
    private static BufferedImage obrazDol;
    private long czas;
    private double xRuch;
    private double yRuch;
    private kierunekPoruszania kierunek;
    private kierunekPoruszania kierunekZamawiany;
    private boolean czyMaChodzic;
    
    private int punkty;
    private int zycia;
    
    public Gracz(int x, int y) {
        this.x = x;
        this.y = y;
        czas = 0 ;
        xRuch = 0;
        yRuch = 0;
        kierunek = kierunekPoruszania.LEWO;
        kierunekZamawiany = kierunekZamawiany;
        czyMaChodzic = false;
        punkty = 0;
        zycia = 3;
    }
    
    public static void wczytajObraz(){
        try {
            obrazPrawo = ImageIO.read(new File("obrazy/grafiki/pacman.png"));
            obrazLewo = ImageIO.read(new File("obrazy/grafiki/pacman_lewo.png"));
            obrazGora = ImageIO.read(new File("obrazy/grafiki/pacman_gora.png"));
            obrazDol = ImageIO.read(new File("obrazy/grafiki/pacman_dol.png"));
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void rysuj(Graphics2D graphics){
        if(null != kierunek)switch (kierunek) {
            case PRAWO:
                graphics.drawImage(obrazPrawo, (int) (y * Mapa.KAFELKA_SZEROKOSC + yRuch), (int) (x * Mapa.KAFELKA_WYSOKOSC + xRuch), null);
                break;
            case LEWO:
                graphics.drawImage(obrazLewo, (int) (y * Mapa.KAFELKA_SZEROKOSC + yRuch), (int) (x * Mapa.KAFELKA_WYSOKOSC + xRuch), null);
                break;
            case GORA:
                graphics.drawImage(obrazGora, (int) (y * Mapa.KAFELKA_SZEROKOSC + yRuch), (int) (x * Mapa.KAFELKA_WYSOKOSC + xRuch), null);
                break;
            case DOL:
                graphics.drawImage(obrazDol, (int) (y * Mapa.KAFELKA_SZEROKOSC + yRuch), (int) (x * Mapa.KAFELKA_WYSOKOSC + xRuch), null);
                break;
            default:
                graphics.drawImage(obrazPrawo, (int) (y * Mapa.KAFELKA_SZEROKOSC + yRuch), (int) (x * Mapa.KAFELKA_WYSOKOSC + xRuch), null);
                break;
        }
        

    }
    
    public void aktualizuj(long czas,ElementMapy[][] mapa,boolean gwiazdki[][],Mapa glownaMapa){
        this.czas += czas;
        if (this.czas >= 10) {
            this.czas = this.czas - 10;
            if (czyMaChodzic) {
                //kod do sprawdzenia czy mozna wykonac zamawiany kierunek
                boolean czyZamawianyPossible = false;
                if(kierunekZamawiany == kierunekPoruszania.LEWO){
                    if(xRuch == 0 && mapa[x][y-1].getElement() == ElementMapy.elementy.PUSTE){
                        czyZamawianyPossible = true;
                    }
                }else if(kierunekZamawiany == kierunekPoruszania.PRAWO){
                    if(xRuch == 0 && mapa[x][y+1].getElement() == ElementMapy.elementy.PUSTE){
                        czyZamawianyPossible = true;
                    }
                }else if(kierunekZamawiany == kierunekPoruszania.GORA){
                    if(yRuch == 0 && mapa[x-1][y].getElement() == ElementMapy.elementy.PUSTE){
                        czyZamawianyPossible = true;
                    }
                }else if(kierunekZamawiany == kierunekPoruszania.DOL){
                    if(yRuch == 0 && mapa[x+1][y].getElement() == ElementMapy.elementy.PUSTE){
                        czyZamawianyPossible = true;
                    }
                }
                //jezeli mozna to kierunek = kierunekZamawiany
                if(czyZamawianyPossible){
                    kierunek = kierunekZamawiany;
                }
                
                if(kierunek == kierunekPoruszania.LEWO){
                    if(mapa[x][y-1].getElement() == ElementMapy.elementy.SCIANA){
                        xRuch = 0;
                        yRuch = 0;
                        return;
                    }
                }else if(kierunek == kierunekPoruszania.PRAWO){
                    if(mapa[x][y+1].getElement() == ElementMapy.elementy.SCIANA){
                        xRuch = 0 ;
                        yRuch = 0;
                        
                        return;
                    }
                }else if(kierunek == kierunekPoruszania.GORA){
                    if(mapa[x-1][y].getElement() == ElementMapy.elementy.SCIANA){
                        xRuch = 0;
                        yRuch = 0;
                        return;
                    }
                }else if(kierunek == kierunekPoruszania.DOL){
                    if(mapa[x+1][y].getElement() == ElementMapy.elementy.SCIANA){
                        xRuch = 0;
                        yRuch = 0;
                        return;
                    }
                }
                
                if(kierunek == kierunekPoruszania.NIEZNANY){
                    kierunek = kierunekPoruszania.GORA;
                }else{
                    switch (kierunek) {
                        case LEWO:
                            yRuch -= PREDKOSC;
                            break;
                        case PRAWO:
                            yRuch += PREDKOSC;
                            break;
                        case GORA:
                            xRuch -= PREDKOSC;
                            break;
                        case DOL:
                            xRuch += PREDKOSC;
                            break;
                        default:
                    }
                    
                    /// do logiki //
                    if (yRuch >= 24) {
                        y++;
                        yRuch = 0;
                        if(gwiazdki[x][y] == true){
                            gwiazdki[x][y] = false;
                            punkty++;
                            glownaMapa.zmniejszGwiazdki();
                        }
                       
                    } else if (yRuch <= -24) {
                        y--;
                        yRuch = 0;
                        if(gwiazdki[x][y] == true){
                            gwiazdki[x][y] = false;
                            punkty++;
                            glownaMapa.zmniejszGwiazdki();
                        }
                        
                    } else if (xRuch >= 24) {
                        x++;
                        xRuch = 0;
                        if(gwiazdki[x][y] == true){
                            gwiazdki[x][y] = false;
                            punkty++;
                            glownaMapa.zmniejszGwiazdki();
                        }
                        
                    } else if (xRuch <= -24) {
                        x--;
                        xRuch = 0;
                        if(gwiazdki[x][y] == true){
                            gwiazdki[x][y] = false;
                            punkty++;
                            glownaMapa.zmniejszGwiazdki();
                        }
                       
                    }
                }
                
                
            }
        }
    }
    
    public void setCzyMaChodzic(boolean b){
        czyMaChodzic = b;
    }
    
    public void setKierunek(kierunekPoruszania kierunek){
        this.kierunekZamawiany = kierunek;
    }
    public int getWspolrzednaX(){
        return (int) (x * Mapa.KAFELKA_WYSOKOSC + xRuch);
    }
    
    public int getWspolrzednaY(){
        return (int) (y * Mapa.KAFELKA_SZEROKOSC + yRuch);
    }
    
    public int getZycia(){
        return zycia;
    }
    
    public void ustawZycia(int zycia){
        this.zycia = zycia;
    }

    public int getPunkty() {
        return punkty;
    }

    public void setPunkty(int punkty) {
        this.punkty = punkty;
    }
    
    public void setPozycja(int x,int y){
        this.x = x;
        this.y = y;
        
        xRuch = 0;
        yRuch = 0;
    }
    
}
