package chau.voipapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class MainActivity extends FragmentActivity 
	implements ActionBar.TabListener{

	ViewPager viewPager;
	SectionPagerAdapter mSect;
	ActionBar actionBar;
	
	static int numberSection;
	
	public static SipManager sipManager;
	public static SipProfile profile;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_main);
		
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		initWiget();
		check();
//		initManager();
		mSect = new SectionPagerAdapter(getSupportFragmentManager());
		
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(false);
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(mSect);
		
		viewPager
			.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				actionBar.setSelectedNavigationItem(arg0);
				
			}
		});
		
		for(int i = 0; i < mSect.getCount(); i++)
		{
			actionBar.addTab(actionBar.newTab()
					.setText(mSect.getPageTitle(i))
					.setTabListener(this));
		}
		
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());
		invalidateOptionsMenu();
	}	

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}
	
	public class SectionPagerAdapter extends FragmentPagerAdapter
	{

		public SectionPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int pos) {
			// TODO Auto-generated method stub
			switch (pos) 
			{
            case 0:
                // The first section of the app is the most interesting -- it offers
                // a launchpad into the other demonstrations in this example application.
                numberSection = 0;
            	return new LaunchpadSectionFragment();
            case 1:
            	numberSection = 1;
            	return new ContactActivity();
            case 2:
            	numberSection = 3;
            	return new HistoryActivity();

            default:
                // The other sections of the app are dummy placeholders.
                Fragment fragment = new DummySectionFragment();
                Bundle args = new Bundle();
                args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, pos + 1);
                fragment.setArguments(args);
                return fragment;
			}
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 3;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) 
			{
			case 0:
				return getString(R.string.title_section1);
			case 1:
				return getString(R.string.title_section2);
			case 2:
				return getString(R.string.title_section3);
				default:
					return null;
			}
		}
	}
	//---------------------------------------------------------
	/**
	 * 
	 * @author com08
	 *
	 */
	
	
	
	//---------------------------------------------------------
	
	public static class LaunchpadSectionFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);

            // Demonstration of a collection-browsing activity.
            rootView.findViewById(R.id.demo_collection_button)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), CollectionDemoActivity.class);
                            startActivity(intent);
                        }
                    });

            // Demonstration of navigating to external activities.
            rootView.findViewById(R.id.demo_external_activity)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Create an intent that asks the user to pick a photo, but using
                            // FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET, ensures that relaunching
                            // the application from the device home screen does not return
                            // to the external activity.
                            Intent externalActivityIntent = new Intent(Intent.ACTION_PICK);
                            externalActivityIntent.setType("image/*");
                            externalActivityIntent.addFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                            startActivity(externalActivityIntent);
                        }
                    });

            return rootView;
        }
    }
	
	public static class DummySectionFragment extends Fragment
	{
		private static final String ARG_SECTION_NUMBER = "section_number";
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState)
		{
			View rootview = inflater.inflate(R.layout.fragment_section_dummy, container, false);
			Bundle args = getArguments();
			((TextView) rootview.findViewById(android.R.id.text1)).setText(
                    getString(R.string.dummy_section_text, args.getInt(ARG_SECTION_NUMBER)));	
			return rootview;
		}
	}
	
	//------------------------------------------------------------
	/**
	 * Tạo menu Login/Edit, Sign Out, About
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.login_bottom_bar, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if(id == R.id.action_Login)
		{
			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
			startActivity(intent);
		}
		if(id == R.id.action_LogOut)
		{
			
		}
		if(id == R.id.action_About)
		{
			Intent intent = new Intent(MainActivity.this, AboutActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
	//------------------------------------------------------------
	/**
	 * kiểm tra điện thoại có hỗ
	 * trợ SIP
	 */
	public void check()
	{
		if(SipManager.isVoipSupported(getApplicationContext()))
		{
			final SharedPreferences pref = getBaseContext().getSharedPreferences("CHECK_FIRST", MODE_PRIVATE);
			if(!pref.contains("FirstTime"))
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Welcome...");
				builder.setCancelable(false);
				builder.setMessage("Hope you like it. :)");
				
				{
					builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Editor editor = pref.edit();
							editor.putBoolean("FirstTime", true);
							editor.commit();
						}
					});			
					builder.show();
				}
			}
		}
		else
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Oops..");
			builder.setMessage("Your Device does NOT SUPPORT SIP-based VOIP API");
			builder.setPositiveButton("I Agree", null);
			builder.setCancelable(false);
			builder.show();
		}
	}
	//--------- Kết nối tài khoản đã đăng nhập---------------------------------------------------
	/**
	 * Kết nối Acc
	 */
//	public void initManager()
//	{
//		if(sipManager == null)
//		{
//			sipManager = SipManager.newInstance(getApplicationContext());
//		}
//		initProfile();
//	}
//	
//	public void initProfile()
//	{
//		if(sipManager == null)
//		{
//			return;
//		}
//		if(profile != null)
//		{
//			closeLocalProfile();
//		}
//		
//		SharedPreferences prefs = getApplicationContext().getSharedPreferences("LOGIN", MODE_PRIVATE);
//		final String username = prefs.getString("username", "");
//		String password = prefs.getString("password", "");
//		String domain = prefs.getString("domain", "");
//		
//		if(username.length() == 0 || password.length() == 0
//				|| domain.length() == 0)
//			return;
//		
//		try
//		{
//			SipProfile.Builder builder = new SipProfile.Builder(username, domain);
//			builder.setPassword(password);
//			profile = builder.build();
//			
//			sipManager.setRegistrationListener(profile.getUriString(), new SipRegistrationListener() {
//				
//				@Override
//				public void onRegistrationFailed(String localProfileUri, int errorCode,
//						String errorMessage) {
//					// TODO Auto-generated method stub
//					updateStatus("Thất Bại!", 2);
//				}
//				
//				@Override
//				public void onRegistrationDone(String localProfileUri, long expiryTime) {
//					// TODO Auto-generated method stub
//					tvAcc.setText(username.substring(5));
//					updateStatus("Kết Nối", 1);
//				}
//				
//				@Override
//				public void onRegistering(String localProfileUri) {
//					// TODO Auto-generated method stub
//					updateStatus("Xin chờ..", 3);
//				}
//			});
//		}
//		catch (ParseException pe) {
//            updateStatus("Error!!", 3);
//            tvStatus.setTextColor(Color.WHITE);
//        } 
//		catch (SipException se) {
//            updateStatus("Error!!", 3);
//            tvStatus.setTextColor(Color.WHITE);
//        }
//	}
//	
//	public void updateStatus(final String status, final int num)
//	{
//		this.runOnUiThread(new Runnable() {
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				tvStatus.setText(status);
//				switch(num)
//				{
//				case 1:
//					tvStatus.setTextColor(Color.GREEN);
//				case 2:
//					tvStatus.setTextColor(Color.RED);
//				default:
//					tvStatus.setTextColor(Color.WHITE);
//				}
//			}
//		});
//	}
//	
//	public void closeLocalProfile() {
//        if (sipManager == null) {
//            return;
//        }
//        try {
//            if (profile != null) {
//                sipManager.close(profile.getUriString());
//                Log.d("CLOSE", "CLOSE_PROFILE");
//            }
//        } catch (Exception ee) {
//            Log.d("/onDestroy", "Oops.. I did it again.", ee);
//        }
//    }
	//------------------------------------------------------------
	public void initWiget()
	{
//		tvAcc = (TextView) viewPager.findViewById(R.id.tvAcc);
//		tvStatus = (TextView)findViewById(R.id.tvStatus);
	}
	//------------------------------------------------------------
}
