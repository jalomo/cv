package com.zavordigital.campoverde;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class circulares extends Activity {
	
	private static String url = "http://187.216.131.135:81/apisapp/consulta_circulares.php";
	JSONParser jsonParser = new JSONParser();
	JSONArray lista = null;
	
	private ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
	
	private static ProgressDialog progress;
	private static circularAdapter adapter;
	private ListView listView;
	
	static final String TAG_nombre = "nombre";
	static final String TAG_direccion= "direccion";
	static String resultado= "";

	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout_circulares);
        
        Intent in = getIntent();
        resultado = in.getStringExtra("resultado");
        
		adapter = new circularAdapter(this, contactList);
		listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);
        
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String direccion = ((TextView) view.findViewById(R.id.direccion))
						.getText().toString();
				
				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						verCircular.class);
				in.putExtra(TAG_direccion, direccion);
				
				
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
		cargaCirculares();
        
    }
    
    private static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			adapter.notifyDataSetChanged();
			progress.dismiss();
		}
	};
	
	
    
    public void cargaCirculares() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("cuenta", resultado));
					String json = jsonParser.makeHttpRequest(url, "GET", params);
	
					Log.d("Albums JSON: ", "> " + json.toString());
					String nombre = "";
					String direccion = "";
					
					
					lista = new JSONArray(json);
					if (lista != null) {
						// looping through All albums
						for (int i = 0; i < lista.length(); i++) {
							JSONObject c = lista.getJSONObject(i);
						
							
							nombre = c.getString(TAG_nombre);
							direccion = c.getString(TAG_direccion);
							
							

							// creating new HashMap
							HashMap<String, String> map = new HashMap<String, String>();

							map.put(TAG_nombre, nombre);
							map.put(TAG_direccion,direccion);
							
							

							// adding HashList to ArrayList
							contactList.add(map);

							
						}
						
						handler.sendEmptyMessage(0);
					} else {
						Log.d("TIENDAS: ", "null");
						handler.sendEmptyMessage(0);
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

