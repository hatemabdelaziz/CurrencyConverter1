package com.alemadi.currencyconverter1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;



import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainActivity extends Activity {

	public int to;
	public int from;
	public String [] val;
	public String [] currencys;
	public String s;
	public String _amount;
	public Handler handler;
	public String exResult;
	public String[] CountryNames;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_main);
        Spinner s1 = (Spinner) findViewById(R.id.spinner1);
        Spinner s2 = (Spinner) findViewById(R.id.spinner2);
        currencys = loadArray("currencys", this);
        CountryNames = loadArray("CountryNames", this);
        //if(currencys == null || currencys.length == 0) {
	        Locale[] locs = Locale.getAvailableLocales();
	        currencys = new String[locs.length];
	        CountryNames = new String[locs.length];
	        
	        
	        
	        int i = 0;
	        for(Locale loc : locs) {
	            try {
	                String val=Currency.getInstance(loc).getCurrencyCode();
	                String CountryName = getCurrencyNames(val)
	                +" - "+Currency.getInstance(loc).getCurrencyCode();
	                currencys[i] = val;
	                CountryNames[i] = CountryName;
	                i++;
	            } catch(Exception exc)
	            {
	                // Locale not found
	            }
	        }
	        
	        saveArray(currencys, "currencys", this);
	        saveArray(CountryNames, "CountryNames", this);
        //}
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, CountryNames);
        
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
        //        this, R.array.name, android.R.layout.simple_spinner_item);
        
        
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        val  = getResources().getStringArray(R.array.value);
        s1.setAdapter(adapter);
        s2.setAdapter(adapter);
        s1.setOnItemSelectedListener(new spinOne(1));
        s2.setOnItemSelectedListener(new spinOne(2));
        Button b = (Button) findViewById(R.id.button1);
        b.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				
				if(from == to)
				{
					Toast.makeText(getApplicationContext(), "Invalid", 4000).show();
				}
				else
				{						
					final TextView t = (TextView) findViewById(R.id.textView4);	
					final EditText theAmount = (EditText) findViewById(R.id.amount);
					
					
						 new Thread()
						 {
						     @Override
						     public void run()
						     {
						    	 try {
						    		 _amount = theAmount.getText().toString();
									s = getJson("http://rate-exchange.appspot.com/currency?from="+currencys[from]+"&to="+currencys[to]+"&q="+_amount);
										JSONObject jObj;
									jObj = new JSONObject(s);
									exResult = jObj.getString("v");//.getJSONObject("results").getJSONObject("rate").getString("Rate");
									//String exResult1 = jObj.getString("rhs");
									runOnUiThread(new Runnable() {
									     public void run() {
									    	 
									    	 t.setText(String.format("%.3f", Float.parseFloat(exResult)));

									    }
									});
									
								} catch (ClientProtocolException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						     }
						 }.start();											 
					}											
				}							 	
        });
        
        
        Button sButton = (Button) findViewById(R.id.settingButton);
        sButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.
//		        startActivity(intent);									
			}							 	
        });
    }
    
    public boolean saveArray(String[] array, String arrayName, Context mContext) {   
        SharedPreferences prefs = mContext.getSharedPreferences("currencyconverter", 0);  
        SharedPreferences.Editor editor = prefs.edit();  
        editor.putInt(arrayName +"_size", array.length);  
        for(int i=0;i<array.length;i++)  
          editor.putString(arrayName + "_" + i, array[i]);  
        return editor.commit();  
      }  
    
    public String[] loadArray(String arrayName, Context mContext) {  
        SharedPreferences prefs = mContext.getSharedPreferences("currencyconverter", 0);  
        int size = prefs.getInt(arrayName + "_size", 0);  
        String array[] = new String[size];  
        for(int i=0;i<size;i++)  
          array[i] = prefs.getString(arrayName + "_" + i, null);  
        return array;  
      }  
    public String getJson(String url)throws ClientProtocolException, IOException {
    	
		StringBuilder build = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
	    HttpResponse response = client.execute(httpGet);
		HttpEntity entity = response.getEntity();
		InputStream content = entity.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(content));
		String con;
		while ((con = reader.readLine()) != null) {
					build.append(con);
				}
		return build.toString();
	}
    
    
    public String getCurrencyNames(String code) throws IOException, JSONException {
    	

        InputStream is = getResources().openRawResource(R.string.CurrencyNames);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }

        String jsonString = writer.toString();
        JSONObject jObj = new JSONObject(jsonString);
		String exResult = jObj.getString(code);
		
		return exResult;
		

	}
    
    private class spinOne implements OnItemSelectedListener
    {
    	int ide;
    	spinOne(int i)
    	{
    		ide =i;
    	}
    	public void onItemSelected(AdapterView<?> parent, View view,
    			int index, long id) {
    		if(ide == 1)
    			from = index;
    		else if(ide == 2)
    			to = index;
    			
    	}

    	public void onNothingSelected(AdapterView<?> arg0) {
    		// TODO Auto-generated method stub	
    	}
    	
    }

}
