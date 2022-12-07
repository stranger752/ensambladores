/**
 * Representa al analizador lexicográfico. Incluye la funcionalidad para realizar el análisis lexicográfico.
 * @version 2.0
 * @author Escalera Jimenez Enrique
 * @author Sánchez Mendieta Jesús Alberto
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Phase1 {

    // lista de elementos del código fuente
    private final ArrayList<Element> elements = new ArrayList<>();
    // lista de 'String[]'. cada 'String[]' contine un elemento y su tipo (identifiación)
    private final ArrayList<String[]> lexical = new ArrayList<>();

    public Phase1(ArrayList<String[]> source){

        // se llena al arreglo 'elements'
        ArrayList<String> aux = new ArrayList<>();
        for ( String[] s : source ) {
            aux.addAll(splitLine(s[0]));
            if ( !s[0].equalsIgnoreCase("") ) aux.add("");
        }
        for ( String s : aux )
            this.elements.add(new Element(s));

        // se llena a la lista del análisis lexicográfico que contine los elementos y su tipo (identifiación)
        for (Element e: elements)
            this.lexical.add( new String[] { e.getText(), e.getType() } );

    }

    /**
     * Separa los elementos de una línea de código.
     * @param line 'String' que es la línea de código a segmentar.
     * @return 'ArrayList' de 'Strings', donde cada 'String' es un elemento de la línea de código.
     */
    private ArrayList<String> splitLine(String line){

        if ( line.equalsIgnoreCase("") ) return new ArrayList<>(List.of(""));

        else {
            // se secciona la línea usando comas, tabuladores y espacios (en ese orden) como separadores
            ArrayList<String> splitted = new ArrayList<>();
            splitted.add(line);
            splitted = splitAt(splitAt(splitAt(splitted,","),"\t")," ");

            // se crea 'ArrayList' donde se almacenan solo los elementos que son diferentes a ""
            ArrayList<String> segments = new ArrayList<>();
            for (String s : splitted)
                if ( !(s.equals("") || s.equals("\t")) ) segments.add(s);

            // se juntan los elementos compuestos. se retorna el 'ArrayList' resultante
            return mergeCompounds(segments);
        }

    }

    /**
     * Método auxiliar al método splitLine(). Segmenta cada una de las 'Strings' de un 'ArrayList' de 'Strings'.
     * Usa como separador a la 'String' dada como parámetro.
     * @param stringsToSplit 'ArrayList' de 'Strings' cuyos elementos van a segmentarse.
     * @param character 'String' que se va a usar como separador.
     * @return 'ArrayList' de 'Strings', donde cada 'String' es uno de los segmentos.
     */
    private ArrayList<String> splitAt(ArrayList<String> stringsToSplit, String character ){
        ArrayList<String> splitted = new ArrayList<>();
        ArrayList<String> aux;
        for ( String s : stringsToSplit ) {
            aux = new ArrayList<>( Arrays.asList(s.split(character)) );
            splitted.addAll(aux);
        }
        return splitted;
    }

    /**
     * Método auxiliar al método splitLine(). Junta los elementos compuestos dentro de un 'ArrayList' de 'Strings'.
     * @param segments 'ArrayList' de 'Strings', donde cada 'String' es un elemento simple de una línea de código.
     * @return 'ArrayList' de 'Strings', donde cada 'String' es un elementos simples o compuesto.
     */
    private ArrayList<String> mergeCompounds(ArrayList<String> segments){
        ArrayList<String> elements = new ArrayList<>();
        String word;

        for ( int i=0; i < segments.size(); i++ ) {
            if ( i != segments.size()-1 ) {
                word = segments.get(i);

                // se unen segmentos y se agrega el elemento compuesto al arreglo 'elements'
                if ( ( word.equalsIgnoreCase(".stack") || word.equalsIgnoreCase("stack")
                        || word.equalsIgnoreCase(".code") || word.equalsIgnoreCase("code")
                        || word.equalsIgnoreCase(".data") || word.equalsIgnoreCase("data") )
                        && segments.get(i+1).equalsIgnoreCase("segment") ) {
                    elements.add( (segments.get(i)) + (" ") + (segments.get(i+1)) );
                    i++;
                }

                // se unen cadenas "byte ptr" y "word ptr", y se agrega el elemento compuesto al arreglo 'elements'
                else if ( ( word.equalsIgnoreCase("byte") || word.equalsIgnoreCase("word") )
                        && segments.get(i+1).equalsIgnoreCase("ptr") ) {
                    elements.add( (segments.get(i)) + (" ") + (segments.get(i+1)) );
                    i++;
                }

                // se unen constantes delimitadas por comillas simples y se agrega el elemento compuesto al arreglo
                else if ( segments.get(i).startsWith("'") ) {
                    for ( int j=i; j<segments.size(); j++ ) {
                        if ( (segments.get(j).endsWith("'")||j==segments.size()-1) && (word.length()!=1||j!=i) ) {
                            StringBuilder constant = new StringBuilder();
                            for ( int k=i; k<=j; k++ ) {
                                if ( k<j ) constant.append( segments.get(k) ).append(" ");
                                else constant.append( segments.get(k) );
                            }
                            elements.add( constant.toString() );
                            i=j;
                            break;
                        }
                    }
                }

                // se unen constantes delimitadas por comillas dobles y se agrega el elemento compuesto al arreglo
                else if ( word.startsWith("\"") ) {
                    for ( int j=i; j<segments.size(); j++ ) {
                        if ( (segments.get(j).endsWith("\"")||j==segments.size()-1) && (word.length()!=1||j!=i) ) {
                            StringBuilder constant = new StringBuilder();
                            for ( int k=i; k<=j; k++ ){
                                if ( k<j ) constant.append( segments.get(k) ).append(" ");
                                else constant.append( segments.get(k) );
                            }
                            elements.add( constant.toString() );
                            i=j;
                            break;
                        }
                    }
                }

                // se unen cadenas "dup(xxx)" y se agrega el elemento compuesto al arreglo 'elements'
                else if ( word.startsWith("dup(") || word.startsWith("DUP(") ) {
                    for ( int j=i; j<segments.size(); j++ ) {
                        if ( segments.get(j).endsWith(")") ) {
                            StringBuilder constant = new StringBuilder();
                            for ( int k=i; k<=j; k++ )
                                constant.append( segments.get(k) );
                            elements.add( constant.toString() );
                            i=j;
                            break;
                        }
                    }
                }

                // se unen cadenas "[xxx]"
                else if ( word.startsWith("[") ) {
                    for ( int j=i; j<segments.size(); j++ ) {
                        if ( segments.get(j).endsWith("]") ) {
                            StringBuilder constant = new StringBuilder();
                            for ( int k=i; k<=j; k++ )
                                constant.append( segments.get(k) );
                            elements.add( constant.toString() );
                            i=j;
                            break;
                        }
                    }
                }
                // si no hay elementos que unir se agrega al elemento actual al arreglo
                else elements.add( word );
            }
            // si es el ultimo elemento, se agrega al elemento actual al arreglo
            else elements.add( segments.get(i) );
        }

        return elements;
    }

    /**
     * @return 'ArrayList' de 'String[]': lista de arreglos que contiene el análisis lexicográfico
     */
    public ArrayList<String[]> getLexical() { return lexical; }

}

