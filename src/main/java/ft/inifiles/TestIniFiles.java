package ft.inifiles;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 *
 * @author LeleFT
 */
public class TestIniFiles {
    public static void main(String[] args) throws Exception {
        // Creo un file INI da zero
        IniFile ini = new IniFile();
        try {
            // Aggiungo 2 configurazioni senza contesto
            ini.addContext("");
            ini.addConfiguration("", "prova-vuoto", "prova");
            ini.addConfiguration("", "prova-2-vuoto", "test");

            // Aggiungo il contesto [Contesto1] e al suo interno
            // creo una configurazione
            ini.addContext("Contesto1");
            ini.addConfiguration("Contesto1", "key1", "value1");

            // Aggiungo il contesto [Contesto2] e al suo interno
            // creo due configurazioni
            ini.addContext("Contesto2");
            ini.addConfiguration("Contesto2", "key2", "value2");
            ini.addConfiguration("Contesto2", "key3", "value3");

            // Provo ad aggiungere una configurazione ad un contesto inesistente
            ini.addConfiguration("pippo", "pluto", "paperino");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Salvo l'INI nel file "test.ini"
        ini.save( new File("test.ini") );

        // Ora rileggo il file INI creato in precedenza per testare anche
        // il meccanismo di lettura
        try {
            IniFile leggiIni = new IniFile( new File("test.ini") );
            List<String> contesti = leggiIni.getContexts();
            for(String ctx : contesti) {
                System.out.println("Contesto: " + ctx);
                Map<String, String> configurazioni = leggiIni.getContextConfigurations(ctx);
                for(Map.Entry<String,String> entry : configurazioni.entrySet()) {
                    System.out.println(entry.getKey() + " --> " + entry.getValue());
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
