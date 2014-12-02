/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package regresiones;

import com.itextpdf.text.DocumentException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
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
import javax.swing.JTable.PrintMode;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;
import org.apache.commons.math3.distribution.TDistribution;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import pdf.PDFmultiple;
import pdf.PDFsimple;

/**
 *
 * @author hazel
 */
public class RegresionSimple implements ActionListener {

    private static void paint(Graphics2D g2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    private int N;
    private Double[][] datos;
    private Double[] sumatorias;
    private Double[] xiyi;
    private Double[] x2;
    private Double[] y2;
    private Double[] yEstimada;
    private Double dxy;
    private Double dx;
    private Double dy;
    private Double r;
    private Double y;
    private Double b0;
    private Double b1;
    private Double mediax;
    private Double mediay;
    private Double redondeoSumatoriax2;
    private Double redondeoSumatoriay2;
    private Double Se;
    private Double prediccionX1;
    private Double prediccionX2;
    private Double prediccionEfect;
    private Double prediccionLs;
    private Double prediccionLi;
    private Double prediccionYestimada; 
    private JTextField cajaVariableX1;
    private JComboBox combo;
    JTable jtable2;
    String[][] arregloFinal;
    private  JTable jtable;
    public RegresionSimple(Double[][] d){
        N = d.length;
        datos = d;
        sumatorias = new Double[9];
        xiyi = new Double[datos.length];
        x2 = new Double[datos.length];
        y2 = new Double[datos.length];
        yEstimada = new Double[datos.length];
        
    }
    
    public void Resolver(JTabbedPane resultados){
        
        for(int i = 0; i<9; i++){
            sumatorias[i] = 0.0;
        }
        try{
            System.out.println("TOTAL DE DATOS: "+N);
        for(int i = 0; i < N ; i++){
            xiyi[i] = datos[i][0] * datos[i][1];
            System.out.println("X*Y"+i+": "+xiyi[i]);
              x2[i] = datos[i][0]*datos[i][0]; //elevamos al cuadrado las x's
              y2[i] = datos[i][1]*datos[i][1]; //elevamos al cuadrado las y's
              sumatorias[0] += datos[i][0]; //sumatoria de x
            sumatorias[1] += datos[i][1]; //sumatoria de y
              
        }
        //sumatoria de xi*yi
           for(int j=0;j<N;j++) {
               sumatorias[2] += xiyi[j];
           }
           
           //sumatoria de x^2
           for(int j=0;j<N;j++) {
               sumatorias[3] += x2[j];
           }
           
           //sumatoria de y^2
           for(int j=0;j<N;j++) {
               sumatorias[4] += y2[j];
           }
           
           mediax = sumatorias[0]/N;
           mediay = sumatorias[1]/N;
           
            System.out.println("RAIS 25: "+Math.sqrt(25));
            
            DecimalFormat df = new DecimalFormat("##.##");
            df.setRoundingMode(RoundingMode.DOWN);
               System.out.println("redondeo x^2-- "+df.format(sumatorias[3]));
            System.out.println("redondeo y^2-- "+df.format(sumatorias[4]));
            redondeoSumatoriax2 = Double.parseDouble(df.format(sumatorias[3]));
            redondeoSumatoriay2 = Double.parseDouble(df.format(sumatorias[4]));
           dxy = ((sumatorias[2])/N)-mediax*mediay;
           dy = Math.sqrt(((redondeoSumatoriay2 /N)-(mediay*mediay)));
           dx = Math.sqrt(((redondeoSumatoriax2/N)-(mediax*mediax)));
           
           b1 = ((sumatorias[2]*N)-sumatorias[0]*sumatorias[1])/((sumatorias[3]*N)-(sumatorias[0]*sumatorias[0]));
           b0 = (sumatorias[1]/N)- ((b1 * sumatorias[0])/N);

           // Y ESTIMADA
           for(int i = 0; i < N ; i++){
           yEstimada[i] = b0 + (b1 *datos[i][0]);
           }
            
           Se = Math.sqrt((sumatorias[4]-(b0*sumatorias[1])-(b1*sumatorias[2]))/(N-2));
           r = dxy/(dx*dy);
           System.out.println("sum x: " + sumatorias[0]);
           System.out.println("sum y: " + sumatorias[1]);
           System.out.println("sum x*y: " + sumatorias[2]);
           System.out.println("sum x^2: " + sumatorias[3]);
            System.out.println("sum y^2: " + sumatorias[4]);
           System.out.println("DX7: " + dxy);
           System.out.println("DY: " + dy);
           System.out.println("DX: "+ dx);
           System.out.println("B0: "+ b0);
           System.out.println("B1: "+ b1);
           
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
        String[] titulos = {"X","Y","Xi*Yi","X2","Y2","Y estimada"};//los titulos de la tabla
        arregloFinal = new String[N][6];
        DecimalFormat formato = new DecimalFormat("0.00");
        for(int i=0;i<N;i++){//armamos el arreglo
            arregloFinal[i][0]= datos[i][0]+""; //X
            arregloFinal[i][1]= datos[i][1]+"";//Y
            arregloFinal[i][2]= formato.format(xiyi[i]);
            arregloFinal[i][3]= formato.format(x2[i]);
            arregloFinal[i][4]= formato.format(y2[i]);
            arregloFinal[i][5]= formato.format(yEstimada[i]);
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
        JLabel etiquetab0 = new JLabel("b0");
        JTextField cajab0 = new JTextField();
        cajab0.setText(b0+"");
        JLabel etiquetab1 = new JLabel("b1");
        JTextField cajab1 = new JTextField();
        cajab1.setText(b1+"");
        JLabel etiquetadxy = new JLabel("DXy");
        JTextField cajadxy = new JTextField();
        cajadxy.setText(dxy+"");
        JLabel etiquetadx = new JLabel("DX");
        JTextField cajadx = new JTextField();
        cajadx.setText(dx+"");
        JLabel etiquetady = new JLabel("DY");
        JTextField cajady = new JTextField();
        cajady.setText(dy+"");
        JLabel etiquetaR = new JLabel("R");
        JTextField cajaR = new JTextField();
        cajaR.setText(r+"");
        JLabel etiquetaSE = new JLabel("SE");
        JTextField cajaSE = new JTextField();
        cajaSE.setText(Se+"");
        JButton boton = new JButton("Exportar a PDF");
        boton.addActionListener(this);
        panel2.add(etiquetaN);
        panel2.add(cajaN);
        panel2.add(etiquetab0);
        panel2.add(cajab0);
        panel2.add(etiquetab1);
        panel2.add(cajab1);
        panel2.add(etiquetadxy);
        panel2.add(cajadxy);
        panel2.add(etiquetadx);
        panel2.add(cajadx);
        panel2.add(etiquetady);
        panel2.add(cajady);
        panel2.add(etiquetaR);
        panel2.add(cajaR);
        panel2.add(etiquetaSE);
        panel2.add(cajaSE);
        panel2.add(boton);
        panel.add(panel2,BorderLayout.SOUTH);//agrego el panel2 con rejilla en el panel principal al sur
        resultados.addTab("resultado", panel);
        //**************************************************************************************
        //intervalos de confianza
        JPanel intervalos= new JPanel(new BorderLayout());
        JPanel variables = new JPanel(new GridLayout(0,2));
        JLabel variableX1 = new JLabel("X1");
        cajaVariableX1 = new JTextField();
        boton = new JButton("calcular");
        boton.addActionListener(this);
        JLabel variableEfectividad = new JLabel("Efectividad");
        String[] efectividades = {"80","85","90","95","99"};
        combo = new JComboBox(efectividades);
        variables.add(variableX1);
        variables.add(cajaVariableX1);
        variables.add(variableEfectividad);
        variables.add(combo);
        variables.add(boton);//comentario
        //cometario2
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
        // ***********************************************************************
        JPanel graficas = new JPanel(new GridLayout(0,1));
        XYDataset dataset = createSampleDataset();
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Grafica",
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
        resultados.addTab("Graficas", graficas);
        //IMPRIMIR JTABLE
       /* MessageFormat headerFormat = new MessageFormat("MI CABECERA");
        MessageFormat footerFormat = new MessageFormat("- Página {0} -");
        jtable.print(PrintMode.FIT_WIDTH, headerFormat, footerFormat);
        */
        
        
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private XYDataset createSampleDataset() {
        XYSeries series1 = new XYSeries("X-Y");
        for(int i=0;i<datos.length;i++){
                series1.add(datos[i][0], datos[i][1]);
        }
        
        XYSeries series2 = new XYSeries("X-Y estimada");
        for(int i=0;i<datos.length;i++){
                series2.add(datos[i][0], yEstimada[i]);
        }
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        return dataset;
    }

    	
   /* private void reporte(Double datos[][]){
        try{
            ArrayList a  = new ArrayList();
            //(JasperReport ) JRLoader.loadObject("reporte2.jasper")
        JasperReport reporte  =  (JasperReport) JRLoader.loadObjectFromFile("reporte2.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport("reporte",reporte);
        }catch(Exception e){
            
        }
    }*/
    
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("calcular")){//podemos comparar por el contenido del boton
            String sx1 = this.cajaVariableX1.getText();
            prediccionEfect = Double.parseDouble(this.combo.getSelectedItem()+"");
            if(isDouble(sx1)){
                prediccionX1= Double.parseDouble(sx1);
                Integer gradosLibertad = N-2;
                TDistribution td = new TDistribution(gradosLibertad);
                Double distribucionT = td.inverseCumulativeProbability(((100.0-prediccionEfect)/2.0)/100.0) * -1.0;
                prediccionYestimada = b0 + b1 *prediccionX1;
                prediccionLi = prediccionYestimada - distribucionT * Se;
                prediccionLs = prediccionYestimada + distribucionT * Se;
                String[] titulos2 = {"Y estimada","Li","Ls"};//los titulos de la tabla
                String[][] pruebaIntervalos = {{prediccionYestimada+"",prediccionLi+"",prediccionLs+""}};
                DefaultTableModel TableModel = new DefaultTableModel( pruebaIntervalos, titulos2 );
                jtable2.setModel(TableModel); 
            }else {
                JOptionPane.showMessageDialog(null, "Valores no validos.");
            }
            System.out.println("imprimir"+ e.getActionCommand());
            
            
            
        }
        if(e.getActionCommand().equals("Exportar a PDF")) {
            try {
                String nombrePDF = JOptionPane.showInputDialog("Escribe el nombre del PDF (sin extension)");
                Double[] prediccionValores = {prediccionX1,prediccionEfect,prediccionYestimada,prediccionLi,prediccionLs};
                PDFsimple.crearPDF("Regresion simple", arregloFinal, nombrePDF,N,b0,b1,Se,prediccionValores);
                
                JOptionPane.showMessageDialog(jtable, "Se creo el PDF");
            } catch (DocumentException | FileNotFoundException ex) {
                Logger.getLogger(RegresionMultiple.class.getName()).log(Level.SEVERE, null, ex);
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



