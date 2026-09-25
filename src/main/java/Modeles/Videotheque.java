package Modeles;

import Modeles.Abstracts.Video;
import Modeles.Interfaces.GestionVideotheque;

import java.util.List;

public class Videotheque implements GestionVideotheque {

    private List<Video> videos;

    @Override
    public void ajouterVideo(Video v) {
        // Implémentation de l'ajout d'une vidéo à la vidéothèque
    }

    @Override
    public void listerVideos() {
        // Implémentation de la liste des vidéos dans la vidéothèque
    }

    @Override
    public Video rechercherVideo(String titre) {
        // Implémentation de la recherche d'une vidéo par titre
        return null;
    }

    @Override
    public void supprimerVideo(String titre) {
        // Implémentation de la suppression d'une vidéo par titre
    }

    @Override
    public void lireVideo(String titre) {
        // Implémentation de la lecture d'une vidéo par titre
    }

    @Override
    public Video convertirVideo(String titre, String format) {
        // Implémentation de la conversion d'une vidéo par titre et format
        return null;
    }
}
