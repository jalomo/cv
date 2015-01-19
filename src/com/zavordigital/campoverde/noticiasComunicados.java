package com.zavordigital.campoverde;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;




public class noticiasComunicados extends Activity {
	
	
	//Declare
		private LinearLayout slidingPanel;
		private boolean isExpanded;
		private DisplayMetrics metrics;	
		//private ListView listView2;
		private RelativeLayout headerPanel;
		private RelativeLayout menuPanel;
		private int panelWidth;
		private ImageView menuViewButton;
		
		FrameLayout.LayoutParams menuPanelParameters;
		FrameLayout.LayoutParams slidingPanelParameters;
		LinearLayout.LayoutParams headerPanelParameters ;
		LinearLayout.LayoutParams listViewParameters;
		
	//	
	
	private static String url = "http://www.zavordigital.com/campoverde/index.php/mobiles/get_noticias";
	JSONParser jsonParser = new JSONParser();
	JSONArray lista = null;
	
	private ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
	
	private static ProgressDialog progress;
	private static noticiasAdapter adapter;
	private ListView listView;
	
	static final String TAG_noticiasId = "noticiasId";
	static final String TAG_noticiasTitulo= "noticiasTitulo";
	static final String TAG_noticiasPathImg= "noticiasPathImg";
	static final String TAG_noticiasTexto= "noticiasTexto";
	static final String TAG_noticiasFecha= "noticiasFecha";
	
	private static final int NOTIFICATION_ID = 664242218;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout_noticias);
        
        
        
        
      //Initialize
      		metrics = new DisplayMetrics();
      		getWindowManager().getDefaultDisplay().getMetrics(metrics);
      		panelWidth = (int) ((metrics.widthPixels)*0.75);
      	
      		headerPanel = (RelativeLayout) findViewById(R.id.header);
      		headerPanelParameters = (LinearLayout.LayoutParams) headerPanel.getLayoutParams();
      		headerPanelParameters.width = metrics.widthPixels;
      		headerPanel.setLayoutParams(headerPanelParameters);
      		
      		menuPanel = (RelativeLayout) findViewById(R.id.menuPanel);
      		menuPanelParameters = (FrameLayout.LayoutParams) menuPanel.getLayoutParams();
      		menuPanelParameters.width = panelWidth;
      		menuPanel.setLayoutParams(menuPanelParameters);
      		
      		slidingPanel = (LinearLayout) findViewById(R.id.slidingPanel);
      		slidingPanelParameters = (FrameLayout.LayoutParams) slidingPanel.getLayoutParams();
      		slidingPanelParameters.width = metrics.widthPixels;
      		slidingPanel.setLayoutParams(slidingPanelParameters);
      		
      		listView = (ListView) findViewById(R.id.list);
      		listViewParameters = (LinearLayout.LayoutParams) listView.getLayoutParams();
      		listViewParameters.width = metrics.widthPixels;
      		listView.setLayoutParams(listViewParameters);
      		
      	
      		//Slide the Panel	
      		menuViewButton = (ImageView) findViewById(R.id.menuViewButton);
      		menuViewButton.setOnClickListener(new OnClickListener() {
      		    public void onClick(View v) {
      		    	if(!isExpanded){
      		    		isExpanded = true;   		    				        		
      		        	
      		    		//Expand
      		    		new ExpandAnimation(slidingPanel, panelWidth,
      		    	    Animation.RELATIVE_TO_SELF, 0.0f,
      		    	    Animation.RELATIVE_TO_SELF, 0.75f, 0, 0.0f, 0, 0.0f);		    			         	    
      		    	}else{
      		    		isExpanded = false;
      		    		
      		    		//Collapse
      		    		new CollapseAnimation(slidingPanel,panelWidth,
                  	    TranslateAnimation.RELATIVE_TO_SELF, 0.75f,
                  	    TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0, 0.0f);
      		   
      					
      		    	}         	   
      		    }
      		});
      	//	
        
    
		adapter = new noticiasAdapter(this, contactList);
		listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);
        
        
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String titulo = ((TextView) view.findViewById(R.id.title))
						.getText().toString();
				String fecha = ((TextView) view.findViewById(R.id.fecha))
						.getText().toString();
				String id_noticia = ((TextView) view.findViewById(R.id.id_noticia))
						.getText().toString();
				String texto = ((TextView) view.findViewById(R.id.texto))
						.getText().toString();
				String url_image = ((TextView) view.findViewById(R.id.url_image))
						.getText().toString();
				
				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						single_noticia.class);
				in.putExtra(TAG_noticiasId, id_noticia);
				in.putExtra(TAG_noticiasTitulo, titulo);
				in.putExtra(TAG_noticiasPathImg, url_image);
				in.putExtra(TAG_noticiasTexto, texto);
				in.putExtra(TAG_noticiasFecha, fecha);
				
				startActivity(in);

			}
		});
		
        progress = new ProgressDialog(this);
		progress.setTitle("Descargando");
		progress.setMessage("Espere por favor...");
		progress.setCancelable(false);
		progress.show();
		contactList.clear();
		adapter.notifyDataSetChanged();
		cargaNoticias();
		
		
		Button btn_web = (Button) findViewById(R.id.menu_item_2);
		 btn_web.setOnClickListener(new View.OnClickListener() {	
				@Override
				public void onClick(View view) {
				
					String url = "http://www.campoverde.edu.mx/";
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					startActivity(i);
				}
			}); 
		 
		 Button btn_face= (Button) findViewById(R.id.menu_item_3);
		 btn_face.setOnClickListener(new View.OnClickListener() {	
				@Override
				public void onClick(View view) {
				
					String url = "https://www.facebook.com/pages/Colegio-Campoverde/135516349844708";
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					startActivity(i);
				}
			}); 
		 
		 
		 Button btn_twitter= (Button) findViewById(R.id.menu_item_4);
		 btn_twitter.setOnClickListener(new View.OnClickListener() {	
			 	@Override
				public void onClick(View view) {
				
					String url = "https://twitter.com/ColCampoverde";
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					startActivity(i);
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
    
    public void cargaNoticias() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					String json = jsonParser.makeHttpRequest(url, "GET", params);
	
					Log.d("Albums JSON: ", "> " + json.toString());
					String noticiasId = "";
					String noticiasTitulo = "";
					String noticiasPathImg = "";
					String noticiasTexto = "";
					String noticiasFecha = "";
					
					lista = new JSONArray(json);
					if (lista != null) {
						// looping through All albums
						for (int i = 0; i < lista.length(); i++) {
							JSONObject c = lista.getJSONObject(i);
						
							
							noticiasId = c.getString(TAG_noticiasId);
							noticiasTitulo = c.getString(TAG_noticiasTitulo);
							noticiasPathImg = c.getString(TAG_noticiasPathImg);
							noticiasTexto = c.getString(TAG_noticiasTexto);
							noticiasFecha = c.getString(TAG_noticiasFecha);
							

							// creating new HashMap
							HashMap<String, String> map = new HashMap<String, String>();

							map.put(TAG_noticiasId, noticiasId);
							map.put(TAG_noticiasTitulo,noticiasTitulo);
							map.put(TAG_noticiasPathImg,
									"http://zavordigital.com/campoverde/"
											+ noticiasPathImg);
							map.put(TAG_noticiasTexto, noticiasTexto);
							map.put(TAG_noticiasFecha, noticiasFecha);
							

							// adding HashList to ArrayList
							contactList.add(map);

							
						}
						
						handler.sendEmptyMessage(0);
					} else {
						Log.d("TIENDAS: ", "null");
						handler.sendEmptyMessage(0);
						Toast.makeText(getApplicationContext(), "error de conexión", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					

				}
			}
		});
		t.start();
	}
    
 
}
