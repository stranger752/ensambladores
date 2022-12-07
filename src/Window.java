/**
 * @author Escalera Jimenez Enrique
 * @author Sánchez Mendieta Jesús Alberto
 */

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;

public class Window {

    // Paleta de colores
    private final Color COLOR_WHITE = new Color(236, 239, 244);
    private final Color COLOR_BLACK = new Color( 46,  52,  64);
    private final Color COLOR_GRAY  = new Color( 76,  86, 106);
    private final Color COLOR_BLUE  = new Color( 94, 129, 172);
    private final Color COLOR_GREEN = new Color(143, 188, 187);

    // Fonts
    private final Font FONT_HEADER = loadFont("src/fonts/JetBrainsMono-Bold.ttf", 20f);
    private final Font FONT_BUTTON = loadFont("src/fonts/JetBrainsMono-Medium.ttf", 15f);
    private final Font FONT_TABLES = loadFont("src/fonts/JetBrainsMono-Regular.ttf", 15f);

    // Componentes del panel 1 (Fase 1. Análisis Lexicográfico)
    private final static String TAB_FASE_01 = "1. Análisis lexicográfico";
    private JPanel paneP1;
    private JLabel bttnFile, textPath, bttnSourceL, bttnSourceR, bttnLexicalL, bttnLexicalR;
    private JTable tableSource, tableLexical;

    // Componentes del panel 2 (Fase 2. Análisis Sintáctico y Semántico)
    private final static String TAB_FASE_02 = "2. Análisis sintáctico y semántico";
    private JPanel panePhase2;
    private JLabel bttnSemanticL, bttnSemanticR, bttnSymbolsL, bttnSymbolsR;
    private JTable tableSemantic, tableSymbols;

    // Componentes del panel 3 (Fase 3. Direcciones de símbolos y codificación de instrucciones)
    private final static String TAB_FASE_03 = "3. Direcciones de símbolos & codificación";
    private JPanel panePhase3;
    private JLabel bttnEncodingL, bttnEncodingR, bttnSymbolsL2, bttnSymbolsR2;
    private JTable tableEncoding, tableSymbols2;

    /**
     * Crea y agrega los paneles de cada fase, como Tabs (o pestañas), al panel principal. Usa 'cardLayout'.
     * @param frame Frame principal. En el panel de este frame se agregarán las pestañas o tabs
     */
    public Window(JFrame frame){
        JTabbedPane tabbedPane = new JTabbedPane();

        // se crea y agrega el primer panel (panel del analisis lexicografico)
        initPanePhase1();
        tabbedPane.addTab(TAB_FASE_01, paneP1);

        // se crea y agrega el segundo panel (panel del analisis sintactico & semantico)
        initPanePhase2();
        tabbedPane.addTab(TAB_FASE_02, panePhase2);

        // se crea y agrega el tercer panel (panel de direcciones y codificacion)
        initPanePhase3();
        tabbedPane.addTab(TAB_FASE_03, panePhase3);

        // se agregan los componentes al frame
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     *  Crea el panel del análisis lexicográfico e inicializa todos sus componentes.
     */
    private void initPanePhase1(){
        paneP1 = new JPanel();
        paneP1.setPreferredSize(new Dimension(1280,720));
        paneP1.setLayout(null);     // se usa posicionamiento absoluto.
        paneP1.setBackground(COLOR_BLACK);

        // Etiqueta: ENCABEZADO
        paneP1.add( initLabel("ENSAMBLADORES. PROYECTO: FASE 1.",
                0,0,1280,50, COLOR_GRAY, COLOR_WHITE) );
        // Etiqueta: CODIGO FUENTE
        paneP1.add( initLabel("Código Fuente",
                90, 150, 475, 40, COLOR_GRAY, COLOR_WHITE) );
        // Etiqueta: ANALISIS LEXICO
        paneP1.add( initLabel("Análisis Léxico",
                715, 150, 475, 40, COLOR_GRAY, COLOR_WHITE) );

        // Botón: SELECCIONAR ARCHIVO
        bttnFile = initButton("Seleccionar archivo",
                60, 90, 210, 30, COLOR_BLUE, COLOR_WHITE);
        paneP1.add(bttnFile);
        // Campo de texto: PATH DEL ARCHIVO
        textPath = initTextField("",
                300, 90, 920, 30, COLOR_WHITE, COLOR_BLACK);
        paneP1.add(textPath);

        // Botón: ANTERIOR (sección del campo de texto del código fuente)
        bttnSourceL = initButton("Anterior",
                197, 650, 100, 30, COLOR_BLUE, COLOR_WHITE);
        paneP1.add(bttnSourceL);
        // Botón: SIGUIENTE (sección del campo de texto del código fuente)
        bttnSourceR = initButton("Siguiente",
                357, 650, 100, 30, COLOR_BLUE, COLOR_WHITE);
        paneP1.add(bttnSourceR);
        // Botón: ANTERIOR (sección del campo de texto del análisis lexicográfico)
        bttnLexicalL = initButton("Anterior",
                822, 650, 100, 30, COLOR_BLUE, COLOR_WHITE);
        paneP1.add(bttnLexicalL);
        // Botón: SIGUIENTE (sección del campo de texto del análisis lexicográfico)
        bttnLexicalR = initButton("Siguiente",
                982, 650, 100, 30, COLOR_BLUE, COLOR_WHITE);
        paneP1.add(bttnLexicalR);

        // Tabla: CÓDIGO FUENTE
        tableSource = initTable(new String[]{"CÓDIGO FUENTE"}, 24);
        paneP1.add( initScroll(tableSource, 40, 210, 575, 420) );
        // Tabla: ANALISIS LEXICOGRAFICO
        tableLexical = initTable(new String[]{"ELEMENTO", "ANÁLISIS LÉXICO"}, 24);
        paneP1.add( initScroll(tableLexical, 665, 210, 575, 420) );
    }

    /**
     *  Crea el panel del análisis sintáctico y semántico, e inicializa todos sus componentes.
     */
    private void initPanePhase2(){
        panePhase2 = new JPanel();
        panePhase2.setPreferredSize(new Dimension(1280,720));
        panePhase2.setLayout(null);     // se usa posicionamiento absoluto.
        panePhase2.setBackground(COLOR_BLACK);

        // Etiqueta: ENCABEZADO
        panePhase2.add( initLabel("ENSAMBLADORES. PROYECTO: FASE 2.",
                0,0,1280,50, COLOR_GRAY, COLOR_WHITE) );
        // Etiqueta: ANALISIS SINTACTICO & SEMANTICO
        panePhase2.add( initLabel("Análisis sintáctico & semántico",
                90, 90, 475, 40, COLOR_GRAY, COLOR_WHITE) );
        // Etiqueta: TABLA DE SIMBOLOS
        panePhase2.add( initLabel("Tabla de símbolos",
                715, 90, 475, 40, COLOR_GRAY, COLOR_WHITE) );

        // Botón: ANTERIOR (seccion de analisis sintactico & semantico)
        bttnSemanticL = initButton("Anterior",
                197, 650, 100, 30, COLOR_BLUE, COLOR_WHITE);
        panePhase2.add(bttnSemanticL);
        // Botón: SIGUIENTE (seccion de analisis sintactico & semantico)
        bttnSemanticR = initButton("Siguiente",
                357, 650, 100, 30, COLOR_BLUE, COLOR_WHITE);
        panePhase2.add(bttnSemanticR);
        // Botón: ANTERIOR (sección de tabla de simbolos)
        bttnSymbolsL = initButton("Anterior",
                822, 650, 100, 30, COLOR_BLUE, COLOR_WHITE);
        panePhase2.add(bttnSymbolsL);
        // Botón: SIGUIENTE (sección de tabla de simbolos)
        bttnSymbolsR = initButton("Siguiente",
                982, 650, 100, 30, COLOR_BLUE, COLOR_WHITE);
        panePhase2.add(bttnSymbolsR);

        // Tabla: ANALISIS SINTACTICO & SEMANTICO
        tableSemantic = initTable(new String[]{"LÍNEA DE CÓDIGO", "ANÁLISIS"}, 28);
        panePhase2.add( initScroll(tableSemantic, 40, 150, 575, 480) );
        // Tabla: SIMBOLOS
        tableSymbols = initTable(new String[]{"SÍMBOLO", "TIPO", "VALOR", "TAMAÑO"}, 28);
        panePhase2.add( initScroll(tableSymbols, 665, 150, 575,  480) );
    }

    /**
     *  Crea el panel para las direcciones y codificación, e inicializa todos sus componentes.
     */
    private void initPanePhase3(){
        panePhase3 = new JPanel();
        panePhase3.setPreferredSize(new Dimension(1280,720));
        panePhase3.setLayout(null);     // se usa posicionamiento absoluto.
        panePhase3.setBackground(COLOR_BLACK);

        // Etiqueta: ENCABEZADO
        panePhase3.add( initLabel("ENSAMBLADORES. PROYECTO: FASE 3.",
                0,0,1280,50, COLOR_GRAY, COLOR_WHITE) );
        // Etiqueta: CP & CODIFICACION
        panePhase3.add( initLabel("Codificación de instrucciones",
                90, 90, 475, 40, COLOR_GRAY, COLOR_WHITE) );
        // Etiqueta: TABLA DE SIMBOLOS
        panePhase3.add( initLabel("Tabla de símbolos",
                715, 90, 475, 40, COLOR_GRAY, COLOR_WHITE) );

        // Botón: ANTERIOR (sección de codificación de instrucciones)
        bttnEncodingL = initButton("Anterior",
                197, 650, 100, 30, COLOR_BLUE, COLOR_WHITE);
        panePhase3.add(bttnEncodingL);
        // Botón: SIGUIENTE (sección de codificación de instrucciones)
        bttnEncodingR = initButton("Siguiente",
                357, 650, 100, 30, COLOR_BLUE, COLOR_WHITE);
        panePhase3.add(bttnEncodingR);
        // Botón: ANTERIOR (sección de tabla de símbolos)
        bttnSymbolsL2 = initButton("Anterior",
                822, 650, 100, 30, COLOR_BLUE, COLOR_WHITE);
        panePhase3.add(bttnSymbolsL2);
        // Botón: SIGUIENTE (sección de tabla de símbolos)
        bttnSymbolsR2 = initButton("Siguiente",
                982, 650, 100, 30, COLOR_BLUE, COLOR_WHITE);
        panePhase3.add(bttnSymbolsR2);

        // Tabla: CODIFICACION
        tableEncoding = initTable(new String[]{"CP","LÍNEA DE CÓDIGO", "CODIFICACIÓN"}, 28);
        //tableEncoding.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //tableEncoding.getColumnModel().getColumn(0).setPreferredWidth(100);
        //tableEncoding.getColumnModel().getColumn(1).setPreferredWidth(271);
        //tableEncoding.getColumnModel().getColumn(2).setPreferredWidth(200);
        panePhase3.add( initScroll(tableEncoding, 40, 150, 575, 480) );
        // Tabla: SIMBOLOS
        tableSymbols2 = initTable(new String[]{"SÍMBOLO", "TIPO", "VALOR", "TAMAÑO", "DIRECCIÓN"}, 28);
        panePhase3.add( initScroll(tableSymbols2, 665, 150, 575, 480) );
    }

    /**
     * Crea etiqueta ('JLabel') con los parámetros especificados.
     *
     * @param text String: texto que aparecerá en la etiqueta
     * @param x Int: posición en el eje x (en pixeles)
     * @param y Int: posición en el eje y (en pixeles)
     * @param width Int: ancho de la etiqueta (en pixeles)
     * @param height Int: altura de la etiqueta (en pixeles)
     * @param colorBG Color: Color de la etiqueta
     * @param colorFG Color: Color del texto
     * @return 'JLabel' creado.
     */
    private JLabel initLabel(String text, int x, int y, int width, int height, Color colorBG, Color colorFG){
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setBounds(x, y, width, height);
        label.setFont(FONT_HEADER);
        label.setOpaque(true);
        label.setBackground(colorBG);
        label.setForeground(colorFG);
        return label;
    }

    /**
     * Crea etiqueta ('JLabel'), que se usará como botón, usando los parámetros especificados.
     *
     * @param text String: texto que aparecerá en el botón
     * @param x Int: posición en el eje x (en pixeles)
     * @param y Int: posición en el eje y (en pixeles)
     * @param width Int: ancho del botón (en pixeles)
     * @param height Int: altura del botón (en pixeles)
     * @param colorBG Color: Color del botón
     * @param colorFG Color: Color del texto
     * @return 'JLabel' creado.
     */
    private JLabel initButton(String text, int x, int y, int width, int height, Color colorBG, Color colorFG){
        JLabel button = new JLabel(text, SwingConstants.CENTER);
        button.setBounds(x, y, width, height);
        button.setFont(FONT_BUTTON);
        button.setOpaque(true);
        button.setBackground(colorBG);
        button.setForeground(colorFG);
        return button;
    }

    /**
     * Crea etiqueta ('JLabel'), que se usará como campo de texto, usando los parámetros especificados.
     *
     * @param text String: texto que aparecerá en el campo de texto
     * @param x Int: posición en el eje x (en pixeles)
     * @param y Int: posición en el eje y (en pixeles)
     * @param width Int: ancho del campo de texto (en pixeles)
     * @param height Int: altura del campo de texto (en pixeles)
     * @param colorBG Color: Color del fondo del campo de texto
     * @param colorFG Color: Color del texto
     * @return 'JLabel' creado.
     */
    private JLabel initTextField(String text, int x, int y, int width, int height, Color colorBG, Color colorFG){
        JLabel textField = new JLabel(text);
        textField.setBounds(x, y, width, height);
        textField.setFont(FONT_BUTTON);
        textField.setOpaque(true);
        textField.setBackground(colorBG);
        textField.setForeground(colorFG);
        return textField;
    }

    /**
     * Inicializa tabla ('JTable') con su encabezado ('JTableHeader') y con Listener 'tooltip'
     * (al colocar el mouse sobre una celda, se muestra el contenido usando un cuadro flotante).
     * @param columnNames String[]: arreglo con los encabezados para cada columna de la tabla.
     * @return JTable: tabla creada.
     */
    private JTable initTable(String[] columnNames, int rows){
        // definición del modelo de la tabla y creación
        TableModel dataModel = new AbstractTableModel() {
            public int getColumnCount() { return columnNames.length; }
            public int getRowCount() { return rows;}
            public Object getValueAt(int row, int col) { return ""; }
            public String getColumnName(int index) { return columnNames[index]; }
        };
        JTable table = new JTable(dataModel){
            //
            public String getToolTipText( MouseEvent e ) {
                int row = rowAtPoint( e.getPoint() );
                int column = columnAtPoint( e.getPoint() );
                Object value = getValueAt(row, column);
                return value == null ? null : value.toString();
            }
        };
        table.setFont(FONT_TABLES);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(FONT_TABLES);
        return table;
    }

    /**
     * Inicializa 'JScrollPane' que contiene a la tabla ('JTable') dada como parámetro.
     *
     * @param table JTable: tabla que estará contenida en el 'JScrollPane'
     * @param x Int: posición en el eje x (en pixeles)
     * @param y Int: posición en el eje y (en pixeles)
     * @param width Int: ancho del scrollPane (en pixeles)
     * @param height Int: altura del scrollPane (en pixeles)
     * @return 'JScrollPane' creado.
     */
    private JScrollPane initScroll(JTable table, int x, int y, int width, int height){
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(x, y, width, height);
        sp.setBorder(new LineBorder(COLOR_BLUE, 2, false));
        return sp;
    }

    /**
     * Carga fuente TTF (archivo TTF ubicado en el path dado) y la almacena en un objeto 'Font'.
     * Precondición: El path dado como parámetro tiene el formato adecuado para la navegación dentro del S.O.
     *
     * @param path String: Path hacia el archivo TTF.
     * @param size Int: Tamaño que tendrá la 'Font' creada.
     * @return Font: fuente creada o null si el archivo TTF no se encuentra en el Path especificado.
     */
    private Font loadFont(String path, float size) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(size);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No se encontro archivo .ttf");
            return null;
        }
    }

    /**
     * @param model TableModel: Modelo de la tabla de la cual se obtendrán los nombres de las columnas.
     * @return String[] que contiene los nombres de las columnas de una Tabla.
     */
    public String[] getColumnNames(TableModel model) {
        int size = model.getColumnCount();
        String[] columnNames = new String[size];
        for (int i = 0; i < size; i++) {
            columnNames[i] = model.getColumnName(i);
        }
        return columnNames;
    }


    public Color getCOLOR_WHITE() {
        return COLOR_WHITE;
    }

    public Color getCOLOR_BLACK() {
        return COLOR_BLACK;
    }

    public Color getCOLOR_GRAY() {
        return COLOR_GRAY;
    }

    public Color getCOLOR_BLUE() {
        return COLOR_BLUE;
    }

    public Color getCOLOR_GREEN() {
        return COLOR_GREEN;
    }

    public Font getFONT_HEADER() {
        return FONT_HEADER;
    }

    public Font getFONT_BUTTON() {
        return FONT_BUTTON;
    }

    public Font getFONT_TABLES() {
        return FONT_TABLES;
    }

    public JLabel getBttnFile() {
        return bttnFile;
    }

    public JLabel getTextPath() {
        return textPath;
    }

    public JLabel getBttnSourceL() {
        return bttnSourceL;
    }

    public JLabel getBttnSourceR() {
        return bttnSourceR;
    }

    public JLabel getBttnLexicalL() {
        return bttnLexicalL;
    }

    public JLabel getBttnLexicalR() {
        return bttnLexicalR;
    }

    public JTable getTableSource() {
        return tableSource;
    }

    public JTable getTableLexical() {
        return tableLexical;
    }

    public JLabel getBttnSemanticL() {
        return bttnSemanticL;
    }

    public JLabel getBttnSemanticR() {
        return bttnSemanticR;
    }

    public JLabel getBttnSymbolsL() {
        return bttnSymbolsL;
    }

    public JLabel getBttnSymbolsR() {
        return bttnSymbolsR;
    }

    public JTable getTableSemantic() {
        return tableSemantic;
    }

    public JTable getTableSymbols() {
        return tableSymbols;
    }

    public JLabel getBttnEncodingL() {
        return bttnEncodingL;
    }

    public JLabel getBttnEncodingR() {
        return bttnEncodingR;
    }

    public JLabel getBttnSymbolsL2() {
        return bttnSymbolsL2;
    }

    public JLabel getBttnSymbolsR2() {
        return bttnSymbolsR2;
    }

    public JTable getTableEncoding() {
        return tableEncoding;
    }

    public JTable getTableSymbols2() {
        return tableSymbols2;
    }
}

