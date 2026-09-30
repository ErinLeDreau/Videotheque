package Video;

import Exceptions.FichierVideoException;
import Modeles.Abstracts.FichierVideo;
import Modeles.VideoAvi;
import Modeles.VideoMp4;
import Outils.Ffmpeg;

import java.io.File;
import java.util.Locale;

public class LecteurVideo implements Runnable{

    private static LecteurVideo lecteurActif;

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

        String nomFichier = fichier.getName().toLowerCase(Locale.ROOT);
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
        if (lecteurActif != null && lecteurActif != this) {
            lecteurActif.arreter();
        }
        lecteurActif = this;
        thread = new Thread(this, "lecture-" + video.getTitre());
        thread.setDaemon(true);
        thread.start();
    }

    public synchronized void arreter(){
        if (processus != null && processus.isAlive()) {
            processus.destroy();
        }
        if (thread != null) {
            thread.interrupt();
        }
        if (lecteurActif == this) {
            lecteurActif = null;
        }
    }

    public static synchronized void arreterLectureActive() {
        if (lecteurActif != null) {
            lecteurActif.arreter();
        }
    }

    public boolean estEnLecture() {
        return processus != null && processus.isAlive();
    }

    @Override
    public void run() {
        try {
            processus = Ffmpeg.demarrerLecture(new File(video.getChemin()), video.getTitre());
            int codeRetour = processus.waitFor();
            if (codeRetour != 0) {
                System.out.println("La lecture a échoué (code " + codeRetour + ").");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("La lecture a été interrompue.");
        } catch (java.io.IOException e) {
            System.out.println("Erreur lors de la lecture de la vidéo : " + e.getMessage());
        } finally {
            processus = null;
            synchronized (LecteurVideo.class) {
                if (lecteurActif == this) {
                    lecteurActif = null;
                }
            }
        }
    }
}
