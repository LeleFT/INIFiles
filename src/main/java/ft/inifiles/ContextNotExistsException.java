package ft.inifiles;

/**
 * Viene lanciata quando si tenta di aggiungere una configurazione ad un contesto
 * che non &egrave; stato definito in precedenza.
 * 
 * @author LeleFT
 */
public class ContextNotExistsException extends RuntimeException {
    public ContextNotExistsException(String msg) {
        super( msg );
    }
}
