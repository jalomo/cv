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


public class chat_lista_alumnos extends Activity {
	
	private static String url = "http://www.zavordigital.com/mensajes/index.php";
	JSONParser jsonParser = new JSONParser();
	JSONArray lista = null;
	
	private ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
	
	private static ProgressDialog progress;
	private static adapterChatAlumno adapter;
	private ListView listView;
	
	static final String TAG_id = "acd_cuenta";
	static final String TAG_email= "alu_nombre";
	String cadenaJson="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_usuarios);
		
		Intent iin= getIntent();
        Bundle b = iin.getExtras();
        
        cadenaJson= (String) b.get("json");
		
		
		 SharedPreferences prefs =
			     getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

		String agente_ = prefs.getString("id_usuario", "");
		
		Toast.makeText(chat_lista_alumnos.this, agente_, Toast.LENGTH_SHORT).show();
		
		
		adapter = new adapterChatAlumno(this, contactList);
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
							lista_maestros.class);
					in.putExtra("cuenta", id_invitado);
					
					
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
						
						
						
						String user_id = "";
						String user_email = "";
						
						lista = new JSONArray(cadenaJson);
						if (lista != null) {
							// looping through All albums
							for (int i = 0; i < lista.length(); i++) {
								JSONObject c = lista.getJSONObject(i);
							
								
								user_id = c.getString(TAG_id);
								user_email = c.getString(TAG_email);
								
								

								// creating new HashMap
								HashMap<String, String> map = new HashMap<String, String>();

								map.put(TAG_id, user_id);
								map.put(TAG_email,user_email);
								
								

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