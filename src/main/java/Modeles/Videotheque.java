package Modeles;

import Exceptions.*;
import Modeles.Abstracts.FichierVideo;
import Modeles.Abstracts.Video;
import Modeles.Interfaces.GestionVideotheque;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Videotheque implements GestionVideotheque {

    private static List<Video> videos;

    public static List<Video> getVideotheque() {
        return videos;
    }

    @Override
    public void ajouterVideo(Video v) throws VideoDejaExistanteException, SaisieInvalideException {
        try {
            rechercherVideo(v.getTitre());
        } catch (VideothequeVideException | VideoIntrouvableException e) {
            if(v instanceof FichierVideo){
                FichierVideo fichierVideo = (FichierVideo) v;
                String chemin = fichierVideo.getChemin();
                if(!(new File(chemin).exists())){
                    throw new SaisieInvalideException("Le fichier vidéo n'existe pas sur le disque.");
                }
            }
            getVideotheque().add(v);
            System.out.println(v + "ajouté avec succès !");
            return;
        }
        throw new VideoDejaExistanteException("Cette vidéo exciste déjà !");
    }

    @Override
    public void listerVideos() throws VideothequeVideException {
        if (getVideotheque().isEmpty()) {
            throw new VideothequeVideException("Discothèque vide !");
        }
        for (Video v : getVideotheque()) {
            System.out.println(v);
        }
    }

    @Override
    public Video rechercherVideo(String titre) throws VideothequeVideException, VideoIntrouvableException {
        if (getVideotheque().isEmpty()) {
            throw new VideothequeVideException("La discothèque est vide.");
        }
        for (Video video : getVideotheque()) {
            if (video.getTitre().equalsIgnoreCase(titre)) {
                System.out.println("Vidéo trouvé: " + video);
                return video;
            }
        }
        throw new VideoIntrouvableException("Album introuvable: " + titre);
    }

    @Override
    public void supprimerVideo(String titre) throws VideothequeVideException, VideoIntrouvableException {
        Video v = rechercherVideo(titre);
        getVideotheque().remove(v);
        System.out.println("Vidéo supprimé de la vidéothèque: " + v);
    }

    @Override
    public void lireVideo(String titre) throws VideothequeVideException, VideoIntrouvableException, LectureImpossibleException {
        rechercherVideo(titre).lire();
    }

    @Override
    public Video convertirVideo(String titre, String format) throws ConversionImpossibleException, VideothequeVideException, VideoIntrouvableException, IOException, InterruptedException {
        Video v = rechercherVideo(titre);
        if (!(v instanceof FichierVideo)) {
            throw new ConversionImpossibleException("La vidéo n'est pas un fichier vidéo et ne peut pas être convertie.");
        }

        Video convertedVideo = ((FichierVideo) v).convertir(format);
        int index = getVideotheque().indexOf(v);
        getVideotheque().set(index, convertedVideo);

        System.out.println("Vidéo convertie: " + convertedVideo);
        return null;
    }
}
