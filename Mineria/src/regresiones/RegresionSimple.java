/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package regresiones;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 *
 * @author hazel
 */
public class RegresionSimple {
    private Double N;
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
    public RegresionSimple(Double[][] d){
        N = 0.0;
        datos = d;
        sumatorias = new Double[9];
        xiyi = new Double[datos.length];
        x2 = new Double[datos.length];
        y2 = new Double[datos.length];
        yEstimada = new Double[datos.length];
        
    }
    
    public void Resolver(){
        for(int i = 0; i<9; i++){
            sumatorias[i] = 0.0;
        }
        try{
        N = datos.length+0.0;
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
           
         
            


           System.out.println("sum x: " + sumatorias[0]);
           System.out.println("sum y: " + sumatorias[1]);
           System.out.println("sum x*y: " + sumatorias[2]);
           System.out.println("sum x^2: " + sumatorias[3]);
            System.out.println("sum y^2: " + sumatorias[4]);
           System.out.println("DX7: " + dxy);
           System.out.println("DY: " + dy);
           System.out.println("DX: "+ dx);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
