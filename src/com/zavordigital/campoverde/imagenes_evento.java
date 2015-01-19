package com.zavordigital.campoverde;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;





public class imagenes_evento extends Activity {
	
	public static final String KEY_THUMB_URL = "imaPath";

	private static String url = "http://www.zavordigital.com/campoverde/index.php/mobiles/getImagesEvento/";
	final ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
	public static final String TGA_ID = "tiendaId";
	JSONParser jsonParser = new JSONParser();
	JSONArray tiendas2 = null;
	imagen_adaptador adapter;
	private ListView listView;
	String id_t="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagenes_evento);
		Intent in = getIntent();
        id_t = in.getStringExtra(TGA_ID);
        //url=url+id_t;
		cargaCategoria();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

	public void cargaCategoria()
    {
		// Hashmap for ListView
 		
 		final List<NameValuePair> params = new ArrayList<NameValuePair>();
 		new Thread(new Runnable() {
 		    public void run() {
 		    	url=url+id_t;
		 		String json = jsonParser.makeHttpRequest(url, "GET",
						params);
		 		Log.d("tienda JSON: ", "> " + json);
		 		String urlimage="";
				try {				
					tiendas2 = new JSONArray(json);
					
					if (tiendas2 != null) {
						// looping through All albums
						for (int i = 0; i < tiendas2.length(); i++) {
							JSONObject c = tiendas2.getJSONObject(i);
							urlimage = c.getString(KEY_THUMB_URL);
							HashMap<String, String> map = new HashMap<String, String>();
							map.put(KEY_THUMB_URL, "http://zavordigital.com/campoverde/"+urlimage);
				 			contactList.add(map);	
						}
					
					}else{
						Log.d("tienda: ", "null");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				runOnUiThread(new Runnable() {
			        public void run() {
			        	load_images_promo();
			        }
			    });
 		   }
 		}).start();
		// ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
		// adapter=new imagen_adaptador(this, contactList); 
		// viewPager.setAdapter(adapter);
		 url = "http://www.zavordigital.com/campoverde/index.php/mobiles/getImagesEvento/";
    } 
	
	public void load_images_promo(){
		 ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
		 adapter=new imagen_adaptador(this, contactList); 
		 viewPager.setAdapter(adapter);
		
	}
	
	 private class imagen_adaptador extends PagerAdapter {
		 private Activity activity;
		    private ArrayList<HashMap<String, String>> data;
		    //private LayoutInflater inflater=null;
		    public ImageLoader imageLoader; 
		 
		 public imagen_adaptador(Activity a,ArrayList<HashMap<String, String>> d) {
		        activity = a;
		        data=d;
		        //inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        imageLoader=new ImageLoader(activity.getApplicationContext());
		    }
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			 return data.size();
		}

		@Override
	    public boolean isViewFromObject(View view, Object object) {
	        return view == ((LinearLayout) object);
	    }

		@Override
	    public void destroyItem(ViewGroup container, int position, Object object) {
	        ((ViewPager) container).removeView((LinearLayout) object);
	 
	    }


		@Override
		public void finishUpdate(ViewGroup container) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View vi=null;
			LayoutInflater inflater=(LayoutInflater)container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			vi = inflater.inflate(R.layout.item_imagen_evento, null);
			ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image);
			HashMap<String, String> song = new HashMap<String, String>();
	        song = data.get(position);
	        imageLoader.DisplayImage(song.get(imagenes_evento.KEY_THUMB_URL), thumb_image);
			
			((ViewPager) container).addView(vi);
			return vi;
		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(ViewGroup container) {
			// TODO Auto-generated method stub
			
		}
		    
		 
	    

	   
	  }
	
}

