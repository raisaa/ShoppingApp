package com.example.shoppingapp;

/**This class is for the database that is used to store shopping items*/

import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ShoppingDB extends SQLiteOpenHelper {
	
	public ShoppingDB(Context shoppingAppContext){
	
	super(shoppingAppContext, "shoppingDB", null, 1);
	
	}

	@Override
	//Create database
	public void onCreate(SQLiteDatabase database) {
			
		String query = "CREATE TABLE shoppinglist ( itemId INTEGER PRIMARY KEY, textItem TEXT, " +
				"textAmount TEXT, textPrice TEXT, textPlace TEXT)";
				
				database.execSQL(query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
					
			String query = "DROP TABLE IF EXISTS shoppinglist";
			
			database.execSQL(query);
			onCreate(database);
		}
	
	//Insert shopping items into database	
public void insertItem(HashMap<String, String> queryValues){
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		
		values.put("textItem", queryValues.get("textItem"));
		values.put("textAmount", queryValues.get("textAmount"));
		values.put("textPrice", queryValues.get("textPrice"));
		values.put("textPlace", queryValues.get("textPlace"));
				
		database.insert("shoppinglist", null, values);
		
		database.close();
		
	}

//Update the shopping items into the database

public int updateItem(HashMap<String, String> queryValues) {
	
	SQLiteDatabase database = this.getWritableDatabase();
	
	ContentValues values = new ContentValues();
	
	values.put("textItem", queryValues.get("textItem"));
	values.put("textAmount", queryValues.get("textAmount"));
	values.put("textPrice", queryValues.get("textPrice"));
	values.put("textPlace", queryValues.get("textPlace"));
			
	return database.update("shoppinglist", values, "itemId" + " = ?", new String[] {queryValues.get("itemId") });
	
}

//Delete items into the database

public void deleteItem(String id){
	
	SQLiteDatabase database = this.getWritableDatabase();
	
	String deleteQuery = "DELETE FROM shoppinglist WHERE itemId='" + id + "'";
	
	database.execSQL(deleteQuery);
	
}

public ArrayList<HashMap<String, String>> getAllItems(){
	
	ArrayList<HashMap<String, String>> itemArrayList = new ArrayList<HashMap<String, String>>();
	
	String selectQuery = "SELECT * FROM shoppinglist ORDER BY textItem";
	
	SQLiteDatabase database = this.getWritableDatabase();
	
	Cursor cursor = database.rawQuery(selectQuery, null);
	
	if(cursor.moveToFirst()){
		
		do{
			
			HashMap<String, String> itemMap = new HashMap<String, String>();
			
			itemMap.put("itemId", cursor.getString(0));
			itemMap.put("textItem", cursor.getString(1));
			itemMap.put("textAmount", cursor.getString(2));
			itemMap.put("textPrice", cursor.getString(3));
			itemMap.put("textPlace", cursor.getString(4));
			
			
		itemArrayList.add(itemMap);
			
		} while(cursor.moveToNext());
		
	}
	
	return itemArrayList;
	
}

public HashMap<String, String> getItemInfo(String id){
	
	HashMap<String, String> itemMap = new HashMap<String, String>();
	
	SQLiteDatabase database = this.getReadableDatabase();
	
	String selectQuery = "SELECT * FROM shoppinglist WHERE itemId='" + id + "'";
	
	Cursor cursor = database.rawQuery(selectQuery, null);
	
	if(cursor.moveToFirst()){
		
		do{
			
			itemMap.put("itemId", cursor.getString(0));
			itemMap.put("textItem", cursor.getString(1));
			itemMap.put("textAmount", cursor.getString(2));
			itemMap.put("textPrice", cursor.getString(3));
			itemMap.put("textPlace", cursor.getString(4));
			
		} while(cursor.moveToNext());
		
	}
	
	return itemMap;
	
}

}