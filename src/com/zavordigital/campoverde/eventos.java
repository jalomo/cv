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

public class eventos extends Activity {
	
	private static String url = "http://www.zavordigital.com/campoverde/index.php/mobiles/get_eventos";
	JSONParser jsonParser = new JSONParser();
	JSONArray lista = null;
	
	private ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
	
	private static ProgressDialog progress;
	private static eventosAdapter adapter;
	private ListView listView;
	
	static final String TAG_eventosId = "eventosId";
	static final String TAG_eventosTitulo= "eventosTitulo";
	static final String TAG_eventosPathImg= "eventosPathImg";
	static final String TAG_eventosTexto= "eventosTexto";
	static final String TAG_eventosHora= "eventosHora";
	static final String TAG_eventosFecha= "eventosFecha";
	static final String TAG_eventosLatitud= "eventosLatitud";
	static final String TAG_eventosLongitud= "eventosLongitud";
	static final String TAG_eventosDireccion= "eventosDireccion";
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout_eventos);
        
    
		adapter = new eventosAdapter(this, contactList);
		listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);
        
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String titulo = ((TextView) view.findViewById(R.id.title))
						.getText().toString();
				String latitud = ((TextView) view.findViewById(R.id.latitud))
						.getText().toString();
				String longitud = ((TextView) view.findViewById(R.id.longitud))
						.getText().toString();
				String texto = ((TextView) view.findViewById(R.id.texto))
						.getText().toString();
				String hora = ((TextView) view.findViewById(R.id.hora))
						.getText().toString();
				String fecha = ((TextView) view.findViewById(R.id.fecha))
						.getText().toString();
				
				String id_evento = ((TextView) view.findViewById(R.id.id_evento))
						.getText().toString();
				
				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						Mapa.class);
				in.putExtra(TAG_eventosLatitud, latitud);
				in.putExtra(TAG_eventosLongitud, longitud);
				in.putExtra(TAG_eventosTexto, texto);
				in.putExtra(TAG_eventosHora, hora);
				in.putExtra(TAG_eventosFecha, fecha);
				in.putExtra(TAG_eventosTitulo, titulo);
				in.putExtra(TAG_eventosId, id_evento);
				
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
		cargaNoticias();
        
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
					String json = jsonParser.makeHttpRequest(url, "GET", params);
	
					Log.d("Albums JSON: ", "> " + json.toString());
					String eventosId = "";
					String eventosTitulo = "";
					String eventosPathImg = "";
					String eventosTexto = "";
					String eventosHora = "";
					String eventosFecha = "";
					String eventosLatitud = "";
					String eventosLongitud = "";
					String eventosDireccion="";
					
					lista = new JSONArray(json);
					if (lista != null) {
						// looping through All albums
						for (int i = 0; i < lista.length(); i++) {
							JSONObject c = lista.getJSONObject(i);
						
							
							eventosId = c.getString(TAG_eventosId);
							eventosTitulo = c.getString(TAG_eventosTitulo);
							eventosPathImg = c.getString(TAG_eventosPathImg);
							eventosTexto = c.getString(TAG_eventosTexto);
							eventosHora = c.getString(TAG_eventosHora);
							eventosFecha = c.getString(TAG_eventosFecha);
							eventosLatitud = c.getString(TAG_eventosLatitud);
							eventosLongitud = c.getString(TAG_eventosLongitud);
							eventosDireccion = c.getString(TAG_eventosDireccion);
							

							// creating new HashMap
							HashMap<String, String> map = new HashMap<String, String>();

							map.put(TAG_eventosId, eventosId);
							map.put(TAG_eventosTitulo,eventosTitulo);
							map.put(TAG_eventosPathImg,
									"http://zavordigital.com/campoverde/"
											+ eventosPathImg);
							map.put(TAG_eventosTexto, eventosTexto);
							map.put(TAG_eventosHora, eventosHora);
							map.put(TAG_eventosFecha, eventosFecha);
							map.put(TAG_eventosLatitud, eventosLatitud);
							map.put(TAG_eventosLongitud, eventosLongitud);
							map.put(TAG_eventosDireccion, eventosDireccion);
							

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
}
