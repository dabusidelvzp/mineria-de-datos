/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package regresiones;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
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
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.apache.commons.math3.distribution.TDistribution;
/**
 *
 * @author hazel
 */
public class RegresionMultiple implements ActionListener{
    private Integer N;
    private Double b0;
    private Double b1;
    private Double b2;
    private Double Se;
    private Double[][] datos;
    private JTextField cajaVariableX1;
    private JTextField cajaVariableX2;
    private JButton boton;
    private JComboBox combo;
    JTable jtable2;
    String[][] arregloFinal;
    JTable jtable;
    
    public RegresionMultiple(Double[][] d){
        N=0;
        b0=0.0;
        b1 = 0.0;
        b2 = 0.0;
        Se = 0.0;
        datos=d;
    }
    @SuppressWarnings("empty-statement")
    public void resolver(JTabbedPane resultados){
        try {
        N = datos.length;
       Double[] sumatorias = new Double[9];
       Double[][] auxiliar = new Double[N][6];
        //inicializamos los arreglos
        for(int i=0;i<9;i++)
            sumatorias[i]=0.0;
        for(int i=0;i<N;i++){
            for(int j=0;j<6;j++){
                auxiliar[i][j]=0.0;
            }
        }
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
        pintar(Yestimada,resultados,auxiliar);
        }catch(Exception e) {
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

    private void pintar(Double[] Yestimada,JTabbedPane resultados,Double[][] auxiliar) {
         // mostramos resultados para la pestaña resultados***********************************************************************
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.white);
        JLabel titulo = new JLabel("Resultados");//creamos el titulo
        panel.add(titulo,BorderLayout.PAGE_START);//lo agregamos al inicio
        jtable = new JTable();//creamos la tabla a mostrar
        jtable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 0), 2, true));
        jtable.setFont(new java.awt.Font("Arial", 1, 14)); 
        jtable.setColumnSelectionAllowed(true);
        jtable.setCursor(new java.awt.Cursor(java.awt.Cursor.N_RESIZE_CURSOR));
        jtable.setInheritsPopupMenu(true);
        jtable.setMinimumSize(new java.awt.Dimension(80, 80));
        String[] titulos = {"X1","X2","Y","Y estimada","X1^2","X2^2","X1*Y","X2*Y","Y-Y estimada"};//los titulos de la tabla
        arregloFinal = new String[N][9];
        DecimalFormat formato = new DecimalFormat("0.000");
        for(int i=0;i<N;i++){//armamos el arreglo
            arregloFinal[i][0]= datos[i][0]+"";
            arregloFinal[i][1]= datos[i][1]+"";
            arregloFinal[i][2]= datos[i][2]+"";
            arregloFinal[i][3]= formato.format(Yestimada[i]);
            arregloFinal[i][4] = formato.format(auxiliar[i][0]);
            arregloFinal[i][5] = formato.format(auxiliar[i][1]);
            arregloFinal[i][6] = formato.format(auxiliar[i][2]);
            arregloFinal[i][7] = formato.format(auxiliar[i][3]);
            arregloFinal[i][8] = formato.format(auxiliar[i][4]);
        }
        DefaultTableModel TableModel = new DefaultTableModel( arregloFinal, titulos );
        jtable.setModel(TableModel); 
        JScrollPane jScrollPane1 = new JScrollPane();
        jScrollPane1.setViewportView(jtable);
        jtable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        panel.add(jScrollPane1,BorderLayout.CENTER);
        JPanel panel2 = new JPanel(new GridLayout(0,6));//creo un panel con rejilla de 4 columnas
        JLabel etiquetaN = new JLabel("N");
        JTextField cajaN = new JTextField();
        cajaN.setText(N+"");
        cajaN.setEditable(false);
        
        JLabel etiquetaK = new JLabel("K");
        JTextField cajaK = new JTextField();
        cajaK.setText("2");
        cajaK.setEditable(false);
        
        JLabel etiquetab0 = new JLabel("b0");
        JTextField cajab0 = new JTextField();
        cajab0.setText(formato.format(b0)+"");
        cajab0.setEditable(false);
        
        JLabel etiquetab1 = new JLabel("b1");
        JTextField cajab1 = new JTextField();
        cajab1.setText(formato.format(b1)+"");
        cajab1.setEditable(false);
        
        JLabel etiquetab2 = new JLabel("b2");
        JTextField cajab2 = new JTextField();
        cajab2.setText(formato.format(b2)+"");
        cajab2.setEditable(false);
        
        JLabel etiquetaSe = new JLabel("Se");
        JTextField cajaSe = new JTextField();
        cajaSe.setText(Se+"");
        cajaSe.setEditable(false);
        cajaSe.setAutoscrolls(true);
        
        JButton botonI = new JButton("imprimir");
        botonI.addActionListener(this);
        
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
        panel2.add(botonI);
        panel.add(panel2,BorderLayout.SOUTH);//agrego el panel2 con rejilla en el panel principal al sur
        resultados.addTab("resultado", panel);
        //**************************************************************************************
        //intervalos de confianza
        JPanel intervalos= new JPanel(new BorderLayout());
        JPanel variables = new JPanel(new GridLayout(0,2));
        JLabel variableX1 = new JLabel("X1");
        cajaVariableX1 = new JTextField();
        JLabel variableX2 = new JLabel("X2");
        cajaVariableX2 = new JTextField();
        boton = new JButton("calcular");
        boton.addActionListener(this);
        JLabel variableEfectividad = new JLabel("Efectividad");
        String[] efectividades = {"80","85","90","95","99"};
        combo = new JComboBox(efectividades);
        variables.add(variableX1);
        variables.add(cajaVariableX1);
        variables.add(variableX2);
        variables.add(cajaVariableX2);
        variables.add(variableEfectividad);
        variables.add(combo);
        variables.add(boton);
        intervalos.add(variables, BorderLayout.NORTH);
        jtable2 = new JTable();//creamos la tabla a mostrar
        jtable2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 0), 2, true));
        jtable2.setFont(new java.awt.Font("Arial", 1, 14)); 
        jtable2.setColumnSelectionAllowed(true);
        jtable2.setCursor(new java.awt.Cursor(java.awt.Cursor.N_RESIZE_CURSOR));
        jtable2.setInheritsPopupMenu(true);
        jtable2.setMinimumSize(new java.awt.Dimension(80, 80));
        String[] titulos2 = {"Y estimada","Li","Ls"};//los titulos de la tabla
        String[][] pruebaIntervalos = {{"","",""}};
        DefaultTableModel TableModel2 = new DefaultTableModel( pruebaIntervalos, titulos2 );
        jtable2.setModel(TableModel2); 
        JScrollPane jScrollPane2 = new JScrollPane();
        jScrollPane2.setViewportView(jtable2);
        jtable2.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        intervalos.add(jScrollPane2,BorderLayout.CENTER);
        resultados.addTab("intervalos", intervalos);
        //***************************************************************************
        JPanel graficas = new JPanel(new GridLayout(0,1));
        XYDataset dataset = createSampleDataset(Yestimada,1);
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Grafica 1 - X1",
            "X",
            "Y",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            false,
            false
        );
        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, true);
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesLinesVisible(1, true);
        renderer.setSeriesShapesVisible(1, true);        
        plot.setRenderer(renderer);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 300));
        graficas.add(chartPanel);//agregamos la primer grafica
        //********** creamos la segunda grafica
        XYDataset dataset2 = createSampleDataset(Yestimada,2);
        JFreeChart chart2 = ChartFactory.createXYLineChart(
            "Grafica 2 -X2",
            "X",
            "Y",
            dataset2,
            PlotOrientation.VERTICAL,
            true,
            false,
            false
        );
        XYPlot plot2 = (XYPlot) chart2.getPlot();
        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
        renderer2.setSeriesLinesVisible(0, true);
        renderer2.setSeriesShapesVisible(0, true);
        renderer2.setSeriesLinesVisible(1, true);
        renderer2.setSeriesShapesVisible(1, true);        
        plot2.setRenderer(renderer2);
        final ChartPanel chartPanel2 = new ChartPanel(chart2);
        chartPanel2.setPreferredSize(new java.awt.Dimension(500, 300));
        graficas.add(chartPanel2);
        resultados.addTab("graficas", graficas);
    }
    private XYDataset createSampleDataset(Double[] estimada,Integer opcion) {
        XYSeries series1 = new XYSeries("X - Y");
        for (Double[] dato : datos) {
            if (opcion.equals(1)) {
                series1.add(dato[0], dato[2]);
            } else {
                series1.add(dato[1], dato[2]);
            }
        }
        
        XYSeries series2 = new XYSeries("X - Y estimada");
        for(int i=0;i<datos.length;i++){
            if(opcion.equals(1))
                series2.add(datos[i][0], estimada[i]);
            else
                series2.add(datos[i][1], estimada[i]);
        }
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        return dataset;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("calcular")){//podemos comparar por el contenido del boton
            String sx1 = this.cajaVariableX1.getText();
            String sx2 = this.cajaVariableX2.getText();
            Double efectividad = Double.parseDouble(this.combo.getSelectedItem()+"");
            if(isDouble(sx1) && isDouble(sx2)){
                Double x1 = Double.parseDouble(sx1);
                Double x2 = Double.parseDouble(sx2);
                Integer gradosLibertad = N-3;
                TDistribution td = new TDistribution(gradosLibertad);
                Double distribucionT = td.inverseCumulativeProbability(((100.0-efectividad)/2.0)/100.0) * -1.0;
                Double estimadaDeY = b0 + b1 *x1 + b2 * x2;
                Double lidisT = estimadaDeY - distribucionT * Se;
                Double lsdisT = estimadaDeY + distribucionT * Se;
                String[] titulos2 = {"Y estimada","Li","Ls"};//los titulos de la tabla
                String[][] pruebaIntervalos = {{estimadaDeY+"",lidisT+"",lsdisT+""}};
                DefaultTableModel TableModel = new DefaultTableModel( pruebaIntervalos, titulos2 );
                jtable2.setModel(TableModel); 
            }else {
                JOptionPane.showMessageDialog(null, "Valores no validos.");
            }
            System.out.println("imprimir"+ e.getActionCommand());
            
            
            
        }
        if(e.getActionCommand().equals("imprimir")) {
                
                 try {   MessageFormat headerFormat = new MessageFormat("MI CABECERA");
                    MessageFormat footerFormat = new MessageFormat("- Página {0} -");

                        jtable.print(JTable.PrintMode.FIT_WIDTH, headerFormat, footerFormat);
                    } catch (PrinterException ex) {
                        Logger.getLogger(RegresionSimple.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
    }
    
    public boolean isDouble(String cadena){
	try {
		Double.parseDouble(cadena);
		return true;
	} catch (NumberFormatException nfe){
		return false;
	}
    }

}
