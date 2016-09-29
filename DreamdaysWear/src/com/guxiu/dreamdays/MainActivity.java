package com.guxiu.dreamdays;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnApplyWindowInsetsListener;
import android.view.WindowInsets;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener, DataApi.DataListener, MessageApi.MessageListener, NodeApi.NodeListener{

	
	private GoogleApiClient mGoogleApiClient;
	private static final String TAG ="Dreamdays Wear";
	private ArrayList<Matter> matters = new ArrayList<Matter>();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Resources res = getResources();
        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        pager.setOnApplyWindowInsetsListener(new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                final boolean round = insets.isRound();
                int rowMargin = res.getDimensionPixelOffset(R.dimen.page_row_margin);
                int colMargin = res.getDimensionPixelOffset(round ?  R.dimen.page_column_margin_round : R.dimen.page_column_margin);
                pager.setPageMargins(rowMargin, colMargin);
                pager.onApplyWindowInsets(insets);
                return insets;
            }
        });
        
        addData();
        
        if(matters.size()>0){
        	pager.setAdapter(new SampleGridPagerAdapter(this, getFragmentManager() , matters));
            DotsPageIndicator dotsPageIndicator = (DotsPageIndicator) findViewById(R.id.page_indicator);
            dotsPageIndicator.setPager(pager);
        }
        
        mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addApi(Wearable.API)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .build();
    }

    /**
     * 
     */
    public void addData(){
    	matters.clear();
    	String data = getSharedPreferences("DreamdaysWear", Context.MODE_PRIVATE).getString("data", "");
    	data = data.replace("&quot;", "\"");
    	System.err.println(data);
        try {
        	JSONObject root = new JSONObject(data);
        	JSONArray matterArr = root.getJSONArray("matters");
        	for(int i= 0 ; i < matterArr.length() ; i++){
        		JSONObject matterJson = matterArr.getJSONObject(i);
        		Matter matter = new Matter();
        		matter.set_id(i);
        		matter.setMatterName(matterJson.getString("matter_name"));
        		matter.setMatterTime(matterJson.getString("matter_time"));
        		matter.setPicName(matterJson.getString("pic_name"));
        		matter.setClassifyType(matterJson.getInt("classify_type"));
        		matters.add(matter);
        	}
    	} catch (Exception e) {
    	}
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        Wearable.MessageApi.removeListener(mGoogleApiClient, this);
        Wearable.NodeApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }
    
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.d(TAG, "onConnected(): Successfully connected to Google API client");
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
        Wearable.NodeApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int cause) {
    	Log.d(TAG, "onConnectionSuspended(): Connection to Google API client was suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.e(TAG, "onConnectionFailed(): Failed to connect, with result: " + result);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
    	Log.d(TAG, "onDataChanged(): " + dataEvents);

    }

	@Override
	public void onPeerConnected(Node arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPeerDisconnected(Node arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessageReceived(MessageEvent event) {
		Log.d(TAG, "onMessageReceived: " + event);
	}
	
	
	
}
