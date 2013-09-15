package com.example.shoppingapp;
/**Get JSONListener, for currency Converter*/
import org.json.JSONArray;

public interface GetJSONListener {
	public void onRemoteCallComplete(JSONArray json);
}