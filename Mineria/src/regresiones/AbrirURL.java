/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package regresiones;

import java.awt.Desktop;
import java.net.URI;

public class AbrirURL {
	public static void main(String[] a)throws Exception {
            try{
		Desktop.getDesktop().browse(new URI("http://issuu.com/robotronwi/docs/guia_c4c844641a30ef"));
            }catch(Exception e){
                        
                        }
	}
}