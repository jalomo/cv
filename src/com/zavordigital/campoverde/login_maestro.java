package com.zavordigital.campoverde;

import java.util.ArrayList;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class login_maestro extends Activity implements OnItemSelectedListener{
	
	private static String url_campus = "http://187.216.131.135:81/apisapp/consulta_campus.php";
	private static String url_maestros = "http://187.216.131.135:81/apisapp/consulta_personal_seccion.php";
	private static String url_login = "http://www.zavordigital.com/campoverde/index.php/mobiles/loginMaestro";
JSONArray lista = null;
	
	String username="";
	String pass="";
	String acd_cuenta = "";
	private static ProgressDialog progress;
	private static encuestasAdapter adapter;
	private TextView txtResultado;
	int bandera=0;
	String error_="";
	String error_m="0";
	String resultado="";
	String TAG_retultado="resultado";
	String id_maestro="";
	
	JSONParser jsonParser = new JSONParser();
	
	List<StringWithTag> listCampus; 
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_login_maestro);
        //txtResultado = (TextView)findViewById(R.id.textView1);
        
       
		getCampus();
        
        
        Button btn_especialidad = (Button) findViewById(R.id.button1);
		btn_especialidad.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				 EditText pass = (EditText)findViewById(R.id.contrasena);
				 String pas=pass.getText().toString();
				 Spinner maestro = (Spinner) findViewById(R.id.spinner_maestro);
			
				 String usuario=maestro.getSelectedItem().toString();
				// Toast.makeText(getApplicationContext(), usuario, Toast.LENGTH_SHORT).show();
				loginProfesor(usuario,pas);
			}
		}); 
        
	}
	

	
	
	public void loginProfesor(final String nombre, final String contrasena) {
		 final String numero_="";
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("contrasena", contrasena));
					params.add(new BasicNameValuePair("nombre", nombre));
					String json = jsonParser.makeHttpRequest(url_login, "POST", params);
	
					Log.d("Albums JSON: ", "> " + json.toString());
					/*String acd_cuenta = "";
					String alu_nombre = "";
					resultado=json;
					lista = new JSONArray(json);
					*/
					error_= json.toString();
					Log.d("esto es el error_: ", error_);
					
					if (lista != null) {

						/*
						Log.v("GCM", "TAMANO: " + lista.length());
						
						// looping through All albums
						for (int i = 0; i < lista.length(); i++) {
							JSONObject c = lista.getJSONObject(i);
						
							//error_=c.getString("c");
							Log.d("TIENDAS: ", error_);
							
						}
						
						*/
						
					} else {
						Log.d("TIENDAS: ", "null");
					}
				} catch (Exception e) {

				}
				runOnUiThread(new Runnable() {
			        public void run() {
			        	
			        	verificar();
			        	
			        }
			    });
			}
		});
		t.start();
	}
	
	public void verificar(){
		
		String aux="1";
		String ver=error_.trim();
		if(ver.equals(aux)){
			
			Intent in = new Intent(getApplicationContext(),
					lista_citas.class);
			in.putExtra("id_maestro", id_maestro);
			startActivity(in);
			
			Toast.makeText(getApplicationContext(), "contraseña corerecta", Toast.LENGTH_SHORT).show();
		}else{
			
			Toast.makeText(getApplicationContext(), "contraseña incorrecta", Toast.LENGTH_SHORT).show();
			//txtResultado.setText("usuario o contraseña incorrecta");
			//Toast.makeText(getApplicationContext(), error_, Toast.LENGTH_SHORT).show();
		}
		
		
	}
	
	 public void onItemSelected(AdapterView<?> parant, View v, int pos, long id) {
	        
			
			Spinner spinner = (Spinner) parant;
		     if(spinner.getId() == R.id.spinner_campus)
		     {
		    	 StringWithTag s = (StringWithTag) parant.getItemAtPosition(pos);
			        String item = parant.getItemAtPosition(pos).toString();
					
					// Showing selected spinner item
					//Toast.makeText(parant.getContext(), "Selected: " + s.tag, Toast.LENGTH_LONG).show();
					
					 Spinner secci = (Spinner) findViewById(R.id.spinner_seccion);
					
					get_maestro(secci.getSelectedItem().toString(), s.tag);
				//	idmaestro=s.tag;
					//GetMaestros(s.tag);
		     }
		     else if(spinner.getId() == R.id.spinner_seccion)
		     {
		       //do this
		    	 StringWithTag x = (StringWithTag) parant.getItemAtPosition(pos);
			        String item = parant.getItemAtPosition(pos).toString();
					
			        
			        Spinner camp = (Spinner) findViewById(R.id.spinner_seccion);
					// Showing selected spinner item
					//Toast.makeText(parant.getContext(), "Selected: " + x.tag, Toast.LENGTH_LONG).show();
			        get_maestro(x.tag, camp.getSelectedItem().toString());
					/*idfamilia=x.tag;
					nombre=x.string;
					GetMaestros(x.tag);*/
		     }
		     else if(spinner.getId() == R.id.spinner_maestro)
		     {
		       //do this
		    	 StringWithTag x = (StringWithTag) parant.getItemAtPosition(pos);
			        
			        id_maestro=x.tag;
			        
					/*idfamilia=x.tag;
					nombre=x.string;
					GetMaestros(x.tag);*/
		     }
	    }
	    
	   
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
	
		
		public void getCampus() {
			

			
			listCampus  = new ArrayList<StringWithTag>();
		    

				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							List<NameValuePair> params = new ArrayList<NameValuePair>();
							
							
							
							String json = jsonParser.makeHttpRequest(url_campus, "GET", params);
			
							Log.d("Albums JSON: ", "> " + json.toString());
							String id = "";
							String nombre = "";
							
							lista = new JSONArray(json);
							
							if (lista != null) {
								for (int i = 0; i < lista.length(); i++) {
				      				JSONObject c = lista.getJSONObject(i);
				      			
				      				
				      				id = c.getString("tm_clave");
				      				nombre = c.getString("tm_nombre");
				      				listCampus.add(new StringWithTag(nombre, id));
				      				
				      				
				      			}
							} else {
								Log.d("TIENDAS: ", "null");
							}
						} catch (Exception e) {

						}
						runOnUiThread(new Runnable() {
					        public void run() {
					        	cargaCampus();
					        }
					    });
						
					}
				});
				t.start();
				
				
				
			}

			public void cargaCampus(){
				
				// Spinner element
			    Spinner spinnerCampus = (Spinner) findViewById(R.id.spinner_campus);
			    // Spinner click listener
			    spinnerCampus.setOnItemSelectedListener(this);
				 ArrayAdapter<StringWithTag> adap = new ArrayAdapter<StringWithTag> (this, android.R.layout.simple_spinner_item, listCampus);
				    
				    adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				    
				    spinnerCampus.setAdapter(adap);
			}
			
			
			

			public void get_maestro(final String idSeccion, final String IdCampus) {
				

				
				listCampus  = new ArrayList<StringWithTag>();
			    

					Thread t = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								List<NameValuePair> params = new ArrayList<NameValuePair>();
								params.add(new BasicNameValuePair("campus", IdCampus));
								params.add(new BasicNameValuePair("seccion", idSeccion));
								
								String json = jsonParser.makeHttpRequest(url_maestros, "GET", params);
				
								Log.d("Albums JSON: ", "> " + json.toString());
								String id = "";
								String nombre = "";
								
								lista = new JSONArray(json);
								
								if (lista != null) {
									for (int i = 0; i < lista.length(); i++) {
					      				JSONObject c = lista.getJSONObject(i);
					      			
					      				
					      				id = c.getString("id");
					      				nombre = c.getString("tm_nombre");
					      				listCampus.add(new StringWithTag(nombre, id));
					      				
					      				
					      			}
								} else {
									Log.d("TIENDAS: ", "null");
								}
							} catch (Exception e) {

							}
							runOnUiThread(new Runnable() {
						        public void run() {
						        	cargaMaestros();
						        }
						    });
							
						}
					});
					t.start();
					
					
					
				}

				public void cargaMaestros(){
					
					// Spinner element
				    Spinner spinnerCampus = (Spinner) findViewById(R.id.spinner_maestro);
				    // Spinner click listener
				    spinnerCampus.setOnItemSelectedListener(this);
					 ArrayAdapter<StringWithTag> adap = new ArrayAdapter<StringWithTag> (this, android.R.layout.simple_spinner_item, listCampus);
					    
					    adap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					    
					    spinnerCampus.setAdapter(adap);
				}
			
	
}

