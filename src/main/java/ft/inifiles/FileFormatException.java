package ft.inifiles;

import java.io.IOException;

/**
 * Viene lanciata se si tenta di costruire un oggetto IniFile a partire da una
 * sorgente che non rispetta la struttura INI o la cui struttura risulta essere
 * malformata.
 * 
 * @author Manuel.Agostinetto
 */
public class FileFormatException extends IOException {
    public FileFormatException(String msg) {
        super( msg );
    }
}
