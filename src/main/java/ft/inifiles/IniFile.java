package ft.inifiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Un oggetto di questa classe rappresenta un file INI.
 * <p>Un file INI &egrave; un file di testo generalmente utilizzato per la
 * configurazione dei programmi ed è composto di &quot;contesti&quot; e
 * configurazioni.</p>
 * <p>Ciascun contesto &egrave; rappresentato da una stringa racchiusa tra
 * parentesi quadre e viene utilizzato per organizzare in blocchi logici le
 * varie configurazioni.</p>
 * <p>&Egrave; possibile avere anche configurazioni non appartenenti a nessun
 * contesto: generalmente queste configurazioni vengono elencate all'inizio
 * del file di testo.<br />
 * Configurazioni di questo tipo senza alcun contesto vengono gestite dalla
 * libreria come configurazioni appartenenti al contesto &quot;&quot;
 * (stringa vuota).</p>
 * <p>Di seguito un esempio della struttura di un file INI:<p>
 * <pre>
 * cfg1 = value1
 * cfg2 = value2
 * 
 * [Contesto_1]
 * configurazione1 = valore_configurazione1
 * configurazione2 = valore_configurazione2
 * 
 * [Contesto_2]
 * key_config_2_1 = val_cfg_1
 * key_config_2_2 = val_cfg_2
 * </pre>
 *
 * @author LeleFT
 */
public class IniFile {
    // Elenco ordinati di contesti
    private ArrayList<String> contexts;
    
    // Mappa che associa a ciascun contesto, l'elenco delle sue configurazioni
    private HashMap<String, Properties> configurations;
    
    /**
     * Costruisce un oggetto <code>IniFile</code> senza alcuna configurazione.
     */
    public IniFile() {
        contexts = new ArrayList<String>();
        configurations = new HashMap<String, Properties>();
    }
    
    /**
     * Costruisce un oggetto <code>IniFile</code> utilizzando l'oggetto
     * <code>File</code> passato come parametro.<br />
     * Si aspetta che il file indicato sia un file di testo conforme alla
     * struttura INI.
     * 
     * @param f il file da leggere per costruire l'oggetto <code>IniFile</code>
     * @throws IOException in caso di errore in lettura del file o file non
     * conforme alla struttura INI.
     */
    public IniFile(File f) throws IOException {
        this();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream( f );
            loadFromInputStream( fis );
        } finally {
            if (fis != null) {
                try { fis.close(); } catch (Exception e) { }
            }
        }
    }

    /**
     * Costruisce un oggetto <code>IniFile</code> utilizzando l'oggetto
     * <code>InputStream</code> passato come parametro.<br />
     * Si aspetta che lo stream di input rappresenti un file di testo conforme
     * alla struttura INI.
     * @param is l'oggetto <code>InputStream</code> da utilizzare come sorgente.
     * @throws IOException in caso di errore in lettura dallo stream di input o
     * stream di input che non rappresenta un file conforme alla struttura INI.
     */
    public IniFile(InputStream is) throws IOException {
        this();
        loadFromInputStream( is );
    }
    
    /**
     * Aggiunge un contesto al file INI.
     * 
     * @param context il nome del contesto da aggiungere
     */
    public void addContext(String context) {
        if ( !contexts.contains(context) ) {
            contexts.add( context );
            configurations.put(context, new Properties());
        }
    }

    /**
     * Aggiunge una configurazione al contesto indicato.<br />
     * Una configurazione &egrave; rappresentata da una coppia <code>chiave =
     * valore</code>.
     * 
     * @param context il contesto a cui aggiungere la configurazione.
     * @param confKey la chiave della coppia di configurazione.
     * @param confValue il valore della coppia di configurazione
     * @throws ContextNotExistsException se il contesto specificato non esiste
     * nel file INI.
     */
    public void addConfiguration(String context, String confKey, String confValue) throws ContextNotExistsException {
        if ( contexts.contains(context) ) {
            Properties prop = configurations.get( context );
            prop.put(confKey, confValue);
        } else {
            throw new ContextNotExistsException("Il contesto specificato non esiste: " + context);
        }
    }
    
    /**
     * Ritorna l'elenco ordinato dei contesti del file INI.
     * @return l'elenco ordinato dei contesti.
     */
    public List<String> getContexts() {
        return new ArrayList<String>( contexts );
    }
    
    /**
     * Ritorna la mappa delle configurazioni per il contesto specificato.<br />
     * La mappa rappresenta l'elenco delle coppie <code>chiave = valore</code>
     * delle configurazioni esistenti per il contesto specificato.
     * 
     * @param context il contesto di cui ottenere l'elenco delle configurazioni.
     * @return una mappa delle configurazioni per il contesto specificato.
     */
    public Map<String, String> getContextConfigurations(String context) {
        Map<String, String> ret = null;
        
        if ( contexts.contains(context) ) {
            Properties prop = configurations.get( context );
            ret = new HashMap<String, String>();
            for(Object key : prop.keySet()) {
                ret.put((String) key, prop.getProperty((String) key));
            }
        }
        
        return ret;
    }
    
    /**
     * Ritorna il valore della configurazione del contesto specificato.
     * 
     * @param context il contesto a cui appartiene la configurazione.
     * @param confKey la chiave della configurazione di cui ottenere il valore.
     * @return il valore della configurazione per il contesto specificato.
     */
    public String getConfigurationValue(String context, String confKey) {
        String ret = null;
        
        if ( contexts.contains(context) ) {
            Properties prop = configurations.get( context );
            ret = prop.getProperty( confKey );
        }
        
        return ret;
    }
    
    /**
     * Consente di salvare l'oggetto <code>IniFile</code> nel file specificato.<br />
     * Verr&agrave; creato un file di testo con la struttura INI dell'oggetto
     * attuale.<br />
     * Se il file esiste gi&agrave; verr&agrave; sovrascritto.
     * 
     * @param file l'oggetto <code>File</code> in cui salvare la struttura INI
     * @throws IOException in caso di errori in scrittura del file.
     */
    public void save(File file) throws IOException {
        save( new FileOutputStream(file) );
    }
    
    /* Legge, riga per riga, l'InpusStream e costruisce la struttura interna
     * dell'oggetto, verificandone la semantica dei contesti.
     */
    private void loadFromInputStream(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader( new InputStreamReader(is) );
        String line = null;
        Properties props = null;
        int lineNumber = 0;
        while((line = br.readLine()) != null) {
            lineNumber++;
            if ( !"".equals(line.trim())) {
                if ( line.trim().startsWith("[") ) {
                    if ( line.trim().endsWith("]") ) {
                        props = findConfiguration( line );
                    } else {
                        throw new FileFormatException("Contesto malformato alla linea " + lineNumber + ": " + line);
                    }
                } else {
                    if (props == null) {
                        props = new Properties();
                        contexts.add("");
                        configurations.put("", props);
                    }
                    String[] couple = line.split("=");
                    props.put(couple[0], couple[1]);
                }
            }
        }
    }
    
    /*
     * Cerca nella struttura interna l'oggetto Properties corrispondente al 
     * contesto specificato. Se il contesto non esiste all'internod ella struttura
     * viene creato ex-novo ed aggiunto.
     */
    private Properties findConfiguration(String context) {
        Properties ret = null;
        
        String contextName = context.substring(1, context.length()-1);
        if ( !contexts.contains(contextName) ) {
            contexts.add( contextName );
            ret = new Properties();
            configurations.put(contextName, ret);
        } else {
            ret = configurations.get( contextName );
        }
        
        return ret;
    }
    
    /*
     * Salva la struttura all'interno dell'OutputStream specificato.
     */
    private void save(OutputStream os) throws IOException {
        PrintStream ps = new PrintStream( os );
        String CR = System.getProperty("line.separator");
        
        StringBuilder sb = new StringBuilder();
        for(String context : contexts) {
            if ( !"".equals(context) ) {
                sb.append("[").append( context ).append("]").append( CR );
            }
            
            Properties prop = configurations.get( context );
            for(Map.Entry<Object, Object> entry : prop.entrySet()) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                
                sb.append( key ).append(" = ").append( value ).append( CR );
            }
            sb.append( CR ).append( CR );
            
            ps.print( sb.toString() );
            sb.setLength( 0 );
        }
        
        ps.flush();
    }
}
