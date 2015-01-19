package com.zavordigital.campoverde;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author Adil Soomro
 *
 */
public class OptionsActivity extends Activity {
	
	private static final String BS_PACKAGE = "com.zavordigital.campoverdechat";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_chat);
        
        Button btn_productos;
        btn_productos = (Button)findViewById(R.id.chat_);    
        
        btn_productos.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				showPopUpPhone();
			}
		}); 
    }
    
    private void showPopUpPhone() { 
		 
		 LayoutInflater factory = LayoutInflater.from(this);

		//text_entry is an Layout XML file containing two text field to display in alert dialog
		final View textEntryView = factory.inflate(R.layout.dialog_chat, null);

		
		
		

		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setIcon(R.drawable.ic_launcher).setTitle("").setView(textEntryView).setPositiveButton("Aceptar",
		  new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog,
		     int whichButton) {

			   Uri uri = Uri.parse("market://search?q=pname:"+BS_PACKAGE);  
			   Intent it = new Intent(Intent.ACTION_VIEW, uri);  
			   startActivity(it); 
		   
		    /* User clicked OK so do some stuff */
		   }
		  }).setNegativeButton("Cancelar",
		  new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog,
		     int whichButton) {
		     /*
		     * User clicked cancel so do some stuff
		     */
		   }
		  });
		alert.show();
		 
		
	      } 
}
