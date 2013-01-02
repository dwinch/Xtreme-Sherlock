package org.xtremeradio.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SongDetails extends SherlockFragmentActivity {
	
	JSONObject songJSON;
	TextView songText;
	ImageView albumArt;
	HttpResponse response;
	Bitmap bitmap;
	
	public SongDetails(View view) {
		songText = (TextView) view.findViewById(R.id.textViewSongDetails);
		albumArt = (ImageView) view.findViewById(R.id.imageViewAlbumArt);
		startMessageThread();
	}
	
	// Need handler for callbacks to the UI thread
    final Handler mHandler = new Handler();

    // Create runnable for posting
    final Runnable mUpdateResults = new Runnable() {
        public void run() {
            updateResultsInUi();
        }
    };
    
    protected void startMessageThread() {
        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
    	ScheduledExecutorService executor =
    		    Executors.newSingleThreadScheduledExecutor();
    	
    	Runnable periodicTask = new Runnable() {
    		public void run() {
		        // Invoke method(s) to do the work
		        getDetails();
                mHandler.post(mUpdateResults);
    		}
    	};
		// Set the thread to run every 10 seconds
		executor.scheduleAtFixedRate(periodicTask, 0, 10, TimeUnit.SECONDS);
    }

    private void updateResultsInUi() {
        // Back in the UI thread -- update our UI elements based on the data in mResults
    	//Update song details
    	try {
			songText.setText(songJSON.getString("XML_OCP_NOW_CARTLINE2") + "\n" +
					songJSON.getString("XML_OCP_NOW_CARTLINE1"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		//Set the album artwork
		albumArt.setImageBitmap(bitmap);
    }
	
	public void getDetails() {
		String songString = getFeed();
        JSONArray jsonArray = null;
		try {
          jsonArray = new JSONArray(songString);
          Log.i(MainActivity.class.getName(),
              "Number of entries " + jsonArray.length());
          
  		songJSON = jsonArray.getJSONObject(0);

        } catch (Exception e) {
          e.printStackTrace();
        }
		
		try {
			bitmap = BitmapFactory.decodeStream((InputStream)
					  new URL(songJSON.getString("picture")).getContent());
		} catch (MalformedURLException e) {
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
	
	
	
	public String getFeed() {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://live.xtremeradio.org/get");
        
        try {
          HttpResponse response = client.execute(httpGet);
          StatusLine statusLine = response.getStatusLine();
          int statusCode = statusLine.getStatusCode();
          if (statusCode == 200) {
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = reader.readLine()) != null) {
              builder.append(line);
            }
          } else {
            Log.e(MainActivity.class.toString(), "Failed to download file");
          }
        } catch (ClientProtocolException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
        return builder.toString();
	}
	
}