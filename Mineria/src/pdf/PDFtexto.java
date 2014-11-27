/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pdf;

/**
 *
 * @author hazel
 */
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.*;

public class PDFtexto {
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    
    public static void  crearPDF(String titulo,String[][] arreglo,String nombre) throws FileNotFoundException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(nombre+".pdf"));
        document.open();
        Paragraph preface = new Paragraph();
        // agregamos el titulo y un parrafo
        agregarTitulo(document,titulo);
        agregarContenido(document,titulo,arreglo);
        

        document.close();
    }
    
    private static void agregarContenido(Document document,String titulo,String[][] arreglo) throws DocumentException {
        
        agregarTabla(document,titulo,arreglo);
        //document.add(catPart);
    }
    
    private static void agregarTitulo(Document documento,String titulo) throws DocumentException{
        Paragraph preface = new Paragraph();
        // Agregamos una linea vacia
        addEmptyLine(preface, 1);
        // Escribimos el titulo
        preface.add(new Paragraph(titulo, catFont));
        addEmptyLine(preface, 2);
        if(titulo.equals("Mineria de Texto")){
            preface.add(new Paragraph("A continuacion se presentan las palabras recopiladas a partir de la mineria de texto.", smallBold));
        }
        addEmptyLine(preface, 1);
        documento.add(preface);
    }
    
    private static void agregarTabla(Document doc,String titulo,String[][] arreglo) throws DocumentException{
        PdfPTable table = new PdfPTable(2);
        
        if(titulo.equals("Mineria de Texto")){
            PdfPCell c1 = new PdfPCell(new Phrase("Palabra"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase("Repetido"));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
            table.setHeaderRows(1);
            for (String[] arreglo1 : arreglo) {
                table.addCell(arreglo1[0]);
                table.addCell(arreglo1[1]);
            }
        }
        doc.add(table);

  }
    
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
          paragraph.add(new Paragraph(" "));
        }
    }
    
   
}
