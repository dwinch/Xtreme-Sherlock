package org.xtremeradio.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Log;

public class SongDetails {
	
	JSONObject songDetails;
	
	public SongDetails() {
		try {
            Class strictModeClass=Class.forName("android.os.StrictMode");
            Class strictModeThreadPolicyClass=Class.forName("android.os.StrictMode$ThreadPolicy");
            Object laxPolicy = strictModeThreadPolicyClass.getField("LAX").get(null);
            Method method_setThreadPolicy = strictModeClass.getMethod(
                    "setThreadPolicy", strictModeThreadPolicyClass );
            method_setThreadPolicy.invoke(null,laxPolicy);
        } catch (Exception e) {

        }
	}
	
	public JSONObject getJSON() {
		String songString = getFeed();
        JSONArray jsonArray = null;
		try {
          jsonArray = new JSONArray(songString);
          Log.i(MainActivity.class.getName(),
              "Number of entries " + jsonArray.length());
          
  		songDetails = jsonArray.getJSONObject(0);

        } catch (Exception e) {
          e.printStackTrace();
        }
        return songDetails;
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