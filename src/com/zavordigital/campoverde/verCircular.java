package com.zavordigital.campoverde;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;


public class verCircular extends Activity {
	
	static final String TAG_direccion= "direccion";
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_circular);
       
        Intent in = getIntent();
        String url = in.getStringExtra(TAG_direccion);
        
        WebView myWebView = (WebView) this.findViewById(R.id.webView);
        myWebView.loadUrl(url);
		
	}
	
	public void atras(View v) {
		
		finish();
	}

}