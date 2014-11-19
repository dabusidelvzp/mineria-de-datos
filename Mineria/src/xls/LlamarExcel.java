package xls;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import regresiones.EstadisticaDescriptiva;
import regresiones.MineriaTexto;
import regresiones.RegresionMultiple;
import regresiones.RegresionSimple;


public class LlamarExcel implements DropTargetListener{
    private DropTarget dt;
    private JTable jtable;
    private DefaultTableModel TableModel = new DefaultTableModel();
    private Integer columnas =0;
    private JTabbedPane p1;
    private boolean isTexto;
    
    public LlamarExcel( JTable jtable ,Integer c,JTabbedPane pestana,File archivo){
        this.jtable = jtable;
        this.columnas=c;
        p1 = pestana;
        isTexto=false;
        dt = new DropTarget( jtable , this );  
        
        if(archivo!= null){
            archivoTransferido(archivo);
        }
        
    }
    
  @Override
  public void dragEnter(DropTargetDragEvent dtde) {}

  @Override
  public void dragExit(DropTargetEvent dte) {}

  @Override
  public void dragOver(DropTargetDragEvent dtde) {}

  @Override
  public void dropActionChanged(DropTargetDragEvent dtde) {}

  @Override
  public void drop(DropTargetDropEvent dtde) {
     try {
            /* proporciona datos para operaciones de transferencia en swing */
            Transferable tr = dtde.getTransferable();
            /* Devuelve una array de objetos DataFlavor */
            DataFlavor[] flavors = tr.getTransferDataFlavors();                        
            if( flavors.length > 0 ){                    
                /* Si existe una lista de objetos de archivo */
                if ( flavors[0].isFlavorJavaFileListType() ) {                     
                    dtde.acceptDrop( DnDConstants.ACTION_COPY );                    
                    /* obtiene un List con los archivos arrastrados al componente */
                    java.util.List list = (java.util.List) tr.getTransferData( flavors[0] );                    
                    if( !list.isEmpty() ){ /* abre el primer archivo */                        
                        File file = new File( list.get(0).toString() );
                        if ( file.exists() ){    
                            /* SI el archivo corresponde a un archivo excel */
                            if(columnas==4) {
                                if( file.getName().endsWith("txt") )
                                {
                                    readTXT( file );
                                }else{
                                    JOptionPane.showMessageDialog(null,"Formato incorrecto,el formato usado es txt","INCOMPATIBILIDAD DE ARCHIVOS", JOptionPane.ERROR_MESSAGE );                                

                                }  
                            } else {
                                if( file.getName().endsWith("xls") )
                                {
                                    readXLS( file );
                                }else{
                                    JOptionPane.showMessageDialog(null,"Formato incorrecto,el formato usado es xls","INCOMPATIBILIDAD DE ARCHIVOS", JOptionPane.ERROR_MESSAGE );                                

                                }  
                            }
                                                      
                        }else{ System.err.println( "error archivo no existe " ); }
                    }                    
                    dtde.dropComplete(true);
                    return;
                }
            }
            System.err.println("Drop failed: " + dtde );
            dtde.rejectDrop();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
  }

  /**
   * Metodo para leer solo la primera hoja de un archivo excel y colocar los datos
   * en un DefaultTableModel
   * @param File xls archivo excel
   */
  private void readXLS( File xls ){               
        try { 
            Workbook workbook = Workbook.getWorkbook( xls );            
            /* Si existen hojas */
            if( workbook.getNumberOfSheets() > 0 ){ //validar el numero de hoja...
                //pedimos el numero de la pagina
                 String numero = null;
                do {            
                numero = JOptionPane.showInputDialog("Escribe el numero de la página donde están los datos");
                }while (!this.isNumeric(numero));
                //validamos el numero de hoja
                //primero validamos que no sea null o se cancele la operacion
                if(numero!=null){
                    Integer numeroPagina=Integer.parseInt(numero);
                    //validamos que el numero sea valido de acuerdo a las hojas existentes
                    if(numeroPagina>0 && numeroPagina<=workbook.getNumberOfSheets()){
                        Sheet hoja = workbook.getSheet( numeroPagina-1 );                
                        
                        /* forma el array para los nombres de las columnas del JTable */

                        if(hoja.getColumns()>=columnas) {
                             String[] columNames = new String[ columnas];
                            /* Forma la matriz para los datos */
                            Double[][] data = new Double[ hoja.getRows() ][ columnas ]; 
                            String[][] texto = new String[hoja.getRows()][columnas];
                            /* Recorre todas las celdas*/
                            String celda="";
                            Integer contador=0;
                            System.out.println("numero de filas:"+ hoja.getRows());
                            for ( int fila = 0; fila < hoja.getRows(); fila++ )
                                //sumar los putos valores de cada celda --(celda+=; en c/iteracion)
                            {   
                                
                                for ( int columna = 0; columna  < columnas ; columna++ )
                                {
                                    /* Asigna nombre de columna */
                                    columNames[columna]="COLUMNA " + ( columna + 1 );
                                    /* Lee celda y coloca en el array */
                                    celda= hoja.getCell(columna, fila).getContents();
                                    if(isDouble(celda))
                                         data[ fila-contador ][ columna ] = Double.parseDouble(celda);
                                    else
                                         contador++;
                                    
                                    
                                        
                                }                                        
                                //VERIFICAR LA ASIGNACION DEL LOS NOMBRE DE LA COLUMNAS
                            }
                            //rearmamos el arreglo para quitarle registros vacios
                           
                           
                                Double[][] datos= depurarArreglo(data,contador);
                                TableModel = new DefaultTableModel( datos, columNames );
                                jtable.setModel(TableModel);
                                //Resolvemos 
                                if(columnas==1){
                                    EstadisticaDescriptiva  e  = new EstadisticaDescriptiva(datos);
                                    e.resolver(p1);

                                }else if(columnas==2){
                                    RegresionSimple simple = new RegresionSimple(datos);
                                    System.out.println("entro 2");
                                    simple.Resolver(p1);

                                }else if(columnas==3){
                                    RegresionMultiple multiple= new RegresionMultiple(datos);
                                    multiple.resolver(p1);
                                }
                            
                        
                        } else {
                            JOptionPane.showMessageDialog(null, "Columnas insuficientes.", "Columas error", JOptionPane.WARNING_MESSAGE);
                        }
                    }else {
                        JOptionPane.showMessageDialog(null, "Numero de página no válido,verificalo.", "Página error", JOptionPane.WARNING_MESSAGE);
                    }
                }
                
            } else {
                JOptionPane.showMessageDialog(null, "El archivo está vacio", "Archivo vacio", JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (IOException ex) {
            System.err.println( ex.getMessage() );
        } catch (BiffException ex) {
            System.err.println( ex.getMessage() );
        }
  }
  
  public void archivoTransferido(File archivo) {
                        File file = archivo;
                        System.out.println("entró");
                        if ( file.exists() ){     
                            /* SI el archivo corresponde a un archivo excel */
                             if(columnas==4) {
                                if( file.getName().endsWith("txt") )
                                {
                                    readTXT( file );
                                }else{
                                    JOptionPane.showMessageDialog(null,"Formato incorrecto,el formato usado es txt","INCOMPATIBILIDAD DE ARCHIVOS", JOptionPane.ERROR_MESSAGE );                                

                                }  
                            } else {
                                if( file.getName().endsWith("xls") )
                                {
                                    readXLS( file );
                                }else{
                                    JOptionPane.showMessageDialog(null,"Formato incorrecto,el formato usado es xls","INCOMPATIBILIDAD DE ARCHIVOS", JOptionPane.ERROR_MESSAGE );                                

                                }      
                           }
                        }else{ System.err.println( "error archivo no existe " ); }
                   
  }
  
  public DefaultTableModel getTableModel()
  {
      return this.TableModel;
  }
  
  public DropTarget getDropTarget()
  {
      return this.dt;
  }
  
  public boolean isNumeric(String cadena){
	try {
		Integer.parseInt(cadena);
		return true;
	} catch (NumberFormatException nfe){
		return false;
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

    private Double[][] depurarArreglo(Double[][] data,Integer c) {
        Double[][] datos= new Double[data.length-c][data[0].length];
        for(int i=0;i<datos.length;i++){
            for(int j=0;j<datos[0].length;j++){
                datos[i][j]=data[i][j];
            }
        }
        return datos;
    }

    private String quitarAcentos(String cadena) {
        cadena=cadena.replaceAll("Á", "A");
        cadena=cadena.replaceAll("É", "E");
        cadena=cadena.replaceAll("Í", "I");
        cadena=cadena.replaceAll("Ó", "O");
        cadena=cadena.replaceAll("Ú", "U");
        return cadena;
    }

    private void readTXT(File file) {
        BufferedReader br=null;
        ArrayList<String> lineas = new ArrayList<String>();
        try {
            
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            // Lectura del fichero
            String linea;
            while((linea=br.readLine())!= null){
               
                lineas.add(quitarAcentos(linea.toUpperCase()));
            }
            String[][] datos = new String[lineas.size()][1];
            for (int i = 0; i < lineas.size(); i++) {
                datos[i][0] = lineas.get(i);
            }
            String[] titulo = {"texto"};
            TableModel = new DefaultTableModel( datos, titulo );
            jtable.setModel(TableModel);
            MineriaTexto mineria = new MineriaTexto();
            mineria.resolver(datos, p1);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LlamarExcel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LlamarExcel.class.getName()).log(Level.SEVERE, null, ex);
        } 
         
    }
          
}
