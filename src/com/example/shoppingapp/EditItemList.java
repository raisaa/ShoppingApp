package com.example.shoppingapp;
/**This  class edit the items of the list or delete them*/

import java.util.HashMap;
import com.example.shoppingapp.MainActivity;
import com.example.shoppingapp.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class EditItemList extends Activity{
	
			EditText textItem;
			EditText textAmount;
			EditText textPrice;
			EditText textPlace;
			
			ShoppingDB shopdatabase = new ShoppingDB(this);
			
			public void onCreate(Bundle savedInstanceState){
				
				super.onCreate(savedInstanceState);
				
				setContentView(R.layout.edit_items);
				
				textItem = (EditText) findViewById(R.id.textItem);
				textAmount = (EditText) findViewById(R.id.textAmount);
				textPrice = (EditText) findViewById(R.id.textPrice);
				textPlace = (EditText) findViewById(R.id.textPlace);
				
				Intent theIntent = getIntent();
				
				String itemId = theIntent.getStringExtra("itemId");
				
				HashMap<String, String> itemList = shopdatabase.getItemInfo(itemId);
				
				if(itemList.size() != 0){
					
					textItem.setText(itemList.get("textItem"));
					textAmount.setText(itemList.get("textAmount"));
					textPrice.setText(itemList.get("textPrice"));
					textPlace.setText(itemList.get("textPlace"));
					
					
				}
			}
			
			//This method is used to edit item list
			
			public void editItems(View view){
				
				HashMap<String, String> queryValuesMap = new HashMap<String, String>();
				
				textItem = (EditText) findViewById(R.id.textItem);
				textAmount = (EditText) findViewById(R.id.textAmount);
				textPrice = (EditText) findViewById(R.id.textPrice);
				textPlace = (EditText) findViewById(R.id.textPlace);
				
				
				Intent theIntent = getIntent();
				
				String itemId = theIntent.getStringExtra("itemId");
				
				queryValuesMap.put("itemId", itemId);
				queryValuesMap.put("textItem", textItem.getText().toString());
				queryValuesMap.put("textAmount", textAmount.getText().toString());
				queryValuesMap.put("textPrice", textPrice.getText().toString());
				queryValuesMap.put("textPlace", textPlace.getText().toString());
				
				
				shopdatabase.updateItem(queryValuesMap);
				this.callMainActivity(view);
				
			}
			//This method is used to remove item from the list
			
			public void removeItems(View view){
				
				Intent theIntent = getIntent();
				
				String itemId = theIntent.getStringExtra("itemId");
				
				shopdatabase.deleteItem(itemId);
				
				this.callMainActivity(view);
				
			}
			
			public void callMainActivity(View view){
				
				Intent objIntent = new Intent(getApplication(), MainActivity.class);
				
				finish();
				
				startActivity(objIntent);
				
			}
			
			//This method is used to exit activity when user press the back button			
		    public void onBackPressed() {
				       
				  Intent backIntent = new Intent(getApplication(), MainActivity.class);
				  finish();
				  startActivity(backIntent);
				    
	       }
			
		}


