package es.upm.miw.SolitarioCelta;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.*;

/**
 * Created by user on 31/10/2016.
 */

public class RetrieveDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final MainActivity main = (MainActivity) getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(main);
        builder
                .setTitle(R.string.txtDialogoRecuperarTitulo)
                .setMessage(R.string.txtDialogoRecuperarPregunta)
                .setPositiveButton(
                        getString(R.string.txtDialogoRecuperarAfirmativo),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                main.juego.deserializaTablero(main.estadoGuardado);
                                main.mostrarTablero();
                            }
                        }
                )
                .setNegativeButton(
                        getString(R.string.txtDialogoRecuperarNegativo),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }
                );

        return builder.create();
    }

}
