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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;



public class inicar_chat extends Activity {

	
	
	private static ProgressDialog progress1;
	EditText usernameT;
	EditText passwordT;
	String username;
	String password;
	
	JSONParser jsonParser = new JSONParser();
	JSONArray lista = null;
	String idAgente="0";
	
	private static String url = "http://www.zavordigital.com/mensajes_campoverde/index.php";
	private static String campoverde_login="http://187.216.131.135:81/apisapp/valida_ingreso.php"; 
	
	String clave_persona="";
	static String token="";
	String cadenaJson="";
	
	private static final int NOTIFICATION_ID = 664242215;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.iniciar_chat);
		
		
		
		Button familia = (Button) findViewById(R.id.familia);
		familia.setOnClickListener(new OnClickListener() {
	 
			@Override
			public void onClick(View v) {
				
				Intent in = new Intent(getApplicationContext(), chat_familia.class);
				
				startActivity(in);
				
				
			}
	 
		});
		
		Button maestro = (Button) findViewById(R.id.maestro);
		maestro.setOnClickListener(new OnClickListener() {
	 
			@Override
			public void onClick(View v) {
				
				Intent in = new Intent(getApplicationContext(), chat_inicio.class);
				
				startActivity(in);
				
				
			}
	 
		});
		
		
		
		
	}
	
	

}