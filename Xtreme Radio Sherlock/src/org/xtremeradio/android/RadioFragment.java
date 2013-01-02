package org.xtremeradio.android;

import java.io.IOException;
import com.actionbarsherlock.app.SherlockFragment;
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
import android.widget.Toast;

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
			Toast.makeText(getSherlockActivity(), "Starting stream, please wait", Toast.LENGTH_LONG).show();
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
		new SongDetails(getView());
	}
	
	@Override
   	public void onDestroy(){
    	super.onDestroy();
       	//mNotificationManager.cancelAll();
       	mp.release();
    }
}