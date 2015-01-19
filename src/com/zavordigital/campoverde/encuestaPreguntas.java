package com.zavordigital.campoverde;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class encuestaPreguntas extends Activity {
	
	private static String url = "http://zavordigital.com/campoverde/index.php/mobiles/get_question/";
	
	private static String url_votar = "http://www.zavordigital.com/campoverde/index.php/mobiles/vote_question/";
	JSONParser jsonParser = new JSONParser();
	JSONArray lista = null;
	
	private ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
	
	private static ProgressDialog progress;
	private static preguntasAdapter adapter;
	private ListView listView;
 
	
	static final String TAG_preguntaId= "preguntaId";
	static final String TAG_preguntaTitulo= "preguntaTitulo";
	static final String TAG_preguntaOpciones="preguntaOpciones";
	
	
	static String id_encuesta;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_preguntas);
        
        Intent in = getIntent();
        id_encuesta = in.getStringExtra("encuestaId");
        String encuesta_titulo=in.getStringExtra("encuestaTitulo");
        TextView Ptitulo = (TextView) findViewById(R.id.titulo_encuesta);
		Ptitulo.setText(""+encuesta_titulo);
        
		
		
        adapter = new preguntasAdapter(this, contactList);
		listView = (ListView) findViewById(R.id.list);
		
		


		// Creating a button - Load More
		Button btnLoadMore = new Button(this);
		btnLoadMore.setText("Aceptar");
		 
		// Adding button to listview at footer
		listView.addFooterView(btnLoadMore);
		
		
		
		listView.setAdapter(adapter);
        
		
		
		
		btnLoadMore.setOnClickListener(new View.OnClickListener() {
			 
            @Override
            public void onClick(View arg0) {
                // Starting a new async task
            	finish();
            }
        });
 
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String arreglo_ = ((TextView) view.findViewById(R.id.opciones))
						.getText().toString();
				showPopUpPhone(arreglo_);
			}
		});
		
		
		
		
        
        progress = new ProgressDialog(this);
		progress.setTitle("Descargando");
		progress.setMessage("Espere por favor...");
		progress.setCancelable(false);
		progress.show();
		contactList.clear();
		adapter.notifyDataSetChanged();
		cargaPreguntas();
		
	}
	
	
	
	
	 private void showPopUpPhone(String arr) { 
		 
		 LayoutInflater factory = LayoutInflater.from(this);

		//text_entry is an Layout XML file containing two text field to display in alert dialog
		final View textEntryView = factory.inflate(R.layout.dialog_layou, null);
		
		
    	final RadioGroup radiogroup = (RadioGroup) textEntryView.findViewById(R.id.grupo_radio);
    	
    	try {
    	String opcionId = "";
    	String Texto_opcion = "";
    	
    	JSONArray lista = null;
    	lista = new JSONArray(arr);
		if (lista != null) {
			// looping through All albums
			for (int i = 0; i < lista.length(); i++) {
				JSONObject c = lista.getJSONObject(i);
			
				
				opcionId = c.getString("opcionId");
				Texto_opcion = c.getString("opcionNombre");
				
				RadioButton newRadioButton = new RadioButton(this);
	            String label = Texto_opcion;
			    newRadioButton.setText(label);
			    newRadioButton.setId(Integer.parseInt(opcionId));
			    radiogroup.addView(newRadioButton);

			}
			
			
		} else {
			Log.d("TIENDAS: ", "null");
		}
    	
    	} catch (Exception e) {

		}
    	
        /*radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
		{
		    public void onCheckedChanged(RadioGroup rGroup, int checkedId)
		    {
		        // This will get the radiobutton that has changed in its check state
		        RadioButton checkedRadioButton = (RadioButton)rGroup.findViewById(checkedId);
		        // This puts the value (true/false) into the variable
		        boolean isChecked = checkedRadioButton.isChecked();
		        // If the radiobutton that has changed in check state is now checked...
		        if (isChecked)
		        {
		            // Changes the textview's text to "Checked: example radiobutton text"
		           // tv.setText("Checked:" + checkedRadioButton.getText());
		            
		           // Toast.makeText(getApplicationContext(), checkedRadioButton.getText(), Toast.LENGTH_SHORT).show();
		            Toast.makeText(getApplicationContext(), checkedRadioButton.getId(), Toast.LENGTH_SHORT).show();
		        }
		    }
		});

*/
	
		
		
		

		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setIcon(R.drawable.ic_launcher).setTitle("").setView(textEntryView).setPositiveButton("Aceptar",
		  new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog,
		     int whichButton) {

			   //RadioGroup radiogroup = (RadioGroup) textEntryView.findViewById(R.id.grupo_radio);
			   System.out.println("radio btn: " + radiogroup.getCheckedRadioButtonId());
			   
			   int id=radiogroup.getCheckedRadioButtonId();
	            
	            
	            if(id!=-1){
	            	
	            	//Toast.makeText(getApplicationContext(), id+" ", Toast.LENGTH_SHORT).show();
	            	votar_pregunta(id);
	            }else{
	            	//Toast.makeText(getApplicationContext(), "nada", Toast.LENGTH_SHORT).show();
	            	
	            }
		    /* User clicked OK so do some stuff */
		   }
		  });/*.setNegativeButton("Cancelar",
		  new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog,
		     int whichButton) {
		     
		     / User clicked cancel so do some stuff
		     
		   }
		  });*/
		alert.show();
		 
		
	      } 
	
	
	 public void votar_pregunta(final int id) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
						
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost(url_votar+id);
						

						ResponseHandler<String> responseHandler = new BasicResponseHandler();
						String res = httpclient.execute(httppost, responseHandler);
						Log.v("GCM", "RES: " + res);
					} catch (Exception e) {
					} 
				}
			});
			t.start();
		}
	
	
	private static Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			adapter.notifyDataSetChanged();
			progress.dismiss();
		}
	};
	
	public void cargaPreguntas() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					String json = jsonParser.makeHttpRequest(url+id_encuesta, "GET", params);
	
					Log.d("Albums JSON: ", "> " + json.toString());
					String preguntaId = "";
					String preguntaTitulo = "";
					String preguntaOpciones = "";
					
					lista = new JSONArray(json);
					if (lista != null) {
						// looping through All albums
						for (int i = 0; i < lista.length(); i++) {
							JSONObject c = lista.getJSONObject(i);
						
							
							preguntaId = c.getString(TAG_preguntaId);
							preguntaTitulo = c.getString(TAG_preguntaTitulo);
							preguntaOpciones = c.getString(TAG_preguntaOpciones);
							
							// creating new HashMap
							HashMap<String, String> map = new HashMap<String, String>();

							map.put(TAG_preguntaId, preguntaId);
							map.put(TAG_preguntaTitulo,preguntaTitulo);
							map.put(TAG_preguntaOpciones, preguntaOpciones);
							

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
	
	public void atras(View v) {
			
			finish();
		}

}