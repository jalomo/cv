package com.zavordigital.campoverde;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class chat extends Activity {
	
	private static String url = "http://www.zavordigital.com/mensajes/index.php";
	JSONParser jsonParser = new JSONParser();
	JSONArray lista = null;
	
	private ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
	
	private static ProgressDialog progress;
	private static ProgressDialog progress1;
	private static adapterChat adapter;
	private ListView listView;
	
	static final String TAG_id = "id";
	static final String TAG_email= "email";
	static String id_invitado;
	
	EditText texto;
	String mensaje;
	static String grupo;
	
	
	static final String TAG_mensajeId = "mensajeId";
	static final String TAG_mensajeIdGrupo = "mensajeIdGrupo";
	static final String TAG_mensajeStatus = "mensajeStatus";
	static final String TAG_mensajeIdUsuario = "mensajeIdUsuario";
	static final String TAG_mensajeTexto = "mensajeTexto";
	
	static final String TAG_miID = "";
	String cuenta;
	
	
	private static final int NOTIFICATION_ID = 664242215;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat);
		
		Intent iin= getIntent();
        Bundle b = iin.getExtras();
        
        id_invitado= (String) b.get("id_invitado");
        //cuenta= (String) b.get("cuenta");
        //TAG_miID=cuenta;
		
		adapter = new adapterChat(this, contactList);
		listView = (ListView) findViewById(R.id.list_view_messages);
		listView.setAdapter(adapter);
        
		
		progress = new ProgressDialog(this);
		 progress.setTitle("Descargando mensajes");
		 progress.setMessage("Espere por favor...");
		 progress.setCancelable(false);
		 progress.show();
		 contactList.clear();
		 adapter.notifyDataSetChanged();
		 cargaUsuarios();
		 
		 
		
		 
		 
		//Toast.makeText(chat.this, id_invitado+id_user, Toast.LENGTH_SHORT).show();
		
		
		Button send = (Button) findViewById(R.id.btnSend);
		send.setOnClickListener(new OnClickListener() {
	 
			@Override
			public void onClick(View v) {
				
				//Toast.makeText(chat.this, grupo, Toast.LENGTH_SHORT).show();
				
				progress1 = new ProgressDialog(chat.this);
				//progress1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progress1.setTitle("enviando mensaje");
				progress1.setMessage("Espere por favor...");
				progress1.setCancelable(false);
				progress1.show();
				
				
				
				send_message();
				
				
				texto = (EditText) findViewById(R.id.inputMsg);
		         texto.setText("");
				
				
			}
	 
		});
		 
		 
		cancelNotification();
		registerUser(this);
		muestraNotificacion();
		 
		
	}
	
	private static Handler actualizaclientes = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			progress1.dismiss();
		}
	};
	
	 private static Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				adapter.notifyDataSetChanged();
				progress.dismiss();
			}
		};
		
		
		public void send_message() {
			    	
			    	texto = (EditText) findViewById(R.id.inputMsg);
			         mensaje=texto.getText().toString();

			        
			        //Toast.makeText(getApplicationContext(), username+password, Toast.LENGTH_SHORT).show();
			    	
					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								
								SharedPreferences prefs =
									     getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

								String id_user = prefs.getString("id_user", "0");
								
								List<NameValuePair> params = new ArrayList<NameValuePair>();
								params.add(new BasicNameValuePair("metodo", "crea_mensaje"));
								params.add(new BasicNameValuePair("texto", mensaje));
								params.add(new BasicNameValuePair("id_grupo", grupo));
								params.add(new BasicNameValuePair("status","0"));
								params.add(new BasicNameValuePair("id_usuario", id_user));
								
								
								String json = jsonParser.makeHttpRequest(url, "POST", params);
				
								Log.d("Albums JSON: ", "----------------------------------------------------------------------> " + json.toString());
								//idAgente=json.toString();
								
								
								
								lista = new JSONArray(json);
								if (lista != null) {
									
									
									
								} else {
									Log.d("TIENDAS: ", "null");
								}
								
								actualizaclientes.sendEmptyMessage(0);
							} catch (Exception e) {
								
							}
							runOnUiThread(new Runnable() {
						        public void run() {
						        	 contactList.clear();
									 adapter.notifyDataSetChanged();
						        	cargaUsuarios();
						        	actualizaclientes.sendEmptyMessage(0);
						        }
						    });
						}
					});
					t.start();
					
					
		}
		
	    
	    public void cargaUsuarios() {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						
						
						SharedPreferences prefs =
							     getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

						String id_user = prefs.getString("id_user", "0");
						
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("metodo", "muestra_conversacion"));
						params.add(new BasicNameValuePair("is_user", id_user));
						params.add(new BasicNameValuePair("id_invitado", id_invitado));
						String json = jsonParser.makeHttpRequest(url, "POST", params);
		
						Log.d("Albums JSON: ", "> " + json.toString());
						String m_mensajeId = "";
						String m_mensajeIdGrupo = "";
						String m_mensajeStatus = "";
						String m_mensajeIdUsuario = "";
						String m_mensajeTexto = "";
						
						
						
						
								lista = new JSONArray(json);
								if (lista != null ) {
									
									
									
										
										// looping through All albums
										for (int i = 0; i < lista.length(); i++) {
											JSONObject c = lista.getJSONObject(i);
											
										
											
											m_mensajeId = c.getString(TAG_mensajeId);
											m_mensajeIdGrupo = c.getString(TAG_mensajeIdGrupo);
											m_mensajeStatus = c.getString(TAG_mensajeStatus);
											m_mensajeIdUsuario = c.getString(TAG_mensajeIdUsuario);
											m_mensajeTexto = c.getString(TAG_mensajeTexto);
											
											grupo=m_mensajeIdGrupo;
			
											// creating new HashMap
											HashMap<String, String> map = new HashMap<String, String>();
			
											map.put(TAG_mensajeId, m_mensajeId);
											map.put(TAG_mensajeIdGrupo,m_mensajeIdGrupo);
											map.put(TAG_mensajeStatus,m_mensajeStatus);
											map.put(TAG_mensajeIdUsuario,m_mensajeIdUsuario);
											map.put(TAG_mensajeTexto,m_mensajeTexto);
											
											map.put(TAG_miID,id_user);
											
											
			
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
	    
	    
	    
	 // Funciones push
		private void cancelNotification() {
			String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager nMgr = (NotificationManager) this
					.getSystemService(ns);
			nMgr.cancel(NOTIFICATION_ID);
		}

		private void registerUser(Context context) {
			try {
				GCMRegistrar.checkDevice(this);
				GCMRegistrar.checkManifest(this);
				final String regId = GCMRegistrar.getRegistrationId(context);
				if (regId.equals("")) {
					GCMRegistrar.register(context, "685386564438");
					Log.d("GCM------------------------", "Registrado");
					//Toast.makeText(getApplicationContext(), "Registrado", Toast.LENGTH_SHORT).show();
				} else {
					Log.d("GCM----------------------------------------", "Ya registrado");
					//Toast.makeText(getApplicationContext(), "ya registrado", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				//Toast.makeText(getApplicationContext(),e.getMessage().toString(), Toast.LENGTH_SHORT).show();
					
			}
		}

		private void muestraNotificacion() {
			SharedPreferences prefe = getSharedPreferences("datos",
					Context.MODE_PRIVATE);
			String msg = prefe.getString("msg", "null");
			if (!msg.equals("null")) {
				SharedPreferences preferencias = getSharedPreferences("datos",
						Context.MODE_PRIVATE);
				Editor editor = preferencias.edit();
				editor.putString("msg", "null");
				editor.commit();
				/*AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("mensajes");
				alert.setMessage(msg);
				alert.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
				alert.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
				AlertDialog dialog = alert.create();
				dialog.show();*/
			}
		}
		// Fin funciones push
	

}