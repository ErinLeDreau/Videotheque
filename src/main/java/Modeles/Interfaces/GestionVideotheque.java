package Modeles.Interfaces;

import Modeles.Abstracts.Video;

public interface GestionVideotheque {

    void ajouterVideo(Video v);
    void listerVideos();
    Video rechercherVideo(String titre);
    void supprimerVideo(String titre);
    void lireVideo(String titre);
    Video convertirVideo(String titre, String format);

}
