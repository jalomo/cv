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
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class calificaciones extends Activity implements OnItemSelectedListener{
	
	String resultado;
	JSONArray lista = null;
	
	private static String url = "http://187.216.131.135:81/apisapp/direccion_boleta.php?cuenta=105265&eva=1";
	JSONParser jsonParser = new JSONParser();

	
	private static ProgressDialog progress;
	private static calificacionesAdapter adapter;
	private ListView listView;
	String cuenta;
	String eva;
	
	private ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_calificaciones);
        
        Intent in = getIntent();
        resultado = in.getStringExtra("resultado");
        carga_alumnos();
        //Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_SHORT).show();
     
        
        listView = (ListView) findViewById(R.id.list);
        
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String direccion = ((TextView) view.findViewById(R.id.direccion))
						.getText().toString();
				String url = direccion;
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
				
			}
		});
        
        
        
        Button btn_especialidad = (Button) findViewById(R.id.button1);
		btn_especialidad.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				 EditText evaluacion = (EditText)findViewById(R.id.editText1);
				 
				eva=evaluacion.getText().toString();
				adapter = new calificacionesAdapter(calificaciones.this, contactList);
				
				listView.setAdapter(adapter);
				
				
				progress = new ProgressDialog(calificaciones.this);
				progress.setTitle("Descargando");
				progress.setMessage("Espere por favor...");
				progress.setCancelable(false);
				progress.show();
				contactList.clear();
				adapter.notifyDataSetChanged();
				cargaCalificaciones();
				
			}
		}); 
	}
	
	 private static Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				adapter.notifyDataSetChanged();
				progress.dismiss();
			}
		};
	  public void cargaCalificaciones() {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						params.add(new BasicNameValuePair("cuenta", cuenta));
						params.add(new BasicNameValuePair("eva", eva));
						
						String json = jsonParser.makeHttpRequest(url, "GET", params);
		
						Log.d("Albums JSON: ", "> " + json.toString());
						String direccion = "";
						
						
						lista = new JSONArray(json);
						if (lista != null) {
							// looping through All albums
							for (int i = 0; i < lista.length(); i++) {
								JSONObject c = lista.getJSONObject(i);
							
								
								direccion = c.getString("direccion");
								
								

								// creating new HashMap
								HashMap<String, String> map = new HashMap<String, String>();

								map.put("direccion", direccion);
								
								

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
public void carga_alumnos(){
		
		// Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        // Spinner click listener
        spinner.setOnItemSelectedListener(calificaciones.this);
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
	     if(spinner.getId() == R.id.spinner1)
	     {
	    	 StringWithTag s = (StringWithTag) parant.getItemAtPosition(pos);
		        String item = parant.getItemAtPosition(pos).toString();
		        cuenta=s.tag;
				// Showing selected spinner item
				//Toast.makeText(parant.getContext(), "Selected: " + s.tag, Toast.LENGTH_LONG).show();
				
				//Toast.makeText(parant.getContext(), "esyo es una prueba ", Toast.LENGTH_LONG).show();
				
	     }
	    
	}


	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	public void atras(View v) {
		
		finish();
	}

	
	
	
}
