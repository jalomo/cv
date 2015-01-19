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




public class adapterChatAlumno extends BaseAdapter {

   private Activity activity;
   private ArrayList<HashMap<String, String>> data;
   private static LayoutInflater inflater=null;
   //public ImageLoader imageLoader; 

   public adapterChatAlumno(Activity a,ArrayList<HashMap<String, String>> d) {
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
       if(convertView==null)
       	if (position % 2 == 0) {
    		   vi = inflater.inflate(R.layout.item_usuarios, null);
    		  } else {
    			  vi = inflater.inflate(R.layout.item_usuarios, null);
    		  }

       TextView email = (TextView)vi.findViewById(R.id.email); 
       TextView id = (TextView)vi.findViewById(R.id.id); 
       
      
       HashMap<String, String> campos = new HashMap<String, String>();
       campos = data.get(position);

       // Setting all values in listview
       email.setText(campos.get(chat_lista_alumnos.TAG_email));
       id.setText(campos.get(chat_lista_alumnos.TAG_id));
      
       
      
       return vi;
   }
}   
