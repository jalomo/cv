package com.zavordigital.campoverde;



import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class serviciosEscolares extends Activity {
	
	private static String url = "http://187.216.131.135:81/apisapp/valida_ingreso.php";
	String username="";
	String pass="";
	String resultado="";
	String error_="";
	JSONParser jsonParser = new JSONParser();
	JSONArray lista = null;
	private TextView txtResultado;
	String TAG_retultado="resultado";
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_servicios);
        
        txtResultado = (TextView)findViewById(R.id.textView1);
		
	}
public void familia(View v) {
		
		showPopUpPhone();
	}


	public void calificaciones_(View v) {
		
		showPopUpPhoneCalificaciones();
	}

private void showPopUpPhone() { 
	 
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
	    username=input1.getText().toString();
	    pass=input2.getText().toString();
	  
	    loginFamilia();
	   
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
				resultado=json;
				lista = new JSONArray(json);
				
				if (lista != null) {

					
					Log.v("GCM", "TAMANO: " + lista.length());
					
					// looping through All albums
					for (int i = 0; i < lista.length(); i++) {
						JSONObject c = lista.getJSONObject(i);
					
						error_=c.getString("c");
						resultado=c.getString("acd_cuenta");
						Log.d("TIENDAS: ", resultado);
						
					}
					//Toast.makeText(getApplicationContext(), "FUERA"+error_, Toast.LENGTH_SHORT).show();
					String aux="1";
					
					
				} else {
					Log.d("TIENDAS: ", "null");
				}
			} catch (Exception e) {

			}
			runOnUiThread(new Runnable() {
		        public void run() {
		        	
		        	verificar(error_);
		        	
		        }
		    });
		}
	});
	t.start();
}

public void verificar(String aux){
	
	if(aux.equals("1")){
		
		Intent in = new Intent(getApplicationContext(),
				circulares.class);
		in.putExtra(TAG_retultado, resultado);
		startActivity(in);
		
		Toast.makeText(getApplicationContext(), "si entro"+resultado, Toast.LENGTH_SHORT).show();
	}else{
		
		//txtResultado.setText("usuario o contraseña incorrecta");
		Toast.makeText(getApplicationContext(), "no entro"+resultado, Toast.LENGTH_SHORT).show();
	}
	
	
}
	
	public void encuestas(View v) {
		
		Intent intent = new Intent(this, encuestas.class);
		startActivity(intent);
	}
	
	public void circulares(View v) {
		
		
		
		Intent intent = new Intent(this, circulares.class);
		startActivity(intent);
	}
	
	public void calificaciones(View v) {
		
		finish();
	}
	
	
	
	
	
	
	
	
	

private void showPopUpPhoneCalificaciones() { 
	 
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
	    username=input1.getText().toString();
	    pass=input2.getText().toString();
	  
	    loginFamiliaCalificaciones();
	   
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
	

public void loginFamiliaCalificaciones() {
	
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
				resultado=json;
				lista = new JSONArray(json);
				
				if (lista != null) {

					
					Log.v("GCM", "TAMANO: " + lista.length());
					
					// looping through All albums
					for (int i = 0; i < lista.length(); i++) {
						JSONObject c = lista.getJSONObject(i);
					
						error_=c.getString("c");
						//resultado=c.getString("acd_cuenta");
						Log.d("TIENDAS: ", resultado);
						
					}
					Toast.makeText(getApplicationContext(), "FUERA"+error_, Toast.LENGTH_SHORT).show();
					String aux="1";
					
					
				} else {
					Log.d("TIENDAS: ", "null");
				}
			} catch (Exception e) {

			}
			runOnUiThread(new Runnable() {
		        public void run() {
		        	
		        	verificarCalificaciones(error_);
		        	
		        }
		    });
		}
	});
	t.start();
}

public void verificarCalificaciones(String aux){
	
	if(aux.equals("1")){
		
		Intent in = new Intent(getApplicationContext(),
				calificaciones.class);
		in.putExtra(TAG_retultado, resultado);
		startActivity(in);
		
		Toast.makeText(getApplicationContext(), "si entro"+resultado, Toast.LENGTH_SHORT).show();
	}else{
		
		//txtResultado.setText("usuario o contraseña incorrecta");
		Toast.makeText(getApplicationContext(), "no entro"+resultado, Toast.LENGTH_SHORT).show();
	}
	
	
}

}