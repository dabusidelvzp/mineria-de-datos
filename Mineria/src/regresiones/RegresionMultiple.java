/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package regresiones;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author hazel
 */
public class RegresionMultiple {
    private Integer N;
    private Double b0;
    private Double b1;
    private Double b2;
    private Double Se;
    private Double[][] datos;
    private Double[][] auxiliar;
    private Double[] sumatorias;
    
    public RegresionMultiple(Double[][] d){
        N=0;
        b0=0.0;
        b1 = 0.0;
        b2 = 0.0;
        Se = 0.0;
        datos=d;
        sumatorias= new Double[9];
    }
    @SuppressWarnings("empty-statement")
    public void resolver(JTabbedPane resultados){
        N = datos.length;
        auxiliar = new Double[N][6];
        //inicializamos los arreglos
        inicializarArreglos();
        //llenamos nuestro arreglo auxiliar donde va x1^2,x2^2,x1*y,x2*y,x1*x2,Y-Y(estimada)
        for(int i=0;i<N;i++){
            auxiliar[i][0]=Math.pow(datos[i][0], 2);
            auxiliar[i][1]=Math.pow(datos[i][1], 2);
            auxiliar[i][2]= datos[i][0] * datos[i][2];
            auxiliar[i][3]= datos[i][1] * datos[i][2];
            auxiliar[i][4]= datos[i][0] * datos[i][1];
            sumatorias[0] += datos[i][0]; //sumatoria de x1
            sumatorias[1] += datos[i][1]; //sumatoria de x2
            sumatorias[2] += datos[i][2];  // sumatoria de y
        }
        //llenamos el arreglo de las sumatorias
       for(int i=0;i<5;i++) {
           for(int j=0;j<N;j++) {
               sumatorias[i+3] += auxiliar[j][i];
           }
       }
       //creamos el arreglo para obtener las determinantes
       Double[][] matrizDeterminantes = {{N+0.0,sumatorias[0],sumatorias[1]},
                                        {sumatorias[0],sumatorias[3],sumatorias[7]},
                                        {sumatorias[1],sumatorias[7],sumatorias[4]}};
       Double detGeneral= calDeterminante(matrizDeterminantes); //sacamos la determinante general
       Double[][] matrizX = matrizDeterminantes;
       
       
       //modificamos valores
       matrizX[0][0]=sumatorias[2];
       matrizX[1][0]=sumatorias[5];
       matrizX[2][0]=sumatorias[6];
       Double detX=calDeterminante(matrizX);//obtenemos la determinante de X
       //modificamos valores
       Double[][] matrizX2 =    {{N+0.0         ,sumatorias[2], sumatorias[1]},
                                {sumatorias[0]  ,sumatorias[5], sumatorias[7]},
                                {sumatorias[1]  ,sumatorias[6], sumatorias[4]}};
       Double detX2=calDeterminante(matrizX2);//obtenemos a determinante de X2
       //modificamos valores
       Double[][] matrizY = {{N+0.0,sumatorias[0],sumatorias[2]},
                                        {sumatorias[0],sumatorias[3],sumatorias[5]},
                                        {sumatorias[1],sumatorias[7],sumatorias[6]}};
       Double detY=calDeterminante(matrizY);//obtenemos la determinante de Y
       //calculamos b0,b1,b2
       b0= detX/detGeneral; 
       b1= detX2/detGeneral; 
       b2= detY/detGeneral; 
       System.out.println("b0: "+b0+", b1: "+b1+", b2: "+b2);
       //calculamos las Y estimadas y su sumatorias
       Double[] Yestimada=new Double[N];
       for(int i=0;i<N;i++) {
          Yestimada[i]=b0+b1*datos[i][0]+b2*datos[i][1];
          auxiliar[i][5]=datos[i][2]-Yestimada[i];
          sumatorias[8]+=auxiliar[i][5];
       }
       //calculamos Se
       Se =Math.sqrt(Math.pow(sumatorias[8], 2)/(N-2-1));
        //System.out.println("Se: "+Se);
       // mostramos resultados para la pestaña resultados***********************************************************************
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.white);
        JLabel titulo = new JLabel("Resultados");//creamos el titulo
        panel.add(titulo,BorderLayout.PAGE_START);//lo agregamos al inicio
        JTable jtable = new JTable();//creamos la tabla a mostrar
        jtable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 0), 2, true));
        jtable.setFont(new java.awt.Font("Arial", 1, 14)); 
        jtable.setColumnSelectionAllowed(true);
        jtable.setCursor(new java.awt.Cursor(java.awt.Cursor.N_RESIZE_CURSOR));
        jtable.setInheritsPopupMenu(true);
        jtable.setMinimumSize(new java.awt.Dimension(80, 80));
        String[] titulos = {"X1","X2","Y","Y estimada"};//los titulos de la tabla
        Double[][] arregloFinal = new Double[N][4];
        for(int i=0;i<N;i++){//armamos el arreglo
            arregloFinal[i][0]= datos[i][0];
            arregloFinal[i][1]= datos[i][1];
            arregloFinal[i][2]= datos[i][2];
            arregloFinal[i][3]= Yestimada[i];
        }
        DefaultTableModel TableModel = new DefaultTableModel( arregloFinal, titulos );
        jtable.setModel(TableModel); 
        JScrollPane jScrollPane1 = new JScrollPane();
        jScrollPane1.setViewportView(jtable);
        jtable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        panel.add(jScrollPane1,BorderLayout.CENTER);
        JPanel panel2 = new JPanel(new GridLayout(0,4));//creo un panel con rejilla de 4 columnas
        JLabel etiquetaN = new JLabel("N");
        JTextField cajaN = new JTextField();
        cajaN.setText(N+"");
        JLabel etiquetaK = new JLabel("K");
        JTextField cajaK = new JTextField();
        cajaK.setText("2");
        JLabel etiquetab0 = new JLabel("b0");
        JTextField cajab0 = new JTextField();
        cajab0.setText(b0+"");
        JLabel etiquetab1 = new JLabel("b1");
        JTextField cajab1 = new JTextField();
        cajab1.setText(b1+"");
        JLabel etiquetab2 = new JLabel("b2");
        JTextField cajab2 = new JTextField();
        cajab2.setText(b2+"");
        JLabel etiquetaSe = new JLabel("Se");
        JTextField cajaSe = new JTextField();
        cajaSe.setText(Se+"");
        panel2.add(etiquetaN);
        panel2.add(cajaN);
        panel2.add(etiquetaK);
        panel2.add(cajaK);
        panel2.add(etiquetab2);
        panel2.add(etiquetab0);
        panel2.add(cajab0);
        panel2.add(etiquetab1);
        panel2.add(cajab1);
        panel2.add(etiquetab2);
        panel2.add(cajab2); 
        panel2.add(etiquetaSe);
        panel2.add(cajaSe);
        panel.add(panel2,BorderLayout.SOUTH);//agrego el panel2 con rejilla en el panel principal al sur
        resultados.addTab("resultado", panel);
    }

    private void inicializarArreglos() {
        for(int i=0;i<9;i++)
            sumatorias[i]=0.0;
        for(int i=0;i<N;i++){
            for(int j=0;j<6;j++){
                auxiliar[i][j]=0.0;
            }
        }
    }
    
    public Double calDeterminante (Double[][] matriz1) {
        Double determinante = ((matriz1[0][0] * matriz1[1][1] * matriz1[2][2]) + 
                (matriz1[0][1] * matriz1[1][2] * matriz1[2][0]) + 
                (matriz1[0][2] * matriz1[1][0] * matriz1[2][1]) )
                - ( (matriz1[0][1] * matriz1[1][0] * matriz1[2][2]) + (matriz1[0][0] * matriz1[1][2] * matriz1[2][1])
                + (matriz1[0][2] * matriz1[1][1] * matriz1[2][0]) );
        return determinante;
    }

}
