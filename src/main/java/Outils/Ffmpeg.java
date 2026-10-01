package Outils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class Ffmpeg {
    private Ffmpeg() {
    } // classe utilitaire : aucune instance

    /**
     * ffmpeg -y -i entree [options] sortie
     * Renvoie le code de retour de ffmpeg (0 = succès).
     */
    public static int convertir(File entree, File sortie, List<String> options)
            throws IOException, InterruptedException {

        List<String> commande = new ArrayList<>();
        commande.add("ffmpeg");
        commande.add("-y");
        commande.add("-loglevel");
        commande.add("error"); // n'affiche que les erreurs
        commande.add("-i");
        commande.add(entree.getAbsolutePath());
        commande.addAll(options);
        commande.add(sortie.getAbsolutePath());
        ProcessBuilder pb = new ProcessBuilder(commande);
        pb.redirectErrorStream(true); // stderr fusionné dans stdout
        Process processus = pb.start();
        // TODO : lire la sortie du processus ligne par ligne
        // (BufferedReader) et l'afficher

        BufferedReader processOut = new BufferedReader(new InputStreamReader(processus.getInputStream()));
        String line;
        while ((line = processOut.readLine()) != null) {
            System.out.println(line);
        }


        return processus.waitFor(); // attend la fin de ffmpeg
    }

    public static Process lire(File fichier, String titreFenetre) throws IOException {
        List<String> commande = new ArrayList<>();
        commande.add("ffplay");
        commande.add("-autoexit");
        commande.add("-window_title");
        commande.add(titreFenetre);
        commande.add(fichier.getAbsolutePath());
        ProcessBuilder pb = new ProcessBuilder(commande);
        pb.redirectErrorStream(true); // stderr fusionné dans stdout
        pb.redirectOutput(ProcessBuilder.Redirect.DISCARD); // on ne veut pas afficher la sortie de ffplay
        return pb.start();
    }

    public static Process webcam(String streamUrl, String streamKey) throws IOException {
        // stream url "rtmp://a.rtmp.youtube.com/live2"
        //temp key dk7g-zakt-qb90-8u7x-6t12
        //ffmpeg -f dshow -i video="HP True Vision HD Camera":audio="Réseau de microphones (Technologie Intel® Smart Sound pour microphones numériques)" -vcodec libx264 -preset ultrafast -tune zerolatency -b:v 1000k -maxrate 1000k -bufsize 2000k -pix_fmt yuv420p -g 60 -f flv "rtmp://a.rtmp.youtube.com/live2/wrrx-t672-z4xr-g307-bbzf"
        String videoDevice = "HP True Vision HD Camera";
        String audioDevice = "Réseau de microphones (Technologie Intel® Smart Sound pour microphones numériques)";
        List<String> commande = new ArrayList<>();
        commande.add("ffmpeg");
        commande.add("-f");
        commande.add("dshow");
        commande.add("-i");
        commande.add("video=" + videoDevice + ":audio=" + audioDevice);
        commande.add("-vcodec");
        commande.add("libx264");
        commande.add("-preset");
        commande.add("ultrafast");
        commande.add("-tune");
        commande.add("zerolatency");
        commande.add("-b:v");
        commande.add("1000k");
        commande.add("-maxrate");
        commande.add("1000k");
        commande.add("-bufsize");
        commande.add("2000k");
        commande.add("-pix_fmt");
        commande.add("yuv420p");
        commande.add("-g");
        commande.add("60");
        commande.add("-f");
        commande.add("flv");
        commande.add("rtmp://" + streamUrl + "/" + streamKey);
        System.out.println("Commande ffmpeg pour le streaming de la webcam : " + String.join(" ", commande));
        ProcessBuilder pb = new ProcessBuilder(commande);
        pb.redirectErrorStream(true); // stderr fusionné dans stdout
        pb.redirectOutput(ProcessBuilder.Redirect.DISCARD); // on ne veut pas afficher la sortie de ffplay
        return pb.start();
    }


}
