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
import android.widget.TextView;

 
public class noticiasAdapter extends BaseAdapter {
 
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
 
    public noticiasAdapter(Activity a,ArrayList<HashMap<String, String>> d) {
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
        	if (position % 2 == 0) {
        		   vi = inflater.inflate(R.layout.list_item, null);
        		  } else {
        			  vi = inflater.inflate(R.layout.list_item2, null);
        		  }
            
 
        TextView titulo = (TextView)vi.findViewById(R.id.title); 
        TextView fecha = (TextView)vi.findViewById(R.id.fecha); 
        TextView id_noticia = (TextView)vi.findViewById(R.id.id_noticia);
        TextView texto = (TextView)vi.findViewById(R.id.texto);
        TextView url_image = (TextView)vi.findViewById(R.id.url_image);
       
        HashMap<String, String> campos = new HashMap<String, String>();
        campos = data.get(position);
 
        // Setting all values in listview
        titulo.setText(campos.get(noticiasComunicados.TAG_noticiasTitulo));
        fecha.setText(campos.get(noticiasComunicados.TAG_noticiasFecha));
        id_noticia.setText(campos.get(noticiasComunicados.TAG_noticiasId));
        texto.setText(campos.get(noticiasComunicados.TAG_noticiasTexto));
        url_image.setText(campos.get(noticiasComunicados.TAG_noticiasPathImg));
       
        return vi;
    }
}    