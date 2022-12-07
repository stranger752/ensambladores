/**
 * @author Escalera Jimenez Enrique
 * @author Sánchez Mendieta Jesús Alberto
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Phase2 {

    // lista de 'String[]', donde cada 'String[]' contiene un elemento el cual es una línea del código fuente
    private ArrayList<String[]> source = new ArrayList<>();
    // lista de 'Elements[]', donde cada arreglo contiene los elementos de cada linea del código fuente
    private ArrayList<Element[]> elements = new ArrayList<>();
    // lista de 'String[]'. cada 'String[]' contine la linea del codigo fuente y si es correcta/incorrecta
    private ArrayList<String[]> semantic = new ArrayList<>();
    // lista de 'String[]'. cada 'String[]' contine un símbolo y sus características (tipo, valor y tamaño)
    private ArrayList<String[]> symbols = new ArrayList<>();
    // lista de 'String[]'. cada 'String[]' contine un símbolo y sus características (tipo, valor, tamaño, direccion)
    private ArrayList<String[]> symbols2 = new ArrayList<>();
    
    private ArrayList<String[]> contador = new ArrayList<>();


    private String msg, msgAux;
    private int counter=0;
    private int cp=0;

    public Phase2(ArrayList<String[]> source){
        this.source = source;

        // se llena al arreglo 'elements'
        for ( String[] s : source ) {
            ArrayList<String> elementsInLine = splitLine(s[0]);
            Element[] elements = new Element[elementsInLine.size()];
            for ( int i=0; i<elementsInLine.size(); i++ )
                elements[i] = new Element((String) elementsInLine.get(i));
            this.elements.add( elements );
        }

        // se llena a la lista del análisis lexicográfico que contine los elementos y su tipo (identifiación)
        for ( int j=0; j<elements.size(); j++ ) {
            this.semantic.add( new String[] { getLine(source.get(j)[0]), getAnalysis(elements.get(j)) ,cp+""} );
            cp = 0;
            }
    }

    private String getAnalysis(Element[] line){

        if ( line.length == 1 && line[0].getText().equalsIgnoreCase("") )
            return "";

        else {
            if ( counter == 0 ) {
                if ( !isSegmentDef(line, ".stack segment") )
                    return "Definir stack segment. " + msg;
                else {
                    counter++;
                    return "Correcta.";
                }
            }
            else if ( counter == 1 ) {
                if ( !isStackSeg(line) ) return msg;
                else {
                    counter++;
                    cp++;
                    return "Correcta.";
                }
            }
            else if ( counter == 2 ) {
                if ( !isSegmentEnd(line) ) return msg;
                else {
                    counter++;
                    return "Correcta.";
                }
            }
            else if ( counter == 3 ) {
                if ( !isSegmentDef(line, ".data segment") )
                    return "Definir stack segment. " + msg;
                else {
                    counter++;
                    return "Correcta.";
                }
            }
            else if ( counter == 4 ) {
                if ( !isDataSeg(line) ) {
                    msgAux = msg;
                    if ( !isSegmentEnd(line) )
                        return msgAux;
                    else {
                        counter++;
                        return "Correcta.";
                    }
                }
                else {
                    return "Correcta.";
                }
            }
            else if ( counter == 5 ) {
                if ( !isSegmentDef(line, ".code segment") )
                    return "Definir code segment. " + msg;
                else {
                    counter++;
                    return "Correcta.";
                }
            }
            else if ( counter == 6 ) {
                if ( !isCodeSeg(line) ) {
                    msgAux = msg;
                    if (!isSegmentEnd(line))
                        return msgAux;
                    else {
                        counter++;
                        return "Correcta.";
                    }
                }
                else {
                    return "Correcta.";
                }
            }

        }
        return "";
    }


    /**
     * Verifica si la línea es la correcta declaración del segmento.
     * @param line    Element[]: arreglo de elementos de la línea a verificar
     * @param segment String: segmento
     * @return true si la linea es la definición correcta del segmento especificado como parametro
     */
    private Boolean isSegmentDef(Element[] line, String segment){
        if ( !hasValidElements(line) ) return false;
        if ( !hasCorrectLength(line, 1) ) return false;
        if ( !hasCorrectElement(line[0], 0, segment) ) return false;

        else return true;
    }

    /**
     * Verifica si la línea es la correcta declaración del fin de un segmento.
     * @param line    Element[]: arreglo de elementos de la línea a verificar
     * @return true si la linea es la definición correcta del fin de un segmento
     */
    private Boolean isSegmentEnd(Element[] line){
        if ( !hasValidElements(line) ) return false;
        if ( !hasCorrectLength(line, 1) ) return false;
        if ( !hasCorrectElement(line[0], 0, "ends") ) return false;

        else return true;
    }

    /**
     * Verifica si la línea es la correcta declaración del tamaño del segmento de pila.
     * @param line    Element[]: arreglo de elementos de la línea a verificar
     * @return true si la linea es la definición correcta del tamaño del segmento de pila
     */
    private Boolean isStackSeg(Element[] line){
        if ( !hasValidElements(line) ) return false;
        if ( !hasCorrectLength(line, 3) ) return false;

        if ( !hasCorrectElement(line[0], 0, "dw") ) return false;

        if ( !hasCorrectType(line[1], 1, "Constante numérica") ) return false;
        if ( !hasCorrectConstant(line[1], 1, "word", true) ) return false;

        if ( !hasCorrectSubtype(line[2], 2, "DUP con argumento constante numerica") ) return false;

        return true;
    }

    /**
     * Verifica si la línea es la correcta declaración de un dato en el data segment y lo agrega a los símbolos
     * @param line    Element[]: arreglo de elementos de la línea a verificar
     * @return true si la linea es la definición correcta del dato
     */
    private Boolean isDataSeg(Element[] line){
        if ( !hasValidElements(line) ) return false;

        if ( !(hasCorrectLength(line, 3) || hasCorrectLength(line, 4)) ) return false;

        else if ( line.length==3 ) {
            if ( !hasCorrectType(line[0], 0, "Símbolo") ) return false;
            if ( !(hasCorrectElement(line[1], 1, "db") || hasCorrectElement(line[1], 1, "dw")
                    || hasCorrectElement(line[1], 1, "equ") ) ) {
                msg = "Incorrecta. El 2º elemento debe ser \"db\", \"dw\" o \"equ\"";
                return false;
            }
            if ( line[1].getText().equalsIgnoreCase("db") ) {
                if ( !(hasCorrectType(line[2], 2, "Constante caracter")
                        || hasCorrectConstant(line[2], 2, "byte", false)) ) {
                    msg = "Incorrecta. El 3ª elemento debe ser constante caracter o numérica byte.";
                    return false;
                }
                else {
                    symbols.add(new String[]{line[0].getText(), "Variable", line[2].getText(), line[2].getSize()});
                    symbols2.add(new String[]{line[0].getText(), "Variable", line[2].getText(), line[2].getSize(), Integer.toHexString(calculateCP(line))});
                    calculateCP(line);
                    return true;
                }
            }
            else {
                if ( !hasCorrectConstant(line[2], 2, "word", false) ) {
                    msg = "Incorrecta. El 3º elemento debe ser constante numérica palabra.";
                    return false;
                }
                else {
                    if ( line[1].getText().equalsIgnoreCase("dw") )
                        symbols.add(new String[]{line[0].getText(), "Variable", line[2].getText(), line[2].getSize()});
                    else
                        symbols.add(new String[]{line[0].getText(), "Constante", line[2].getText(), line[2].getSize()});
                    calculateCP(line);
                    return true;
                }
            }
        }

        else {
            if ( !hasCorrectType(line[0], 0, "Símbolo") ) return false;
            if ( !(hasCorrectElement(line[1],1,"db") || hasCorrectElement(line[1],1,"dw")) ) {
                msg = "Incorrecta. El 2º elemento debe ser \"db\" o \"dw\"";
                return false;
            }
            if ( !hasCorrectConstant(line[2], 2, "word", true) ) {
                msg = "Incorrecta. El 3ª elemento debe ser constante numérica word sin signo.";
                return false;
            }
            if ( !startsWithIgnoreCase(line[3].getText(), "dup") ) {
                System.out.println(line[3].getText());
                msg = "Incorrecta. El 4º elemento no es DUP()";
                return false;
            }
            else if ( !hasCorrectType(line[3],3,"Pseudoinstruccion") ) {
                msg = "Incorrecta. DUP( ) es inválido/mal definido.";
                return false;
            }
            else {
                if ( line[1].getText().equalsIgnoreCase("db") ) {
                    if ( !((hasCorrectSubtype(line[3], 3, "DUP con argumento constante numerica")
                            && line[3].getSize().equalsIgnoreCase("byte"))
                            || hasCorrectSubtype(line[3], 3, "DUP con argumento constante caracter")) ){
                        msg = "Incorrecta. El 4º elemento debe ser DUP(Constante caracter byte) o DUP(Constante numerica byte)";
                        return false;
                    }
                    else {
                        symbols.add(new String[]{line[0].getText(), "Variable(array)", line[3].getArgument().getText(), "byte"});
                        calculateCP(line);
                        return true;
                    }
                }
                else {
                    if ( hasCorrectSubtype(line[3], 3, "DUP con argumento constante numerica")
                            && (line[3].getSize().equalsIgnoreCase("word")
                            || line[3].getArgument().getSubtype().equalsIgnoreCase("DEC")) ) {
                        symbols.add(new String[]{line[0].getText(), "Variable(array)", line[3].getArgument().getText(), "word"});
                        calculateCP(line);
                        return true;
                    }
                    else {
                        msg = "Incorrecta. El 4º elemento debe ser DUP(Constante numerica word)";
                        return false;
                    }
                }
            }

        }

    }

    private int calculateCP(Element[] array){
        if ( array[2].getType().equalsIgnoreCase("Constante caracter") ){
            cp=array[2].getText().length();
        }
        else if ( array[2].getType().startsWith("Constante numérica") ){
            if (array[1].getSize().equalsIgnoreCase("word"))
                cp =16;
            else
                cp =8;
        }
        else if ( array[4].getSubtype().startsWith("DUP") ){
            if (array[1].getSize().equalsIgnoreCase("word")) {
                cp = 16*Integer.parseInt(array[2].getText());
            }
            else cp =  8*Integer.parseInt(array[2].getText());
        }
        else if (array[0].getSubtype().equalsIgnoreCase("LABEL")) {
        }
        else {
            cp = 16;
        }
        return cp;
    }




    private Boolean isCodeSeg(Element[] line){

        if ( !hasValidElements(line) ) return false;

        if ( !(hasCorrectLength(line,1) || hasCorrectLength(line,2) || hasCorrectLength(line,3)) )
            return false;

        else if (line.length==1) {
            switch (line[0].getType()) {
                case "Instrucción" -> {
                    if ( !line[0].getSubtype().equalsIgnoreCase("SIN OPS") ){
                        msg = getErrorMsg1(line[0]);
                        return false;
                    }
                    return true;
                }
                case "Símbolo" -> {
                    if ( !line[0].getSubtype().equalsIgnoreCase("LABEL") ){
                        msg = "Incorrecta. Instrucción inválida.";
                        return false;
                    }
                    else {
                        symbols.add(new String[]{line[0].getText().substring(0,line[0].getText().length()-1), "Etiqueta", "-", "-"});
                        return true;
                    }
                }
                default -> msg = "Incorrecta. Instrucción inválida.";
            }
        }

        else if ( line.length==2 ) {
            if ( !hasCorrectType(line[0], 0, "Instrucción")){
                msg = "Incorrecta. Instrucción inválida.";
                return false;
            }
            else {
                switch(line[0].getSubtype()) {
                    case "UN OP":
                        return switch (line[0].getText()) {
                            case "POP" -> ( hasCorrectOperand(line[1], 1, "REG")
                                    || ( hasCorrectOperand(line[1], 1, "SREG")
                                    && !line[1].getText().equalsIgnoreCase("CS"))
                                    || ( hasCorrectOperand(line[1], 1, "memory")
                                    || isInSymbolsTable(line[1], "" ) ) );
                            case "IDIV", "NEG" -> ( hasCorrectOperand(line[1], 1, "REG")
                                    || ( hasCorrectOperand(line[1], 1, "memory")
                                    || isInSymbolsTable(line[1], "Variable")
                                    || isInSymbolsTable(line[1], "Constante") ) );
                            case "PUSH" -> ( hasCorrectOperand(line[1], 1, "REG")
                                    || ( hasCorrectOperand(line[1], 1, "SREG")
                                    && !line[1].getText().equalsIgnoreCase("CS"))
                                    || ( hasCorrectOperand(line[1], 1, "memory")
                                    || isInSymbolsTable(line[1], "Variable")
                                    || isInSymbolsTable(line[1], "Constante"))
                                    || hasCorrectOperand(line[1], 1, "immediate") );
                            default -> false;
                        };

                    case "UN OP/ETQ":
                        return switch (line[0].getText()) {
                            case "JNGE","JNP","JP","LOOPE","JA","JC" -> ( isInSymbolsTable(line[1],"Etiqueta") );
                            default -> false;
                        };

                    default:
                        return false;
                }
            }
        }

        else if ( line.length==3 ) {
            if ( !hasCorrectType(line[0], 0, "Instrucción")){
                msg = "Incorrecta. Instrucción inválida.";
                return false;
            }
            else {
                if ("DOS OPS".equals(line[0].getSubtype())) {
                    return switch (line[0].getText()) {
                        case "RCL", "SHL" -> ( ( (hasCorrectOperand(line[1], 1, "memory")
                                || isInSymbolsTable(line[1], "Variable")
                                || isInSymbolsTable(line[1], "Constante"))
                                && ( hasCorrectOperand(line[2], 2, "immediate")
                                || line[2].getText().equalsIgnoreCase("CL") ) )
                                || ( hasCorrectOperand(line[1], 1, "REG")
                                && ( hasCorrectOperand(line[2], 2, "immediate")
                                || line[2].getText().equalsIgnoreCase("CL") ) ) );
                        case "XCHG" -> ( ( hasCorrectOperand(line[1], 1, "REG")
                                && (hasCorrectOperand(line[2], 2, "memory")
                                || isInSymbolsTable(line[2], "Variable")
                                || isInSymbolsTable(line[2], "Constante")) )
                                || ( hasCorrectOperand(line[1], 1, "REG")
                                && hasCorrectOperand(line[1], 1, "REG") )
                                || ( hasCorrectOperand(line[2], 2, "REG")
                                && (hasCorrectOperand(line[1], 1, "memory")
                                || isInSymbolsTable(line[1], "Variable")
                                || isInSymbolsTable(line[1], "Constante")) ) );
                        case "ADD" -> ( ( hasCorrectOperand(line[1], 1, "REG")
                                && (hasCorrectOperand(line[2], 2, "memory")
                                || isInSymbolsTable(line[2], "Variable")
                                || isInSymbolsTable(line[2], "Constante")) )
                                || ( hasCorrectOperand(line[1], 1, "REG")
                                && hasCorrectOperand(line[1], 1, "REG") )
                                || ( hasCorrectOperand(line[2], 2, "REG")
                                && (hasCorrectOperand(line[1], 1, "memory")
                                || isInSymbolsTable(line[1], "Variable")
                                || isInSymbolsTable(line[1], "Constante")) )
                                || ( (hasCorrectOperand(line[1], 1, "memory")
                                || isInSymbolsTable(line[1], "Variable")
                                || isInSymbolsTable(line[1], "Constante"))
                                && hasCorrectOperand(line[2], 2, "immediate") )
                                || ( hasCorrectOperand(line[1], 1, "REG")
                                && hasCorrectOperand(line[2], 2, "immediate") ) );
                        default -> false;
                    };
                }
                return false;
            }
        }

        return false;
    }

    private Boolean isInSymbolsTable(Element element, String type){
        if (element.getType().equalsIgnoreCase("Símbolo")){
            for ( String[] array : symbols ){
                if ( array[0].equalsIgnoreCase(element.getText()) )
                    if ( array[1].equalsIgnoreCase(type) ) {
                        return true;
                    }
                    else {
                        msg = "Incorrecta. Tipo de símbolo incorrecto.";
                        return false;
                    }
            }
            msg = "Incorrecta. \"" + element.getText() + "\" no está definida.";
            return false;
        }
        return false;
    }

    /**
     * @param line Arreglo de elementos
     * @return true si no hay algún elemento inválido
     */
    private Boolean hasValidElements(Element[] line){
        for ( Element e : line )
            if ( !e.isValid() ) {
                msg = "Incorrecta. " + e.getText().toUpperCase() + " es un elemento inválido.";
                return false;
            }
        return true;
    }

    /**
     * Verifica si el número de elementos en una línea es correcto y prepara mensaje.
     * @param line   Arreglo de elementos de una línea
     * @param length Número de elementos correcto
     * @return true si el arreglo tiene el número de elementos dado.
     */
    private Boolean hasCorrectLength(Element[] line, int length) {
        if ( line.length != length ) {
            msg = "Incorrecta. Número de elementos incorrecto.";
            return false;
        }
        return true;
    }

    /**
     * Verifica si un elemento tiene el tipo correcto y prepara mensaje.
     * @param element Elemento a verificar
     * @param index   Número de elemento en la línea
     * @param type    Tipo correcto
     * @return true si el elemento es del tipo dado como parámetro
     */
    private Boolean hasCorrectType(Element element, int index, String type) {
        if ( !element.getType().startsWith(type) ) {
            msg = "Incorrecta. El "+ (index+1) +"º elemento debe ser: " + type;
            return false;
        }
        return true;
    }

    /**
     * Verifica si un elemento tiene el subtipo correcto y prepara mensaje.
     * @param element Elemento a verificar
     * @param index   Número de elemento en la línea
     * @param subtype Subtipo correcto
     * @return true si el elemento es del subtipo dado como parámetro
     */
    private Boolean hasCorrectSubtype(Element element, int index, String subtype) {
        if ( !element.getSubtype().startsWith(subtype) ) {
            msg = "Incorrecta. El "+ (index+1) +"º elemento debe ser: " + subtype;
            return false;
        }
        return true;
    }

    private String getErrorMsg1(Element element) {
        if ( element.getSubtype().startsWith("SIN OPS") )
            return "Incorrecta. "+ element.getText() +" no debe tener operandos.";
        else if ( element.getSubtype().startsWith("UN OP") )
            return "Incorrecta. "+ element.getText() +" debe tener un operando.";
        else if ( element.getSubtype().startsWith("DOS OPS") )
            return "Incorrecta. "+ element.getText() +" debe tener dos operandos.";
        else
            return "Incorrecta. "+ element.getText() +" debe tener operando (etiqueta).";
    }

    private Boolean hasCorrectOperand(Element element, int index, String operandType){
        if (element.getText().equalsIgnoreCase("CS") && index==1){
            msg = "Incorrecta. CS solamente puede ser el segundo operando.";
            return false;
        }
        if ( !element.getOperandType().startsWith(operandType) ) {
            msg = "Incorrecta. Tipo(s) de operando(s) incorrectos/incompatibles.";
            return false;
        }
        return true;
    }

    /**
     * Verifica si un elemento es un elemento en específico y prepara mensaje.
     * @param element Elemento a verificar
     * @param index   Número de elemento en la línea
     * @param text    Elemento en especifico
     * @return true si el elemento es del tipo dado como parámetro
     */
    private Boolean hasCorrectElement(Element element, int index, String text) {
        if ( !element.getText().equalsIgnoreCase(text) ) {
            msg = "Incorrecta. El "+ (index+1) +"º elemento debe ser: " + text;
            return false;
        }
        return true;
    }

    /**
     * @param line        Arreglo de elementos
     * @param firstIndex  Indice del primer elemento
     * @param secondIndex Indice del segundo elemento
     * @return true si los elementos de line en los indices dados tienen el mismo tamaño (word/byte)
     */
    private Boolean hasCorrectSize(Element[] line, int firstIndex, int secondIndex){
        if (line[firstIndex].getSize().equalsIgnoreCase(line[secondIndex].getSize()) )
            return true;
        else if ( line[firstIndex].getType().equalsIgnoreCase("DEC")
                && line[secondIndex].getSize().equalsIgnoreCase("word") )
            return true;
        else if ( line[secondIndex].getType().equalsIgnoreCase("DEC")
                && line[firstIndex].getSize().equalsIgnoreCase("word") )
            return true;
        else {
            msg = "Incorrecta. " + line[firstIndex].getText() + "& " + line[firstIndex].getText()
                    + "no son del mismo tamaño.";
            return false;
        }
    }

    /**
     * Verifica si una constante numerica tiene el tamaño dado y si es con/sin signo, y prepara mensaje.
     * @param element     Elemento (constante numérica) a verificar
     * @param index       Número de elemento en la línea
     * @param size        Tamaño (word/byte)
     * @param withoutSign Booleano que indica no debe tener signo
     * @return true si la constante numerica tiene el tamaño y signo correctos.
     */
    private Boolean hasCorrectConstant(Element element, int index, String size, Boolean withoutSign) {
        if ( withoutSign && element.isNegative() ) {
            msg = "Incorrecta. El "+ (index+1) +"º elemento no debe tener signo";
            return false;
        }

        if ( element.getSize().equalsIgnoreCase(size) ) return true;
        else if ( element.getSubtype().equalsIgnoreCase("DEC")
                && size.equalsIgnoreCase("word") ) return true;
        else {
            msg = "Incorrecta. El "+ (index+1) +"º elemento debe tener tamaño " + size;
            return false;
        }
    }


    /**
     * @param str    String
     * @param prefix prefijo
     * @return true si la cadena empieza con el prefijo, ignorando mayúsculas/minúsculas
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return str.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    /**
     * Remueve espacios (" ") al final de la línea de código dada como parámetro.
     * @param line String: linea de código
     * @return String: línea de código sin espacios (" ") al final de la cadena.
     */
    private String getLine(String line){
        if ( !line.endsWith(" ") )
            return line;
        else return getLine(line.substring(0,line.length()-1));
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
     * @return 'ArrayList' de 'String[]': lista de arreglos que contiene el análisis sintáctico/semántico
     */
    public ArrayList<String[]> getSemantic() { return semantic; }

    /**
     * @return 'ArrayList' de 'String[]': lista de arreglos que contiene los símbolos
     */
    public ArrayList<String[]> getSymbols() { return symbols; }

    public ArrayList<String[]> getSymbols2() { return symbols2; }
    


    public ArrayList<String[]> getContador() { return contador; }

}
