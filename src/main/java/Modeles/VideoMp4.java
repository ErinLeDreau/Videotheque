package Modeles;

import Modeles.Abstracts.FichierVideo;
import Modeles.Abstracts.Video;

import java.util.List;

public class VideoMp4 extends FichierVideo{

    public VideoMp4(String titre, String realisateur, java.time.LocalDate dateSortie, int duree, String chemin) {
        super(titre, realisateur, dateSortie, duree, chemin);
    }

    public VideoMp4(VideoAvi videoAvi){
        String nouveauChemin = videoAvi.getChemin().replaceAll("\\.avi$", ".mp4");
        super(videoAvi.getTitre(), videoAvi.getRealisateur(), videoAvi.getDateSortie(), videoAvi.getDuree(), videoAvi.getChemin());
    }

    @Override
    public String getSupport() {
        return "MP4";
    }

    @Override
    public List<String> optionsEncodage() {
        return List.of("-crf 23", "-c:a aac");
    }
}
