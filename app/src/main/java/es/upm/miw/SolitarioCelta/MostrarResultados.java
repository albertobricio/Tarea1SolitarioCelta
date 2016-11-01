package es.upm.miw.SolitarioCelta;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by user on 31/10/2016.
 */

public class MostrarResultados extends Activity {

    Context context = null;
    List<String> resultados = new ArrayList<String>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_resultados);

        context = getApplicationContext();

        ListView lvlistado = (ListView) findViewById(R.id.listado);

        try {
            resultados = obtenerResultadosFichero();
            List<Resultado> listaObjetosResultado = obtenerListaObjetosResultado(resultados);
            resultados=ordenarResultados(listaObjetosResultado);
            Adaptador adaptador = new Adaptador(this, R.layout.elemento, resultados);
            lvlistado.setAdapter(adaptador);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Resultado> obtenerListaObjetosResultado(List<String> resultados) {
        List<Resultado> list = new ArrayList<Resultado>();
        for (String resul : resultados)
        {
            String[] separated = resul.split("\t");
            Resultado r = new Resultado(
                    separated[0],
                    separated[1],
                    Integer.parseInt(separated[2])
            );
            list.add(r);
        }
        return list;
    }

    private List<String> ordenarResultados(List<Resultado> resultados) {
        List<String> listaOrdenada = new ArrayList<String>();
        Collections.sort(resultados);
        for (Resultado r : resultados){
            String resultadoOrdenado ="NOMBRE JUGADOR:"+r.getNombreJugador()+"\n"+
                    "FECHA:"+r.getFecha()+"\n"+
                    "PIEZAS RESTANTES:"+r.getNumPiezas();
            listaOrdenada.add(resultadoOrdenado);
        }
        return listaOrdenada;
    }

    private List<String> obtenerResultadosFichero() throws IOException {
        BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput("Resultados.text")));
        String linea = fin.readLine();
        while (linea!=null) {
            resultados.add(linea);
            linea=fin.readLine();
        }
        fin.close();
        return resultados;
    }

}
