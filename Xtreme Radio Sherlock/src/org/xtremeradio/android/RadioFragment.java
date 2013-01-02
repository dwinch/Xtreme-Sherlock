package org.xtremeradio.android;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import com.actionbarsherlock.app.SherlockFragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class RadioFragment extends SherlockFragment{
	
	MediaPlayer mp;
	ImageButton playButton;
	ImageView albumArt;
	TextView songText;
	Boolean playing = false;
	String url = "http://streams.xtremeradio.org/high.mp3";
	
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
		View view = inflater.inflate(R.layout.radio_fragment, container, false);
		
		mp = new MediaPlayer();
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
		    @Override
		    public void onPrepared(MediaPlayer mp) {
		    mp.start();
		    }
		});
		
		songText = (TextView) view.findViewById(R.id.textViewSongDetails);
		albumArt = (ImageView) view.findViewById(R.id.imageViewAlbumArt);
		
		playButton = (ImageButton) view.findViewById(R.id.imageButtonPlay);
		playButton.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
		        playClick();
		    }
		});
		
		return view;
	}
	
	public void playClick () {
		if(!playing){
			playButton.setImageResource(R.drawable.icon_pause);
			setRadioDataSource();
			mp.prepareAsync();
			displaySongDetails();
			playing = true;
		} else {
			playButton.setImageResource(R.drawable.icon_play);
			mp.reset();
			playing = false;
		}
	}
	
	private void setRadioDataSource() {
		try {
			mp.setDataSource(url);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void displaySongDetails() {
		SongDetails songDetails = new SongDetails();
		JSONObject songJSON = songDetails.getJSON();
		//Set the artist and song name
		try {
			songText.setText(songJSON.getString("XML_OCP_NOW_CARTLINE2") + "\n" +
					songJSON.getString("XML_OCP_NOW_CARTLINE1"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Set the album artwork
		try {
			Bitmap bitmap = BitmapFactory.decodeStream((InputStream)
					  new URL(songJSON.getString("picture")).getContent());
			albumArt.setImageBitmap(bitmap);
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
	
	@Override
   	public void onDestroy(){
    	super.onDestroy();
       	//mNotificationManager.cancelAll();
       	mp.release();
    }
}