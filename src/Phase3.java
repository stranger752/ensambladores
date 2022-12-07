import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Phase3 {

    // lista de 'String[]'. cada 'String[]' contine la linea del codigo fuente y si es correcta/incorrecta
    private ArrayList<String[]> semantic = new ArrayList<>();
    // lista de 'String[]'. cada 'String[]' contine un símbolo y sus características (tipo, valor y tamaño)
    private ArrayList<String[]> symbols = new ArrayList<>();
    // lista de 'String[]'. cada 'String[]' contine la codificación de las instrucciones
    private ArrayList<String[]> encoding = new ArrayList<>();
    // lista de 'String[]'. cada 'String[]' contine símbolo y sus características (tipo, valor, tamaño y direccion)
    private ArrayList<String[]> symbols2 = new ArrayList<>();
    private ArrayList<Element[]> elements = new ArrayList<>();
    
    private int cpDef = 768;
    private int cp = cpDef;
    private boolean cc = true;
    
    private String cod = "",con = "";


    public Phase3(ArrayList<String[]> semantic, ArrayList<String[]> symbols, ArrayList<String[]> symbols2){
        this.semantic = semantic;
        this.symbols = symbols;
        
        // se llena al arreglo 'elements'
        for ( String[] s : semantic ) {
            ArrayList<String> elementsInLine = splitLine(s[0]);
            Element[] elements = new Element[elementsInLine.size()];
            for ( int i=0; i<elementsInLine.size(); i++ )
                elements[i] = new Element((String) elementsInLine.get(i));
            this.elements.add( elements );
		}


        // se llena a la lista del análisis lexicográfico que contine los elementos y su tipo (identifiación)
        for ( int j=0; j<elements.size(); j++ ) {
        	

        	cod = "  "+isCodeSeg(elements.get(j));
        	con = ("0"+Integer.toHexString(cp+Integer.parseInt(semantic.get(j)[2]))+"").toUpperCase();
        	cp+=Integer.parseInt(semantic.get(j)[2]);
        	System.out.println(getLine(semantic.get(j)[0])+"  "+semantic.get(j)[2]);
            this.encoding.add( new String[] {"         "+con, getLine(semantic.get(j)[0]),cod } );
            //cc = chekEquals(semantic.get(j)[0],("Correcta."));

    		//this.symbols2 = symbols2;
    		for ( int i=0; i<this.symbols.size(); i++ ){
    			
    			if(hasCorrectElement(elements.get(j)[0],0,this.symbols.get(i)[0]+":")){
    				this.symbols2.add(new String[]{symbols.get(i)[0],symbols.get(i)[1],symbols.get(i)[2],symbols.get(i)[3], con});
    			}
    			
    			if(hasCorrectElement(elements.get(j)[0],0,this.symbols.get(i)[0])&&
    					//(hasCorrectType(elements.get(j)[1],1,this.symbols.get(i)[1]))
    					//&& 
    					(checkValue(elements.get(j)[2],elements.get(j),elements.get(j).length,this.symbols.get(i)))
    					) {
    			this.symbols2.add(new String[]{symbols.get(i)[0],symbols.get(i)[1],symbols.get(i)[2],symbols.get(i)[3], con});
    			}
    			
    		}
            
        }
        // En el array symbols2, el elemento i es muy similar al elemento i de symbols
        // los elemento i son arrays String[5] y String[4], respecticamente
        // se tendría que poner algo como
        // for (int i=0; i<longitud_symbols ;i++)
        //        encoding.add(new String[]{symbols.get(i)[0],symbols.get(i)[1],symbols.get(i)[2], symbols.get(i)[3], la_direccion})
    }
    
private String isCodeSeg(Element[] line){
    	

        String error1 = "Incorrecta. Número de elementos incorrecto.";
        String error2 = "Incorrecta. Operando(s) incorrecto(s)/inválido(s).";
        String error3 = "Incorrecta. Operandos deben ser del mismo tamaño.";
        String error4 = "Incorrecta. La etiqueta no ha sido definida previamente.";
        String error5 = "Incorrecta. Instrucción no asignada.";
        
        String w = "",d = "",s = "",mod = "",rm = "",reg = "",msg = "-";
    	
        if ( !hasValidElements(line) ) return "-";
        
        if (  !(hasCorrectLength(line, 1) ||hasCorrectLength(line, 2) ||hasCorrectLength(line, 3))) return "-";

        else if ( line.length==3 ) {
        	
        	int i = 0;
        	boolean is1st = false;
        	boolean is2nd = false;
        	
            if ( !(hasCorrectType(line[0], 0, "Instrucción")&&hasCorrectSubtype(line[0], 0, "DOS OPS")) ) return "-";
            
            if(getMem(line[1].getText())!=(-1)) {
            	is1st = true;
            	i = getMem(line[1].getText());
            }
            else if(getMem(line[2].getText())!=(-1)) {
            	is2nd = true;
            	i = getMem(line[2].getText());
            }
            
            switch(line[0].getText()) {
            
            case "RCL":
				switch(line[1].getText()) {
				case "AX":
					w ="1";
					reg = "000";
					mod = "11";
					rm = "000";
					break;
				case "CX":
					w ="1";
					reg = "001";
					mod = "11";
					rm = "001";
					break;
				case "DX":
					w ="1";
					reg = "010";
					mod = "11";
					rm = "010";
					break;
				case "BX":
					w ="1";
					reg = "011";
					mod = "11";
					rm = "011";
					break;
				case "SP":
					w ="1";
					reg = "100";
					mod = "11";
					rm = "100";
					break;
				case "BP":
					w ="1";
					reg = "101";
					mod = "11";
					rm = "101";
					break;
				case "SI":
					w ="1";
					reg = "110";
					mod = "11";
					rm = "110";
					break;
				case "DI":
					w ="1";
					reg = "111";
					mod = "11";
					rm = "111";
					break;
				case "AL":
					w ="0";
					reg = "000";
					mod = "11";
					rm = "000";
					break;
				case "CL":
					w ="0";
					reg = "001";
					mod = "11";
					rm = "001";
					break;
				case "DL":
					w ="0";
					reg = "010";
					mod = "11";
					rm = "010";
					break;
				case "BL":
					w ="0";
					reg = "011";
					mod = "11";
					rm = "011";
					break;
				case "AH":
					w ="0";
					reg = "100";
					mod = "11";
					rm = "100";
					break;
				case "CH":
					w ="0";
					reg = "101";
					mod = "11";
					rm = "101";
					break;
				case "DH":
					w ="0";
					reg = "110";
					mod = "11";
					rm = "110";
					break;
				case "BH":
					w ="0";
					reg = "111";
					mod = "11";
					rm = "111";
					break;
				}
				
				if(is1st) {
            		switch(line[2].getText()) {
            		case "1":
            			cp += 2;
            			return binToHex(("1100000"+w+mod+"010"+rm).split("")) + binToHex(this.symbols.get(i)[2].split(""));
            		case "CL":
            			cp += 2;
            			return binToHex(("1100000"+w+mod+"010"+rm).split(""))+ binToHex(this.symbols.get(i)[2].split(""));
            		}
            		switch(line[2].getType()) {
                	case "Constante numérica decimal":
            			cp += 2;
                		return binToHex(("1100000"+w+"00"+"010"+"110").split("")) + binToHex(this.symbols.get(i)[2].split(""));
                	case "Constante numérica binaria":
            			cp += 2;
                		return binToHex(("1100000"+w+"00"+"010"+"110").split("")) + binToHex(this.symbols.get(i)[2].split(""));
                	case "Constante numérica hexadecimal":
            			cp += 2;
                		return binToHex(("1100000"+w+"00"+"010"+"110").split("")) + binToHex(this.symbols.get(i)[2].split(""));
            		}
				}
				
            	switch(line[1].getType()) {
            	case "Registro":
            		switch(line[2].getText()) {
            		case "1":
            			cp += 2;
            			return binToHex(("1101000"+w+mod+"010"+rm).split(""));
            		case "CL":
            			cp += 2;
            			return binToHex(("1101000"+w+mod+"010"+rm).split(""));
            		}
            		switch(line[2].getType()) {
                	case "Constante numérica decimal":
            			cp += 2;
            			return binToHex(("1101000"+"1"+"00"+"010"+"110").split(""));
                	case "Constante numérica binaria":
            			cp += 2;
            			return binToHex(("1101000"+"1"+"00"+"010"+"110").split(""));
                	case "Constante numérica hexadecimal":
            			cp += 2;
            			return binToHex(("1101000"+"1"+"00"+"010"+"110").split(""));
            		}
            		break;
            	case "Constante numérica decimal":
            		switch(line[2].getText()) {
            		case "1":
            			cp += 2;
            			return binToHex(("1100000"+w+mod+"010"+rm).split(""));
            		case "CL":
            			cp += 2;
            			return binToHex(("1100000"+w+mod+"010"+rm).split(""));
            		}
            		switch(line[2].getType()) {
                	case "Constante numérica decimal":
            			cp += 2;
                		return binToHex(("1100000"+w+"00"+"010"+"110").split(""));
                	case "Constante numérica binaria":
            			cp += 2;
                		return binToHex(("1100000"+w+"00"+"010"+"110").split(""));
                	case "Constante numérica hexadecimal":
            			cp += 2;
                		return binToHex(("1100000"+w+"00"+"010"+"110").split(""));
            		}
            		break;
            	case "Constante numérica binaria":
            		switch(line[2].getText()) {
            		case "1":
            			cp += 2;
            			return binToHex(("1100000"+w+mod+"010"+rm).split(""));
            		case "CL":
            			cp += 2;
            			return binToHex(("1100000"+w+mod+"010"+rm).split(""));
            		}
            		switch(line[2].getType()) {
                	case "Constante numérica decimal":
            			cp += 2;
                		return binToHex(("1100000"+w+"00"+"010"+"110").split(""));
                	case "Constante numérica binaria":
            			cp += 2;
                		return binToHex(("1100000"+w+"00"+"010"+"110").split(""));
                	case "Constante numérica hexadecimal":
            			cp += 2;
                		return binToHex(("1100000"+w+"00"+"010"+"110").split(""));
            		}
            		break;
            	case "Constante numérica hexadecimal":
            		switch(line[2].getText()) {
            		case "1":
            			cp += 2;
            			return binToHex(("1100000"+w+mod+"010"+rm).split(""));
            		case "CL":
            			cp += 2;
            			return binToHex(("1100000"+w+mod+"010"+rm).split(""));
            		}
            		switch(line[2].getType()) {
                	case "Constante numérica decimal":
            			cp += 2;
                		return binToHex(("1100000"+w+"00"+"010"+"110").split(""));
                	case "Constante numérica binaria":
            			cp += 2;
                		return binToHex(("1100000"+w+"00"+"010"+"110").split(""));
                	case "Constante numérica hexadecimal":
            			cp += 2;
                		return binToHex(("1100000"+w+"00"+"010"+"110").split(""));
            		}
            		break;
            	}
            case "SHL":

				switch(line[1].getText()) {
				case "AX":
					w ="1";
					reg = "000";
					mod = "11";
					rm = "000";
					break;
				case "CX":
					w ="1";
					reg = "001";
					mod = "11";
					rm = "001";
					break;
				case "DX":
					w ="1";
					reg = "010";
					mod = "11";
					rm = "010";
					break;
				case "BX":
					w ="1";
					reg = "011";
					mod = "11";
					rm = "011";
					break;
				case "SP":
					w ="1";
					reg = "100";
					mod = "11";
					rm = "100";
					break;
				case "BP":
					w ="1";
					reg = "101";
					mod = "11";
					rm = "101";
					break;
				case "SI":
					w ="1";
					reg = "110";
					mod = "11";
					rm = "110";
					break;
				case "DI":
					w ="1";
					reg = "111";
					mod = "11";
					rm = "111";
					break;
				case "AL":
					w ="0";
					reg = "000";
					mod = "11";
					rm = "000";
					break;
				case "CL":
					w ="0";
					reg = "001";
					mod = "11";
					rm = "001";
					break;
				case "DL":
					w ="0";
					reg = "010";
					mod = "11";
					rm = "010";
					break;
				case "BL":
					w ="0";
					reg = "011";
					mod = "11";
					rm = "011";
					break;
				case "AH":
					w ="0";
					reg = "100";
					mod = "11";
					rm = "100";
					break;
				case "CH":
					w ="0";
					reg = "101";
					mod = "11";
					rm = "101";
					break;
				case "DH":
					w ="0";
					reg = "110";
					mod = "11";
					rm = "110";
					break;
				case "BH":
					w ="0";
					reg = "111";
					mod = "11";
					rm = "111";
					break;
				}
            	switch(line[1].getType()) {
            	case "Registro":
            		switch(line[2].getText()) {
            		case "1":
            			cp += 2;
            			return binToHex(("1101000"+w+mod+"100"+rm).split(""));
            		case "CL":
            			cp += 2;
            			return binToHex(("1101000"+w+mod+"100"+rm).split(""));
            		}
            		switch(line[2].getType()) {
                	case "Constante numérica decimal":
            			cp += 2;
            			return binToHex(("1101000"+"1"+"00"+"100"+"110").split(""));
                	case "Constante numérica binaria":
            			cp += 2;
            			return binToHex(("1101000"+"1"+"00"+"100"+"110").split(""));
                	case "Constante numérica hexadecimal":
            			cp += 2;
            			return binToHex(("1101000"+"1"+"00"+"100"+"110").split(""));
            		}
            		break;
            	case "Constante numérica decimal":
            		switch(line[2].getText()) {
            		case "1":
            			cp += 2;
            			return binToHex(("1100000"+w+mod+"100"+rm).split(""));
            		case "CL":
            			cp += 2;
            			return binToHex(("1100000"+w+mod+"100"+rm).split(""));
            		}
            		switch(line[2].getType()) {
                	case "Constante numérica decimal":
            			cp += 2;
                		return binToHex(("1100000"+w+"00"+"100"+"110").split(""));
                	case "Constante numérica binaria":
            			cp += 2;
                		return binToHex(("1100000"+w+"00"+"100"+"110").split(""));
                	case "Constante numérica hexadecimal":
            			cp += 2;
                		return binToHex(("1100000"+w+"00"+"100"+"110").split(""));
            		}
            		break;
            	case "Constante numérica binaria":
            		switch(line[2].getText()) {
            		case "1":
            			cp += 2;
            			return binToHex(("1100000"+w+mod+"100"+rm).split(""));
            		case "CL":
            			cp += 2;
            			return binToHex(("1100000"+w+mod+"100"+rm).split(""));
            		}
            		switch(line[2].getType()) {
                	case "Constante numérica decimal":
            			cp += 2;
                		return binToHex(("1100000"+w+"00"+"100"+"110").split(""));
                	case "Constante numérica binaria":
            			cp += 2;
                		return binToHex(("1100000"+w+"00"+"100"+"110").split(""));
                	case "Constante numérica hexadecimal":
            			cp += 2;
                		return binToHex(("1100000"+w+"00"+"100"+"110").split(""));
            		}
            		break;
            	case "Constante numérica hexadecimal":
            		switch(line[2].getText()) {
            		case "1":
            			cp += 2;
            			return binToHex(("1100000"+w+mod+"100"+rm).split(""));
            		case "CL":
            			cp += 2;
            			return binToHex(("1100000"+w+mod+"100"+rm).split(""));
            		}
            		switch(line[2].getType()) {
                	case "Constante numérica decimal":
            			cp += 2;
                		return binToHex(("1100000"+w+"00"+"100"+"110").split(""));
                	case "Constante numérica binaria":
            			cp += 2;
                		return binToHex(("1100000"+w+"00"+"100"+"110").split(""));
                	case "Constante numérica hexadecimal":
            			cp += 2;
                		return binToHex(("1100000"+w+"00"+"100"+"110").split(""));
            		}
            		break;
            	}
            case "XCHGX":

				switch(line[1].getText()) {
				case "AX":
					w ="1";
					reg = "000";
					mod = "11";
					rm = "000";
					break;
				case "CX":
					w ="1";
					reg = "001";
					mod = "11";
					rm = "001";
					break;
				case "DX":
					w ="1";
					reg = "010";
					mod = "11";
					rm = "010";
					break;
				case "BX":
					w ="1";
					reg = "011";
					mod = "11";
					rm = "011";
					break;
				case "SP":
					w ="1";
					reg = "100";
					mod = "11";
					rm = "100";
					break;
				case "BP":
					w ="1";
					reg = "101";
					mod = "11";
					rm = "101";
					break;
				case "SI":
					w ="1";
					reg = "110";
					mod = "11";
					rm = "110";
					break;
				case "DI":
					w ="1";
					reg = "111";
					mod = "11";
					rm = "111";
					break;
				case "AL":
					w ="0";
					reg = "000";
					mod = "11";
					rm = "000";
					break;
				case "CL":
					w ="0";
					reg = "001";
					mod = "11";
					rm = "001";
					break;
				case "DL":
					w ="0";
					reg = "010";
					mod = "11";
					rm = "010";
					break;
				case "BL":
					w ="0";
					reg = "011";
					mod = "11";
					rm = "011";
					break;
				case "AH":
					w ="0";
					reg = "100";
					mod = "11";
					rm = "100";
					break;
				case "CH":
					w ="0";
					reg = "101";
					mod = "11";
					rm = "101";
					break;
				case "DH":
					w ="0";
					reg = "110";
					mod = "11";
					rm = "110";
					break;
				case "BH":
					w ="0";
					reg = "111";
					mod = "11";
					rm = "111";
					break;
				}
            	switch(line[1].getType()) {
            	case "Registro":
            		switch(line[2].getType()) {
                	
            		case "Registro":
            			cp += 2;
            			return binToHex(("1000011"+w+mod+reg+rm).split(""));
            		case "Constante numérica decimal":
            			cp += 2;
            			return binToHex(("1000011"+w+"00"+reg+"11'").split(""));
                	case "Constante numérica binaria":
            			cp += 2;
            			return binToHex(("1000011"+w+"00"+reg+"11'").split(""));
                	case "Constante numérica hexadecimal":
            			cp += 2;
            			return binToHex(("1000011"+w+"00"+reg+"11'").split(""));
            		}
            		break;
            	
            case "ADD":

				switch(line[1].getText()) {
				case "AX":
					w ="1";
					reg = "000";
					mod = "11";
					rm = "000";
					break;
				case "CX":
					w ="1";
					reg = "001";
					mod = "11";
					rm = "001";
					break;
				case "DX":
					w ="1";
					reg = "010";
					mod = "11";
					rm = "010";
					break;
				case "BX":
					w ="1";
					reg = "011";
					mod = "11";
					rm = "011";
					break;
				case "SP":
					w ="1";
					reg = "100";
					mod = "11";
					rm = "100";
					break;
				case "BP":
					w ="1";
					reg = "101";
					mod = "11";
					rm = "101";
					break;
				case "SI":
					w ="1";
					reg = "110";
					mod = "11";
					rm = "110";
					break;
				case "DI":
					w ="1";
					reg = "111";
					mod = "11";
					rm = "111";
					break;
				case "AL":
					w ="0";
					reg = "000";
					mod = "11";
					rm = "000";
					break;
				case "CL":
					w ="0";
					reg = "001";
					mod = "11";
					rm = "001";
					break;
				case "DL":
					w ="0";
					reg = "010";
					mod = "11";
					rm = "010";
					break;
				case "BL":
					w ="0";
					reg = "011";
					mod = "11";
					rm = "011";
					break;
				case "AH":
					w ="0";
					reg = "100";
					mod = "11";
					rm = "100";
					break;
				case "CH":
					w ="0";
					reg = "101";
					mod = "11";
					rm = "101";
					break;
				case "DH":
					w ="0";
					reg = "110";
					mod = "11";
					rm = "110";
					break;
				case "BH":
					w ="0";
					reg = "111";
					mod = "11";
					rm = "111";
					break;
				}
            	switch(line[1].getType()) {
            	case "Registro":
            		switch(line[2].getType()) {
                	
            		case "Registro":
            			cp += 2;
            			return binToHex(("000000"+d+w+mod+reg+rm).split(""));
            		case "Constante numérica decimal":
            			cp += 2;
            			return binToHex(("00000011"+"00"+reg+"110").split(""));
                	case "Constante numérica binaria":
            			cp += 2;
            			return binToHex(("00000011"+"00"+reg+"110").split(""));
                	case "Constante numérica hexadecimal":
            			cp += 2;
            			return binToHex(("00000011"+"00"+reg+"110").split(""));
            		}
            		break;

        		case "Constante numérica decimal":
                   switch(line[2].getType()) {                	
            		case "Registro":
            			cp += 2;
            			return binToHex(("0000000"+"1"+"00"+reg+"110").split(""));
            			}
            		break;
            	case "Constante numérica binaria":
                    switch(line[2].getType()) {                	
             		case "Registro":
            			cp += 2;
            			return binToHex(("0000000"+"1"+"00"+reg+"110").split(""));
             			}
            	case "Constante numérica hexadecimal":
                    switch(line[2].getType()) {                	
             		case "Registro":
            			cp += 2;
            			return binToHex(("0000000"+"1"+"00"+reg+"110").split(""));
             			}
            }
        }}}
        else if ( line.length==2 ) {
        	if ( (hasCorrectType(line[0], 0, "Instrucción"))){
        		switch(line[0].getSubtype()) {
        		case "UN OP":
        			switch(line[0].getText()) {
        			case "POP":{

                		if(getMem(line[1].getText())!=(-1)) {
                			switch(getTipo(this.symbols.get(getMem(line[1].getText()))[2].split(""))) {
                			case "Bin":
            					cp+= 2+ ((binToHex(this.symbols.get(getMem(line[1].getText()))[2].split(""))).length())/2;
            					
            					return  binToHex(("10001111"+"00"+"000"+"110").split("")) + binToHex(this.symbols.get(getMem(line[1].getText()))[2].split(""));
            		             
                			case "Dec":
                    			if(Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2])).length() == 1 || Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2])).length() == 3 ){

                					cp+=  2+ (("0" + Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2]))).length())/2;
                					
                					return binToHex(("10001111"+"00"+"000"+"110").split("")) + "0" + Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2]));
                    			}
            					cp+=  2+ ((Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2]))).length())/2;
            					
            					
            					return  binToHex(("10001111"+"00"+"000"+"110").split("")) + Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2]));
            		             
                			case "Hex":
            					cp+=  2+ ((this.symbols.get(getMem(line[1].getText()))[2]).length())/2;
            					
            					return  binToHex(("10001111"+"00"+"000"+"110").split("")) + this.symbols.get(getMem(line[1].getText()))[2];
                			}
                		}   		
        				
        				switch(line[1].getText()) {
        				case "AX":
                			cp += 2;
        					return binToHex(("10001111"+"11"+"000"+"000").split(""));
        				case "CX":
                			cp += 2;
        					return binToHex(("10001111"+"11"+"000"+"001").split(""));
        				case "DX":
                			cp += 2;
        					return binToHex(("10001111"+"11"+"000"+"010").split(""));
        				case "BX":
                			cp += 2;
        					return binToHex(("10001111"+"11"+"000"+"011").split(""));
        				case "SP":
                			cp += 2;
        					return binToHex(("10001111"+"11"+"000"+"100").split(""));
        				case "BP":
                			cp += 2;
        					return binToHex(("10001111"+"11"+"000"+"101").split(""));
        				case "SI":
                			cp += 2;
        					return binToHex(("10001111"+"11"+"000"+"101").split(""));
        				case "DI":
                			cp += 2;
        					return binToHex(("10001111"+"11"+"000"+"111").split(""));
        				}

        				switch(line[1].getType()) {
        				case "Constante numérica hexadecimal":
                			cp += 2;
        					return binToHex(("10001111"+"00"+"000"+"110").split(""));
        				case "Constante numérica binaria":
                			cp += 2;
        					return binToHex(("10001111"+"00"+"000"+"110").split(""));
        				case "Constante numérica decimal":
                			cp += 2;
                			if(Integer.toHexString(Integer.parseInt(line[1].getText())).length() == 1 || Integer.toHexString(Integer.parseInt(line[1].getText())).length() == 3 ){

            					return binToHex(("10001111"+"00"+"000"+"110").split("")) + "0" + Integer.toHexString(Integer.parseInt(line[1].getText()));
                			}
        					return binToHex(("10001111"+"00"+"000"+"110").split(""));
        				}
        			}
        			case "IDIV":
        			{

                		if(getMem(line[1].getText())!=(-1)) {
                			switch(getTipo(this.symbols.get(getMem(line[1].getText()))[2].split(""))) {
                			case "Bin":
            					cp+=  2+ ((binToHex(this.symbols.get(getMem(line[1].getText()))[2].split(""))).length())/2;
            					
            					return  binToHex(("11110111"+"00"+"111"+"110").split("")) + binToHex(this.symbols.get(getMem(line[1].getText()))[2].split(""));
            		             
                			case "Dec":
                    			if(Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2])).length() == 1 || Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2])).length() == 3 ){

                					cp+=  2+ (("0" + Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2]))).length())/2;
                					
                					return binToHex(("11110111"+"00"+"111"+"110").split("")) + "0" + Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2]));
                    			}
            					cp+=  2+ ((Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2]))).length())/2;
            					
            					
            					return  binToHex(("11110111"+"00"+"111"+"110").split("")) + Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2]));
            		             
                			case "Hex":
            					cp+= 2+  ((this.symbols.get(getMem(line[1].getText()))[2]).length())/2;
            					
            					return  binToHex(("11110111"+"00"+"111"+"110").split("")) + this.symbols.get(getMem(line[1].getText()))[2];
                			}
                		}   		
        				
        				
        				
        				switch(line[1].getText()) {
        				case "AX":
                			cp += 2;
        					return binToHex(("11110111"+"11"+"111"+"000").split(""));
        				case "CX":
                			cp += 2;
        					return binToHex(("11110111"+"11"+"111"+"001").split(""));
        				case "DX":
                			cp += 2;
        					return binToHex(("11110111"+"11"+"111"+"010").split(""));
        				case "BX":
                			cp += 2;
        					return binToHex(("11110111"+"11"+"111"+"011").split(""));
        				case "SP":
                			cp += 2;
        					return binToHex(("11110111"+"11"+"111"+"100").split(""));
        				case "BP":
                			cp += 2;
        					return binToHex(("11110111"+"11"+"111"+"101").split(""));
        				case "SI":
                			cp += 2;
        					return binToHex(("11110111"+"11"+"111"+"101").split(""));
        				case "DI":
                			cp += 2;
        					return binToHex(("11110111"+"11"+"111"+"111").split(""));
        				case "AL":
                			cp += 2;
        					return binToHex(("11110110"+"11"+"111"+"000").split(""));
        				case "CL":
                			cp += 2;
        					return binToHex(("11110110"+"11"+"111"+"001").split(""));
        				case "DL":
                			cp += 2;
        					return binToHex(("11110110"+"11"+"111"+"010").split(""));
        				case "BL":
                			cp += 2;
        					return binToHex(("11110110"+"11"+"111"+"011").split(""));
        				case "AH":
                			cp += 2;
        					return binToHex(("11110110"+"11"+"111"+"100").split(""));
        				case "CH":
                			cp += 2;
        					return binToHex(("11110110"+"11"+"111"+"101").split(""));
        				case "DH":
                			cp += 2;
        					return binToHex(("11110110"+"11"+"111"+"101").split(""));
        				case "BH":
                			cp += 2;
        					return binToHex(("11110110"+"11"+"111"+"111").split(""));
        				}

        				switch(line[1].getType()) {
        				case "Constante numérica hexadecimal":
                			cp += 2;
        					return binToHex(("11110111"+"00"+"111"+"110").split(""))+line[1].getText();
        				case "Constante numérica binaria":
                			cp += 2;
        					return binToHex(("11110111"+"00"+"111"+"110").split("")) + binToHex(line[1].getText().split(""));
        				case "Constante numérica decimal":
                			cp += 2;
                			if(Integer.toHexString(Integer.parseInt(line[1].getText())).length() == 1 || Integer.toHexString(Integer.parseInt(line[1].getText())).length() == 3 ){

            					return binToHex(("11110111"+"00"+"111"+"110").split("")) + "0" + Integer.toHexString(Integer.parseInt(line[1].getText()));
                			}
        					return binToHex(("11110111"+"00"+"111"+"110").split("")) + Integer.toHexString(Integer.parseInt(line[1].getText()));
        				}
        			}
        			case "NEG":
        			{


                		if(getMem(line[1].getText())!=(-1)) {
                			switch(getTipo(this.symbols.get(getMem(line[1].getText()))[2].split(""))) {
                			case "Bin":
            					cp+=  2+ ((binToHex(this.symbols.get(getMem(line[1].getText()))[2].split(""))).length())/2;
            					
            					return  binToHex(("11110111"+"00"+"111"+"110").split("")) + binToHex(this.symbols.get(getMem(line[1].getText()))[2].split(""));
            		             
                			case "Dec":
                    			if(Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2])).length() == 1 || Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2])).length() == 3 ){

                					cp+=  2+ (("0" + Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2]))).length())/2;
                					
                					return binToHex(("11111111"+"00"+"110"+"110").split("")) + "0" + Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2]));
                    			}
            					cp+=  2+ ((Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2]))).length())/2;
            					
            					
            					return  binToHex(("11111111"+"00"+"110"+"110").split("")) + Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2]));
            		             
                			case "Hex":
            					cp+=  2+ ((this.symbols.get(getMem(line[1].getText()))[2]).length())/2;
            					
            					return  binToHex(("11111111"+"00"+"110"+"110").split("")) + this.symbols.get(getMem(line[1].getText()))[2];
                			}
                		}  
                		
        				switch(line[1].getText()) {
        				case "AX":
                			cp += 2;
        					return binToHex(("11110111"+"11"+"011"+"000").split(""));
        				case "CX":
                			cp += 2;
        					return binToHex(("11110111"+"11"+"011"+"001").split(""));
        				case "DX":
                			cp += 2;
        					return binToHex(("11110111"+"11"+"011"+"010").split(""));
        				case "BX":
                			cp += 2;
        					return binToHex(("11110111"+"11"+"011"+"011").split(""));
        				case "SP":
                			cp += 2;
        					return binToHex(("11110111"+"11"+"011"+"100").split(""));
        				case "BP":
                			cp += 2;
        					return binToHex(("11110111"+"11"+"011"+"101").split(""));
        				case "SI":
                			cp += 2;
        					return binToHex(("11110111"+"11"+"011"+"101").split(""));
        				case "DI":
                			cp += 2;
        					return binToHex(("11110111"+"11"+"011"+"111").split(""));
        				case "AL":
                			cp += 2;
        					return binToHex(("11110110"+"11"+"011"+"000").split(""));
        				case "CL":
                			cp += 2;
        					return binToHex(("11110110"+"11"+"011"+"001").split(""));
        				case "DL":
                			cp += 2;
        					return binToHex(("11110110"+"11"+"011"+"010").split(""));
        				case "BL":
                			cp += 2;
        					return binToHex(("11110110"+"11"+"011"+"011").split(""));
        				case "AH":
                			cp += 2;
        					return binToHex(("11110110"+"11"+"011"+"100").split(""));
        				case "CH":
                			cp += 2;
        					return binToHex(("11110110"+"11"+"011"+"101").split(""));
        				case "DH":
                			cp += 2;
        					return binToHex(("11110110"+"11"+"011"+"101").split(""));
        				case "BH":
                			cp += 2;
        					return binToHex(("11110110"+"11"+"011"+"111").split(""));
        				}

        				switch(line[1].getType()) {
        				case "Constante numérica hexadecimal":
                			cp += 2;
        					return binToHex(("11110111"+"00"+"011"+"110").split(""));
        				case "Constante numérica binaria":
                			cp += 2;
        					return binToHex(("11110111"+"00"+"011"+"110").split(""));
        				case "Constante numérica decimal":
                			cp += 2;
                			if(Integer.toHexString(Integer.parseInt(line[1].getText())).length() == 1 || Integer.toHexString(Integer.parseInt(line[1].getText())).length() == 3 ){

            					return binToHex(("11110111"+"00"+"011"+"110").split("")) + "0" + Integer.toHexString(Integer.parseInt(line[1].getText()));
                			}
        					return binToHex(("11110111"+"00"+"011"+"110").split(""));
        				}
        			}
        			case "PUSH":
        			{


                		if(getMem(line[1].getText())!=(-1)) {
                			switch(getTipo(this.symbols.get(getMem(line[1].getText()))[2].split(""))) {
                			case "Bin":
            					cp+= ((binToHex(this.symbols.get(getMem(line[1].getText()))[2].split(""))).length())/2;
            					
            					return  binToHex(("10001111"+"00"+"000"+"110").split("")) + binToHex(this.symbols.get(getMem(line[1].getText()))[2].split(""));
            		             
                			case "Dec":
                    			if(Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2])).length() == 1 || Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2])).length() == 3 ){

                					cp+=  2+ (("0" + Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2]))).length())/2;
                					
                					return binToHex(("10001111"+"00"+"000"+"110").split("")) + "0" + Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2]));
                    			}
            					cp+=  2+ ((Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2]))).length())/2;
            					
            					return  binToHex(("10001111"+"00"+"000"+"110").split("")) + Integer.toHexString(Integer.parseInt(this.symbols.get(getMem(line[1].getText()))[2]));
            		             
                			case "Hex":
            					cp+=  2+ ((this.symbols.get(getMem(line[1].getText()))[2]).length())/2;
            					
            					return  binToHex(("10001111"+"00"+"000"+"110").split("")) + this.symbols.get(getMem(line[1].getText()))[2];
                			}
                		} 
        				
        				switch(line[1].getText()) {
        				case "AX":
                			cp += 2;
        					return binToHex(("11111111"+"11"+"110"+"000").split(""));
        				case "CX":
                			cp += 2;
        					return binToHex(("11111111"+"11"+"110"+"001").split(""));
        				case "DX":
                			cp += 2;
        					return binToHex(("11111111"+"11"+"110"+"010").split(""));
        				case "BX":
                			cp += 2;
        					return binToHex(("11111111"+"11"+"110"+"011").split(""));
        				case "SP":
                			cp += 2;
        					return binToHex(("11111111"+"11"+"110"+"100").split(""));
        				case "BP":
                			cp += 2;
        					return binToHex(("11111111"+"11"+"110"+"101").split(""));
        				case "SI":
                			cp += 2;
        					return binToHex(("11111111"+"11"+"110"+"101").split(""));
        				case "DI":
                			cp += 2;
        					return binToHex(("11111111"+"11"+"110"+"111").split(""));
        				}

        				switch(line[1].getType()) {
        				case "Constante numérica hexadecimal":
                			cp += 2;
        					return binToHex(("11111111"+"00"+"110"+"110").split(""));
        				case "Constante numérica binaria":
                			cp += 2;
        					return binToHex(("11111111"+"00"+"110"+"110").split(""));
        				case "Constante numérica decimal":
                			cp += 2;
                			if(Integer.toHexString(Integer.parseInt(line[1].getText())).length() == 1 || Integer.toHexString(Integer.parseInt(line[1].getText())).length() == 3 ){

            					cp+= (("0"+Integer.toHexString(Integer.parseInt(line[1].getText()))).length())/2;
            					return binToHex(("11111111"+"00"+"110"+"110").split("")) + "0" + Integer.toHexString(Integer.parseInt(line[1].getText()));
                			}
        					return binToHex(("11111111"+"00"+"110"+"110").split(""));
        				}
        			}
        			
        			}
        		case  "UN OP/ETQ":
        			int i = getMem(line[1].getText());
        			switch(line[0].getText()) {
        			case "JNGE":
            			cp += 2;
            			if(i !=(-1)) {
            				cp+= ((this.symbols2.get(i)[4].length())/2);
        				return binToHex(("0000111110001100").split(""))+this.symbols2.get(i)[4];
        				}
        			case "JNP":
            			cp += 2;
            			if(i !=(-1)) {
            				cp+= ((this.symbols2.get(i)[4].length())/2);
        				return binToHex(("0000111110001011").split(""))+this.symbols2.get(i)[4];}
        			case "JP":
            			cp += 2;
            			if(i !=(-1)) {
            				cp+= ((this.symbols2.get(i)[4].length())/2);
        				return binToHex(("0000111110001010").split(""))+this.symbols2.get(i)[4];}
        			case "LOOPE":
            			cp += 1;
        				return binToHex(("11100001").split(""));
        			case "JA":
            			cp += 2;
            			if(i !=(-1)) {
            				cp+= ((this.symbols2.get(i)[4].length())/2);
        				return binToHex(("0000111110000111").split(""))+this.symbols2.get(i)[4];}
        			case "JC":
            			cp += 2;
            			if(i !=(-1)) {
            				cp+= ((this.symbols2.get(i)[4].length())/2);
        				return binToHex(("0000111110000010").split(""))+this.symbols2.get(i)[4];}
        			
        			}
                }
            }}
        else if ( line.length==1 ) {switch(line[0].getType()) {
        	case "Instrucción":switch(line[0].getText()) {
    			case "HLT":
                	
                	//msg = "Error - No usa operandos";
                	
        			cp += 1;
        			//return binToHex("11110100".split(""));
    				return binToHex(("11110100").split(""));
    			case "LODSW":
        			cp += 1;
    				return binToHex(("10101101").split(""));
    			case "POPF":
        			cp += 1;
    				return binToHex(("10011101").split(""));
    			case "STC":
        			cp += 1;
    				return binToHex(("11111001").split(""));
    			case "XLATB":
        			cp += 1;
    				return binToHex(("11010111").split(""));
    			case "AAA":
        			cp += 1;
    				return binToHex(("00110111").split(""));
    				}
        	case "Etiqueta":
        		System.out.println(line[0].getText());
        		System.out.println(line[0].getType());
        		if(hasCorrectType(line[0],0,"Etiqueta"))
        		return "Etiqueta - No se codifica";
        	default:
        		if(line[0].getText().endsWith(":")) {
            		return "Etiqueta - No se codifica";
        		}
        		return msg;
        	}
        }
        return msg;
    }


private String binToHex(String[] s) {
	String r = "",a="",b="";
	for(int i = 0 ; i <= s.length -4 ; i++) {
		//System.out.println("AAAAAAAAAAAAAAAAAa");
		r = ""+(((Integer.parseInt(s[i]))*(8))+((Integer.parseInt(s[i+1]))*(4))+((Integer.parseInt(s[i+2]))*(2))+((Integer.parseInt(s[i+3]))*(1)));
		a = Integer.toHexString(Integer.parseInt(r));
		b = b+a;
		System.out.println(s[i]+s[i+1]+s[i+2]+s[i+3]);
		System.out.println(b);
		System.out.println(Integer.toHexString(Integer.parseInt(r)));
		i += 3;
		b = b.toUpperCase();
	}
	//System.out.println("BBBBBBBBBBB");
	return  b;
}

private Boolean typesMatch(String[][] s,String a,String b) {
	 for(int i = 0; i<s.length ; i++) {
		 if(s[0][i].equalsIgnoreCase(a) && s[1][i].equalsIgnoreCase(b)) {
			 return true;
		 }
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
       return false;
   }
   return true;
}

private Boolean checkEquals(Element element, int index, String a) {
	String aux;   
	//if ( !element.getString() ) {
	       //return false;
	   //}
	   
	   switch(element.getText()) {
	   case "dw":
		   aux = "word";
		   if(a.equalsIgnoreCase(aux)) {
		   return true;
		   }
	   case "db":
		   aux = "byte";
		   if(a.equalsIgnoreCase(aux)) {
		   return true;
		   }
	   }
	   return false;
}

private String getTipo(String[] s) {
	String aux = "";
	


    if ( s[s.length-1].equalsIgnoreCase("B") && ( s.length-1==8 || s.length-1==16 ) ) {
        aux = "Bin";
        for ( int i = 0; i < s.length-1; i++ ) {
            if ( !(s[i].matches("[0-1]+") ) ) {
            	aux = "";
                break;
            }
        }
    }
	


    if ( s[0].matches("[0-9]+") || s[0].matches("-") ) {
        aux = "Dec";
        for ( int i = 0; i < s.length; i++ ) {
            if ( i == 0 ) {
                if ( !( s[i].matches("[0-9]+") || s[i].matches("-") ) ) {
                    aux = "";
                    break;
                }
            }
            else {
                if ( !(s[i].matches("[0-9]+") ) ) {
                    aux = "";
                    break;
                }
            }
        }
    }
	
	if ( s[0].matches("0") && s[s.length-1].equalsIgnoreCase("H")
            && ( s.length-2==4 || s.length-2==2 ) ) {
        aux = "Hex";
        for ( int i = 1; i < s.length-1; i++ ) {
            if ( !(s[i].matches("[0-9]+") || s[i].equalsIgnoreCase("A")
                    || s[i].equalsIgnoreCase("B") || s[i].equalsIgnoreCase("C")
                    || s[i].equalsIgnoreCase("D") || s[i].equalsIgnoreCase("E")
                    || s[i].equalsIgnoreCase("F")) ) {
                aux = "";
                break;
            }
        }
    }
	
	return aux;
}


private Boolean checkValue(Element element, Element[] s, int index, String[] a) {
	String aux;
	String[] ap;
	System.out.println("INDEX: "+index);
	switch(index) {
	case 3:
		if (element.getText().equalsIgnoreCase(a[2])) {
	   return true;
		}else return false;
	case 4:{
		/*
		if(!s[index-2].getString().equalsIgnoreCase(""+a[2]+""))
			return false;
		else*/
		
		///ESTA PARTE ES LA QUE GENERA EL ERROR
		if(s[index-1].getText().equalsIgnoreCase("DUP("+a[2]+")"))
	   {
			System.out.println(s[index-1].getText());
	   return true;
	   }else return false;
		
		//En caso de querer evitar el error comentar todo el if y else de arriba y descomentar el return true;
		//return true;
	   }
	default:
		return false;
		}
	//return false;
}

private Boolean checkArg(Element element, String a) {
	String aux;
	String[] ap;
	if(a.equalsIgnoreCase(element.getText())) {
		return true;
	}return false;
}

private Boolean checkDUP(Element element, int index, String[] a) {
	String aux;
	String[] ap;
	switch(index) {
	case 4:
		System.out.println(index);
		System.out.println(element.getText()+" "+a[2]);
		if(element.getText().equalsIgnoreCase("DUP("+a[2]+")"))
	   {
	   return true;
	   }
	   }
	return false;
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
       return false;
   }

   if ( element.getSize().equalsIgnoreCase(size) ) return true;
   else if ( element.getSubtype().equalsIgnoreCase("DEC")
           && size.equalsIgnoreCase("word") ) return true;
   else {
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


private int getMem(String s) {
	for(int i = 0;i<this.symbols.size();i++) {
		if(this.symbols.get(i)[0].equals(s))
			return i;
	}
	return -1;
}

    public ArrayList<String[]> getEncoding() { return encoding; }


    public ArrayList<String[]> getSymbols2() { return symbols2; }


}
