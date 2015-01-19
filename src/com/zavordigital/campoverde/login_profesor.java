package com.zavordigital.campoverde;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class login_profesor  extends Activity {
	
	
	private static ProgressDialog progress1;
	EditText usernameT;
	EditText passwordT;
	String username;
	String password;
	
	JSONParser jsonParser = new JSONParser();
	JSONArray lista = null;
	String idAgente="0";
	
	private static String url = "http://www.zavordigital.com/mensajes_campoverde/index.php";
	private static String campoverde_login="http://187.216.131.135:81/apisapp/valida_ingreso_personal.php"; 
	
	String clave_persona="";
	static String token="";
	
	private static final int NOTIFICATION_ID = 664242215;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_login);
		
		
		SharedPreferences prefs =
			     getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

		String agente_ = prefs.getString("id_user", "");
		
		/*if(agente_.equals("")){
			
			//Toast.makeText(login.this, "da de alta un servidor", Toast.LENGTH_SHORT).show();
			Toast.makeText(chat_inicio.this, agente_, Toast.LENGTH_SHORT).show();
		}else{
			
			Intent in = new Intent(getApplicationContext(), lista_usuarios.class);
			
			startActivity(in);
			
			Toast.makeText(chat_inicio.this, "session iniciada"+agente_, Toast.LENGTH_SHORT).show();
		}
		*/
		
		
		Button button = (Button) findViewById(R.id.login);
		button.setOnClickListener(new OnClickListener() {
	 
			@Override
			public void onClick(View v) {
				
				//Intent in = new Intent(getApplicationContext(), lista_usuarios.class);
				
				//startActivity(in);
				SharedPreferences prefs =
					     getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

				String token = prefs.getString("token", "0");
				
				
				
				
				progress1 = new ProgressDialog(login_profesor.this);
				//progress1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progress1.setTitle("conectando");
				progress1.setMessage("Espere por favor...");
				progress1.setCancelable(false);
				progress1.show();
				login_campoverde();
				
			}
	 
		});
		
	/*	Button button_medico = (Button) findViewById(R.id.registrar);
		button_medico.setOnClickListener(new OnClickListener() {
	 
			@Override
			public void onClick(View v) {
				
				SharedPreferences prefs =
					     getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

				String token = prefs.getString("token", "0");
				//Toast.makeText(chat_inicio.this, token, Toast.LENGTH_SHORT).show();
				
				Intent in = new Intent(getApplicationContext(), registrar.class);
				
				startActivity(in);
				
				
			}
	 
		});
		*/
		cancelNotification();
		registerUser(this);
		muestraNotificacion();
		
		
	}
	
	/*
	 * login campoverde para maestros
	 * autor: jalomo <jalomo@hotmail.es> 
	 */
	public void login_campoverde() {
    	
    	usernameT = (EditText) findViewById(R.id.editText1);
        passwordT = (EditText) findViewById(R.id.editText2);
        username=usernameT.getText().toString();
        password=passwordT.getText().toString();
        
       // Toast.makeText(getApplicationContext(), username+password, Toast.LENGTH_SHORT).show();
    	
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					
					SharedPreferences prefs =
						     getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

					String token = prefs.getString("token", "0");
					
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					//params.add(new BasicNameValuePair("metodo", "login"));
					params.add(new BasicNameValuePair("usuario", username));
					params.add(new BasicNameValuePair("password", password));
					//params.add(new BasicNameValuePair("token", token));
					//params.add(new BasicNameValuePair("device", "ANDROID"));
					
					
					String json = jsonParser.makeHttpRequest(campoverde_login, "GET", params);
	
					Log.d("Albums JSON: ", "----------------------------------------------------------------------> " + json.toString());
					idAgente=json.toString();
					
					
					
					lista = new JSONArray(json);
					if (lista != null) {
							
						for (int i = 0; i < lista.length(); i++) {
							JSONObject c = lista.getJSONObject(i);
						
							
							clave_persona = c.getString("clave_personal");
							
							
						}
						
						
					} else {
						Log.d("TIENDAS: ", "null");
					}
					
					actualizaclientes.sendEmptyMessage(0);
				} catch (Exception e) {
					
				}
				runOnUiThread(new Runnable() {
			        public void run() {
			        	login_maestro();
			        }
			    });
			}
		});
		t.start();
		
		
	}
	
	/*
	 * al terminar el login se lanza esta funcion.
	 * autor: jalomo <jalomo@hotmail.es>
	 */
	public void login_maestro(){
		Toast.makeText(login_profesor.this, clave_persona, Toast.LENGTH_SHORT).show();
       if(clave_persona.equals("0")){
    	   actualizaclientes.sendEmptyMessage(0);
			Toast.makeText(login_profesor.this, "NO ENTRO ", Toast.LENGTH_SHORT).show();
		}else{
			
			Intent in = new Intent(getApplicationContext(),
					lista_citas.class);
			in.putExtra("id_maestro", clave_persona);
			startActivity(in);
			
			
			
			
		}
		
		
	}
	
	private static Handler actualizaclientes = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			progress1.dismiss();
		}
	};
		
		
	    
	    public void registro() {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						
						SharedPreferences prefs =
							     getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

						token = prefs.getString("token", "0");
						
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("metodo", "registrar_usuario"));
						params.add(new BasicNameValuePair("id", clave_persona));
						params.add(new BasicNameValuePair("status", "2"));
						params.add(new BasicNameValuePair("token", token));
						params.add(new BasicNameValuePair("device", "ANDROID"));
						params.add(new BasicNameValuePair("pass", ""));
						String json = jsonParser.makeHttpRequest(url, "POST", params);
						
						
		
						Log.d("id usuario JSON------------: ", "> " + json.toString());
						
						idAgente=json.toString();
						
						Toast.makeText(getApplicationContext(), "usuario creado", Toast.LENGTH_SHORT).show();
						lista = new JSONArray(json);
						//handler.sendEmptyMessage(0);
						if (lista != null) {
							// looping through All albums
							
							
							actualizaclientes.sendEmptyMessage(0);
						} else {
							Log.d("TIENDAS: ", "null");
							actualizaclientes.sendEmptyMessage(0);
						}
					} catch (Exception e) {

					}
					runOnUiThread(new Runnable() {
				        public void run() {
				        	exito();
				        }
				    });
				}
			});
			t.start();
		}
	    
	    public void exito(){
	    	
	    	//Toast.makeText(chat_inicio.this,"registrado", Toast.LENGTH_SHORT).show();
	    	actualizaclientes.sendEmptyMessage(0);
	    	
	    	SharedPreferences prefs =
				     getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
	    	
	    	if(idAgente.trim().equals("0")){
				//pass incorrecto
					Toast.makeText(login_profesor.this, "usuario o contraseña incorrectos"+idAgente, Toast.LENGTH_SHORT).show();
				}else{
					
					//Toast.makeText(login.this, "exito_"+idAgente, Toast.LENGTH_SHORT).show();
					
					SharedPreferences prefs1 =
						     getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

					SharedPreferences.Editor editor = prefs1.edit();
					editor.putString("id_user", idAgente);
					editor.commit();
					
					Toast.makeText(login_profesor.this, "entro"+idAgente, Toast.LENGTH_SHORT).show();
					
					
					//Toast.makeText(login.this,"id_agente:"+idAgente, Toast.LENGTH_SHORT).show();
					Intent in = new Intent(getApplicationContext(), lista_usuarios.class);
					
					
					startActivity(in);
					
					
				}
	    	
	    	
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
