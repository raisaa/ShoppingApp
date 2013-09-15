package com.example.shoppingapp;

public class CurrencyConverter {
	private String label;
    private String shortcode;
 
    public CurrencyConverter(String label, String shortcode) {
        this.label = label;
        this.shortcode = shortcode;
    }
 
    public String getLabel() {
        return label;
    }
 
    public void setLabel(String label) {
        this.label = label;
    }
    
    public String getShortcode() {
        return shortcode;
    }
 
    public void setShortcode(String shortcode) {
        this.shortcode = shortcode;
    }
}