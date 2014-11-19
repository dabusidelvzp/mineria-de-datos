/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package regresiones;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hazel
 */
public class MineriaTexto {
    private ArrayList<Palabra> palabras;
    private String[] preposiciones = {"ANTE", "BAJO","DESDE", "DURANTE","ENTRE", "EXCEPTO", "HACIA", "HASTA", "MEDIANTE", "PARA","SALVO", "SEGUN", "SOBRE","TRAS"};
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
        JLabel titulo = new JLabel("Resultados");//creamos el titulo
        resultados.add(titulo,BorderLayout.PAGE_START);//lo agregamos al inicio
        JTable jtable = new JTable();//creamos la tabla a mostrar
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
        DefaultTableModel TableModel = new DefaultTableModel( arregloFinal, titulos );
        jtable.setModel(TableModel); 
        JScrollPane jScrollPane1 = new JScrollPane();
        jScrollPane1.setViewportView(jtable);
        jtable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
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
    
}
