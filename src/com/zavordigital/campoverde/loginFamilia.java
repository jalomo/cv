package com.zavordigital.campoverde;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class loginFamilia extends Activity {
	
	private static String url = "http://187.216.131.135:81/apisapp/valida_ingreso.php";
	JSONParser jsonParser = new JSONParser();
	JSONArray lista = null;
	
	String username="";
	String pass="";
	
	private ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
	
	private static ProgressDialog progress;
	private static encuestasAdapter adapter;
	private ListView listView;
	
	static final String TAG_acd_cuenta = "encuestaId";
	static final String TAG_alu_nombre= "encuestaTitulo";
	String acd_cuenta = "";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_encuestas);
        
        
        //login_usuario();
     
	}
	
	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//adapter.notifyDataSetChanged();
			progress.dismiss();
		}
	};
	
	public void login_usuario(final Handler actualizaLista) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					nameValuePairs.add(new BasicNameValuePair("familia","ferrusca ramos"));
					nameValuePairs.add(new BasicNameValuePair("password", "ferrusca"));
					
					
					DefaultHttpClient httpClient = new DefaultHttpClient();
					String paramString = URLEncodedUtils.format(nameValuePairs, HTTP.UTF_8).replace("+", "%20");
					/*String encoded = URLEncoder.encode(paramString, "UTF-8")
		                    .replace("+", "%20").replace("*", "%2A")
		                    .replace("%7E", "~");*/
					
					url += "?" + paramString;
					HttpGet httpGet = new HttpGet(url);

					/*HttpResponse httpResponse = httpClient.execute(httpGet);
					HttpEntity httpEntity = httpResponse.getEntity();
					res = httpEntity.getContent();
					
					*/
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					String res = httpClient.execute(httpGet, responseHandler);
					
					Log.v("GCM", "RES: " + url);
					Log.v("GCM", "RESULTADO: " + res);
					
					lista = new JSONArray(res);
					if (lista != null) {
						// looping through All albums
						for (int i = 0; i < lista.length(); i++) {
							JSONObject c = lista.getJSONObject(i);
						
							
							acd_cuenta = c.getString("acd_cuenta");
							
							
							
							
							Log.d("TIENDAS: ", acd_cuenta);
							actualizaLista.sendEmptyMessage(0);
							
						}
						
						
					} else {
						Log.d("TIENDAS: ", "null");
					}
					
				} catch (Exception e) {
				} 
			}
		});
		t.start();
	}
	
	
	private void showPopUpPhone(final String id_prod) { 
		 
		 LayoutInflater factory = LayoutInflater.from(this);

		//text_entry is an Layout XML file containing two text field to display in alert dialog
		final View textEntryView = factory.inflate(R.layout.dialog_login, null);

		final EditText input1 = (EditText) textEntryView.findViewById(R.id.id_usuario);
		final EditText input2 = (EditText) textEntryView.findViewById(R.id.id_contrasena);



		input1.setText("", TextView.BufferType.EDITABLE);
		input2.setText("", TextView.BufferType.EDITABLE);
		

		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setIcon(R.drawable.ic_launcher).setTitle("").setView(textEntryView).setPositiveButton("Aceptar",
		  new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog,
		     int whichButton) {

		    Log.i("AlertDialog","TextEntry 1 Entered "+input1.getText().toString());
		    Log.i("AlertDialog","TextEntry 2 Entered "+input2.getText().toString());
			  // String nombre1 = input1.getText().toString().trim();
			   //String email1 = input2.getText().toString().trim();
			   //String telefono1 = input3.getText().toString().trim();
			  
				
	           // Toast.makeText(getApplicationContext(), cantidad1, Toast.LENGTH_SHORT).show();
		    /* User clicked OK so do some stuff */
		   }
		  }).setNegativeButton("Cancelar",
		  new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog,
		     int whichButton) {
		     /*
		     * User clicked cancel so do some stuff
		     */
		   }
		  });
		alert.show();
		 
		
	      }  
	
	
	public void loginFamilia() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("familia", username));
					params.add(new BasicNameValuePair("password", pass));
					String json = jsonParser.makeHttpRequest(url, "GET", params);
	
					Log.d("Albums JSON: ", "> " + json.toString());
					String acd_cuenta = "";
					String alu_nombre = "";
					
					lista = new JSONArray(json);
					if (lista != null) {
						// looping through All albums
						for (int i = 0; i < lista.length(); i++) {
							JSONObject c = lista.getJSONObject(i);
						
							
							acd_cuenta = c.getString(TAG_acd_cuenta);
							alu_nombre = c.getString(TAG_alu_nombre);
							
							
							// creating new HashMap
							HashMap<String, String> map = new HashMap<String, String>();

							map.put(TAG_acd_cuenta, acd_cuenta);
							map.put(TAG_alu_nombre,alu_nombre);
							
							

							// adding HashList to ArrayList
							contactList.add(map);
							
							Log.d("TIENDAS: ", "null");
							
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