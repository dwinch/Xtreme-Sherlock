package org.xtremeradio.android;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.actionbarsherlock.app.SherlockFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactFragment extends SherlockFragment{
	
	EditText nameText, messageText;
	String captcha = "four";
	Button submitButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contact_fragment, container, false);
		
		nameText = (EditText) view.findViewById(R.id.editTextName);
		messageText = (EditText) view.findViewById(R.id.editTextMessage);
		
		submitButton = (Button) view.findViewById(R.id.button_submit);
		submitButton.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
		        submitClick();
		    }
		});
		
		try {
            Class strictModeClass=Class.forName("android.os.StrictMode");
            Class strictModeThreadPolicyClass=Class.forName("android.os.StrictMode$ThreadPolicy");
            Object laxPolicy = strictModeThreadPolicyClass.getField("LAX").get(null);
            Method method_setThreadPolicy = strictModeClass.getMethod(
                    "setThreadPolicy", strictModeThreadPolicyClass );
            method_setThreadPolicy.invoke(null,laxPolicy);
        } catch (Exception e) {

        }
		
		return view;
	}
	
	protected void submitClick() {
		if(nameText.getText().toString().equals("")
				|| messageText.getText().toString().equals("")) {
			Toast.makeText(getView().getContext(), "Please enter a name and a message", Toast.LENGTH_SHORT).show();
		} else {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://www.xtremeradio.org/betaplayer/mail.php?mode=1");
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			
			pairs.add(new BasicNameValuePair("name3", nameText.getText().toString()));
			pairs.add(new BasicNameValuePair("msg3", messageText.getText().toString()));
			pairs.add(new BasicNameValuePair("v4", captcha));
			
			try {
				post.setEntity(new UrlEncodedFormEntity(pairs));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				HttpResponse response = client.execute(post);
				System.out.println(response.getStatusLine().toString());
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}