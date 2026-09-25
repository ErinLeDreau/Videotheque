package Modeles.Abstracts;

import Exceptions.ConversionImpossibleException;
import Exceptions.LectureImpossibleException;
import Modeles.Interfaces.Convertible;
import Modeles.VideoAvi;
import Modeles.VideoMp4;
import Outils.Ffmpeg;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public abstract class FichierVideo extends Video implements Convertible {

    protected String chemin;

    public FichierVideo(String titre, String realisateur, LocalDate dateSortie, int duree, String chemin) {
        super(titre, realisateur, dateSortie, duree);
        this.chemin = chemin;
    }

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }


    public abstract String getSupport();

    protected abstract List<String> optionsEncodage();
    @Override
    public void lire() throws LectureImpossibleException {
        File fichier = new File(chemin);
        if (!fichier.exists()) {
            throw new LectureImpossibleException("Le fichier vidéo n'existe pas : " + chemin);
        }

        Thread thread = new Thread();
        thread.setDaemon(true);
    }

    public FichierVideo convertir(String format) throws IOException, InterruptedException, ConversionImpossibleException {
        if(!format.equalsIgnoreCase("mp4") && !format.equalsIgnoreCase("avi")) {
            throw new ConversionImpossibleException("Format de conversion non supporté : " + format);
        }

        if(format.equalsIgnoreCase(this.getSupport())) {
            throw new ConversionImpossibleException("Le fichier est déjà au format " + format);
        }

        if(format.equalsIgnoreCase("mp4")){
            //TODO remplacer l'extension du chemin par .avi, créer l'objet VideoAvi
            //TODO le convertir en utilisant les options d'encodage de VideoAvi et la fonction convertir de Ffmpeg
            //TODO retourner l'objet VideoAvi

            assert this instanceof VideoAvi;
            VideoMp4 videoMp4 = new VideoMp4((VideoAvi) this);

            File fichierEntrant = new File(this.getChemin());
            File fichierSortant = new File(videoMp4.getChemin());

            Ffmpeg.convertir(fichierEntrant, fichierSortant, videoMp4.optionsEncodage());

            return videoMp4;
        }

        if(format.equalsIgnoreCase("avi")){
            //TODO remplacer l'extension du chemin par .mp4, créer l'objet VideoMp4
            //TODO le convertir en utilisant les options d'encodage de VideoMp4 et la fonction convertir de Ffmpeg
            //TODO retourner l'objet VideoMp4

            assert this instanceof VideoMp4;
            VideoAvi videoAvi = new VideoAvi((VideoMp4) this);

            File fichierEntrant = new File(this.getChemin());
            File fichierSortant = new File(videoAvi.getChemin());

            Ffmpeg.convertir(fichierEntrant, fichierSortant, videoAvi.optionsEncodage());

            return videoAvi;
        }

        return null;
    }

    @Override
    public String toString() {
        return super.toString() + " [Chemin: " + chemin + "]";
    }
}
