/**
 * Representa al analizador del código fuente.
 * Cuenta con 'ArrayList', donde se almacenan
 *      las líneas del codigo fuente
 *      las líneas del codigo fuente sin comentarios
 *      el análisis lexicográfico de cada elemento del código fuente
 *      el análisis sintáctico y semántico de cada línea del código fuente
 * @author Escalera Jimenez Enrique
 * @author Sánchez Mendieta Jesús Alberto
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;


public class Analyzer {

    // lista de 'String[]', donde cada 'String[]' contiene un elemento el cual es una línea del código fuente
    private ArrayList<String[]> source;
    // lista de 'String[]'. cada 'String[]' contiene una línea del código fuente con los comentarios removidos
    private ArrayList<String[]> sourceWithoutComments;
    // lista de 'String[]'. cada 'String[]' contine un elemento y su tipo (identifiación)
    private ArrayList<String[]> lexical;
    // lista de 'String[]'. cada 'String[]' contine la linea del codigo fuente y si es correcta/incorrecta
    private ArrayList<String[]> semantic = new ArrayList<>();
    // lista de 'String[]'. cada 'String[]' contine un símbolo y sus características (tipo, valor y tamaño)
    private ArrayList<String[]> symbols = new ArrayList<>();
    // lista de 'String[]'. cada 'String[]' contine la codificación de las instrucciones
    private ArrayList<String[]> encoding = new ArrayList<>();
    // lista de 'String[]'. cada 'String[]' contine símbolo y sus características (tipo, valor, tamaño y direccion)
    private ArrayList<String[]> symbols2 = new ArrayList<>();
    private ArrayList<String[]> symbolsnew = new ArrayList<>();

    /**
     * Constructor: genera listas donde se almacena el código fuente, análisis lexicográfico, análisis
     * sintáctico y semántico y codificación.
     * @param path String: ruta del archivo asm
     * @throws Exception Error si la ruta no es válida.
     */
    public Analyzer(String path) throws Exception {
        this.source = loadFile(path);
        this.sourceWithoutComments = removeComments(this.source);

        Phase1 phase1 = new Phase1( this.sourceWithoutComments );
        this.lexical = phase1.getLexical();

        Phase2 phase2 = new Phase2( this.sourceWithoutComments );
        this.semantic = phase2.getSemantic();
        this.symbols = phase2.getSymbols();
        this.symbolsnew = phase2.getSymbols2();

        Phase3 phase3 = new Phase3( this.semantic, this.symbols, this.symbolsnew );
        this.encoding = phase3.getEncoding();
        this.symbols2 = phase3.getSymbols2();
    }

    /**
     * Lee las líneas del archivo de código ubicado en el path dado y las almacena en una lista.
     * Precondición: la ruta (path) del archivo debe ser correcta y en esta debe encontrarse un archivo asm.
     * @param path String: cadena con la ruta al archivo con extensión '.asm'.
     * @return 'ArrayList' de 'Strings', donde cada 'String' de la lista es una de las líneas del codigo fuente
     */
    public ArrayList<String[]> loadFile(String path) throws Exception {
        // se abre al archivo y se almacena en buffer
        File doc = new File(path);
        BufferedReader obj = new BufferedReader(new FileReader(doc));

        // se guarda a cada línea en una lista
        String l;
        ArrayList<String[]> lines = new ArrayList<>();
        while ( (l=obj.readLine()) != null )
            lines.add( new String[]{l} );

        return lines;
    }

    /**
     * Remueve los comentarios de cada línea del codigo fuente.
     * @param source 'ArrayList' de 'Strings', donde cada elemento de la lista es una de las líneas del codigo
     * @return 'ArrayList' de 'Strings', donde cada elemento de la lista es una de las líneas del codigo fuente
     * sin comentarios.
     */
    private ArrayList<String[]> removeComments(ArrayList<String[]> source){
        // se define arreglo donde se almacenarán las líneas (del código) sin comentarios
        ArrayList<String[]> linesWithoutComments = new ArrayList<>();

        for ( String[] s : source ) {
            // se agrega a 'linesWithoutComments' una línea vacía si la línea original empieza con ';' o vacía
            if ( s[0].startsWith(";") ) linesWithoutComments.add(new String[]{""});
            else if ( s[0].isBlank() ) linesWithoutComments.add(new String[]{""});

            // se agrega la linea a 'linesWithoutComments' después de remover los comentarios
            else {
                int numOfSimpleQuotes = 0, numOfDoubleQuotes = 0;
                for ( int i=0; i<s[0].length(); i++ ) {
                    if ( s[0].charAt(i) == '\'' )
                        numOfSimpleQuotes++;
                    else if ( s[0].charAt(i) == '\"' )
                        numOfDoubleQuotes++;
                    else if ( s[0].charAt(i)==';' && numOfSimpleQuotes%2==0 && numOfDoubleQuotes%2==0 ) {
                        linesWithoutComments.add(new String[]{ s[0].split(";")[0] } );
                        break;
                    }
                    else if ( i==s[0].length()-1 ) {
                        linesWithoutComments.add( s );
                        break;
                    }
                }
            }
        }

        // se remueve el exceso de líneas vacías de 'linesWithoutComments'
        ArrayList<String[]> sourceFinal = new ArrayList<>();
        for ( int i=0; i<linesWithoutComments.size(); i++ ) {
            if (i == 0) sourceFinal.add(linesWithoutComments.get(i));
            else if (!(linesWithoutComments.get(i - 1)[0].equalsIgnoreCase("")
                    && linesWithoutComments.get(i)[0].equalsIgnoreCase("")))
                sourceFinal.add(linesWithoutComments.get(i));
        }

        return sourceFinal;
    }

    /**
     * Crea una copia de elementos desde una lista a un arreglo, dentro de un rango específico.
     * @param list         ArrayList: lista de donde se van a copiar los elementos
     * @param startIndex Int: Índice inicial del rango a copiar
     * @param endIndex   Int: Índice final del rango a copiar. Excluyente (este índice no se incluye).
     * @return String[][]: Arreglo con copia de los elementos.
     */
    public String[][] getLines(ArrayList<String[]> list, int startIndex, int endIndex){
        // se crea arreglo en el que se almacenarán las líneas
        String[][] array = new String[endIndex-startIndex][list.get(0).length];

        // se crea arreglo auxiliar, donde cada componente es una cadena vacía
        String[] empty = new String[list.get(0).length];
        for ( int i = 0; i < list.get(0).length; i++ )
            empty[i] = "";

        // se almacenan solo las lineas a recuperar
        if ( list.size() > endIndex )
            for ( int i = startIndex; i < endIndex; i++ )
                array[i-startIndex] = list.get(i);
        else
            for ( int i = startIndex; i < endIndex; i++ ) {
                if ( i < list.size() ) array[i-startIndex] = list.get(i);
                else array[i-startIndex] = empty;
            }

        return array;
    }

    /**
     * @return 'ArrayList' de 'String[]': lista de arreglos que contiene el código fuente original
     */
    public ArrayList<String[]> getSource() { return source; }

    /**
     * @return 'ArrayList' de 'String[]': lista de arreglos que contiene el análisis lexicográfico
     */
    public ArrayList<String[]> getLexical() { return lexical; }

    /**
     * @return 'ArrayList' de 'String[]': lista de arreglos que contiene el análisis sintáctico/semántic
     */
    public ArrayList<String[]> getSemantic() { return semantic; }

    /**
     * @return 'ArrayList' de 'String[]': lista de arreglos que contiene los símbolos
     */
    public ArrayList<String[]> getSymbols() { return symbols; }

    public ArrayList<String[]> getEncoding() { return encoding; }

    public ArrayList<String[]> getSymbols2() { return symbols2; }

}
