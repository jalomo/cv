package com.zavordigital.campoverde;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class citasAdapter extends BaseAdapter {

   private Activity activity;
   private ArrayList<HashMap<String, String>> data;
   private static LayoutInflater inflater=null;
   public ImageLoader imageLoader; 

   public citasAdapter(Activity a,ArrayList<HashMap<String, String>> d) {
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
           vi = inflater.inflate(R.layout.list_item_citas, null);

       TextView titulo = (TextView)vi.findViewById(R.id.title); 
       TextView fecha = (TextView)vi.findViewById(R.id.fecha); 
       TextView id_evento = (TextView)vi.findViewById(R.id.id_evento);
       TextView texto = (TextView)vi.findViewById(R.id.texto);
       TextView lugar = (TextView)vi.findViewById(R.id.lugar);
       TextView latitud = (TextView)vi.findViewById(R.id.latitud);
       TextView tel_ = (TextView)vi.findViewById(R.id.tel_);
       TextView hora = (TextView)vi.findViewById(R.id.hora);
      
       HashMap<String, String> campos = new HashMap<String, String>();
       campos = data.get(position);

       // Setting all values in listview
       id_evento.setText(campos.get(lista_citas.TAG_citaId));
       titulo.setText(campos.get(lista_citas.TAG_citaNombre));
      // texto.setText(campos.get(lista_citas.TAG_citaFecha));
      // hora.setText(campos.get(lista_citas.TAG_citaHora));
       fecha.setText(campos.get(lista_citas.TAG_citaHora));
       //latitud.setText(campos.get(lista_citas.TAG_eventosLatitud));
       tel_.setText(campos.get(lista_citas.TAG_citaTelefono));
       lugar.setText(campos.get(lista_citas.TAG_citaFecha));
       
      
       return vi;
   }
}    