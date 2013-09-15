package com.example.shoppingapp;

/**This is the main class for the currency converter*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

import com.example.shoppingapp.CurrencyConverter;
import com.example.shoppingapp.GetJSONListener;
import com.example.shoppingapp.JSONClient;
import com.example.shoppingapp.StartConverter;
import com.example.shoppingapp.R;
import com.example.shoppingapp.ExchangeRate;

public class StartConverter extends Activity {
	JSONArray rates;
	public static String PACKAGE_NAME;
	List ratesList;
	
	ListView fromList;
	ListView toList;
	String[] ratesArray;
	
	BigDecimal fromAmount;
	BigDecimal toAmount;
	
	BigDecimal fromRate;
	BigDecimal toRate;
	
	boolean firstFetch;
	
	double value;
	
	int fromSelected;
	int toSelected;
	
	EditText fromInput;
	
	Dialog fromDialog;
	Dialog toDialog;
	
	ExchangeRate fromAdapter;
	ExchangeRate toAdapter;
	
	Time lastUpdated;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.converter_curr);
		
		PACKAGE_NAME = getApplicationContext().getPackageName();
		
		fromSelected = 35;//NOK
		toSelected = 3;//Euro

		final View view = this.findViewById(android.R.id.content);
		final Activity activity = this;
		
		firstFetch = true;
		lastUpdated = new Time();
		
	    checkConnectivity();
        
        view.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
			    Rect r = new Rect();
			    //r will be populated with the coordinates of view that area still visible.
			    view.getWindowVisibleDisplayFrame(r);

			    int heightDiff = view.getRootView().getHeight() - (r.bottom - r.top);
			    if (heightDiff > 100) { 
			    	fromInput.setCursorVisible(true);
			    }
			 }
			});
			
			
			view.setOnTouchListener(new View.OnTouchListener() {  
				@Override
			    public boolean onTouch (View v, MotionEvent event)
			    {
			        hideSoftKeyboard(activity);
					EditText fromInput = (EditText) findViewById(R.id.fromInput);
					EditText answer = (EditText) findViewById(R.id.answer);
					fromInput.clearFocus();
					fromInput.setCursorVisible(false);
			        return false;
			    }
			});
			
			// Setup event to pick currency from
			final Button fromButton = (Button) findViewById(R.id.selectConvertFrom);
	        fromButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	showSelectFrom(v);
	            }
	        });
	        
	        // Setup event to pick currency to
	        final Button toButton = (Button) findViewById(R.id.selectConvertTo);
	        toButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	showSelectTo(v);
	            }
	        });
	        
	        // Setup event for swap button
	        final ImageButton swapButton = (ImageButton) findViewById(R.id.swapButton);
	        swapButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	swapCurrencies(v);
	            }
	        });
	        
	        
	        fromInput = (EditText)findViewById(R.id.fromInput);
			fromInput.setCursorVisible(false);
	        fromInput.addTextChangedListener(new TextWatcher() {
	        	@Override
	        	public void afterTextChanged(Editable s) {
	        		try {
	        			value = Double.parseDouble(s.toString());
	        			calculateExchangeRate();
	        		} catch(NumberFormatException e) {
	        			e.printStackTrace();
	        		}
	            }

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onTextChanged(CharSequence arg0, int arg1, int arg2,
						int arg3) {
					// TODO Auto-generated method stub
					
				}
	        });
	        fromInput.setOnEditorActionListener(new OnEditorActionListener() {        
	            @Override
	            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	                if(actionId==EditorInfo.IME_ACTION_DONE){
	                     fromInput.clearFocus();
	                     fromInput.setCursorVisible(false);
	                }
	                return false;
	            }
	        });
	}
 
    GetJSONListener l = new GetJSONListener(){
 
		@Override
		public void onRemoteCallComplete(JSONArray jsonFromNet) {
			rates = jsonFromNet;

			EditText fromInput = (EditText) findViewById(R.id.fromInput);
			
			try {
				fromRate = new BigDecimal(rates.getJSONObject(fromSelected).getString("rate").toString());
				fromInput.setText("1.00");
				toRate = new BigDecimal(rates.getJSONObject(toSelected).getString("rate").toString());
				calculateExchangeRate();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	};
	
	@Override
	public void onResume(){
		super.onResume();
		if (!firstFetch) {
			checkConnectivity();
		}
	}
	
	@Override
	public void onPause(){
		super.onPause();
		firstFetch = false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
              if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
        	fromInput.setCursorVisible(true);
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
        	
        }
    }
    
    private void checkConnectivity() {
    	Time currentTime = new Time();
    	currentTime.setToNow();
    	Time lastTime = new Time(lastUpdated);
    	lastTime.monthDay += 1;
    	lastTime.hour = 13;
    	
    	if (Time.isEpoch(lastUpdated) || lastTime.before(currentTime)) {
	    	ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
		        
		    // This is what happened if the network is not connected
	        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
	        	new AlertDialog.Builder(this)
	        	.setTitle("No Internet Connection")
	        	.setMessage("Rates could not be updated and may be out of date.")
	        	.setPositiveButton("OK", new OnClickListener() {
	        	    public void onClick(DialogInterface arg0, int arg1) {
	        	          }
	        	})
	        	.show();

	            try {
		        	    BufferedReader reader = new BufferedReader(
		        	        new InputStreamReader(getAssets().open("rates.json"), "UTF-8"));
		        	    
		        	    String json = new String();
		        	    
		        	    
		        	    String mLine;
						try {
							mLine = reader.readLine();
			        	    if (mLine != null) {
			        	      
			        	       json += mLine;
			        	       rates = new JSONArray(json);
			        	       
			        	       EditText fromInput = (EditText) findViewById(R.id.fromInput);
			       			
				       			try {
				       				fromRate = new BigDecimal(rates.getJSONObject(fromSelected).getString("rate").toString());
				       				fromInput.setText("1.00");
				       				toRate = new BigDecimal(rates.getJSONObject(toSelected).getString("rate").toString());
				       				calculateExchangeRate();
				       				reader.close();
				       			} catch (JSONException e) {
				       				
				       				e.printStackTrace();
				       			}		        	       
			        	    }
			
						} catch (IOException e) {
							
							e.printStackTrace();
						}
	            } catch (JSONException e) {
							
							e.printStackTrace();
	            } catch (UnsupportedEncodingException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
	        } else {
				JSONClient client = new JSONClient(this, l);
				
				
				String url = "http://exchng.ca/rates?%7b%22$sort%22:%7b%22order%22:1%7d%7d";// URL from where the exchange rate are taken
				client.execute(url);
				lastUpdated.setToNow();
	    }
    	}
    }

	public void calculateExchangeRate() {
		try {
			fromRate = new BigDecimal(rates.getJSONObject(fromSelected).getString("rate").toString());
			toRate = new BigDecimal(rates.getJSONObject(toSelected).getString("rate").toString());
			EditText fromInput = (EditText) findViewById(R.id.fromInput);
			double value = Double.parseDouble(fromInput.getText().toString());
			double exchangeRate = fromRate.doubleValue() / toRate.doubleValue();
			double answer = value * exchangeRate;
		
			EditText answerText = (EditText) findViewById(R.id.answer);
			answerText.setText(String.format("%.2f", answer));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void swapCurrencies(View v) {
		
		ImageView fromFlag = (ImageView) findViewById(R.id.fromImage);
		ImageView toFlag = (ImageView) findViewById(R.id.toImage);
		
		// To Swap the currency
		try {
			String file = rates.getJSONObject(fromSelected).getString("shortcode").toString().toLowerCase() + "_flag";
			int resID = getResources().getIdentifier(file , "drawable", StartConverter.PACKAGE_NAME);
			toFlag.setImageResource(resID);
			file = rates.getJSONObject(toSelected).getString("shortcode").toString().toLowerCase() + "_flag";
			resID = getResources().getIdentifier(file , "drawable", StartConverter.PACKAGE_NAME);
			fromFlag.setImageResource(resID);
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		
		int tempSelected = toSelected;
		toSelected = fromSelected;
		fromSelected = tempSelected;
		
		if (fromList != null) {
			fromList.setItemChecked(fromSelected, true);
		}
		if (toList != null) {
			toList.setItemChecked(toSelected, true);
		}
		
		Button selectFromButton = (Button) findViewById(R.id.selectConvertFrom);
		String labelTo = (String) selectFromButton.getText();
	    
	    Button selectToButton = (Button) findViewById(R.id.selectConvertTo);
	    String labelFrom = (String) selectToButton.getText();
	    
	    selectFromButton.setText(labelFrom);
	    selectToButton.setText(labelTo);
		
		calculateExchangeRate();
	}
	
	public void showSelectFrom(View v) {
		if (fromDialog == null) {
			fromDialog = new Dialog(getBaseContext());
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Select Currency");
			builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    dialog.dismiss();
                }
            });
			
			
			fromList = new ListView(this);
			
			ratesList = new ArrayList(rates.length());
			fromList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			
	        
	        for(int i=0; i < rates.length(); i++){
	        	try {
	        		ratesList.add(new CurrencyConverter(rates.getJSONObject(i).getString("label").toString(), rates.getJSONObject(i).getString("shortcode").toString()));
	        	} catch (JSONException e) {
					
					e.printStackTrace();
				}
	        }
	        
	        fromAdapter = new ExchangeRate(this, R.layout.converter_checkbox, ratesList, getResources(), fromSelected);

			fromList.setAdapter(fromAdapter);
			
			fromList.setItemChecked(fromSelected, true);
			
			
	        OnItemClickListener itemClickListener = new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
	            	StartConverter.this.selectNewFrom(position);
	            	fromDialog.dismiss();
	             }
	        };
	        
	        
	        fromList.setOnItemClickListener(itemClickListener);
	        fromList.setBackgroundColor(getResources().getColor(R.color.almostBlack));
	       
			builder.setView(fromList);
			fromDialog = builder.create();
		}
 
		fromDialog.show();
	}
	
	public void showSelectTo(View v) {
		if (toDialog == null) {
			
			toDialog = new Dialog(getBaseContext());
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Select Currency");
			builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    dialog.dismiss();
                }
            });
			
			toList = new ListView(this);
			
			ratesList = new ArrayList(rates.length());
			toList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			
	       
	        for(int i=0; i < rates.length(); i++){
	        	try {
	        		ratesList.add(new CurrencyConverter(rates.getJSONObject(i).getString("label").toString(), rates.getJSONObject(i).getString("shortcode").toString()));
	        	} catch (JSONException e) {
					
					e.printStackTrace();
				}
	        }
	        
	        toAdapter = new ExchangeRate(this, R.layout.converter_checkbox, ratesList, getResources(), toSelected);

			toList.setAdapter(toAdapter);
			toList.setItemChecked(toSelected, true);
			
	        OnItemClickListener itemClickListener = new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
	            	StartConverter.this.selectNewTo(position);
	            	toDialog.dismiss();
	            }
	        };
	        
	        
	        toList.setOnItemClickListener(itemClickListener);
	        toList.setBackgroundColor(getResources().getColor(R.color.almostBlack));
	      
			builder.setView(toList);
			toDialog = builder.create();
		}
		
		toDialog.show();
	}
	
	public boolean selectNewFrom(int selected) {
	    fromSelected = selected;
	    fromList.setItemChecked(fromSelected, true);
	    
	    fromAdapter.setSelected(fromSelected);
	    
	    Button button = (Button) findViewById(R.id.selectConvertFrom);
	    ImageView flag = (ImageView) findViewById(R.id.fromImage);
	    
	    try {
	    	button.setText(rates.getJSONObject(fromSelected).getString("label").toString());
			fromRate = new BigDecimal(rates.getJSONObject(fromSelected).getString("rate").toString());
			String file = rates.getJSONObject(fromSelected).getString("shortcode").toString().toLowerCase() + "_flag";
			int resID = getResources().getIdentifier(file , "drawable", StartConverter.PACKAGE_NAME);
			flag.setImageResource(resID);
			calculateExchangeRate();
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	    
	    return true;
	}
	
	public boolean selectNewTo(int selected) {
	    toSelected = selected;
	    toList.setItemChecked(toSelected, true);
	    
	    toAdapter.setSelected(toSelected);
	    
	    Button button = (Button) findViewById(R.id.selectConvertTo);
	    ImageView flag = (ImageView) findViewById(R.id.toImage);
	    
	    try {
	    	button.setText(rates.getJSONObject(toSelected).getString("label").toString());
			fromRate = new BigDecimal(rates.getJSONObject(toSelected).getString("rate").toString());
			String file = rates.getJSONObject(toSelected).getString("shortcode").toString().toLowerCase() + "_flag";
			int resID = getResources().getIdentifier(file , "drawable", StartConverter.PACKAGE_NAME);
			flag.setImageResource(resID);
			calculateExchangeRate();
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	    
	    return true;
	}
	
	public static void hideSoftKeyboard(Activity activity) {
	    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}
}

