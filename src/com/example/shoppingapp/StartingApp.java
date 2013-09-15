package com.example.shoppingapp;

/**This is the main class that loads when we start the application*/

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class StartingApp extends Activity{
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		setContentView(R.layout.start_app);


				}

	//This are activities for each of the buttons in when the apps  starts.
	public void showShoppingList(View view) {
		Intent shopIntent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity (shopIntent);
	}
	
	public void showConverter(View view) {
		Intent convertIntent = new Intent(getApplicationContext(), StartConverter.class);
		startActivity(convertIntent);
	}
		
		public void showPhoto(View view) {
			Intent photoIntent = new Intent(getApplicationContext(), PhotoDowload.class);
			startActivity(photoIntent);
		}
		
		public void showMap(View view) {
			Intent mapIntent = new Intent(getApplicationContext(), MyMap.class);
			startActivity(mapIntent);
		}
		
		//This is used when we want to exit the app. A dialog box is appeared
		
		@Override
		public void onBackPressed() {
		    new AlertDialog.Builder(this)
		        .setIcon(android.R.drawable.ic_dialog_alert)
		        .setTitle("Closing Shopping App")
		        .setMessage("Are you sure you want to exit?")
		        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
		    {
		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		            finish();    
		        }

		    })
		    .setNegativeButton("No", null)
		    .show();
		}
	}




