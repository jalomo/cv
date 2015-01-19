package com.zavordigital.campoverde;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class lista_citas extends Activity {
	
	private static String url = "http://www.zavordigital.com/campoverde/index.php/mobiles/get_citas/";
	private static String url_eliminar_cita = "http://www.zavordigital.com/campoverde/index.php/mobiles/deleteCita/";
	JSONParser jsonParser = new JSONParser();
	JSONArray lista = null;
	
	private ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
	
	private static ProgressDialog progress;
	private static citasAdapter adapter;
	private ListView listView;
	
	static final String TAG_citaId = "citaId";
	static final String TAG_citaIdFamilia= "citaIdFamilia";
	static final String TAG_citaIdMaestro= "citaIdMaestro";
	static final String TAG_citaNombre= "citaNombre";
	static final String TAG_citaTelefono= "citaTelefono";
	static final String TAG_citaFecha= "citaFecha";
	static final String TAG_citaHora= "citaHora";

	
	
	String idMaestro="";
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_cita_maestro);
        
        Intent in = getIntent();
        idMaestro = in.getStringExtra("id_maestro");
    
		adapter = new citasAdapter(this, contactList);
		listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);
        
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String idCita = ((TextView) view.findViewById(R.id.id_evento))
						.getText().toString();
				String tel_ = ((TextView) view.findViewById(R.id.tel_))
						.getText().toString();
				
				showPopUpPhone(idCita,tel_);
				
			}
		});
		
        progress = new ProgressDialog(this);
		progress.setTitle("Descargando");
		progress.setMessage("Espere por favor...");
		progress.setCancelable(false);
		progress.show();
		contactList.clear();
		adapter.notifyDataSetChanged();
		cargaNoticias();
        
    }
    
    
    
    private void showPopUpPhone(final String idCita,final String tel_) { 
		 
		 LayoutInflater factory = LayoutInflater.from(this);

		//text_entry is an Layout XML file containing two text field to display in alert dialog
		final View textEntryView = factory.inflate(R.layout.dialog_citias, null);

		 

		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setIcon(R.drawable.ic_launcher).setTitle("").setView(textEntryView).setPositiveButton("Aceptar",
		  new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog,
		     int whichButton) {

			  
		  
		    
		   
		    /* User clicked OK so do some stuff */
		   }
		  });/*.setNegativeButton("Cancelar",
		  new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog,
		     int whichButton) {
		     
		   }
		  });*/
		alert.show();
		 
		
		
		Button btn_llamar = (Button) textEntryView.findViewById(R.id.button1);
		btn_llamar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:"+tel_+""));
				startActivity(callIntent);
				
				Toast.makeText(getApplicationContext(), "llAMAR", Toast.LENGTH_SHORT).show();
			}
		}); 
		
		 Button btn_eliminar = (Button) textEntryView.findViewById(R.id.button2);
			btn_eliminar.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					
					eliminar_cita(idCita);
					
					Toast.makeText(getApplicationContext(), "elimanar", Toast.LENGTH_SHORT).show();
				}
			}); 
			
		
	      } 
	
    
    private static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			adapter.notifyDataSetChanged();
			progress.dismiss();
		}
	};
	
	
    
    public void cargaNoticias() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					
					String json = jsonParser.makeHttpRequest(url+idMaestro, "GET", params);
	
					Log.d("Albums JSON: ", "> " + json.toString());
					String citaId = "citaId";
					String citaIdFamilia= "citaIdFamilia";
					String citaIdMaestro= "citaIdMaestro";
					String citaNombre= "citaNombre";
					String citaTelefono= "citaTelefono";
					String citaFecha= "citaFecha";
					String citaHora= "citaHora";
					
					lista = new JSONArray(json);
					if (lista != null) {
						// looping through All albums
						for (int i = 0; i < lista.length(); i++) {
							JSONObject c = lista.getJSONObject(i);
						
							
							citaId = c.getString(TAG_citaId);
							citaIdFamilia = c.getString(TAG_citaIdFamilia);
							citaIdMaestro = c.getString(TAG_citaIdMaestro);
							citaNombre = c.getString(TAG_citaNombre);
							citaTelefono = c.getString(TAG_citaTelefono);
							citaFecha = c.getString(TAG_citaFecha);
							citaHora = c.getString(TAG_citaHora);
							
							

							// creating new HashMap
							HashMap<String, String> map = new HashMap<String, String>();

							map.put(TAG_citaId, citaId);
							map.put(TAG_citaIdFamilia, citaIdFamilia);
							map.put(TAG_citaIdMaestro, citaIdMaestro);
							map.put(TAG_citaNombre, citaNombre);
							map.put(TAG_citaTelefono, citaTelefono);
							map.put(TAG_citaFecha, citaFecha);
							map.put(TAG_citaHora, citaHora);
							
							

							// adding HashList to ArrayList
							contactList.add(map);

							
						}
						
						handler.sendEmptyMessage(0);
					} else {
						Log.d("TIENDAS: ", "null");
						handler.sendEmptyMessage(0);
					}
				} catch (Exception e) {
					handler.sendEmptyMessage(0);

				}
			}
		});
		t.start();
	}
    
    
    
    public void eliminar_cita(final String id_cita) {
    	


    		Thread t = new Thread(new Runnable() {
    			@Override
    			public void run() {
    				try {
    					List<NameValuePair> params = new ArrayList<NameValuePair>();
    					
    					
    					
    					String json = jsonParser.makeHttpRequest(url_eliminar_cita+id_cita, "GET", params);
    	
    					Log.d("Albums JSON: ", "> " + json.toString());
    					String id = "";
    					String nombre = "";
    					
    					lista = new JSONArray(json);
    					
    					if (lista != null) {
    						
    					} else {
    						Log.d("TIENDAS: ", "null");
    					}
    				} catch (Exception e) {

    				}
    				runOnUiThread(new Runnable() {
    			        public void run() {
    			        	progress = new ProgressDialog(lista_citas.this);
    			    		progress.setTitle("Descargando");
    			    		progress.setMessage("Espere por favor...");
    			    		progress.setCancelable(false);
    			    		progress.show();
    			    		contactList.clear();
    			    		adapter.notifyDataSetChanged();
    			    		cargaNoticias();
    			        }
    			    });
    				
    			}
    		});
    		t.start();
    		
    		
    		
    	}
    
    
}