package Applications;

import Exceptions.*;
import Modeles.Abstracts.FichierVideo;
import Modeles.Abstracts.Video;
import Modeles.Dvd;
import Modeles.Interfaces.GestionVideotheque;
import Modeles.VideoAvi;
import Modeles.VideoMp4;
import Modeles.Videotheque;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Controller {

    public static Scanner scan = new Scanner(System.in);

    public void afficherMenu() {
        System.out.println("===== VIDÉOTHÈQUE =====");
        System.out.println("1. Ajouter une vidéo");
        System.out.println("2. Lister toute les vidéos");
        System.out.println("3. Rechercher une vidéo");
        System.out.println("4. Supprimer une vidéo");
        System.out.println("5. Lire une vidéo");
        System.out.println("6. Convertir une vidéo");
        System.out.println("0. Quitter");
        System.out.println("=======================");
    }

    public void ajouterVideo(GestionVideotheque videotheque) throws SaisieInvalideException, VideoDejaExistanteException {
        System.out.println("Support (1 = DVD, 2 = fichier MP4, 3 = Fichier AVI) : ");
        int choix = scan.nextInt();

        Video created = null;
        switch (choix) {
            case 1:
                created = creerDVD();
                break;
            case 2:
                created = creerVideoMp4();
                break;
            case 3:
                created = creerVideoAvi();
                break;
            default:
                return;
        }



        videotheque.ajouterVideo(created);
    }

    public void listerVideos(GestionVideotheque videotheque) throws VideothequeVideException {
        videotheque.listerVideos();
    }

    public void rechercherVideo(GestionVideotheque videotheque) throws SaisieInvalideException, VideothequeVideException, VideoIntrouvableException {
        System.out.print("Titre de la vidéo à rechercher:");
        String titre = saisieTitre();
        videotheque.rechercherVideo(titre);
    }

    public void supprimerVideo(GestionVideotheque videotheque) throws VideothequeVideException, VideoIntrouvableException {
        System.out.println("Titre de la vidéo à supprimer:");
        String titre = scan.nextLine();
        videotheque.supprimerVideo(titre);
    }

    public void lireVideo(GestionVideotheque videotheque) throws SaisieInvalideException, VideothequeVideException, LectureImpossibleException, VideoIntrouvableException {
        System.out.println("Titre de la vidéo à lire:");
        String titre = saisieTitre();
        videotheque.lireVideo(titre);
    }

    public void convertirVideo(GestionVideotheque videotheque) throws VideothequeVideException, VideoIntrouvableException, SaisieInvalideException, ConversionImpossibleException {
        System.out.println("Titre de la vidéo à supprimer:");
        String titre = saisieTitre();

        System.out.println("Format de conversion (AVI ou MP4):");
        String format = saisieFormat();

        videotheque.convertirVideo(titre, format);
    }

    private Dvd creerDVD () throws SaisieInvalideException {
        String titre = saisieTitre();
        String realisateur = saisieRealisateur();
        LocalDate date = saisieDate();
        int duree = saisieDuree();
        String numero = saisieNumero();
        int zone = saisieZone();



        Dvd dvd = new Dvd(titre, realisateur, date, duree, numero, zone);
        return dvd;
    }

    private VideoMp4 creerVideoMp4 () throws SaisieInvalideException {
        String titre = saisieTitre();
        String realisateur = saisieRealisateur();
        LocalDate date = saisieDate();
        int duree = saisieDuree();
        String chemin = saisieChemin();



        VideoMp4 videoMp4 = new VideoMp4(titre, realisateur, date, duree, chemin);
        return videoMp4;
    }

    private VideoAvi creerVideoAvi () throws SaisieInvalideException {
        String titre = saisieTitre();
        String realisateur = saisieRealisateur();
        LocalDate date = saisieDate();
        int duree = saisieDuree();
        String chemin = saisieChemin();



        VideoAvi videoAvi = new VideoAvi(titre, realisateur, date, duree, chemin);
        return videoAvi;
    }

    private String saisieTitre() throws SaisieInvalideException {
        System.out.print("Titre:");
        String titre = scan.nextLine();
        if (titre.isEmpty()) {
            throw new SaisieInvalideException("Titre doit être présent");
        }
        return titre;
    }

    private String saisieRealisateur() throws SaisieInvalideException {
        System.out.print("Réalisateur:");
        String realisateur = scan.nextLine();
        if (realisateur.isEmpty()) {
            throw new SaisieInvalideException("Réalisateur doit être présent");
        }
        return realisateur;
    }

    private int saisieDuree() throws SaisieInvalideException {
        System.out.print("Durée (en minutes):");
        int duree = scan.nextInt();
        scan.nextLine();
        if (duree <= 0) {
            throw new SaisieInvalideException("La vidéo ne peut pas avoir une durée négative ou nulle");
        }
        return duree;
    }

    private String saisieNumero() throws SaisieInvalideException {
        System.out.print("Numéro de série:");
        String numero = scan.nextLine();
        if (numero.isEmpty()) {
            throw new SaisieInvalideException("Numéro de série doit être présent");
        }
        return numero;
    }

    private int saisieZone() throws SaisieInvalideException {
        System.out.print("Zone:");
        int zone = scan.nextInt();
        if (zone < 0) {
            throw new SaisieInvalideException("Zone ne peut pas être négative");
        }
        return zone;
    }

    private LocalDate saisieDate() throws SaisieInvalideException {
        System.out.println("Saisissez la date de publication au format dd/mm/yyyy");
        String date = scan.nextLine();
        if(!date.matches("^\\d{2}/\\d{2}/\\d{4}$")){
            throw new SaisieInvalideException("La date n'est pas au format dd/mm/yyyy");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return LocalDate.parse(date, formatter);
    }

    private String saisieChemin() throws SaisieInvalideException {
        System.out.print("Chemin du fichier (exemple musiques/bidule.mp3):");
        String chemin = scan.nextLine();
        if (chemin.isEmpty()) {
            throw new SaisieInvalideException("Chemin doit être présent");
        }
        //TODO, peut être un regex pour valider le format ?
        return chemin;
    }

    private String saisieFormat() throws SaisieInvalideException {
        System.out.print("Format (MP4 ou AVI):");
        String format = scan.nextLine();
        if (format.isEmpty()) {
            throw new SaisieInvalideException("Format doit être fourni");
        }
        if(!format.equalsIgnoreCase("MP4") && !format.equalsIgnoreCase("AVI")) {
            throw new SaisieInvalideException("Format doit être MP4 ou AVI");
        }
        return format;
    }
}
