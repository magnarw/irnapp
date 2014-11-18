package no.irn.app.hijri;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import no.irn.app.BaseActivity;
import no.irn.app.domain.NewsItem;
import no.irn.app.images.ImageCacheManager;
import no.irn.app.news.NewsDetailsFragment;
import no.irn.app.news.NewsManager;
import no.irn.app.news.NewsListFragment.NewsListAdapter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

import no.snms.app.R;
import no.snms.app.R.id;
import no.snms.app.R.layout;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ImageView.ScaleType;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TextView;
import android.widget.Toast;

public class HijriList extends Fragment  {


	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		   super.onCreateView(inflater, container,
			        savedInstanceState);

		  View root = inflater.inflate(R.layout.listhijiri, container,false);
		  WebView webview =(WebView) root.findViewById(R.id.webview);
		  
		  webview.getSettings().setAppCacheEnabled(false);
		    webview.getSettings().setJavaScriptEnabled(true);
		    webview.setInitialScale(1);
		    webview.getSettings().setPluginState(PluginState.ON);

		    webview.setWebViewClient(new WebViewClient() {
		        @Override
		        public boolean shouldOverrideUrlLoading(WebView view, String url) {
		            view.loadUrl(url);
		            return true;
		        }
		    });

		    WebSettings webSettings = webview.getSettings();
		    webSettings.setJavaScriptEnabled(true);
		    webSettings.setBuiltInZoomControls(true);
		    webview.getSettings().setAppCacheMaxSize( 5 * 1024 * 1024 ); // 5MB
		    webview.getSettings().setAppCachePath( this.getActivity().getApplicationContext().getCacheDir().getAbsolutePath() );
		  
		    webSettings.setLoadsImagesAutomatically(true);
		    webSettings.setLoadWithOverviewMode(true);
		    webSettings.setSupportZoom(true);
		    webSettings.setUseWideViewPort(true);
			webview.loadUrl("http://www.irn.no/islamsk-kalender");
		   // setContentView(webview);
		  
		  

		  return root; 

	}
	
	
	
	
	
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	

			

	

	
	
	
	
	
		

}
