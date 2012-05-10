package com.ihunda.android.binauralbeat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;


public class Comments extends Activity {

	private static final String TAG = "About";

	@Override
	protected void onCreate(Bundle icicle) {
		
		super.onCreate(icicle);

		setTheme(android.R.style.Theme_Dialog);
		setContentView(R.layout.comments);
		
	
		setTitle(R.string.comments_title);
	
		WebView w = (WebView) findViewById(R.id.commentView);
		w.setWebViewClient(new WebViewClient() {
		});
		WebSettings ws = w.getSettings();

		ws.setJavaScriptEnabled(true);
		ws.setBlockNetworkLoads(false);
		ws.setLoadsImagesAutomatically(true);
		
		String page = readRawTextFile(R.raw.comment);
		w.loadData(page, "application/xhtml+xml", null);
		
		
		// Register the onClick listener with the implementation above
	    Button button = (Button) findViewById(R.id.CommentBack);
		button.setOnClickListener( new OnClickListener() {
			public void onClick(View v) {
				// Close window
				Intent intent = getIntent();
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
	
	public  String readRawTextFile(int resId)
    {
         InputStream inputStream = this.getResources().openRawResource(resId);

            InputStreamReader inputreader = new InputStreamReader(inputStream);
            BufferedReader buffreader = new BufferedReader(inputreader);
             String line;
             StringBuilder text = new StringBuilder();

             try {
               while (( line = buffreader.readLine()) != null) {
                   text.append(line);
                   text.append('\n');
                 }
           } catch (IOException e) {
               return null;
           }
             return text.toString();
    }
	
}
