/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package regresiones;

import com.itextpdf.text.DocumentException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import pdf.PDFtexto;

/**
 *
 * @author hazel
 */
public class MineriaTexto implements ActionListener{
    private ArrayList<Palabra> palabras;
    private JTable jtable;
    private String[][] auxiliar;
    private String[] preposiciones = {"ANTE", "BAJO","DESDE", 
        "DURANTE","ENTRE", "EXCEPTO", "HACIA", "HASTA", "MEDIANTE", 
        "PARA","SALVO", "SEGUN", "SOBRE","TRAS",
        "PUEDE","COMO","PUEDEN","SOLO","HACER","CUAL","TIENE","DONDE","OTRO","OTRA",
        "OTROS","OTRAS","HECHO","CUALES","PERO","TODOS","TODAS","ESTA","ESTO","ESTOS","ESTAS"};
    public MineriaTexto() {
        palabras = new ArrayList<Palabra>();
    }
    
    public void resolver(String[][] texto,JTabbedPane panel) {
        String[] linea;
        for (int i = 0; i < texto.length; i++) {
            for (int j = 0; j < texto[0].length; j++) {
                //dividimos la linea en palabras
                linea = dividirLinea(texto[i][j]);
                //iteramos las palabras
                for(String p:linea){
                    if(existe(p))
                        agregar(p);
                    else
                        palabras.add(new Palabra(p,1));
                }   
            }  
        }
       Collections.sort(palabras, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Palabra p1= (Palabra) o1;
                Palabra p2 = (Palabra) o2;
                return p2.getRepetido().compareTo(p1.getRepetido());
            }
        });
        pintar(panel);
    }

    private String[] dividirLinea(String string) {
        string = string.replace(".", " ");
        string = string.replace(",", " ");
        string = string.replace("(", " ");
        string = string.replace(")", " ");
        String[] pa = string.split(" ");
        ArrayList<String> depurado = new ArrayList<String>();
        for(String palabra:pa){
            if(palabra.length()>3){
                if( !esPreposicion(palabra))
                    depurado.add(palabra);
            }
        }
        String[] arregloNuevo= depurado.toArray(new String[depurado.size()]);
        return arregloNuevo;
    }

    private boolean existe(String p) {
        boolean bandera= false;
        for (int i = 0; i < palabras.size(); i++) {
            if(palabras.get(i).getPalabra().equals(p))
                bandera=true;
        }
        return bandera;
    }

    private void agregar(String p) {
        for (int i = 0; i < palabras.size(); i++) {
            if(palabras.get(i).getPalabra().equals(p))
                palabras.get(i).setRepetido(palabras.get(i).getRepetido()+1);
        }
       
    }

    private void pintar(JTabbedPane panel) {
        JPanel resultados = new JPanel(new BorderLayout());
        //JLabel titulo = new JLabel("Resultados");//creamos el titulo
        //resultados.add(titulo,BorderLayout.PAGE_START);//lo agregamos al inicio
        //Creamos el select para los rangos
        JPanel opcionesCombo = new JPanel(new GridLayout(0,2));
        JLabel labelOpciones = new JLabel("Mostrar a partir de: ");
        JComboBox comboOp = new JComboBox(repeticiones());
        //agregamos el listener al combo
        comboOp.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
               // The item affected by the event.
                Object item = e.getItem();
                
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    //JOptionPane.showMessageDialog(null, item.toString()+" seleccionado");
                    String[] titulos = {"Palabra","Repetido"};//los titulos de la tabla
                    auxiliar = redimencionarArreglo(Integer.parseInt(item.toString()));
                    DefaultTableModel TableModel = new DefaultTableModel( auxiliar, titulos );
                    jtable.setModel(TableModel); 
                }
                 
            }
        });
        JButton botonI = new JButton("Exportar a PDF");
        botonI.addActionListener(this);
        opcionesCombo.add(labelOpciones);
        opcionesCombo.add(comboOp);
        opcionesCombo.add(botonI);
        //Creamos la tabla
        jtable = new JTable();//creamos la tabla a mostrar
        jtable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 0), 2, true));
        jtable.setFont(new java.awt.Font("Arial", 1, 14)); 
        jtable.setColumnSelectionAllowed(true);
        jtable.setCursor(new java.awt.Cursor(java.awt.Cursor.N_RESIZE_CURSOR));
        jtable.setInheritsPopupMenu(true);
        jtable.setMinimumSize(new java.awt.Dimension(80, 80));
        String[] titulos = {"Palabra","Repetido"};//los titulos de la tabla
        String[][] arregloFinal = new String[palabras.size()][2];
        for(int i=0;i<arregloFinal.length;i++){//armamos el arreglo
            arregloFinal[i][0]= palabras.get(i).getPalabra();
            arregloFinal[i][1]= palabras.get(i).getRepetido()+"";
        }
        auxiliar = arregloFinal;
        DefaultTableModel TableModel = new DefaultTableModel( arregloFinal, titulos );
        jtable.setModel(TableModel); 
        JScrollPane jScrollPane1 = new JScrollPane();
        jScrollPane1.setViewportView(jtable);
        jtable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        //agregamos los paneles
        resultados.add(opcionesCombo,BorderLayout.NORTH);
        resultados.add(jScrollPane1,BorderLayout.CENTER);
        panel.addTab("Resultados", resultados);
    }

    private boolean esPreposicion(String palabra) {
        boolean bandera = false;
        for (int i = 0; i < preposiciones.length; i++) {
            if(palabra.equals(preposiciones[i]))
                bandera= true;
        }
        return bandera;
    }
    
    private Integer[] repeticiones(){
        ArrayList<Integer> rep = new ArrayList<Integer>();
        Boolean bandera= true;
        for(int i=0;i<palabras.size();i++){
            bandera = true;
            for(int j=0;j<rep.size();j++){
                if(rep.get(j)==palabras.get(i).getRepetido())
                    bandera = false;
            }
            if(bandera)
                rep.add(palabras.get(i).getRepetido());
        }
        Collections.sort(rep, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Integer p1= (Integer) o1;
                Integer p2 = (Integer) o2;
                return p1.compareTo(p2);
            }
        });
        return rep.toArray(new Integer[rep.size()]);
    }
    
    private String[][] redimencionarArreglo(Integer n){
        Integer contador =0;
        for (int i = 0; i <palabras.size() ; i++) {
            if(palabras.get(i).getRepetido()>=n)
                contador ++;
        }
        String[][] arrTemporal = new String[contador][2];
        contador=0;
        for (int i = 0; i <palabras.size() ; i++) {
            if(palabras.get(i).getRepetido()>=n){
                arrTemporal[contador][0] = palabras.get(i).getPalabra();
                arrTemporal[contador][1] = palabras.get(i).getRepetido()+"";
                contador++;
            }
                
        }
        return arrTemporal;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if(e.getActionCommand().equals("Exportar a PDF")){

                    String nombrePDF = JOptionPane.showInputDialog("Escribe el nombre del PDF (sin extension)");
                    PDFtexto.crearPDF("Mineria de Texto", auxiliar, nombrePDF);
                    JOptionPane.showMessageDialog(jtable, "Se creo el PDF");

            }
        } catch (FileNotFoundException ex) {
                Logger.getLogger(MineriaTexto.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DocumentException ex) {
                Logger.getLogger(MineriaTexto.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
}
