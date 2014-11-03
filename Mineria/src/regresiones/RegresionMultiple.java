/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package regresiones;

/**
 *
 * @author hazel
 */
public class RegresionMultiple {
    private Integer N;
    private Double b0;
    private Double b1;
    private Double b2;
    private Double k;
    private Double Se;
    private Double[][] datos;
    private Double[][] auxiliar;
    private Double[] sumatorias;
    
    public RegresionMultiple(Double[][] d){
        N=0;
        b0=0.0;
        b1 = 0.0;
        b2 = 0.0;
        k = 0.0;
        Se = 0.0;
        datos=d;
        sumatorias= new Double[9];
    }
    @SuppressWarnings("empty-statement")
    public void resolver(){
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
       Double detX=calDeterminante(matrizX);
       //modificamos valores
       Double[][] matrizX2 =    {{N+0.0         ,sumatorias[2], sumatorias[1]},
                                {sumatorias[0]  ,sumatorias[5], sumatorias[7]},
                                {sumatorias[1]  ,sumatorias[6], sumatorias[4]}};
       Double detX2=calDeterminante(matrizX2);
       //modificamos valores
       Double[][] matrizY = {{N+0.0,sumatorias[0],sumatorias[2]},
                                        {sumatorias[0],sumatorias[3],sumatorias[5]},
                                        {sumatorias[1],sumatorias[7],sumatorias[6]}};
       Double detY=calDeterminante(matrizY);
       //calculamos b0,b1,b2
       Double b0= detX/detGeneral; 
       Double b1= detX2/detGeneral; 
       Double b2= detY/detGeneral; 
       System.out.println("b0: "+b0+", b1: "+b1+", b2: "+b2);
       //calculamos las Y estimadas y su sumatorias
       Double[] Yestimada=new Double[N];
       for(int i=0;i<N;i++) {
          Yestimada[i]=b0+b1*datos[i][0]+b2*datos[i][1];
          auxiliar[i][5]=datos[i][2]-Yestimada[i];
          sumatorias[8]+=auxiliar[i][5];
       }
       //calculamos Se
       Double Se =Math.sqrt(Math.pow(sumatorias[8], 2)/(N-2-1));
        System.out.println("Se: "+Se);
       
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
