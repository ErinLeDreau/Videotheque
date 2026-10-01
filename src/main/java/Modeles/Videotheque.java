package Modeles;

import Exceptions.*;
import Modeles.Abstracts.FichierVideo;
import Modeles.Abstracts.Video;
import Modeles.Interfaces.GestionVideotheque;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Videotheque implements GestionVideotheque {

    private static List<Video> videos = new ArrayList<>();

    public static List<Video> getVideotheque() {
        return videos;
    }

    @Override
    public void ajouterVideo(Video v) throws VideoDejaExistanteException, SaisieInvalideException {
        try {
            rechercherVideo(v.getTitre());
        } catch (VideothequeVideException | VideoIntrouvableException e) {
            if(v instanceof FichierVideo fichierVideo){
                String chemin = fichierVideo.getChemin();
                File fichier = new File(chemin);
                if(!fichier.isFile()){
                    throw new SaisieInvalideException("Le fichier vidéo n'existe pas sur le disque.");
                }
                String cheminMinuscule = chemin.toLowerCase(Locale.ROOT);
                if ((v instanceof VideoMp4 && !cheminMinuscule.endsWith(".mp4"))
                        || (v instanceof VideoAvi && !cheminMinuscule.endsWith(".avi"))) {
                    throw new SaisieInvalideException("L'extension du fichier ne correspond pas à son type.");
                }
            }
            getVideotheque().add(v);
            System.out.println(v + "ajouté avec succès !");
            return;
        }
        throw new VideoDejaExistanteException("Cette vidéo existe déjà !");
    }

    public void initList(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate date = LocalDate.parse("10/12/2010", formatter);
        getVideotheque().add(new Dvd("Film1", "Christopher Nolan", date, 148, "numero123", 1));
        getVideotheque().add(new VideoMp4("Film2", "Lana Wachowski, Lilly Wachowski", date, 136, "C:\\Users\\CDAX24\\Downloads\\Denis-Ah.mp4"));
        getVideotheque().add(new VideoAvi("Film3", "James Cameron", date, 162, "C:\\Users\\CDAX24\\Downloads\\meme-compil.avi"));
    }
    @Override
    public void listerVideos() throws VideothequeVideException {
        if (getVideotheque().isEmpty()) {
            throw new VideothequeVideException("Vidéothèque vide !");
        }
        for (Video v : getVideotheque()) {
            System.out.println(v);
        }
    }

    @Override
    public Video rechercherVideo(String titre) throws VideothequeVideException, VideoIntrouvableException {
        if (getVideotheque().isEmpty()) {
            throw new VideothequeVideException("La vidéothèque est vide.");
        }
        for (Video video : getVideotheque()) {
            if (video.getTitre().equalsIgnoreCase(titre)) {
                System.out.println("Vidéo trouvé: " + video);
                return video;
            }
        }
        throw new VideoIntrouvableException("Vidéo introuvable: " + titre);
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
    public Video convertirVideo(String titre, String format) throws ConversionImpossibleException, VideothequeVideException, VideoIntrouvableException {
        Video v = rechercherVideo(titre);
        if (!(v instanceof FichierVideo)) {
            throw new ConversionImpossibleException("La vidéo n'est pas un fichier vidéo et ne peut pas être convertie.");
        }

        Video convertedVideo = ((FichierVideo) v).convertir(format);
        int index = getVideotheque().indexOf(v);
        getVideotheque().set(index, convertedVideo);

        System.out.println("Vidéo convertie: " + convertedVideo);
        return convertedVideo;
    }
}
