/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regresiones;

import static java.lang.Math.floor;
import static java.lang.Math.log;
import static java.lang.Math.log10;
import static java.lang.Math.round;
import java.util.Arrays;
import javax.swing.JTabbedPane;

/**
 *
 * @author hazel
 */
public class EstadisticaDescriptiva {

    private Integer N;
    private Double MAXIMO;
    private Double MINIMO;
    private Double RANGO;
    private Double INTERVALOS;
    private Double AMPLITUD;
    private Double RANGOAMPLIADO;
    private Double DIFERENCIARANGOS;
    private Double LIPI;
    private Double LSUI;
    private Double MEDIA;
    private Integer MODA;
    private Integer MEDIANA;
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

    public EstadisticaDescriptiva(Double[][] d) {

        datos = d;
        N = datos.length;

    }

    public void resolver() {

        /*   Arrays.sort(datos);        
         for (Double[] i : datos) {
         System.out.print(i + ", ");
         }        
         */
        N = datos.length;
        //System.out.println("total de valores " + N);
        Double[] mmm = MaxYmin();
        System.out.println("Max " + mmm[0]);        
        System.out.println("Min " + mmm[1]);
        
        RANGO = Rango();
        System.out.println("Intervalos" + Intervalo());
        INTERVALOS = Intervalo();
        System.out.println("Amplitud : " + Ampli());
        System.out.println("Rango ampliado " + RangoAmp());
        System.out.println("Diferencia de Rango : " + DifRango());
        System.out.println("Lipi : " + LiPi());
        LIPI = LiPi();
        System.out.println("Lsui : " + Lsui());
        LSUI = Lsui();
        System.out.println("Media : " + Media());
        MEDIA = Media();
        System.out.println("" + RecorreTabla());
        Double[][] tabla=MakeMatriz();

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

    public Double Intervalo() {

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
        return M;
    }

    public Double Ampli() {
        // aplitud = (rango / intervalo  ) +5;
        Double amplitud = 0.0;
        int cinco = 5;
        Double r = Rango();
        Double i = Intervalo();

        amplitud = (r / i) + cinco;
        amplitud = floor(amplitud);
        return amplitud;
    }

    public double RangoAmp() {

        // RangoA =(aplitud * intervalo  ) ;
        Double RangoA = 0.0;

        Double a = Ampli();
        Double i = Intervalo();

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
        if (Lipi <0){
            Lipi = 0.0;            
        }

        return Lipi;
    }

    public Double Lsui() {
        //=TRUNCAR(min-DifR/2)
        Double lipi = LiPi();
        if(lipi == 0){
            Double Lsui = 0.0;
                    double r = DifRango();
            Lsui = r - 1;                    
        }
        
        Double Lsui = 0.0;
        Double[] mmm = MaxYmin();
        Double max = 0.0;
        //  System.out.println("max " + max);
        Double r = DifRango();
        System.out.println("");
        max = mmm[0];

        Lsui = (max + r / 2);

        return Lsui;
    }

    public Double Media() {
        //Double media = datos[0][0];
        N = datos.length;
        Double Med = 0.0;

        for (int i = 0; i < N; i++) {

            //media = datos[i][0];
            Med += datos[i][0];
             //sum = sum + numbers[i];               
            //calculate average value
            //double average = sum / numbers.length;            
        }
        //System.out.println("valor maximo " + maximo);
        Med /= N;
     //   System.out.println("Media " + Med);

        return Med;
    }

    public Double Mediana() {  //la vv4rga 
        
        //=TRUNCAR(min-DifR/2)
        Double mediana = 0.0;

        return mediana;
    }
 
    public Double Moda() {

        //=TRUNCAR(min-DifR/2)
        Double Mo = 0.0;
        Double Li = 0.0;
        Double fi = 0.0;
        double ai = 0.0;
        // Mo = Li +((fi - (fi-1))/((fi - (fi-1)) + (fi - (fi+1)))*ai;
        Double moda = 0.0;

        return moda;
    }

    public Double[][] RecorreTabla() {
        System.out.println("-------------------------");
        //Arrays.sort(datos);
        Double a = datos[0][0];        
        for (int i = 0; i < datos.length; i++) {
            
                System.out.print(datos[i][0]);
            
            
        }
        return datos;
    }
    
    public Double[][]  MakeMatriz(){
        System.out.println("CReando matriz");
        Double[][] auxiliar= new Double[N][10];
       
        //INTERVALOS
        //LLenamos la primer fila
        auxiliar[0][0]=LIPI;
        auxiliar[0][1]=auxiliar[0][0]+AMPLITUD;
        auxiliar[0][2]=(auxiliar[0][0]+auxiliar[0][1])/2;//Xi
        auxiliar[0][3]=Fi(auxiliar[0][0],auxiliar[0][1]);//Fi
        auxiliar[0][4]=auxiliar[0][3];//fa
        auxiliar[0][5] = auxiliar[0][3]/N;//Fr
        auxiliar[0][6] = auxiliar[0][5];//Fra
        auxiliar[0][7] = auxiliar[0][2] * auxiliar[0][3];//XiFi
        auxiliar[0][8] = Math.abs(auxiliar[0][2]-MEDIA) * auxiliar[0][3];//(Xi-Media)*FI
        auxiliar[0][9] = Math.pow(auxiliar[0][2]-MEDIA, 2)* auxiliar[0][3];//(Xi-Media)^2 Fi
        for (int i = 1; i < auxiliar.length; i++) {
             auxiliar[i][0]=auxiliar[i-1][1];
            auxiliar[i][1]=auxiliar[i][0]+AMPLITUD;
            auxiliar[i][2]=(auxiliar[i][0]+auxiliar[i][1])/2;//Xi
            auxiliar[i][3]=Fi(auxiliar[i][0],auxiliar[i][1]);//Fi
            auxiliar[i][4]=auxiliar[i][3];//fa
            auxiliar[i][5] = auxiliar[i][3]/N;//Fr
            auxiliar[i][6] = auxiliar[i][5];//Fra
            auxiliar[i][7] = auxiliar[i][2] * auxiliar[0][3];//XiFi
            auxiliar[i][8] = Math.abs(auxiliar[i][2]-MEDIA) * auxiliar[i][3];//(Xi-Media)*FI
            auxiliar[i][9] = Math.pow(auxiliar[i][2]-MEDIA, 2)* auxiliar[i][3];//(Xi-Media)^2 Fi
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
  
    private Double Xi(){
        //Xi =(Li + Ls) /2;
        Double Xi = 0.0;
        Double Li = LiPi();
        Double Ls = Lsui();
        
        //calculando  Xi
                if(Ls == 0 && Li == 0){
                    Xi = 0.0;
                }else{
                    Xi = (Li + Ls)/2;
                }                
                //
                return Xi;
    }
    
    
    /*
    
    public Double mode(){
    Double n = datos[0][0];
    Arrays.sort(datos);
    
    Double count2 = 0.0;
    Double count1 = 0.0;
    Double pupular1 =0.0;
    Double popular2 =0.0;


    for (int i = 0; i < datos.length; i++)
    {
            pupular1 = datos[i];
            count1 = 0;    //see edit

        for (int j = i + 1; j < n.length; j++)
        {
            if (pupular1 == n[j]) count1++;
        }

        if (count1 > count2)
        {
                popular2 = pupular1;
                count2 = count1;
        }

        else if(count1 == count2)
        {
            popular2 = Math.min(popular2, pupular1);
        }
    }

    return popular2;
    
}*/

    private Double Fi(Double aDouble, Double aDouble0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
