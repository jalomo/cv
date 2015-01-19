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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class lista_maestros extends Activity {
	
	private static String url = "http://187.216.131.135:81/apisapp/consulta_maestros.php";
	JSONParser jsonParser = new JSONParser();
	JSONArray lista = null;
	
	private ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
	
	private static ProgressDialog progress;
	private static adapterListaMaestros adapter;
	private ListView listView;
	
	static final String TAG_id = "clave_personal";
	static final String TAG_email= "nombre_maestro";
	static final String TAG_materia="materia";
	
	String cuenta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_maestros);
		
		
		Intent iin= getIntent();
        Bundle b = iin.getExtras();
        
        cuenta= (String) b.get("cuenta");
		
		
		adapter = new adapterListaMaestros(this, contactList);
		listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);
        
		
		 progress = new ProgressDialog(this);
		 progress.setTitle("Descargando");
		 progress.setMessage("Espere por favor...");
		 progress.setCancelable(false);
		 progress.show();
		 contactList.clear();
		 adapter.notifyDataSetChanged();
		 cargaUsuarios();
		 
		 
		 
		 
		 
		 listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String id_invitado = ((TextView) view.findViewById(R.id.id))
							.getText().toString();
					
					
					// Starting new intent
					Intent in = new Intent(getApplicationContext(),
							chat.class);
					in.putExtra("id_invitado", id_invitado);
				//	in.putExtra("cuenta", cuenta);
					
					
					
					SharedPreferences prefs1 =
						     getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

					SharedPreferences.Editor editor = prefs1.edit();
					editor.putString("id_user", cuenta);
					editor.commit();
					
					startActivity(in);

				}
			});
		 
		 
		 
		 
		
	}
	
	public void atras(View v) {
		
		finish();
	}
	
	 private static Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				adapter.notifyDataSetChanged();
				progress.dismiss();
			}
		};
		
		
	    
	    public void cargaUsuarios() {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						
						
						
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("cuenta", cuenta));
						
						String json = jsonParser.makeHttpRequest(url, "GET", params);
		
						Log.d("Albums JSON: ", "> " + json.toString());
						String user_id = "";
						String user_email = "";
						String materia="";
						
						lista = new JSONArray(json);
						if (lista != null) {
							// looping through All albums
							for (int i = 0; i < lista.length(); i++) {
								JSONObject c = lista.getJSONObject(i);
							
								
								user_id = c.getString(TAG_id);
								user_email = c.getString(TAG_email);
								materia = c.getString(TAG_materia);
								
								

								// creating new HashMap
								HashMap<String, String> map = new HashMap<String, String>();

								map.put(TAG_id, user_id);
								map.put(TAG_email,user_email);
								map.put(TAG_materia,materia);
								
								

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