package com.zavordigital.campoverde;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class encuestas extends Activity {
	
	private static String url = "http://zavordigital.com/campoverde/index.php/mobiles/get_encuesta";
	JSONParser jsonParser = new JSONParser();
	JSONArray lista = null;
	
	private ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
	
	private static ProgressDialog progress;
	private static encuestasAdapter adapter;
	private ListView listView;
	
	static final String TAG_encuestaId = "encuestaId";
	static final String TAG_encuestaTitulo= "encuestaTitulo";
	static final String TAG_encuestaFechaCreacion= "encuestaFechaCreacion";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_encuestas);
        
        
        
        adapter = new encuestasAdapter(this, contactList);
		listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);
        
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				String titulo = ((TextView) view.findViewById(R.id.title))
						.getText().toString();
				String id_encuesta = ((TextView) view.findViewById(R.id.id_encuesta))
						.getText().toString();
			
				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						encuestaPreguntas.class);
				in.putExtra(TAG_encuestaId, id_encuesta);
				in.putExtra(TAG_encuestaTitulo, titulo);
				
				
				startActivity(in);

			}
		});
        
        
        progress = new ProgressDialog(this);
		progress.setTitle("Descargando");
		progress.setMessage("Espere por favor...");
		progress.setCancelable(false);
		progress.show();
		contactList.clear();
		adapter.notifyDataSetChanged();
		cargaEncuestas();
		
	}
	
	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			adapter.notifyDataSetChanged();
			progress.dismiss();
		}
	};
	
	public void cargaEncuestas() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					String json = jsonParser.makeHttpRequest(url, "GET", params);
	
					Log.d("Albums JSON: ", "> " + json.toString());
					String encuestaId = "";
					String encuestaTitulo = "";
					String encuestaFechaCreacion = "";
					
					lista = new JSONArray(json);
					if (lista != null) {
						// looping through All albums
						for (int i = 0; i < lista.length(); i++) {
							JSONObject c = lista.getJSONObject(i);
						
							
							encuestaId = c.getString(TAG_encuestaId);
							encuestaTitulo = c.getString(TAG_encuestaTitulo);
							encuestaFechaCreacion = c.getString(TAG_encuestaFechaCreacion);
							
							// creating new HashMap
							HashMap<String, String> map = new HashMap<String, String>();

							map.put(TAG_encuestaId, encuestaId);
							map.put(TAG_encuestaTitulo,encuestaTitulo);
							map.put(TAG_encuestaFechaCreacion, encuestaFechaCreacion);
							

							// adding HashList to ArrayList
							contactList.add(map);

							
						}
						
						handler.sendEmptyMessage(0);
					} else {
						Log.d("TIENDAS: ", "null");
					}
				} catch (Exception e) {

				}
			}
		});
		t.start();
	}
	
	public void atras(View v) {
			
			finish();
		}

}