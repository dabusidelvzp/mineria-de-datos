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
import java.io.FileNotFoundException;
import static java.lang.Math.floor;
import static java.lang.Math.log10;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
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
import org.jfree.data.category.DefaultCategoryDataset;
import pdf.PDFdescriptiva;

/**
 *
 * @author hazel
 */
public class EstadisticaDescriptiva implements ActionListener{

    private Integer N;
    private Double MAXIMO;
    private Double MINIMO;
    private Double RANGO;
    private Integer INTERVALOS;
    private Double AMPLITUD;
    private Double RANGOAMPLIADO;
    private Double DIFERENCIARANGOS;
    private Double LIPI;
    private Double LSUI;
    private Double MEDIA;
    private Double MODA;
    private Double MEDIANA;
    private Double DM;
    private Double DE;
    private Double VARIANZA;
    private Integer PRIMERO;
    private Integer CONTADOR;
    private Integer LI1;
    private Integer LS1;
    private Integer XI1;
    private Integer FI1;
    private Integer FA1;
    private Integer FR1;
    private Integer FRA1;
    private Integer XIFI1;
    private Integer AUXFI;
    private Integer AUXFRA;
    private Integer AUXFA;

    private Double[][] datos;
    private Double[][] tablaComplete;
     private Double[][] tablaCompleta;
    private JTable jtable;

    public EstadisticaDescriptiva(Double[][] d) {

        datos = quitarNegativos(d);
        N = datos.length;

    }

    public void resolver(JTabbedPane panel1) {

        N = datos.length;
        //System.out.println("total de valores " + N);
        Double[] mmm = MaxYmin();
        System.out.println("Max " + mmm[0]);
        System.out.println("Min " + mmm[1]);
        MAXIMO = mmm[0];
        MINIMO = mmm[1];
        RANGO = Rango();
        INTERVALOS = Intervalo();
        AMPLITUD = Ampli();
        System.out.println("Rango ampliado " + RangoAmp());
        RANGOAMPLIADO = RangoAmp();
        System.out.println("Diferencia de Rango : " + DifRango());
        DIFERENCIARANGOS = DifRango();
        System.out.println("Lipi : " + LiPi());
        LIPI = LiPi();
        System.out.println("Lsui : " + Lsui());
        LSUI = Lsui();

        //System.out.println("" + RecorreTabla());
        Double[][] tabla = MakeMatriz();
        //obtenemos la media
        MEDIA = Media(tabla);
        System.out.println("Media : " + MEDIA);
        //rellenamos la matriz
        
        
        tablaCompleta = rellenar(tabla);
        
        //mediana
        MEDIANA = Mediana(tablaCompleta);
        System.out.println("MEDIANA: " + MEDIANA);
        //Moda
        MODA = Moda(tablaCompleta);
        System.out.println("MODA: " + MODA);
        //Desviacion media
        DM = DM(tablaCompleta);
        System.out.println("DM: " + DM);
        //varianza
        VARIANZA = Varianza(tablaCompleta);
        System.out.println("VARIANZA: " + VARIANZA);
        //Desviacion estandart

        System.out.println("Desviacion: " + Math.sqrt(VARIANZA));
        DE = Math.sqrt(VARIANZA);
        //pintar
        pintar(panel1, tablaCompleta);
    }
    //

    @SuppressWarnings("empty-statement")
    public Double[] MaxYmin() {

        Double maximo = datos[0][0];
        Double minimo = datos[0][0];

        for (int i = 0; i < datos.length; i++) {
            //maximo
            if (datos[i][0] > maximo) {
                maximo = datos[i][0];
            }
            //minimo
            if (datos[i][0] < minimo) {
                minimo = datos[i][0];
            }
            //System.out.println("---" +  datos);
        }
        //System.out.println("valor maximo " + maximo);
        //      System.out.println("valor maximo " + minimo);
        Double[] max = {maximo, minimo};
        return max;
    }

    public double Rango() {

        Double[] mmm = MaxYmin();
        double max = 0.0;
        double min = 0.0;
        double r = 0.0;
        max = mmm[0];
        min = mmm[1];
        floor(r = mmm[0] - mmm[1]);
        //System.out.println("Rango =  "+ r);
        return r;

    }

    public Integer Intervalo() {

        //intervalo = floor(1+3.3*LOG(N));
        N = datos.length;
        Double tres = 3.3;
        Double M = 0.0;
        Double logaN = 0.0;

        logaN = log10(N);
        M = floor(1 + (tres * logaN));

        //System.out.println("-N- = " + N);
        //System.out.println("Log" + logaN);
        //   System.out.println("intervalos " + M);
        return M.intValue();
    }

    public Double Ampli() {
        // aplitud = (rango / intervalo  ) +5;
        Double amplitud = 0.0;
        int cinco = 4;
        Double r = Rango();
        Integer i = Intervalo();
        // 
        Double[] MM = MaxYmin();
        amplitud = (r / i) + (MM[1]/4);
        amplitud = floor(amplitud);
        return amplitud;
    }

    public double RangoAmp() {

        // RangoA =(aplitud * intervalo  ) ;
        Double RangoA = 0.0;

        Double a = Ampli();
        Integer i = Intervalo();

        RangoA = floor(a * i);
        //  System.out.println("Rango amp " + RangoA);
        return RangoA;
    }

    public Double DifRango() {
        //System.out.println("=RangoAmp-RANGO");
        Double DifR = 0.0;
        Double Ra = RangoAmp();
        Double Rango = Rango();

        DifR = Ra - Rango;

        return DifR;
    }

    public Double LiPi() {
        //=TRUNCAR(min-DifR/2)
        Double Lipi = 0.0;
        Double[] mmm = MaxYmin();
        double min = 0.0;
        double r = DifRango();
        min = mmm[1];

        Lipi = (min - (r / 2));
        if(Lipi<0.0)
            Lipi=0.0;
        return Lipi;
    }

    public Double Lsui() {
        //=TRUNCAR(min-DifR/2)
       
        Double Lsui = 0.0;
        Double[] mmm = MaxYmin();
        Double max = 0.0;
        //  System.out.println("max " + max);
        Double r = DifRango();
        System.out.println("");
        max = mmm[0];

        Lsui = (max + r / 2);
        Double min = mmm[1];

        Double LipiAyuda = (min - (r / 2));
        if(LipiAyuda<0)
            Lsui = Lsui - LipiAyuda;
        
        return Lsui;
    }

    public Double Media(Double[][] temporal) {
        Double Med = 0.0;
        for (int i = 0; i < INTERVALOS; i++) {

            //media = datos[i][0];
            Med += temporal[i][7];
        }
        Med /= N;

        return Med;
    }

    public Double Mediana(Double[][] tabla) {  //la vv4rga   
        Double mediana = 0.0;
        Double inter = 0.0;
        Double fa = 0.0;
        Double fi = 0.0;
        for (int i = 0; i < tabla.length; i++) {
            if (tabla[i][4] > (N / 2)) {
                inter = tabla[i][0];
                fa = tabla[i - 1][4];
                fi = tabla[i][3];
                i = tabla.length + 1;
            }
        }
        mediana = inter + (((N / 2) - fa) / fi) * AMPLITUD;
        return mediana;
    }

    public Double Moda(Double[][] tabla) {
        Double Mo = 0.0;
        Double max = tabla[0][3];
        Integer posicion = 0;
        for (int i = 0; i < INTERVALOS; i++) {
            if (tabla[i][3] > max) {
                posicion = i;
                i = INTERVALOS + 1;
            }
        }
        Double fi = tabla[posicion][3];
        Double f = 0.0;
        Double fiSig = 0.0;
        if (posicion != 0) {
            f = tabla[posicion - 1][3];
        }
        if (posicion != tabla.length - 1) {
            fiSig = tabla[posicion + 1][3];
        }
        Mo = tabla[posicion][0] + ((fi - f) / (2 * fi - f - fiSig)) * AMPLITUD;

        return Mo;
    }

    public Double[][] RecorreTabla() {
        System.out.println("-------------------------");
        //Arrays.sort(datos);
        //  Double a = datos[0][0];        
        for (int i = 0; i < datos.length; i++) {

            System.out.print(datos[i][0]);

        }
        return datos;
    }

    public Double[][] MakeMatriz() {

        System.out.println("CReando matriz");
        Double[][] auxiliar = new Double[INTERVALOS][10];
        try {
            //INTERVALOS
            //LLenamos la primer fila
            auxiliar[0][0] = LIPI;
            auxiliar[0][1] = auxiliar[0][0] + AMPLITUD;
            auxiliar[0][2] = (auxiliar[0][0] + auxiliar[0][1]) / 2;//Xi
            auxiliar[0][3] = Fi(auxiliar[0][0], auxiliar[0][1]);//Fi
            auxiliar[0][4] = auxiliar[0][3];//fa
            auxiliar[0][5] = auxiliar[0][3] / N;//Fr
            auxiliar[0][6] = auxiliar[0][5];//Fra
            auxiliar[0][7] = auxiliar[0][2] * auxiliar[0][3];//XiFi
            // auxiliar[0][8] = Math.abs(auxiliar[0][2]-MEDIA) * auxiliar[0][3];//(Xi-Media)*FI
            //auxiliar[0][9] = Math.pow(auxiliar[0][2]-MEDIA, 2)* auxiliar[0][3];//(Xi-Media)^2 Fi

            for (int i = 1; i < auxiliar.length; i++) {
                auxiliar[i][0] = auxiliar[i - 1][1];
                auxiliar[i][1] = auxiliar[i][0] + AMPLITUD;
                auxiliar[i][2] = (auxiliar[i][0] + auxiliar[i][1]) / 2;//Xi
                auxiliar[i][3] = Fi(auxiliar[i][0], auxiliar[i][1]);//Fi
                auxiliar[i][4] = auxiliar[i - 1][4] + auxiliar[i][3];//fa
                auxiliar[i][5] = auxiliar[i][3] / N;//Fr
                auxiliar[i][6] = auxiliar[i][5] + auxiliar[i - 1][6];//Fra
                auxiliar[i][7] = auxiliar[i][2] * auxiliar[i][3];//XiFi
                // auxiliar[i][8] = Math.abs(auxiliar[i][2]-MEDIA) * auxiliar[i][3];//(Xi-Media)*FI
                // auxiliar[i][9] = Math.pow(auxiliar[i][2]-MEDIA, 2)* auxiliar[i][3];//(Xi-Media)^2 Fi

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return auxiliar;
    }

    private Double Ls() {
        //=LIPI+AMPLITUD
        Double Ls = 0.0;
        Double Li = LiPi();
        Double a = Ampli();

        //calculando            
        Ls = Li + a;
        //                
        return Ls;
    }

    private Double Xi() {
        //Xi =(Li + Ls) /2;
        Double Xi = 0.0;

        Double Li = LiPi();
        Double Ls = Lsui();

        //calculando  Xi
        Xi = (Li + Ls) / 2;

        //
        return Xi;
    }

    //frecuencia
    private Double Fi(Double Li, Double Ls) {
        // fr > L1 & <= ls 
        Double Contador = 0.0;
        for (int i = 0; i < datos.length; i++) {
            //Double[] doubles = x[i];
            if (datos[i][0] > Li && datos[i][0] <= Ls) {
                Contador++;
            }

        }

        return Contador;

    }

    public Double DM(Double[][] tabla) {
        Double DM = 0.0;
        for (int i = 0; i < tabla.length; i++) {
            DM += tabla[i][8];
        }
        return DM / N;
    }

    public Double Varianza(Double[][] tabla) {
        Double V = 0.0;
        for (int i = 0; i < tabla.length; i++) {
            V += tabla[i][9];
        }

        return V / N;
    }

    private Double[][] rellenar(Double[][] auxiliar) {
        auxiliar[0][8] = Math.abs(auxiliar[0][2] - MEDIA) * auxiliar[0][3];//(Xi-Media)*FI
        auxiliar[0][9] = Math.pow(auxiliar[0][2] - MEDIA, 2) * auxiliar[0][3];//(Xi-Media)^2 Fi

        for (int i = 1; i < auxiliar.length; i++) {
            auxiliar[i][8] = Math.abs(auxiliar[i][2] - MEDIA) * auxiliar[i][3];//(Xi-Media)*FI
            auxiliar[i][9] = Math.pow(auxiliar[i][2] - MEDIA, 2) * auxiliar[i][3];//(Xi-Media)^2 Fi

        }
        for (int i = 0; i < auxiliar.length; i++) {

            for (int j = 0; j < auxiliar[0].length; j++) {
                System.out.print(auxiliar[i][j] + " - ");

            }
            System.out.println("");
        }

        return auxiliar;
    }

    private void pintar(JTabbedPane panel1, Double[][] tabla) {
        JPanel panel = new JPanel(new BorderLayout());
        //tabla
        jtable = new JTable();//creamos la tabla a mostrar
        jtable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 0, 0), 2, true));
        jtable.setFont(new java.awt.Font("Arial", 1, 14));
        jtable.setColumnSelectionAllowed(true);
        jtable.setCursor(new java.awt.Cursor(java.awt.Cursor.N_RESIZE_CURSOR));
        jtable.setInheritsPopupMenu(true);
        jtable.setMinimumSize(new java.awt.Dimension(80, 80));
        String[] titulos = {"i", "Li", "Ls", "Xi", "Fi", "Fa", "Fr", "Fra", "XiFi", "|Xi-X|Fi", "(Xi-X)^2 Fi"};//los titulos de la tabla
        DefaultTableModel TableModel = new DefaultTableModel(formato(tabla), titulos);
        jtable.setModel(TableModel);

        JScrollPane jScrollPane1 = new JScrollPane();
        jScrollPane1.setViewportView(jtable);
        jtable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        panel.add(jScrollPane1, BorderLayout.CENTER);
        //campos
        JPanel medidas = new JPanel(new GridLayout(0, 6));

        JLabel etiquetaMax = new JLabel("Maximo");
        JTextField cajaMax = new JTextField();
        cajaMax.setText(MAXIMO + "");
        cajaMax.setEditable(false);

        JLabel etiquetaMin = new JLabel("Minimo");
        JTextField cajaMin = new JTextField();
        cajaMin.setText(MINIMO + "");
        cajaMin.setEditable(false);

        JLabel etiquetaN = new JLabel("N");
        JTextField cajaN = new JTextField();
        cajaN.setText(N + "");
        cajaN.setEditable(false);

        JLabel etiquetaRango = new JLabel("Rango");
        JTextField cajaRango = new JTextField();
        cajaRango.setText(MAXIMO - MINIMO + "");
        cajaRango.setEditable(false);

        JLabel etiquetaIntervalos = new JLabel("Intervalos");
        JTextField cajaIntervalos = new JTextField();
        cajaIntervalos.setText(INTERVALOS + "");
        cajaIntervalos.setEditable(false);

        JLabel etiquetaAmp = new JLabel("Amplitud");
        JTextField cajaAmp = new JTextField();
        cajaAmp.setText(AMPLITUD + "");
        cajaAmp.setEditable(false);

        JLabel etiquetaRamp = new JLabel("Rango Ampliado");
        JTextField cajaRamp = new JTextField();
        cajaRamp.setText(RANGOAMPLIADO + "");
        cajaRamp.setEditable(false);

        JLabel etiquetaDifRam = new JLabel("Dif Rangos");
        JTextField cajaDifRam = new JTextField();
        cajaDifRam.setText(DIFERENCIARANGOS + "");
        cajaDifRam.setEditable(false);

        JLabel etiquetaLIPI = new JLabel("LIPI");
        JTextField cajaLIPI = new JTextField();
        cajaLIPI.setText(LIPI + "");
        cajaLIPI.setEditable(false);

        JLabel etiquetaLSUI = new JLabel("LSUI");
        JTextField cajaLSUI = new JTextField();
        cajaLSUI.setText(LSUI + "");
        cajaLSUI.setEditable(false);

        JLabel etiquetaDM = new JLabel("Desv media");
        JTextField cajaDM = new JTextField();
        cajaDM.setText(DM + "");
        cajaDM.setEditable(false);

        JLabel etiquetaV = new JLabel("Varianza");
        JTextField cajaV = new JTextField();
        cajaV.setText(VARIANZA + "");
        cajaV.setEditable(false);

        JLabel etiquetaDE = new JLabel("Desv estandar");
        JTextField cajaDE = new JTextField();
        cajaDE.setText(DE + "");
        cajaDE.setEditable(false);

        JLabel etiquetaMe = new JLabel("Media");
        JTextField cajaMe = new JTextField();
        cajaMe.setText(MEDIA + "");
        cajaMe.setEditable(false);

        JLabel etiquetaMEDIANA = new JLabel("Mediana");
        JTextField cajaMEDIANA = new JTextField();
        cajaMEDIANA.setText(MEDIANA + "");
        cajaMEDIANA.setEditable(false);

        JLabel etiquetaModa = new JLabel("Moda");
        JTextField cajaModa = new JTextField();
        cajaModa.setText(MODA + "");
        cajaModa.setEditable(false);

        JButton boton = new JButton("Exportar a PDF");
        boton.addActionListener(this);
        
        medidas.add(etiquetaMax);
        medidas.add(cajaMax);
        medidas.add(etiquetaMin);
        medidas.add(cajaMin);
        medidas.add(etiquetaN);
        medidas.add(cajaN);

        medidas.add(etiquetaRango);
        medidas.add(cajaRango); //Jhony        
        medidas.add(etiquetaIntervalos);
        medidas.add(cajaIntervalos);
        medidas.add(etiquetaAmp);
        medidas.add(cajaAmp);
        medidas.add(etiquetaRamp);
        medidas.add(cajaRamp);
        medidas.add(etiquetaDifRam);
        medidas.add(cajaDifRam);
        medidas.add(etiquetaLIPI); //lipi
        medidas.add(cajaLIPI);

        medidas.add(etiquetaLSUI);
        medidas.add(cajaLSUI);
        medidas.add(etiquetaDM); //DESVIACION MEDIA
        medidas.add(cajaDM);
        medidas.add(etiquetaV);   //VARIANZA
        medidas.add(cajaV);
        medidas.add(etiquetaDE); //DESVIACION ESTANDAR
        medidas.add(cajaDE);
        medidas.add(etiquetaMe); //MEDIA
        medidas.add(cajaMe);
        medidas.add(etiquetaMEDIANA);  // mediana
        medidas.add(cajaMEDIANA);
        medidas.add(etiquetaModa);  //moda
        medidas.add(cajaModa);
        medidas.add(boton);
        //SECCION DE GRAFICAS
                
        JFreeChart Grafica;
        DefaultCategoryDataset Datos = new DefaultCategoryDataset();
        //Buscar las pociciones de  LI       LS      Fa
        //                         0,1      0,2     0,5
//agregar los valores a la grafica
        for (int i = 0; i < INTERVALOS; i++) {
            
            Datos.addValue(tabla[i][3],"frecuencia",tabla[i][1]+" - "+tabla[i][2]);
        }
       
       Grafica =  ChartFactory.createLineChart("Histograma", "Frecuencia", "Li Ls", Datos,       
       PlotOrientation.VERTICAL,true,true,false);
       
       ChartPanel p = new ChartPanel(Grafica);
        
        
        
        
        // termina seccion de grafica
        
        
        
        panel.add(medidas, BorderLayout.SOUTH);
        //panel.add(jScrollPane1, BorderLayout.CENTER); 
        panel1.addTab("Resultados", panel);
        panel1.addTab("Grafica", p);
    }

    private String[][] formato(Double[][] tabla) {

        String[][] nueva = new String[tabla.length][tabla[0].length + 1];
        try {
            for (int i = 0; i < nueva.length; i++) {
                nueva[i][0] = i + 1 + "";
            }
            DecimalFormat formato = new DecimalFormat("0.00");
            for (int i = 0; i < nueva.length; i++) {
                for (int j = 1; j < nueva[0].length; j++) {
                    if (j == 6 || j == 7) {
                        nueva[i][j] = formato.format(tabla[i][j - 1] * 100) + "%";   //----------------------------------------
                    } else {
                        nueva[i][j] = formato.format(tabla[i][j - 1]) + "";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nueva;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
         if(e.getActionCommand().equals("Exportar a PDF")) {
            try {
                String nombrePDF = JOptionPane.showInputDialog("Escribe el nombre del PDF (sin extension)");
                String[] datosVariables = {MAXIMO+"",MINIMO+"",RANGO+"",INTERVALOS+"",AMPLITUD+"",RANGOAMPLIADO+"",
                                    DIFERENCIARANGOS+"",LIPI+"",LSUI+"",DM+"",VARIANZA+"",DE+"",MEDIA+"",MEDIANA+"",MODA+"" };
                PDFdescriptiva.crearPDF("Estadistica descriptiva",formato(tablaCompleta),nombrePDF,N,datosVariables);
                
                JOptionPane.showMessageDialog(jtable, "Se creo el PDF");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(RegresionMultiple.class.getName()).log(Level.SEVERE, null, ex);
            } catch (DocumentException ex) {
                 Logger.getLogger(EstadisticaDescriptiva.class.getName()).log(Level.SEVERE, null, ex);
             }
                 
      }
        
    }

    private Double[][] quitarNegativos(Double[][] d) {
        Integer negativos =0;
        for (int i = 0; i < d.length; i++) {
            if(d[i][0]<0)
                negativos ++;
            
        }
        Double[][] apoyo = new Double[d.length-negativos][1];
        Integer x=0;
        for (int i = 0; i < d.length; i++) {
            if(d[i][0]>=0)
                apoyo[i-x][0]=d[i][0];
            else
                x++;
            
        }
        return apoyo;
    }

    

}
