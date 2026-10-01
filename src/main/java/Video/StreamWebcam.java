package Video;

import Outils.Ffmpeg;

import java.io.File;

public class StreamWebcam implements Runnable {
    private String url = "rtmp://a.rtmp.youtube.com/live2";
    private String key = "votre_cle_de_streaming"; // Remplacez par votre clé de streaming

    private volatile Process processus;
    private Thread thread;

    public StreamWebcam(String url, String key) {
        this.url = url;
        this.key = key;
    }

    @Override
    public void run() {
        try{
            System.out.println("Streaming de la webcam à l'URL : " + url);
            processus = Ffmpeg.webcam(this.url,this.key);
            int codeRetour = processus.waitFor();
            if (codeRetour != 0) {
                System.out.println("La lecture a échoué (code " + codeRetour + ").");
            }
            System.out.println("ici");
        } catch (Exception e) {
            System.out.println("Erreur lors du streaming de la webcam : " + e.getMessage());
        } finally {
            processus = null;
        }

    }

    public void lancerWebcam(){
        thread = new Thread(this, "webcam streaming");
        thread.setDaemon(true);
        thread.start();
    }
}
