
package com.example.shoppingapp;

/**This class is the page of shopping list*/

import java.util.ArrayList;
import java.util.HashMap;
import android.app.ListActivity;
import com.example.shoppingapp.ShoppingDB;
import com.example.shoppingapp.EditItemList;
import com.example.shoppingapp.MainActivity;
import com.example.shoppingapp.AddNewItems;
import com.example.shoppingapp.R;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.ListView;


public class MainActivity extends ListActivity {

	Intent intent;
	TextView itemId;

	ShoppingDB shopdatabase = new ShoppingDB(this);

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);//define that the interface used is activity_main
		
		//Store data from database in an array list
		
		ArrayList<HashMap<String, String>> contactList =  shopdatabase.getAllItems();

		//Check if there are items to display
		if(contactList.size()!=0) {
			
			ListView listView = getListView();
			listView.setOnItemClickListener(new OnItemClickListener() {
				
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					
								
					itemId = (TextView) view.findViewById(R.id.itemId);
					
					String itemIdValue = itemId.getText().toString();	
					
					Intent  theIndent = new Intent(getApplication(),EditItemList.class);
					
								
					theIndent.putExtra("itemId", itemIdValue); 
					
					finish();
					
					startActivity(theIndent); 
					
									}
			}); 
			
			// Here we use ListAdapter as a bridge between ListView and the data of ListView
			
			ListAdapter adapter = new SimpleAdapter( MainActivity.this,contactList, R.layout.item_list_entry, new String[] { "itemId","textAmount", "textItem"}, new int[] {R.id.itemId, R.id.textAmount, R.id.textItem});
			
			setListAdapter(adapter);
			
			
		}
		
	}
	
	//When we press button that calls showAddItem activity then AddNewItems is called	
		
	public void showAddItem(View view) {
		Intent theIntent = new Intent(getApplicationContext(), AddNewItems.class);
		finish();
		startActivity(theIntent);
	}
	
	
}
