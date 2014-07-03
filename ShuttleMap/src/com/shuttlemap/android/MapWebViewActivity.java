package com.shuttlemap.android;

import com.shuttlemap.android.fragment.common.TitleBar;
import com.shuttlemap.android.fragment.common.TitleBar.TitleBarListener;

import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MapWebViewActivity extends ShuttlemapBaseActivity implements TitleBarListener{
	private TitleBar titleBar;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_map_web);
		
		this.titleBar = (TitleBar)getFragmentManager().findFragmentById(R.id.titleBar);
		this.titleBar.setTitle("노선지도");
		
		String mapUrl = "https://mapsengine.google.com/map/viewer?mid=z0jGw3jBIwG0.kRDXixhrK5dI";
		
		WebView webView = (WebView)findViewById(R.id.webview);
		
		webView.setWebViewClient(new WebClient()); // 응룡프로그램에서 직접 url 처리
        WebSettings set = webView.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);
        webView.loadUrl(mapUrl);
	}

	@Override
	public void onBackButtonClicked(TitleBar titleBar) {
		finish();
	}

	 class WebClient extends WebViewClient {
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            view.loadUrl(url);
	            return true;
	        }
	    }
	
}
