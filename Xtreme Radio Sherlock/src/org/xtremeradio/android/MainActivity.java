package org.xtremeradio.android;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends SherlockFragmentActivity {
	ViewPager mViewPager;
	TabsAdapter mTabsAdapter;
	TextView tabCenter, tabText;
	RadioFragment radioFragment;
	ContactFragment contactFragment;
	WebcamFragment webcamFragment;
	
	View radioView, contactView, webcamView;
	
	TextView song;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        radioFragment = new RadioFragment();
        contactFragment = new ContactFragment();
        webcamFragment = new WebcamFragment();
        
        radioView = radioFragment.getView();
        contactView = contactFragment.getView();
        webcamView = webcamFragment.getView();
        
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(2);
		mViewPager.setId(1);

		ActionBar bar = getSupportActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mTabsAdapter = new TabsAdapter(this, mViewPager);

		mTabsAdapter.addTab(
				bar.newTab().setText("Radio"),
				RadioFragment.class, null);
		
		mTabsAdapter.addTab(
				bar.newTab().setText("Contact"),
				ContactFragment.class, null);
		
		mTabsAdapter.addTab(
				bar.newTab().setText("Webcam"),
				WebcamFragment.class, null);
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
	public void onBackPressed(){
    	alertDiag();
    }
    
    void alertDiag(){
    	AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
    	 myAlertDialog.setTitle("Are you sure you want to exit?");
    	 myAlertDialog.setMessage("To hide the app and continue playback, use the home button.");
    	 
    	 myAlertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    	  public void onClick(DialogInterface dialog, int id) {
    		  //mNotificationManager.cancelAll();
    		  //RadioFragment.mp.release();
    		  finish();
    	  }});
    	 
    	 myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
    	  public void onClick(DialogInterface dialog, int id) {
    		  dialog.cancel();
    	  }});
    	 
    	 myAlertDialog.show();
    }
    
    

	public static class TabsAdapter extends FragmentPagerAdapter implements
			ActionBar.TabListener, ViewPager.OnPageChangeListener
	{
		private final Context mContext;
		private final ActionBar mActionBar;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		static final class TabInfo
		{
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(Class<?> _class, Bundle _args)
			{
				clss = _class;
				args = _args;
			}
		}

		public TabsAdapter(SherlockFragmentActivity activity, ViewPager pager)
		{
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mActionBar = activity.getSupportActionBar();
			mViewPager = pager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args)
		{
			TabInfo info = new TabInfo(clss, args);
			tab.setTag(info);
			tab.setTabListener(this);
			mTabs.add(info);
			mActionBar.addTab(tab);
			notifyDataSetChanged();
		}

		@Override
		public int getCount()
		{
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position)
		{
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(),
					info.args);
		}

		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels)
		{
		}

		public void onPageSelected(int position)
		{
			mActionBar.setSelectedNavigationItem(position);
		}

		public void onPageScrollStateChanged(int state)
		{
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft)
		{
			Object tag = tab.getTag();
			for (int i = 0; i < mTabs.size(); i++)
			{
				if (mTabs.get(i) == tag)
				{
					mViewPager.setCurrentItem(i);
				}
			}
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft)
		{
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft)
		{
		}
	}
}