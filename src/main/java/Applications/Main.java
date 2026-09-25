package Applications;

import Exceptions.SaisieInvalideException;
import Modeles.Interfaces.GestionVideotheque;
import Modeles.Videotheque;

import java.time.DateTimeException;
import java.util.InputMismatchException;

public class Main {

    private static final GestionVideotheque videotheque = new Videotheque();

    public static void main(String[] args) {

        Controller c = new Controller();
        int choix = -1;

        do {
            try {
                c.afficherMenu();
                System.out.print("Choix:");
                choix = Controller.scan.nextInt();

                switch (choix) {
                    case 1:
                        c.ajouterVideo(videotheque);
                        break;
                    case 2:
                        c.listerVideos(videotheque);
                        break;
                    case 3:
                        c.rechercherVideo(videotheque);
                        break;
                    case 4:
                        c.supprimerVideo(videotheque);
                        break;
                    case 5:
                        c.lireVideo(videotheque);
                        break;
                    case 6:
                        c.convertirVideo(videotheque);
                        break;
                    case 0:
                        System.out.println("Au revoir !");
                        break;
                    default:
                        System.out.println("Choix invalide, veuillez réessayer.");
                }

                System.out.println();
            }  catch (InputMismatchException e){
                System.out.println("Erreur: " + e.getClass().getSimpleName());
                Controller.scan.nextLine();
            } catch (Exception e) {
                System.out.println("Erreur: "+ e.getMessage() + " (" + e.getClass().getSimpleName() + ")");
            }

        } while (choix != 0);

        Controller.scan.close();

    }
}
