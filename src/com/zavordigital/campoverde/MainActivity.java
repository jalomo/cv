package com.zavordigital.campoverde;

import com.google.android.gcm.GCMRegistrar;


import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author Adil Soomro
 *
 */
public class MainActivity extends TabActivity {
	/** Called when the activity is first created. */
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		SharedPreferences prefe = getSharedPreferences("datos",
				Context.MODE_PRIVATE);
		String msg = prefe.getString("idusuario", "null");
		
		//Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
		
		
		setTabs() ;
	}
	private void setTabs()
	{
		addTab("", R.drawable.tab_noticias, noticiasComunicados.class);
		//addTab("", R.drawable.tab_chat, OptionsActivity.class);
		addTab("", R.drawable.tab_chat, inicar_chat.class);
		
		addTab("", R.drawable.tab_citas, citas.class);
		addTab("", R.drawable.tab_eventos, eventos.class);
		addTab("", R.drawable.tab_info, serviciosEscolares.class);
	}
	
	private void addTab(String labelId, int drawableId, Class<?> c)
	{
		TabHost tabHost = getTabHost();
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);	
		
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}
	
	
}