package es.upm.miw.SolitarioCelta;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {

	JuegoCelta juego;
    private final String GRID_KEY = "GRID_KEY";
    public String estadoGuardado;
    private int numFichas;
    private final String  ficheroPartidaGuardada  = "PartidaGuardada.text";
    private final String  resultados  = "Resultados.text";
    Chronometer cronometro;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        juego = new JuegoCelta();
        mostrarTablero();
        cronometro=((Chronometer) findViewById(R.id.chronometer));
        cronometro.start();
    }

    /**
     * Se ejecuta al pulsar una ficha
     * Las coordenadas (i, j) se obtienen a partir del nombre, ya que el botón
     * tiene un identificador en formato pXY, donde X es la fila e Y la columna
     * @param v Vista de la ficha pulsada
     */
    public void fichaPulsada(View v) throws IOException {
        String resourceName = getResources().getResourceEntryName(v.getId());
        int i = resourceName.charAt(1) - '0';   // fila
        int j = resourceName.charAt(2) - '0';   // columna

        juego.jugar(i, j);

        mostrarTablero();
        if (juego.juegoTerminado()) {
            // TODO guardar puntuación
            cronometro.stop();
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            String nombre = sharedPref.getString("nombreJugador","default");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTimeStamp = dateFormat.format(new Date());
            Resultado resultado = new Resultado(nombre,currentTimeStamp,juego.numeroFichas());
            guardarResultado(resultado);
            new AlertDialogFragment().show(getFragmentManager(), "ALERT DIALOG");
        }
    }

    /**
     * Visualiza el tablero
     */
    public void mostrarTablero() {
        RadioButton button;
        String strRId;
        String prefijoIdentificador = getPackageName() + ":id/p"; // formato: package:type/entry
        int idBoton;

        for (int i = 0; i < JuegoCelta.TAMANIO; i++)
            for (int j = 0; j < JuegoCelta.TAMANIO; j++) {
                strRId = prefijoIdentificador + Integer.toString(i) + Integer.toString(j);
                idBoton = getResources().getIdentifier(strRId, null, null);
                if (idBoton != 0) { // existe el recurso identificador del botón
                    button = (RadioButton) findViewById(idBoton);
                    button.setChecked(juego.obtenerFicha(i, j) == JuegoCelta.FICHA);
                }
            }
    }

    /**
     * Guarda el estado del tablero (serializado)
     * @param outState Bundle para almacenar el estado del juego
     */
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(GRID_KEY, juego.serializaTablero());
        super.onSaveInstanceState(outState);
    }

    /**
     * Recupera el estado del juego
     * @param savedInstanceState Bundle con el estado del juego almacenado
     */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String grid = savedInstanceState.getString(GRID_KEY);
        juego.deserializaTablero(grid);
        mostrarTablero();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.opciones_menu, menu);
        return true;
    }

    public void recuperarPartida() throws IOException {
        if(this.numFichas>juego.numeroFichas()){
            BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput(ficheroPartidaGuardada)));
            this.estadoGuardado=fin.readLine();
            fin.close();
            if(this.estadoGuardado!=null) {
                DialogFragment dialogFragment = new RetrieveDialogFragment();
                dialogFragment.show(getFragmentManager(), "retrieve");
                this.numFichas=juego.numeroFichas();
                Toast.makeText(this, getString(R.string.txtPartidaRecuperada), Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput(ficheroPartidaGuardada)));
            this.estadoGuardado=fin.readLine();
            fin.close();
            if(this.estadoGuardado!=null)
            {
                juego.deserializaTablero(estadoGuardado);
                this.mostrarTablero();
                this.numFichas=juego.numeroFichas();
                Toast.makeText(this, getString(R.string.txtPartidaRecuperada), Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, getString(R.string.txtNoPartidaGuardada), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void guardarPartida() throws IOException {
        this.estadoGuardado=juego.serializaTablero();
        FileOutputStream fos = openFileOutput(ficheroPartidaGuardada, Context.MODE_PRIVATE);
        fos.write(estadoGuardado.getBytes());
        fos.close();
        Toast.makeText(this, getString(R.string.txtPartidaGuardada), Toast.LENGTH_SHORT).show();
        this.numFichas=juego.numeroFichas();
    }

    public void guardarResultado(Resultado resultado) throws IOException {
        FileOutputStream fos = openFileOutput(resultados, Context.MODE_APPEND);
        fos.write(resultado.getNombreJugador().getBytes());
        fos.write("\t".getBytes());
        fos.write(resultado.getFecha().getBytes());
        fos.write("\t".getBytes());
        fos.write(String.valueOf(resultado.getNumPiezas()).getBytes());
        fos.write("\n".getBytes());
        fos.close();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.opcAjustes:
                startActivity(new Intent(this, SCeltaPrefs.class));
                return true;
            case R.id.opcAcercaDe:
                startActivity(new Intent(this, AcercaDe.class));
                return true;
            case R.id.opcGuardarPartida:
                try {
                    guardarPartida();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.opcMejoresResultados:
                startActivity(new Intent(this, MostrarResultados.class));
                return true;
            case R.id.opcBorrarResultados:
                FileOutputStream fos = null;
                try {
                    fos = openFileOutput(resultados, Context.MODE_PRIVATE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    fos.write("".getBytes());
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(this, "Resultados eliminados", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.opcRecuperarPartida:
                try {
                    recuperarPartida();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.opcReiniciarPartida:
                DialogFragment dialogFragment = new RestartDialogFragment();
                dialogFragment.show(getFragmentManager(),"restart");
                return true;

            default:
                Toast.makeText(
                        this,
                        getString(R.string.txtSinImplementar),
                        Toast.LENGTH_SHORT
                ).show();
        }
        return true;
    }
}
