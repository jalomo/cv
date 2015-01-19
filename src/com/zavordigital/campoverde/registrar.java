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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;




public class registrar extends Activity {
	
	static EditText email;
	static EditText password;
	static String token="";
	
	private static ProgressDialog progress1;
	
	private static ProgressDialog progress;
	
	private static String url = "http://www.zavordigital.com/mensajes/index.php";
	JSONParser jsonParser = new JSONParser();
	JSONArray lista = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registrar);
		
		
		
		 email = (EditText) findViewById(R.id.email);
		
		password = (EditText) findViewById(R.id.password);
		
		Button button_medico = (Button) findViewById(R.id.button1);
		button_medico.setOnClickListener(new OnClickListener() {
	 
			@Override
			public void onClick(View v) {
				
				progress1 = new ProgressDialog(registrar.this);
				//progress1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progress1.setTitle("conectando");
				progress1.setMessage("Espere por favor...");
				progress1.setCancelable(false);
				progress1.show();
				 registro();
				
				
				//Toast.makeText(getApplicationContext(), email+password, Toast.LENGTH_SHORT).show();
			}
	 
		});
		
		
	}
	
	public void variables(){
		
		
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
						params.add(new BasicNameValuePair("email", email.getText().toString()));
						params.add(new BasicNameValuePair("status", "0"));
						params.add(new BasicNameValuePair("token", token));
						params.add(new BasicNameValuePair("device", "ANDROID"));
						params.add(new BasicNameValuePair("pass", password.getText().toString()));
						String json = jsonParser.makeHttpRequest(url, "POST", params);
						
						
		
						Log.d("Albums JSON: ", "> " + json.toString());
						
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
	    	
	    	Toast.makeText(registrar.this,"registrado", Toast.LENGTH_SHORT).show();
	    	actualizaclientes.sendEmptyMessage(0);
	    	
	    	
	    }
	    
	    
	

}