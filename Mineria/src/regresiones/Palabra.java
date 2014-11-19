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
public class Palabra {
    private String palabra;
    private Integer repetido;

    public String getPalabra() {
        return palabra;
    }

    public void setPalabra(String palabra) {
        this.palabra = palabra;
    }

    public Integer getRepetido() {
        return repetido;
    }

    public void setRepetido(Integer repetido) {
        this.repetido = repetido;
    }
    
    public Palabra(String p,Integer r) {
        palabra=p;
        repetido=r;
    }
    
    
}
