package com.zavordigital.campoverde;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class Mapa extends android.support.v4.app.FragmentActivity {
		
		private GoogleMap mapa;
		private GPS gps;
		
		
		static final String TAG_eventosId = "eventosId";
		static final String TAG_eventosTitulo= "eventosTitulo";
		static final String TAG_eventosPathImg= "eventosPathImg";
		static final String TAG_eventosTexto= "eventosTexto";
		static final String TAG_eventosHora= "eventosHora";
		static final String TAG_eventosFecha= "eventosFecha";
		static final String TAG_eventosLatitud= "eventosLatitud";
		static final String TAG_eventosLongitud= "eventosLongitud";
		static final String TAG_eventosDireccion= "eventosDireccion";
		
		static double latidudE=0; 
		static  double longitudE=0;
		 String eventosId;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			setContentView(R.layout.main_ubicacion);
			//Bundle bundle = getIntent().getExtras();
				Intent in = getIntent();
		        String latitud = in.getStringExtra(TAG_eventosLatitud);
		        String longitud = in.getStringExtra(TAG_eventosLongitud);
		        String titulo = in.getStringExtra(TAG_eventosTitulo);
		        String texto = in.getStringExtra(TAG_eventosTexto);
		        eventosId = in.getStringExtra(TAG_eventosId);
		        
		        TextView Ptitulo = (TextView) findViewById(R.id.titulo);
				Ptitulo.setText(""+titulo);
				TextView Ptexto = (TextView) findViewById(R.id.texto);
				Ptexto.setText(""+texto);
				latidudE=Double.parseDouble(latitud);
				longitudE=Double.parseDouble(longitud);
			setMap("campo verde",Double.parseDouble(latitud), Double.parseDouble(longitud));
			gps = new GPS(this);
			
			
			
			
			
			 Button btn_productos;
		        btn_productos = (Button)findViewById(R.id.ver_fotos);    
		        
		        btn_productos.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent in = new Intent(getApplicationContext(), imagenes_evento.class);
						in.putExtra("tiendaId", eventosId);
						startActivity(in);
					}
				}); 
		}
		
		@Override
		protected void onResume() {
			super.onResume();
			gps.start();
		}

		@Override
		protected void onPause() {
			super.onPause();
			gps.stop();
		}

		public void comoLlegar(View v) {
			String[] location = gps.getLocation();
			if (location == null) {
				String uriBegin = "geo:" + latidudE + "," + longitudE;
				String query = latidudE + "," + longitudE+ "(campo verde)";
				String encodedQuery = Uri.encode(query);
				String uriString = uriBegin + "?q=" + encodedQuery + "&z=18";
				Uri uri = Uri.parse(uriString);
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			} else {
				String uri = "http://maps.google.com/maps?saddr="
						+ location[0] + "," + location[1] + "&daddr="
						+ latidudE+ "," + longitudE;
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(uri));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		}
		
		private void setMap(String nombre, double latitud, double longitud) {
			mapa = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			LatLng donVino = new LatLng(latitud, longitud);
			CameraPosition camPos = new CameraPosition.Builder().target(donVino)
					.zoom(16).build();

			CameraUpdate camUpd3 = CameraUpdateFactory.newCameraPosition(camPos);

			mapa.animateCamera(camUpd3);
			mapa.setMyLocationEnabled(true);
			mapa.addMarker(new MarkerOptions()
	        .position(new LatLng(latitud, longitud))
	        .title(nombre));
		}
		
		public void atras(View v) {
			
			finish();
		}
		
		
		
		
		/*public void guardaAlarma(){
			
			AlarmManager alarmMgr = null;
			PendingIntent alarmIntent = null;
			alarmMgr = (AlarmManager)this.getSystemService(this.ALARM_SERVICE);
			Intent intent = new Intent(this, Mapa.class);
			alarmIntent = PendingIntent.getBroadcast(this, Integer.parseInt(eventosId), intent, 0);

			// Set the alarm to start at 8:30 a.m.
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.set(Calendar.HOUR_OF_DAY, 18);
			calendar.set(Calendar.MINUTE, 42);
			
		}*/

}
