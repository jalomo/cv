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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class citaFamilia extends Activity implements OnItemSelectedListener{
	
	private static String url = "http://zavordigital.com/campoverde/index.php/mobiles/savecita";
	private static String url_maestros = "http://187.216.131.135:81/apisapp/consulta_maestros.php";
JSONArray lista = null;
	
	
	private static ProgressDialog progress;
	private static encuestasAdapter adapter;
	
	int bandera=0;
	String error_="";
	
	String nombre;
	String telefono;
	String idfamilia;
	String idmaestro;
	String hora;
	String fecha;
	String dispositivo;
	String token;
	
	String resultado;
	JSONParser jsonParser = new JSONParser();
	
	String id_alumno="";
	 List<StringWithTag> listM; 
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_cita_alumno);
        
        Intent in = getIntent();
        resultado = in.getStringExtra("resultado");
        
        //Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_SHORT).show();
     
        carga_alumnos();
        
        
        
        Button btn_especialidad = (Button) findViewById(R.id.envia_c);
		btn_especialidad.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				cargaNoticias();
			}
		}); 
	}
	
	
	public void carga_alumnos(){
		
		// Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinnerAl);
        // Spinner click listener
        spinner.setOnItemSelectedListener(citaFamilia.this);
        List<StringWithTag> list = new ArrayList<StringWithTag>();
        
        try {
        	 String alu_nombre="";
             String acd_cuenta="";
      		lista = new JSONArray(resultado);
      		if (lista != null) {
      			// looping through All albums
      			for (int i = 0; i < lista.length(); i++) {
      				JSONObject c = lista.getJSONObject(i);
      			
      				
      				alu_nombre = c.getString("alu_nombre");
      				acd_cuenta = c.getString("acd_cuenta");
      				list.add(new StringWithTag(alu_nombre, acd_cuenta));
      				
      				
      			}
      			
      			
      		} else {
      			Log.d("TIENDAS: ", "null");
      		}
		} catch (Exception e) {

		}
        
       
        ArrayAdapter<StringWithTag> adap = new ArrayAdapter<StringWithTag> (this, android.R.layout.simple_spinner_item, list);
        
        adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        spinner.setAdapter(adap);
	}
	
	
	 public void onItemSelected(AdapterView<?> parant, View v, int pos, long id) {
	        
			
			Spinner spinner = (Spinner) parant;
		     if(spinner.getId() == R.id.spinnerMa)
		     {
		    	 StringWithTag s = (StringWithTag) parant.getItemAtPosition(pos);
			        String item = parant.getItemAtPosition(pos).toString();
					
					// Showing selected spinner item
					//Toast.makeText(parant.getContext(), "Selected: " + s.tag, Toast.LENGTH_LONG).show();
					
					//Toast.makeText(parant.getContext(), "esyo es una prueba ", Toast.LENGTH_LONG).show();
					idmaestro=s.tag;
					//GetMaestros(s.tag);
		     }
		     else if(spinner.getId() == R.id.spinnerAl)
		     {
		       //do this
		    	 StringWithTag x = (StringWithTag) parant.getItemAtPosition(pos);
			        String item = parant.getItemAtPosition(pos).toString();
					
					// Showing selected spinner item
					//Toast.makeText(parant.getContext(), "Selected: " + x.tag, Toast.LENGTH_LONG).show();
					idfamilia=x.tag;
					nombre=x.string;
					GetMaestros(x.tag);
		     }
	    }
	    
	   
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	
	
public void GetMaestros(final String id_alum) {
	

	
    listM  = new ArrayList<StringWithTag>();
    

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("cuenta", id_alum));
					
					
					String json = jsonParser.makeHttpRequest(url_maestros, "GET", params);
	
					Log.d("Albums JSON: ", "> " + json.toString());
					String id = "";
					String nombre = "";
					
					lista = new JSONArray(json);
					
					if (lista != null) {
						for (int i = 0; i < lista.length(); i++) {
		      				JSONObject c = lista.getJSONObject(i);
		      			
		      				
		      				id = c.getString("clave_personal");
		      				nombre = c.getString("nombre_maestro");
		      				listM.add(new StringWithTag(nombre, id));
		      				
		      				
		      			}
					} else {
						Log.d("TIENDAS: ", "null");
					}
				} catch (Exception e) {

				}
				runOnUiThread(new Runnable() {
			        public void run() {
			        	cargaM();
			        }
			    });
				
			}
		});
		t.start();
		
		
		
	}

	public void cargaM(){
		
		// Spinner element
	    Spinner spinnerMA = (Spinner) findViewById(R.id.spinnerMa);
	    // Spinner click listener
	    spinnerMA.setOnItemSelectedListener(this);
		 ArrayAdapter<StringWithTag> adap = new ArrayAdapter<StringWithTag> (this, android.R.layout.simple_spinner_item, listM);
		    
		    adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    
		    spinnerMA.setAdapter(adap);
	}
	
	
	
	 public void cargaNoticias() {
		 
		 EditText hora1 = (EditText)findViewById(R.id.editText3);
		 hora=hora1.getText().toString();
		 
		 EditText tele = (EditText)findViewById(R.id.editText2);
		 telefono=tele.getText().toString();
		 
		 Spinner dia = (Spinner) findViewById(R.id.spinner1);
		 Spinner mes = (Spinner) findViewById(R.id.spinner2);
		 Spinner anio = (Spinner) findViewById(R.id.spinner3);
		 
		
		 fecha=  dia.getSelectedItem().toString()+"-"+mes.getSelectedItem().toString()+"-"+anio.getSelectedItem().toString();
		 dispositivo="ANDROID";
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						/*String nombre = "";
						String telefono = "";
						String idfamilia = "";
						String idmaestro = "";
						String hora = "";
						String fecha = "";
						String dispositivo = "";
						String token = "";*/
						params.add(new BasicNameValuePair("nombre", nombre));
						params.add(new BasicNameValuePair("telefono", telefono));
						params.add(new BasicNameValuePair("idfamilia", idfamilia));
						params.add(new BasicNameValuePair("idmaestro", idmaestro));
						params.add(new BasicNameValuePair("fecha", fecha));
						params.add(new BasicNameValuePair("dispositivo", dispositivo));
						params.add(new BasicNameValuePair("token", token));
						params.add(new BasicNameValuePair("hora", hora));
						
						String json = jsonParser.makeHttpRequest(url, "POST", params);
		
						Log.d("Albums JSON: ", "> " + json.toString());
						
						
						
						lista = new JSONArray(json);
						if (lista != null) {
							// looping through All albums
							for (int i = 0; i < lista.length(); i++) {
								JSONObject c = lista.getJSONObject(i);
							
								
								
							}
							
							//handler.sendEmptyMessage(0);
						} else {
							Log.d("TIENDAS: ", "null");
						}
					} catch (Exception e) {

					}
					runOnUiThread(new Runnable() {
				        public void run() {
				        	finaliza_cita();
				        }
				    });
					
				}
			});
			t.start();
		}
	
	public void finaliza_cita(){
		Toast.makeText(getApplicationContext(), "cita enviada", Toast.LENGTH_SHORT).show();
		finish();
		
	}
	
}
