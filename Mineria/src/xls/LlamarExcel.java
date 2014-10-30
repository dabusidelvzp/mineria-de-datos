package xls;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


public class LlamarExcel implements DropTargetListener{
    private DropTarget dt;
    private JTable jtable;
    private DefaultTableModel TableModel = new DefaultTableModel();
    private Integer columnas =0;
    
    public LlamarExcel( JTable jtable ,Integer c){
        this.jtable = jtable;
        this.columnas=c;
        dt = new DropTarget( jtable , this );  
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
                            if( file.getName().endsWith("xls") )
                            {
                                readXLS( file );
                            }else{
                                JOptionPane.showMessageDialog(null,"Formato incorrecto,el formato usado es xls","INCOMPATIBILIDAD DE ARCHIVOS", JOptionPane.ERROR_MESSAGE );                                
                                
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
            System.err.println( ex.getMessage() );
            dtde.rejectDrop();
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
                String numero = JOptionPane.showInputDialog("Escribe el numero de la página donde están los datos");
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
                            Object[][] data = new String[ hoja.getRows() ][ columnas ];  
                            /* Recorre todas las celdas*/
                            for ( int fila = 0; fila < hoja.getRows(); fila++ )
                                //sumar los putos valores de cada celda --(celda+=; en c/iteracion)
                            {   
                                
                                for ( int columna = 0; columna  < columnas ; columna++ )
                                {
                                    /* Asigna nombre de columna */
                                    columNames[columna]="COLUMNA " + ( columna + 1 );
                                    /* Lee celda y coloca en el array */
                                    data[ fila ][ columna ] = hoja.getCell(columna, fila).getContents();
                                }                                        
                                //VERIFICAR LA ASIGNACION DEL LOS NOMBRE DE LA COLUMNAS
                            }
                            /* Crea el TableModel y asigna a tabla */
                            TableModel = new DefaultTableModel( data, columNames );
                            jtable.setModel(TableModel);
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
  
  public DefaultTableModel getTableModel()
  {
      return this.TableModel;
  }
  
  public DropTarget getDropTarget()
  {
      return this.dt;
  }
  
}
