package com.example.shoppingapp;

/**This class is used to add new item in the shopping list*/

import java.util.HashMap;
import com.example.shoppingapp.ShoppingDB;
import com.example.shoppingapp.MainActivity;
import com.example.shoppingapp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddNewItems extends Activity {
	
	// The EditText objects

		EditText textItem;
		EditText textAmount;
		EditText textPrice;
		EditText textPlace;
		
		ShoppingDB shopdatabase = new ShoppingDB(this);//use the database that we have created

@Override
public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);

	setContentView(R.layout.add_item);//define the interface that is used, add_item.xml

	
	textItem = (EditText) findViewById(R.id.textItem);
	textAmount = (EditText) findViewById(R.id.textAmount);
	textPrice = (EditText) findViewById(R.id.textPrice);
	textPlace = (EditText) findViewById(R.id.textPlace);
	}

/**This is the metod that adds items*/

public void addNewItems(View view) {
	
	HashMap<String, String> queryValuesMap =  new  HashMap<String, String>();

		
	queryValuesMap.put("textItem", textItem.getText().toString());
	queryValuesMap.put("textAmount", textAmount.getText().toString());
	queryValuesMap.put("textPrice", textPrice.getText().toString());
	queryValuesMap.put("textPlace", textPlace.getText().toString());
	
	
	shopdatabase.insertItem(queryValuesMap);//add into database
	

	this.callMainActivity(view);
}
public void callMainActivity(View view) {
	Intent theIntent = new Intent(getApplicationContext(), MainActivity.class);
	finish();
	startActivity(theIntent);
}	
}
