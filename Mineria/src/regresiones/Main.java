
import java.util.Locale;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYDataset;

public class Main {
    public static void main(String args[]){
        // Fuente de Datos
        DefaultPieDataset data = new DefaultPieDataset();
        data.setValue("C", 40);
        data.setValue("Java", 45);
        data.setValue("Python", 15);
 
        // Creando el Grafico
        JFreeChart chart = ChartFactory.createPieChart("Ejemplo Rapido de Grafico en un ChartFrame",data,true,true,false);
        
        //graficas de pastel 3D
      //  JFreeChart chart1 = ChartFactory.createPieChart3D("Ejemplo 2", data, true, true, false);
        
       // JFreeChart chart1 = ChartFactory.createXYAreaChart("", "X", "", );
                
        // Mostrar Grafico
        ChartFrame frame = new ChartFrame("JFreeChart", chart);
        //ChartFrame frame2 = new ChartFrame("JFreeChart", chart1);
        
        frame.pack();
        frame.setVisible(false);
        
        //frame2.pack();
        //frame2.setVisible(true);
    }
}
 


//Post completo en: http://jonathanmelgoza.com/blog/como-hacer-graficos-con-java/#ixzz3Ian5bprM