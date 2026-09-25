package Modeles;

import Modeles.Abstracts.Video;

import java.time.LocalDate;

public class Dvd extends Video {

    private String numero;
    private int zone;

    public Dvd(String titre, String realisateur, LocalDate dateSortie, int duree, String numero, int zone) {
        super(titre, realisateur, dateSortie, duree);
        this.numero = numero;
        this.zone = zone;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public String getSupport() {
        return "DVD";
    }
    public void lire() {
        String message = "Prenez le DVD " + this.titre + " " + this.numero + " et insérez-le dans un lecteur zone 2.";
        System.out.println(message);
    }

    @Override
    public String toString() {
        return super.toString() + "support='" + this.getSupport() + '\'' + ", numero='" + this.getNumero() + '\'' + ", zone=" + this.getZone() + '}';
    }
}
