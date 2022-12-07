import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class Controller {

    private JFrame frame;
    private Window main;
    private Analyzer analyzer;

    private int counterSource, counterLexical, counterSemantic, counterSymbols, counterEncoding, counterSymbols2;

    /**
     * Constructor: Crea, configura y muestra la ventana principal de la GUI.
     */
    public Controller() {
        // se crea y configura la ventana
        frame = new JFrame("Ensambladores");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // se crea y configura el content pane
        main = new Window( frame );

        // configuracion adicional y se muestra la ventana
        frame.setSize(1280,720);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);

        // agrega 'Listeners' a los componentes
        addListenersBttnFile();
        addListenersBttnSourceL();
        addListenersBttnSourceR();
        addListenersBttnLexicalL();
        addListenersBttnLexicalR();
        addListenersBttnSemanticL();
        addListenersBttnSemanticR();
        addListenersBttnEncodingL();
        addListenersBttnEncodingR();

    }

    /**
     * Agrega listeners a la etiqueta/botón 'Seleccionar archivo'
     */
    private void addListenersBttnFile(){
        JLabel bttnFile = main.getBttnFile();

        bttnFile.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                // se crea 'JFileChooser' y se le agrega filtro de busqueda para archivos con extensión .asm
                FileNameExtensionFilter extension = new FileNameExtensionFilter("ASM","asm");
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(extension);
                fileChooser.showOpenDialog(fileChooser);

                try{
                    // se guarda el path seleccionado
                    String path = fileChooser.getSelectedFile().getAbsolutePath();
                    if ( isASMFile(path) ) {
                        main.getTextPath().setText( (" ") + (path) );  // se actualiza campo de texto con la ruta
                        analyzer = new Analyzer(path);                 // se crea al analizador

                        // se actualiza tabla del código fuente (en el panel 1)
                        counterSource = 0;
                        main.getTableSource().setModel(getUpdatedTableModel( main.getTableSource(),
                                analyzer.getSource(), counterSource, counterSource+24) );

                        // se actualiza tabla del análisis lexicográfico
                        counterLexical = 0;
                        main.getTableLexical().setModel(getUpdatedTableModel( main.getTableLexical(),
                                analyzer.getLexical(), counterLexical, counterLexical+24));

                        // se actualiza tabla del análisis semántico
                        counterSemantic = 0;
                        main.getTableSemantic().setModel(getUpdatedTableModel( main.getTableSemantic(),
                                analyzer.getSemantic(), counterSemantic, counterSemantic+28));

                        // se actualiza tabla de símbolos
                        counterSymbols = 0;
                        main.getTableSymbols().setModel(getUpdatedTableModel( main.getTableSymbols(),
                                analyzer.getSymbols(), counterSymbols, counterSymbols+28));

                        // se actualiza tabla de codificacion
                        counterEncoding = 0;
                        main.getTableEncoding().setModel(getUpdatedTableModel( main.getTableEncoding(),
                                analyzer.getEncoding(), counterEncoding, counterEncoding+28));

                        // se actualiza tabla de simbolos2
                        counterSymbols2 = 0;
                        main.getTableSymbols2().setModel(getUpdatedTableModel( main.getTableSymbols2(),
                                analyzer.getSymbols2(), counterSymbols2, counterSymbols2+28));

                    } else
                        throw new Exception("Error: archivo debe tener extensión .asm");
                } catch (Exception e){
                    if ( e.getMessage().equalsIgnoreCase("Error: archivo debe tener extensión .asm"))
                        JOptionPane.showMessageDialog(null, e.getMessage());
                    else JOptionPane.showMessageDialog(null, "Archivo no seleccionado.");
                }

            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bttnFile.setForeground(main.getCOLOR_BLACK());
                bttnFile.setBackground(main.getCOLOR_GREEN());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bttnFile.setForeground(main.getCOLOR_WHITE());
                bttnFile.setBackground(main.getCOLOR_BLUE());
            }
        });

    }

    /**
     * Agrega listeners a la etiqueta/botón 'Anterior' del campo del código fuente
     */
    private void addListenersBttnSourceL(){
        JLabel bttn = main.getBttnSourceL();

        bttn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if ( counterSource >= 24 ) {
                    counterSource = counterSource-24;
                    main.getTableSource().setModel(getUpdatedTableModel( main.getTableSource(),
                            analyzer.getSource(), counterSource, counterSource+24) );
                }
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bttn.setForeground(main.getCOLOR_BLACK());
                bttn.setBackground(main.getCOLOR_GREEN());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bttn.setForeground(main.getCOLOR_WHITE());
                bttn.setBackground(main.getCOLOR_BLUE());
            }
        });

    }

    /**
     * Agrega listeners a la etiqueta/botón 'Siguiente' del campo del código fuente
     */
    private void addListenersBttnSourceR(){
        JLabel bttn = main.getBttnSourceR();

        bttn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if ( counterSource <= analyzer.getSource().size()-24 ) {
                    counterSource = counterSource+24;
                    main.getTableSource().setModel(getUpdatedTableModel( main.getTableSource(),
                            analyzer.getSource(), counterSource, counterSource+24) );
                }
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bttn.setForeground(main.getCOLOR_BLACK());
                bttn.setBackground(main.getCOLOR_GREEN());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bttn.setForeground(main.getCOLOR_WHITE());
                bttn.setBackground(main.getCOLOR_BLUE());
            }
        });

    }

    /**
     * Agrega listeners a la etiqueta/botón 'Anterior' del campo del análisis lexicográfico
     */
    private void addListenersBttnLexicalL(){
        JLabel bttn = main.getBttnLexicalL();

        bttn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if ( counterLexical >= 24 ) {
                    counterLexical = counterLexical-24;
                    main.getTableLexical().setModel(getUpdatedTableModel( main.getTableLexical(),
                            analyzer.getLexical(), counterLexical, counterLexical+24) );
                }
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bttn.setForeground(main.getCOLOR_BLACK());
                bttn.setBackground(main.getCOLOR_GREEN());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bttn.setForeground(main.getCOLOR_WHITE());
                bttn.setBackground(main.getCOLOR_BLUE());
            }
        });

    }

    /**
     * Agrega listeners a la etiqueta/botón 'Siguiente' del campo del análisis lexicográfico
     */
    private void addListenersBttnLexicalR(){
        JLabel bttn = main.getBttnLexicalR();

        bttn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if ( counterLexical <= analyzer.getLexical().size()-24 ) {
                    counterLexical = counterLexical+24;
                    main.getTableLexical().setModel(getUpdatedTableModel( main.getTableLexical(),
                            analyzer.getLexical(), counterLexical, counterLexical+24) );
                }
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bttn.setForeground(main.getCOLOR_BLACK());
                bttn.setBackground(main.getCOLOR_GREEN());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bttn.setForeground(main.getCOLOR_WHITE());
                bttn.setBackground(main.getCOLOR_BLUE());
            }
        });

    }

    /**
     * Agrega listeners a la etiqueta/botón 'Anterior' del campo del análisis sintáctico/semántico
     */
    private void addListenersBttnSemanticL(){
        JLabel bttn = main.getBttnSemanticL();

        bttn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if ( counterSemantic >= 28 ) {
                    counterSemantic = counterSemantic-28;
                    main.getTableSemantic().setModel(getUpdatedTableModel( main.getTableSemantic(),
                            analyzer.getSemantic(), counterSemantic, counterSemantic+28) );
                }
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bttn.setForeground(main.getCOLOR_BLACK());
                bttn.setBackground(main.getCOLOR_GREEN());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bttn.setForeground(main.getCOLOR_WHITE());
                bttn.setBackground(main.getCOLOR_BLUE());
            }
        });

    }
    

    private void addListenersBttnEncodingL(){
        JLabel bttn = main.getBttnEncodingL();

        bttn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if ( counterEncoding >= 28 ) {
                    counterEncoding = counterEncoding-28;
                    main.getTableEncoding().setModel(getUpdatedTableModel( main.getTableEncoding(),
                            analyzer.getEncoding(), counterEncoding, counterEncoding+28) );
                }
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bttn.setForeground(main.getCOLOR_BLACK());
                bttn.setBackground(main.getCOLOR_GREEN());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bttn.setForeground(main.getCOLOR_WHITE());
                bttn.setBackground(main.getCOLOR_BLUE());
            }
        });

    }

    /**
     * Agrega listeners a la etiqueta/botón 'Siguiente' del campo del análisis sintáctico/semántico
     */
    private void addListenersBttnSemanticR(){
        JLabel bttn = main.getBttnSemanticR();

        bttn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if ( counterSemantic <= analyzer.getSemantic().size()-28 ) {
                    counterSemantic = counterSemantic+28;
                    main.getTableSemantic().setModel(getUpdatedTableModel( main.getTableSemantic(),
                            analyzer.getSemantic(), counterSemantic, counterSemantic+28) );
                }
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bttn.setForeground(main.getCOLOR_BLACK());
                bttn.setBackground(main.getCOLOR_GREEN());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bttn.setForeground(main.getCOLOR_WHITE());
                bttn.setBackground(main.getCOLOR_BLUE());
            }
        });

    }

    private void addListenersBttnEncodingR(){
        JLabel bttn = main.getBttnEncodingR();

        bttn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if ( counterEncoding <= analyzer.getEncoding().size()-28 ) {
                    counterEncoding = counterEncoding+28;
                    main.getTableEncoding().setModel(getUpdatedTableModel( main.getTableEncoding(),
                            analyzer.getEncoding(), counterEncoding, counterEncoding+28) );
                }
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bttn.setForeground(main.getCOLOR_BLACK());
                bttn.setBackground(main.getCOLOR_GREEN());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bttn.setForeground(main.getCOLOR_WHITE());
                bttn.setBackground(main.getCOLOR_BLUE());
            }
        });

    }

    /**
     * Agrega listeners a la etiqueta/botón 'Anterior' del campo del símbolos
     */
    private void addListenersBttnSymbolsL(){
        JLabel bttn = main.getBttnSymbolsL();

        bttn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if ( counterSymbols >= 28 ) {
                    counterSymbols = counterSymbols-28;
                    main.getTableSymbols().setModel(getUpdatedTableModel( main.getTableSymbols(),
                            analyzer.getSymbols(), counterSymbols, counterSymbols+28) );
                }
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bttn.setForeground(main.getCOLOR_BLACK());
                bttn.setBackground(main.getCOLOR_GREEN());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bttn.setForeground(main.getCOLOR_WHITE());
                bttn.setBackground(main.getCOLOR_BLUE());
            }
        });

    }

    /**
     * Agrega listeners a la etiqueta/botón 'Siguiente' del campo de símbolos
     */
    private void addListenersBttnSymbolsR(){
        JLabel bttn = main.getBttnSymbolsR();

        bttn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if ( counterSymbols <= analyzer.getSymbols().size()-28 ) {
                    counterSymbols = counterSymbols+28;
                    main.getTableSymbols().setModel(getUpdatedTableModel( main.getTableSymbols(),
                            analyzer.getSymbols(), counterSymbols, counterSymbols+28) );
                }
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bttn.setForeground(main.getCOLOR_BLACK());
                bttn.setBackground(main.getCOLOR_GREEN());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bttn.setForeground(main.getCOLOR_WHITE());
                bttn.setBackground(main.getCOLOR_BLUE());
            }
        });

    }

    /**
     * Verifica que la extension del archivo seleccionado sea asm.
     * @param path String con la ruta (path) del archivo a verificar.
     * @return Booleano. TRUE si la ruta termina en .asm y FALSE en caso contrario.
     */
    private boolean isASMFile(String path){
        String[] s = path.split("");
        String p = (s[s.length-4]) + (s[s.length-3]) + (s[s.length-2]) + (s[s.length-1]);
        return p.compareTo(".asm") == 0;
    }

    /**
     * @param table    JTable: Tabla a la que se le actualizarán los renglones.
     * @param array    ArrayList de String[]: Arreglos desde el que se obtienen los renglones a mostrar
     * @param initial  int: indice del rango inicial de renglones a mostrar de la lista
     * @param finalRow int: indice del rango final de renglones a mostrar de la lista
     * @return TableModel: Modelo de la tabla con los valores de las celdas actualizados.
     */
    private TableModel getUpdatedTableModel(JTable table, ArrayList<String[]> array, int initial, int finalRow){
        String[] columnNames = main.getColumnNames( table.getModel() );
        String[][] rows = analyzer.getLines(array, initial, finalRow);
        TableModel model = new AbstractTableModel() {
            public int getColumnCount() {
                return columnNames.length;
            }
            public int getRowCount() {
                return finalRow-initial;
            }
            public Object getValueAt(int row, int col) {
                return rows[row][col];
            }
            public String getColumnName(int index) {
                return columnNames[index];
            }
        };
        return model;
    }

}
