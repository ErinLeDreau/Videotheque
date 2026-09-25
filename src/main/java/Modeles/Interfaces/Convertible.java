package Modeles.Interfaces;

import Exceptions.ConversionImpossibleException;
import Exceptions.SaisieInvalideException;
import Modeles.Abstracts.FichierVideo;

import java.io.IOException;

public interface Convertible {

    /** Convertit au format "MP4" ou "AVI" et renvoie le nouvel objet.
     Le fichier d'origine est conservé. */
    FichierVideo convertir(String formatCible)
            throws ConversionImpossibleException, SaisieInvalideException, IOException, InterruptedException;
}
