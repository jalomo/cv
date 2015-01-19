package com.zavordigital.campoverde;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

 
public class preguntasAdapter extends BaseAdapter {
 
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
 
    public preguntasAdapter(Activity a,ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }
 
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_item_preguntas, null);
 
        TextView titulo = (TextView)vi.findViewById(R.id.pregunta); 
        TextView opciones = (TextView)vi.findViewById(R.id.opciones); 
        //TextView id_evento = (TextView)vi.findViewById(R.id.id_encuesta);
   
       
        HashMap<String, String> campos = new HashMap<String, String>();
        campos = data.get(position);
 
        // Setting all values in listview
        //id_evento.setText(campos.get(encuestas.TAG_encuestaId));
        titulo.setText(campos.get(encuestaPreguntas.TAG_preguntaTitulo));
        
        opciones.setText(campos.get(encuestaPreguntas.TAG_preguntaOpciones));
       
      
        
        return vi;
    }
    
    
}    