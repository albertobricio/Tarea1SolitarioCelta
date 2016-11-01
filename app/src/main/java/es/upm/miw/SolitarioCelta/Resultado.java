package es.upm.miw.SolitarioCelta;

import java.util.Comparator;

/**
 * Created by user on 31/10/2016.
 */

public class Resultado implements Comparable<Resultado>{

    private String nombreJugador;
    private String fecha;
    private int numPiezas;

    public Resultado(String nombreJugador, String fecha, int numPiezas) {
        this.nombreJugador = nombreJugador;
        this.fecha=fecha;
        this.numPiezas=numPiezas;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getNumPiezas() {
        return numPiezas;
    }

    public void setNumPiezas(int numPiezas) {
        this.numPiezas = numPiezas;
    }

    @Override
    public int compareTo(Resultado resultado) {
        if(numPiezas<resultado.numPiezas) {
            return -1;
        }
        if(numPiezas>resultado.numPiezas){
            return 1;
        }
        return 0;
    }
}
