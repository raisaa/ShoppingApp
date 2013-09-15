package com.example.shoppingapp;

import java.util.List;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.shoppingapp.CurrencyConverter;
import com.example.shoppingapp.StartConverter;
import com.example.shoppingapp.R;


public class ExchangeRate extends ArrayAdapter {
   	
	Resources res;
	int selected;
	
    public ExchangeRate(Context context, int textViewResourceId,List objects, Resources res, int selected) {
        super(context, textViewResourceId, objects);
        this.res = res;
        this.selected = selected;
    }
    
    public void setSelected (int selected) {
    	this.selected = selected;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
 
        final CurrencyConverter c = (CurrencyConverter) getItem(position);
 
        
        ViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.converter_checkbox, null);
 
            holder = new ViewHolder();
            holder.label = (TextView) convertView.findViewById(R.id.label);
            holder.flag = (ImageView) convertView.findViewById(R.id.flag);
            holder.flag.setFocusable(false);
            holder.flag.setFocusableInTouchMode(false);
            holder.cbk = (CheckBox) convertView
                    .findViewById(R.id.cbk);
            convertView.setTag(holder);
        } else {
           
            holder = (ViewHolder) convertView.getTag();
        }
 
               holder.label.setText(c.getLabel());
        
        String mDrawableName = c.getShortcode().toLowerCase() + "_flag";
        int resID = res.getIdentifier(mDrawableName , "drawable", StartConverter.PACKAGE_NAME);
        holder.flag.setImageResource(resID);
        
        if (position == selected) {
        	holder.cbk.setChecked(true);
        } else {
        	holder.cbk.setChecked(false);
        }
        
        return convertView;
    }
 
    static class ViewHolder {
        public CheckBox cbk;
		public TextView label;
        ImageView flag;
    }
 
}