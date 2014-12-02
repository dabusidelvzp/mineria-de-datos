/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pdf;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 *
 * @author hazel
 */
public class PDFdescriptiva {
     private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    
    public static void crearPDF(String titulo,String[][] arreglo,String nombre,Integer N,String[] variables) throws DocumentException, FileNotFoundException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(nombre+".pdf"));
        document.open();
        Paragraph preface = new Paragraph();
        // agregamos el titulo y un parrafo
        agregarTitulo(document,titulo);
        agregarContenido(document,titulo,arreglo,N,variables);
        

        document.close();
    }
    
    private static void agregarContenido(Document document,String titulo,String[][] arreglo,Integer N,String[] variables) throws DocumentException {
        
        agregarTabla(document,titulo,arreglo);
        
        Anchor anchor = new Anchor("Datos", catFont);
        anchor.setName("Datos");
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);
        
        Paragraph subPara = new Paragraph("Variables", subFont);
        Section subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("N: "+N));
        subCatPart.add(new Paragraph("Maximo: "+variables[0]));
        subCatPart.add(new Paragraph("Minimo: "+variables[1]));
        subCatPart.add(new Paragraph("Rango: "+variables[2]));
        subCatPart.add(new Paragraph("Intervalos: "+variables[3]));
        subCatPart.add(new Paragraph("Amplitud: "+variables[4]));
        subCatPart.add(new Paragraph("Rango Ampliado: "+variables[5]));
        subCatPart.add(new Paragraph("Dif Rangos: "+variables[6]));
        subCatPart.add(new Paragraph("LIPI: "+variables[7]));
        subCatPart.add(new Paragraph("LSUI: "+variables[8]));
        subCatPart.add(new Paragraph("Desv Media: "+variables[9]));
        subCatPart.add(new Paragraph("Varianza: "+variables[10]));
        subCatPart.add(new Paragraph("Desv Estandar: "+variables[11]));
        subCatPart.add(new Paragraph("Media: "+variables[12]));
        subCatPart.add(new Paragraph("Mediana: "+variables[13]));
        subCatPart.add(new Paragraph("Moda: "+variables[14]));
        
        
        
        
        document.add(catPart);
    }
    
    private static void agregarTitulo(Document documento,String titulo) throws DocumentException{
        Paragraph preface = new Paragraph();
        // Agregamos una linea vacia
        addEmptyLine(preface, 1);
        // Escribimos el titulo
        preface.add(new Paragraph(titulo, catFont));
        addEmptyLine(preface, 2);
        
        preface.add(new Paragraph("A continuacion se presenta el desarrollo para la estadistica descriptiva, se muestra la tabla con las operaciones necesarias y algunas variables de gran importancia.", smallBold));
       
        addEmptyLine(preface, 1);
        documento.add(preface);
    }
    
    private static void tablaPrediccion(Section subCatPart,Double Y,Double li,Double ls) throws DocumentException {
         PdfPTable table = new PdfPTable(3);
        
        PdfPCell c1 = new PdfPCell(new Phrase("Y estimada"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Li"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Ls"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        table.setHeaderRows(1);
        table.addCell(Y+"");
        table.addCell(li+"");
        table.addCell(ls+"");
        subCatPart.add(table);
        
    }
    
    private static void agregarTabla(Document doc,String titulo,String[][] arreglo) throws DocumentException{
        PdfPTable table = new PdfPTable(9);
        
        PdfPCell c1 = new PdfPCell(new Phrase("i"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Li"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Ls"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Xi"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Fi"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Fa"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Fr"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Fra"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("XiFi"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        
        
        

        table.setHeaderRows(1);
        for (String[] arreglo1 : arreglo) {
            table.addCell(arreglo1[0]);
            table.addCell(arreglo1[1]);
            table.addCell(arreglo1[2]);
            table.addCell(arreglo1[3]);
            table.addCell(arreglo1[4]);
            table.addCell(arreglo1[5]);
            table.addCell(arreglo1[6]);
            table.addCell(arreglo1[7]);
            table.addCell(arreglo1[8]);
            
        }
        doc.add(table);

  }
    
    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
          paragraph.add(new Paragraph(" "));
        }
    }

    
}
