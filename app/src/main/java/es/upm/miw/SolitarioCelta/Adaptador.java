package es.upm.miw.SolitarioCelta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 01/11/2016.
 */

public class Adaptador extends ArrayAdapter {

    private Context contexto;
    private List<String> resultados;
    private int idRecursoLayout;

    public Adaptador(Context context, int resource, List objects) {
        super(context, resource, objects);
        contexto=context;
        resultados = (List<String>) objects;
        idRecursoLayout=resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout vista;

        if (null != convertView) {
            vista = (LinearLayout) convertView;
        } else {
            LayoutInflater linf = (LayoutInflater) contexto
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vista = (LinearLayout) linf.inflate(idRecursoLayout, parent, false);
        }

        TextView tvResultado = (TextView) vista.findViewById(R.id.resultado);
        tvResultado.setText(resultados.get(position));

        return vista;
    }
}
