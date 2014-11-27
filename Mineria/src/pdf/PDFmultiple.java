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
public class PDFmultiple {
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    
    public static void crearPDF(String titulo,String[][] arreglo,String nombre,Integer N,Double b0,Double b1,Double b2,Double Se,Double[] prediccion) throws DocumentException, FileNotFoundException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(nombre+".pdf"));
        document.open();
        Paragraph preface = new Paragraph();
        // agregamos el titulo y un parrafo
        agregarTitulo(document,titulo);
        agregarContenido(document,titulo,arreglo,N,b0,b1,b2,Se,prediccion);
        

        document.close();
    }
    
    private static void agregarContenido(Document document,String titulo,String[][] arreglo,Integer N,Double b0,Double b1,Double b2,Double Se,Double[] prediccion) throws DocumentException {
        
        agregarTabla(document,titulo,arreglo);
        
        Anchor anchor = new Anchor("Datos", catFont);
        anchor.setName("Datos");
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);
        
        Paragraph subPara = new Paragraph("Variables", subFont);
        Section subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("N: "+N));
        subCatPart.add(new Paragraph("K: 2"));
        subCatPart.add(new Paragraph("b0: "+b0));
        subCatPart.add(new Paragraph("b1: "+b1));
        subCatPart.add(new Paragraph("b2: "+b2));
        subCatPart.add(new Paragraph("Se: "+Se));
        Paragraph espacio = new Paragraph();
        addEmptyLine( espacio, 1);
        subCatPart.add(espacio);
        subPara = new Paragraph("Predicciones", subFont);
        subCatPart = catPart.addSection(subPara);
        if(prediccion[0]!= null && prediccion[1]!= null && prediccion[2]!= null && prediccion[3]!= null && prediccion[4]!= null && prediccion[5]!= null){
            subCatPart.add(new Paragraph("X1: "+prediccion[0]));
            subCatPart.add(new Paragraph("X2: "+prediccion[1]));
            subCatPart.add(new Paragraph("Efectividad: "+prediccion[2]));
            addEmptyLine( espacio, 1);
            subCatPart.add(espacio);
            tablaPrediccion(subCatPart, prediccion[3], prediccion[4], prediccion[5]);
        } else {
            subCatPart.add(new Paragraph("No se realizó ninguna prediccion."));
        }
        
        document.add(catPart);
    }
    
    private static void agregarTitulo(Document documento,String titulo) throws DocumentException{
        Paragraph preface = new Paragraph();
        // Agregamos una linea vacia
        addEmptyLine(preface, 1);
        // Escribimos el titulo
        preface.add(new Paragraph(titulo, catFont));
        addEmptyLine(preface, 2);
        
        preface.add(new Paragraph("A continuacion se presenta el desarrollo para la regresión lineal multiple, se muestra la tabla con las operaciones necesarias, algunas variables de gran importancia y la prediccion de las variables.", smallBold));
       
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
        
        PdfPCell c1 = new PdfPCell(new Phrase("X1"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("X2"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Y"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Y estimada"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("X1^2"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("X2^2"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("X1*Y"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("X2*Y"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        
        c1 = new PdfPCell(new Phrase("Y-Y estimada"));
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
