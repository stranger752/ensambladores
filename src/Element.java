/**
 * @author Escalera Jimenez Enrique
 * @author Sánchez Mendieta Jesús Alberto
 */

import java.util.ArrayList;

public class Element {

    private String text;
    private String type;
    private String subtype;
    private String size;
    private Element argument;
    private boolean isValid;
    private boolean isNegative;
    private String operandType;

    /**
     * Constructor.
     * @param text Cadena que será evaluada e identificada.
     */
    public Element(String text) {
        this.text = text;

        // se verifica que tipo de elemento es
        if ( isBlank() || isRegister() || isInstruction() || isUnassignedInstruction() || isCharacterConstant()
                || isNumericConstant() || isPseudoinstruction() || isDUP() || isSymbol() )
            this.isValid = true;
        else {
            this.type = "Elemento invalido";
            this.isValid = false;
        }

    }

    /**
     * Verifica si la cadena está vacía o constituida únicamente por espacios.
     * @return TRUE si la cadena está vacía o constituida por espacios, y FALSE en caso contrario.
     */
    private boolean isBlank(){
        this.type = "";
        return this.text.isBlank();
    }

    /**
     * Verifica si la cadena corresponde a un registro y, si lo es, le asigna el tipo "Registro",
     * el subtipo ("REG", "SREG" o "SREG2") y el tamaño ("word" o "byte"), tipo de operando
     * @return TRUE si la cadena corresponde a un registro del 8086, y FALSE en caso contrario.
     */
    private boolean isRegister(){
        String[][] registers = { { "AX","AH","AL","BX","BH","BL","CX","CH","CL","DX","DH","DL","SI","DI","BP","SP"},
                { "DS", "ES", "SS", "CS" } };
        String[] types = { "REG", "SREG" };

        int i = 0;
        for ( String[] set : registers ) {
            for ( String register : set )
                if ( this.text.equalsIgnoreCase(register) ) {
                    this.type = "Registro";
                    this.subtype = types[i];
                    this.operandType = types[i];
                    if ( register.endsWith("H") || register.endsWith("L") ) this.size = "byte";
                    else this.size = "word";
                    return true;
                }
            i++;
        }

        return false;
    }

    /**
     * Verifica si la cadena corresponde a una instruccion (asignada) y, si lo es, le asigna el tipo "Instrucción",
     * el subtipo (entre "sin operandos", "un operando", "dos operandos", "etiqueta").
     * @return TRUE si la cadena corresponde a una instruccion (asignada), y FALSE en caso contrario.
     */
    private boolean isInstruction(){
        // arreglos con conjuntos de instrucciones (sin operandos, un operando, dos operandos o de una etiqueta)
        String [][] instructions = { { "HLT", "LODSW", "POPF", "STC", "XLATB", "AAA"},
                {"POP", "IDIV", "NEG", "PUSH"},
                {"RCL", "SHL", "XCHG", "ADD"},
                {"JNGE", "JNP", "JP", "LOOPE", "JA", "JC"} };
        String[] types = {"SIN OPS", "UN OP", "DOS OPS", "UN OP/ETQ"};

        int i = 0;
        for ( String[] set : instructions ) {
            for ( String instruccion : set )
                if ( this.text.equalsIgnoreCase(instruccion) ) {
                    this.type = "Instrucción";
                    this.subtype = types[i];
                    this.operandType = " ";
                    return true;
                }
            i++;
        }

        return false;
    }

    /**
     * Verifica si la cadena corresponde a una instrucciòn no asignada y, si lo es, le asigna el tipo "Símbolo".
     * @return TRUE si la cadena corresponde a una instrucciòn no asignada, y FALSE en caso contrario.
     */
    private boolean isUnassignedInstruction(){
        String[] instructions = { "AAD", "AAM", "AAS", "ADC", "AND", "CALL", "CBW", "CLC", "CLD", "CLI", "CMC",
                "CMP", "CMPSB", "CMPSW", "CWD", "DAA", "DAS", "DEC", "DIV", "IMUL", "IN", "INC",
                "INT", "INTO", "IRET", "JAE", "JB", "JBE", "JCXZ", "JE", "JG", "JGE", "JL", "JLE",
                "JMP", "JNA", "JNAE", "JNB", "JNBE", "JNC", "JNE", "JNG", "JNL", "JNLE", "JNO",
                "JNS", "JNZ", "JO", "JPE", "JPO", "JS", "JZ", "LAHF", "LDS", "LEA", "LES", "LODSB",
                "LOOP", "LOOPNE", "LOOPNZ", "LOOPZ", "MOV", "MOVSB", "MOVSW", "MUL",
                "NOP", "NOT", "OR", "OUT", "POPA", "PUSHA", "PUSHF", "RCR", "REP",
                "REPE", "REPNE", "REPNZ", "REPZ", "RET", "RETF", "ROL", "ROR", "SAHF", "SAL", "SAR", "SBB",
                "SCASB", "SCASW", "SHR", "STD", "STI", "STOSB", "STOSW", "SUB", "TEST", "XOR" };

        for ( String s : instructions )
            if ( this.text.equalsIgnoreCase(s) ) {
                this.type = "Símbolo";
                this.subtype = "NO ASIGNADA";
                this.operandType = " ";
                return true;
            }

        return false;
    }

    /**
     * Verifica si la cadena corresponde a una Constante del tipo caracter, y si lo es, le asigna el tipo "Caracter".
     * @return TRUE si la cadena corresponde a una Constante [Caracter] y FALSE en caso contrario.
     */
    private boolean isCharacterConstant(){
        if ( ( this.text.startsWith("\"") && this.text.endsWith("\""))
                || ( this.text.startsWith("'") && this.text.endsWith("'")) ) {
            this.type = "Constante caracter";
            this.subtype = "CARACTER";
            this.operandType = " ";
            if ( this.text.length()<128 ) this.size = "byte";
            else if ( this.text.length()<32768 ) this.size = "word";
            else return false;
            return true;
        }

        return false;
    }

    /**
     * Verifica si la cadena corresponde a una cte numerica, y si lo es, le asigna el tipo Numerico correspondiente.
     * @return TRUE si la cadena corresponde a una Constante [Numerica] y FALSE en caso contrario.
     */
    private boolean isNumericConstant(){
        String[] s = this.text.split("");

        // se verifica si la cadena es una constante numérica hexadecimal
        if ( isHex(s) ) {
            this.type = "Constante numérica hexadecimal";
            this.subtype = "HEX";
            this.operandType = "immediate";
            if ( this.text.length()-2==4 ) this.size = "word";
            else this.size = "byte";
            this.isNegative = this.text.startsWith("08") || this.text.startsWith("09")
                    || this.text.startsWith("0A") || this.text.startsWith("0B") || this.text.startsWith("0C")
                    || this.text.startsWith("0D") || this.text.startsWith("0E") || this.text.startsWith("0F");
            return true;
        }

        // se verifica si la cadena es una constante numérica binaria
        else if ( isBin(s) ) {
            this.type = "Constante numérica binaria";
            this.subtype = "BIN";
            this.operandType = "immediate";
            if ( this.text.length()-1==8 ) this.size = "byte";
            else this.size = "word";
            this.isNegative = this.text.startsWith("1");
            return true;
        }

        // se verifica si la cadena es una constante numérica decimal
        else if ( isDec(s) ) {
            try {
                int num = Integer.parseInt(this.text);
                if ( num >= 32768 )
                    return false;
                else {
                    this.type = "Constante numérica decimal";
                    this.subtype = "DEC";
                    this.operandType = "immediate";
                    this.isNegative = num < 0;
                    if ( num < 128 && num > -128 ) {
                        this.size = "byte";
                        return true;
                    }
                    else if ( num < 32768 && num > -32768 ) {
                        this.size = "word";
                        return true;
                    }
                }
            }
            catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    /**
     * Verifica si los caracteres almacenados en un arreglo 'String[]' corresponden a un número hexadecimal.
     * @param s String[]: Arreglo que almacena los caracteres de la cadena a verificar
     * @return TRUE si la cadena corresponde a una Constante numérica hexadecimal y FALSE en caso contrario.
     */
    private boolean isHex(String[] s){
        boolean isHex = false;
        if ( s[0].matches("0") && s[s.length-1].equalsIgnoreCase("H")
                && ( s.length-2==4 || s.length-2==2 ) ) {
            isHex = true;
            for ( int i = 1; i < s.length-1; i++ ) {
                if ( !(s[i].matches("[0-9]+") || s[i].equalsIgnoreCase("A")
                        || s[i].equalsIgnoreCase("B") || s[i].equalsIgnoreCase("C")
                        || s[i].equalsIgnoreCase("D") || s[i].equalsIgnoreCase("E")
                        || s[i].equalsIgnoreCase("F")) ) {
                    isHex = false;
                    break;
                }
            }
        }
        return isHex;
    }

    /**
     * Verifica si los caracteres almacenados en un arreglo 'String[]' corresponden a un número binario.
     * @param s String[]: Arreglo que almacena los caracteres de la cadena a verificar
     * @return TRUE si la cadena corresponde a una Constante numérica binaria y FALSE en caso contrario.
     */
    private boolean isBin(String[] s){
        boolean isBin = false;

        if ( s[s.length-1].equalsIgnoreCase("B") && ( s.length-1==8 || s.length-1==16 ) ) {
            isBin = true;
            for ( int i = 0; i < s.length-1; i++ ) {
                if ( !(s[i].matches("[0-1]+") ) ) {
                    isBin = false;
                    break;
                }
            }
        }
        return isBin;
    }

    /**
     * Verifica si los caracteres almacenados en un arreglo 'String[]' corresponden a un número decimal.
     * @param s String[]: Arreglo que almacena los caracteres de la cadena a verificar
     * @return TRUE si la cadena corresponde a una Constante numérica decimal y FALSE en caso contrario.
     */
    private boolean isDec(String[] s){
        boolean isDec = false;

        if ( s[0].matches("[0-9]+") || s[0].matches("-") ) {
            isDec = true;
            for ( int i = 0; i < s.length; i++ ) {
                if ( i == 0 ) {
                    if ( !( s[i].matches("[0-9]+") || s[i].matches("-") ) ) {
                        isDec = false;
                        break;
                    }
                }
                else {
                    if ( !(s[i].matches("[0-9]+") ) ) {
                        isDec = false;
                        break;
                    }
                }
            }
        }
        return isDec;
    }

    /**
     * Verifica si la cadena corresponde a una pseudoinstruccion y, si lo es, le asigna dicho tipo
     * y el subtipo (entre "segment", "data", "access", "macro", "procedure").
     * @return TRUE si la cadena corresponde a una pseudoinstruccion (no asignada) y FALSE en caso contrario.
     */
    private boolean isPseudoinstruction(){
        String[][] pseudoinstructions = {
                {".data segment", ".code segment", ".stack segment", "ends"},   // segmento
                {"dw", "db", "equ"},                                            // tipo de dato
                {"byte ptr", "word ptr"},                                       // memory access
                {"macro", "endm"},                                              // macro
                {"proc", "endp"} };                                             // procedimiento
        String[] types = {"SEGMENT", "DATA", "ACCESS", "MACRO", "PROCEDURE"};

        int i = 0;
        for ( String[] set : pseudoinstructions ) {
            for ( String pseudoinst : set )
                if ( this.text.equalsIgnoreCase(pseudoinst) ) {
                    this.type = "Pseudoinstrucción";
                    this.subtype = types[i];
                    this.operandType = " ";
                    if (text.equalsIgnoreCase("dw") || text.equalsIgnoreCase("equ"))
                        this.size = "word";
                    else if (this.text.equalsIgnoreCase("db"))
                        this.size = "byte";
                    return true;
                }
            i++;
        }

        return false;
    }

    /**
     * Verifica si la cadena corresponde a una pseudoinstruccion DUP, y si lo es, le asigna el tipo
     * "Pseudoinstruccion" y el subtipo (entre "DUP con argumento sin definir",
     * "DUP con argumento constante caracter", "DUP con argumento constante numerica" o
     * "DUP con argumento invalido").
     * @return TRUE si la cadena corresponde a una pseudoinstruccion DUP y FALSE en caso contrario.
     */
    private boolean isDUP(){
        if ( (this.text.startsWith("DUP(") || this.text.startsWith("dup(")) && this.text.endsWith(")") ) {

            StringBuilder cad = new StringBuilder();
            for (int i = this.text.indexOf("(")+1; i < this.text.length()-1; i++ )
                cad.append(this.text.charAt(i));

            if ( cad.toString().equalsIgnoreCase("\"?\"")
                    || cad.toString().equalsIgnoreCase("'?'") ) {
                this.type = "Pseudoinstruccion";
                this.subtype = "DUP con argumento sin definir";
                this.operandType = " ";
            }
            else {
                Element argument = new Element(cad.toString());
                this.argument = argument;
                if ( argument.isCharacterConstant() ) {
                    this.type = "Pseudoinstruccion";
                    this.subtype = "DUP con argumento constante caracter";
                    this.operandType = "";
                    this.size = argument.getSize();
                    return true;
                }
                else if ( argument.isNumericConstant() ) {
                    this.type = "Pseudoinstruccion";
                    this.subtype = "DUP con argumento constante numerica";
                    this.operandType = "";
                    this.size = argument.getSize();
                    return true;
                }
                else {
                    return false;
                }
            }
        }

        return false;
    }

    /**
     * Verifica si la cadena corresponde a una localidad de memoria.
     * @return TRUE si la cadena corresponde a una localidad de memoria y FALSE en caso contrario.
     */
    private boolean isMemoryAccess(){
        if ( this.text.startsWith("[") && this.text.endsWith("]") ) {
            // se genera cadena con el contenido entre corchetes
            StringBuilder cad = new StringBuilder();
            for (int i = 1; i<this.text.length()-1; i++) {
                cad.append(this.text.charAt(i));
            }

            String[] split = cad.toString().split("\\+");
            ArrayList<String> split2 = new ArrayList<>();
            for ( String s : split ) {
                if ( s.startsWith(" ") || s.endsWith(" ") ) split2.add( s.replace(" ", "") );
                else split2.add(s);
            }

            ArrayList<Element> elements = new ArrayList<>();
            for ( String s : split2 )
                elements.add( new Element(s) );

            boolean elementsAreValid = true;
            for ( Element e : elements ) {
                if (!(e.getText().equalsIgnoreCase("BX")
                        || e.getText().equalsIgnoreCase("SI")
                        || e.getText().equalsIgnoreCase("DI")
                        || e.getText().equalsIgnoreCase("BP")
                        || e.getText().equalsIgnoreCase("d8")
                        || e.getText().equalsIgnoreCase("d16")
                        || e.getSubtype().equalsIgnoreCase("HEX")
                        || e.getSubtype().equalsIgnoreCase("BIN")
                        || e.getSubtype().equalsIgnoreCase("DEC"))) {
                    elementsAreValid = false;
                    break;
                }
            }

            if ( elementsAreValid && (elements.size()==2 || elements.size()==3) ) {
                this.type = "Memory Access";
                this.operandType = "memory";
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica si la cadena corresponde a un simbolo, y si lo es, le asigna el tipo "Simbolo".
     * @return TRUE si la cadena corresponde a un simbolo y FALSE en caso contrario.
     */
    private boolean isSymbol(){
        // String[] types = {"VARIABLE", "CONSTANT", "LABEL", "MACRO", "PROCEDURE"};

        if ( this.text.length()<=11
                && Character.isLetter(this.text.charAt(0))
                && this.text.endsWith(":") ) {
            this.type = "Símbolo";
            this.subtype = "LABEL";
            this.operandType = "label";
            return true;
        }
        else if ( this.text.length() <= 10 && Character.isLetter(this.text.charAt(0)) ) {
            this.type = "Símbolo";
            this.operandType = " ";
            return true;
        }
        else if ( this.text.contains("[")
                && this.text.contains("]")
                && this.text.indexOf('[') < this.text.indexOf(']')
                && this.text.endsWith("]")
                && this.text.indexOf('[') < 10
                && Character.isLetter(this.text.charAt(0)) ) {
            this.type = "Símbolo";
            this.operandType = " ";
            return true;
        }

        return false;
    }

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getSize() {
        return size;
    }

    public Element getArgument() {
        return argument;
    }

    public boolean isValid() {
        return isValid;
    }

    public boolean isNegative() {
        return isNegative;
    }

    public String getOperandType() { return operandType; }

}

