package com.example.shoppingapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import com.example.shoppingapp.GetJSONListener;

public class JSONClient extends AsyncTask<String, Void, JSONArray>{
    ProgressDialog progressDialog ;
    GetJSONListener getJSONListener;
    Context curContext;
    public JSONClient(Context context, GetJSONListener listener){
        this.getJSONListener = listener;
        curContext = context;
    }
    private static String convertStreamToString(InputStream is) {
        /**BufferedReader.readLine() is used to convert the InputStream to String*/
    	
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
 
        return sb.toString();
    }
 
 
    public static JSONArray connect(String url) throws JSONException
    {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("Accept", "application/json");
        
       HttpResponse response;
        try {
            response = httpclient.execute(httpget);
            
           
            HttpEntity entity = response.getEntity();
 
            if (entity != null) {
 
                
                InputStream instream = entity.getContent();
                String result= convertStreamToString(instream);
 
                
                JSONArray jsonArray=new JSONArray(result);
 
                
                instream.close();
 
                return jsonArray;
            }
 
 
        } catch (ClientProtocolException e) {
           
            e.printStackTrace();
        } catch (IOException e) {
            
            e.printStackTrace();
        } catch (JSONException e) {
           
            e.printStackTrace();
        }
 
        return null;
    }
    @Override
    public void onPreExecute() {
        progressDialog = new ProgressDialog(curContext);
        progressDialog.setMessage("Converter is loading!Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
 
    }
 
    @Override
    protected JSONArray doInBackground(String... urls) {
        try {
			return connect(urls[0]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
 
    @Override
    protected void onPostExecute(JSONArray json ) {
        getJSONListener.onRemoteCallComplete(json);
        progressDialog.dismiss();
    }
}