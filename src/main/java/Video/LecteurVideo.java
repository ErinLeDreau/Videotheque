package Video;

import Exceptions.FichierVideoException;
import Modeles.Abstracts.FichierVideo;
import Modeles.VideoAvi;
import Modeles.VideoMp4;
import Outils.Ffmpeg;

import java.io.File;

public class LecteurVideo implements Runnable{

    private static volatile LecteurVideo lecteurActif;
    private volatile boolean arretDemande;

    private final FichierVideo video;
    private volatile Process processus;
    private Thread thread;

    public LecteurVideo(FichierVideo video) throws FichierVideoException {
        if (video == null) {
            throw new FichierVideoException("La vidéo n'existe pas'.");
        }
        if (video.getChemin() == null || video.getChemin().isBlank()) {
            throw new FichierVideoException("Le chemin du fichier vidéo n'est pas présent.");
        }

        File fichier = new File(video.getChemin());
        if (!fichier.isFile()) {
            throw new FichierVideoException(
                    "Le fichier vidéo n'existe pas ou n'est pas un fichier : " + video.getChemin());
        }

        String nomFichier = fichier.getName().toLowerCase();
        String formatAttendu;
        if (video instanceof VideoMp4) {
            formatAttendu = ".mp4";
        } else if (video instanceof VideoAvi) {
            formatAttendu = ".avi";
        } else {
            throw new FichierVideoException(
                    "Type de vidéo non supporté : " + video.getClass().getSimpleName());
        }

        if (!nomFichier.endsWith(formatAttendu)) {
            throw new FichierVideoException(
                    "Le type de l'objet (" + video.getClass().getSimpleName()
                            + ") ne correspond pas au format du fichier : " + video.getChemin());
        }

        this.video = video;
    }


    public synchronized void demarrer(){
        thread = new Thread(this, "lecture-" + video.getTitre());
        thread.setDaemon(true);
        thread.start();
        if(lecteurActif != null) {
            //On force l'interruption de la lecture en cours si il y en a déjà une
            arreterLecteurActif();
        }
        lecteurActif = this;
    }

    public void arreter(){
        arretDemande = true;
        if (processus != null && processus.isAlive()) {
            processus.destroy();
        }
        if (thread != null) {
            thread.interrupt();
        }
    }

    public static boolean arreterLecteurActif() {
        LecteurVideo lecteur = lecteurActif;

        if (lecteur == null) {
            return false;
        }

        lecteur.arreter();
        return true;
    }

    public boolean estEnLecture() {
        return processus != null && processus.isAlive();
    }

    @Override
    public synchronized void run() {
        try {
            if (arretDemande) {
                return;
            }

            processus = Ffmpeg.lire(new File(video.getChemin()), video.getTitre());
            int codeRetour = processus.waitFor();
            if (codeRetour != 0) {
                System.out.println("La lecture a échoué (code " + codeRetour + ").");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("La lecture a été interrompue.");
        } catch (java.io.IOException e) {
            if (!arretDemande) {
                System.out.println("Erreur lors de la lecture de la vidéo : " + e.getMessage());
            }
        } finally {
            processus = null;

            if (lecteurActif == this) {
                lecteurActif = null;
            }
        }
    }
}
