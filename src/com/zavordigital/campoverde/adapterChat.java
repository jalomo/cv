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


public class adapterChat extends BaseAdapter {

   private Activity activity;
   private ArrayList<HashMap<String, String>> data;
   private static LayoutInflater inflater=null;
   //public ImageLoader imageLoader; 

   public adapterChat(Activity a,ArrayList<HashMap<String, String>> d) {
       activity = a;
       data=d;
       inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       //imageLoader=new ImageLoader(activity.getApplicationContext());
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
       //if(convertView==null)
       	/*if (position % 2 == 0) {
    		   vi = inflater.inflate(R.layout.item_chat, null);
    		  } else {
    			  vi = inflater.inflate(R.layout.item_chat, null);
    		  }

       
       */
      
       HashMap<String, String> campos = new HashMap<String, String>();
       campos = data.get(position);
       
       
       String id_invitado=campos.get(chat.TAG_mensajeIdUsuario);
       String mi_id=campos.get(chat.TAG_miID);
       
       if(convertView==null)
       if(id_invitado.trim().equals(mi_id.trim())){
    	   
    	   vi = inflater.inflate(R.layout.item_chat, null);
       }else{
    	   vi = inflater.inflate(R.layout.item_chat_oculto, null);
       }
       
       TextView texto = (TextView)vi.findViewById(R.id.texto); 
       TextView id = (TextView)vi.findViewById(R.id.id); 
       TextView grupo = (TextView)vi.findViewById(R.id.grupo); 
       

       // Setting all values in listview
       texto.setText(campos.get(chat.TAG_mensajeTexto));
       id.setText(campos.get(chat.TAG_mensajeIdUsuario));
       grupo.setText(campos.get(chat.TAG_miID));
      
       
      
       return vi;
   }
}  