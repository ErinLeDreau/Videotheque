package Modeles.Abstracts;

import Exceptions.ConversionImpossibleException;
import Exceptions.FichierVideoException;
import Exceptions.LectureImpossibleException;
import Modeles.Interfaces.Convertible;
import Modeles.VideoAvi;
import Modeles.VideoMp4;
import Outils.Ffmpeg;
import Video.LecteurVideo;

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
        if (!fichier.isFile()) {
            throw new LectureImpossibleException("Le fichier vidéo n'existe pas : " + chemin);
        }

        try {
            new LecteurVideo(this).demarrer();
        } catch (FichierVideoException e) {
            throw new LectureImpossibleException(e.getMessage());
        }
    }

    public FichierVideo convertir(String format) throws ConversionImpossibleException {
        if(!format.equalsIgnoreCase("mp4") && !format.equalsIgnoreCase("avi")) {
            throw new ConversionImpossibleException("Format de conversion non supporté : " + format);
        }

        if(format.equalsIgnoreCase(this.getSupport())) {
            throw new ConversionImpossibleException("Le fichier est déjà au format " + format);
        }

        if(format.equalsIgnoreCase("mp4")){
            if (!(this instanceof VideoAvi)) {
                throw new ConversionImpossibleException("Le type de la vidéo ne correspond pas au format source AVI.");
            }
            VideoMp4 videoMp4 = new VideoMp4((VideoAvi) this);

            File fichierEntrant = new File(this.getChemin());
            File fichierSortant = new File(videoMp4.getChemin());

            try{
                int codeRetour = Ffmpeg.convertir(fichierEntrant, fichierSortant, videoMp4.optionsEncodage());
                if (codeRetour != 0) {
                    throw new ConversionImpossibleException("FFmpeg a échoué (code " + codeRetour + ").");
                }
            } catch (IOException | InterruptedException | ConversionImpossibleException e) {
                throw new ConversionImpossibleException("Erreur lors de la conversion : " + e.getMessage());
            }

            return videoMp4;
        }

        if(format.equalsIgnoreCase("avi")){
            if (!(this instanceof VideoMp4)) {
                throw new ConversionImpossibleException("Le type de la vidéo ne correspond pas au format source MP4.");
            }
            VideoAvi videoAvi = new VideoAvi((VideoMp4) this);

            File fichierEntrant = new File(this.getChemin());
            File fichierSortant = new File(videoAvi.getChemin());

            try {
                int codeRetour = Ffmpeg.convertir(fichierEntrant, fichierSortant, videoAvi.optionsEncodage());
                if (codeRetour != 0) {
                    throw new ConversionImpossibleException("FFmpeg a échoué (code " + codeRetour + ").");
                }
            } catch (IOException | InterruptedException | ConversionImpossibleException e) {
                throw new ConversionImpossibleException("Erreur lors de la conversion : " + e.getMessage());
            }


            return videoAvi;
        }

        throw new ConversionImpossibleException("Format de conversion non supporté : " + format);
    }

    @Override
    public String toString() {
        return super.toString() + " [Chemin: " + chemin + "]";
    }
}
