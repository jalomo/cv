
package com.zavordigital.campoverde;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class single_noticia extends Activity {
	static final String TAG_noticiasId = "noticiasId";
	static final String TAG_noticiasTitulo= "noticiasTitulo";
	static final String TAG_noticiasPathImg= "noticiasPathImg";
	static final String TAG_noticiasTexto= "noticiasTexto";
	static final String TAG_noticiasFecha= "noticiasFecha";
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_single_noticia);
        Intent in = getIntent();
        String titulo = in.getStringExtra(TAG_noticiasTitulo);
        String url_image = in.getStringExtra(TAG_noticiasPathImg);
        String texto = in.getStringExtra(TAG_noticiasTexto);
        String fecha = in.getStringExtra(TAG_noticiasFecha);
        
        
        TextView Ptitulo = (TextView) findViewById(R.id.title);
		Ptitulo.setText(""+titulo);
		
		TextView Ptexto = (TextView) findViewById(R.id.texto);
		String letras= "<html><body ><p align=right>"+texto+"<p/> </body></Html>";
		Ptexto.setText(Html.fromHtml(letras));
		
		TextView Pfecha = (TextView) findViewById(R.id.fecha);
		Pfecha.setText(""+fecha);
		
		
		
		ImageView image = (ImageView) findViewById(R.id.image);
        String image_url = url_image;
        ImageLoader imgLoader = new ImageLoader(getApplicationContext());
        imgLoader.DisplayImage(image_url, image);
		
		
	}
	
	public void atras(View v) {
		
		finish();
	}

}