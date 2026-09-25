package Modeles;

import Modeles.Abstracts.FichierVideo;
import Modeles.Abstracts.Video;

import java.time.LocalDate;
import java.util.List;

public class VideoAvi extends FichierVideo {

    public VideoAvi(String titre, String realisateur, LocalDate dateSortie, int duree, String chemin) {
        super(titre, realisateur, dateSortie, duree, chemin);
    }

    public VideoAvi(VideoMp4 videoMp4){
        String nouveauChemin = videoMp4.getChemin().replaceAll("\\.mp4$", ".avi");
        super(videoMp4.getTitre(), videoMp4.getRealisateur(), videoMp4.getDateSortie(), videoMp4.getDuree(), nouveauChemin);
    }

    @Override
    public String getSupport() {
        return "AVI";
    }

    @Override
    public List<String> optionsEncodage() {
        return List.of("-q:v 5", "-c:a libmp3lame");
    }

    @Override
    public String toString() {
        return super.toString() + "support='" + this.getSupport();
    }
}
