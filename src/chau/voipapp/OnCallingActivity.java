package chau.voipapp;

import java.io.File;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.sip.SipAudioCall;
import android.net.sip.SipProfile;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class OnCallingActivity extends Activity {

	ImageButton btnHold, btnSpeaker;
	Button btnEndCall;
	
	MediaPlayer mP;
	
    public AudioManager am;
	
	View.OnClickListener listenerHold, listenerSpeaker, listenerEnd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_on_calling);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.setFinishOnTouchOutside(false);
		initWiget();
		initEvent();

		mP = MediaPlayer.create(getApplicationContext(), R.raw.ringback);
		SipInit.speakerPhone = false;
		am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setSpeakerphoneOn(false);
		
		btnEndCall.setOnClickListener(listenerEnd);
		btnHold.setOnClickListener(listenerHold);
		btnSpeaker.setOnClickListener(listenerSpeaker);
		
//		SaveHisto("088", "SVEN", "20", "30", false, false);
//		SaveHisto("088", "TRAXEX", "20", "30", true, false);
//		SaveHisto("088", "SNIPER", "20", "30", false, true);
//		SaveHisto("088", "VIPER", "20", "30", true, false);
//		SaveHisto("088", "INVOKER", "20", "30", false, false);
		
		initCall();
	}
	
	@SuppressLint("SimpleDateFormat")
	public void initCall()
	{
		try
		{
			SipInit.listener = new SipAudioCall.Listener()
			{
				@Override
				public void onCallEstablished(SipAudioCall call) {
					// TODO Auto-generated method stub
					if(call != null && call.isInCall())
					{
						if(!SipInit.callHeld)
						{
							SipInit.callHeld = true;
							return;
						}
						else
						{
							SipInit.callHeld = false;
							//Nhan cuoc goi den
							return;
						}
					}
					mP.stop();
					SipInit.startTime = SystemClock.elapsedRealtime();
					SimpleDateFormat sdf = new SimpleDateFormat("H:mmaa   EEEE, dd MM, yyyy");
					SipInit.callDate = sdf.format(new Date());
					SipInit.outgoingCall = true;
					SipInit.callHeld = false;
					call.startAudio();
					call.setSpeakerMode(false);
					if(SipInit.speakerPhone)
					{
						am = (AudioManager)getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
						am.setSpeakerphoneOn(true);
					}
					am.setMode(AudioManager.MODE_IN_COMMUNICATION);
				}
				
				@Override
				public void onCallHeld(SipAudioCall call) {
					// TODO Auto-generated method stub
					SipInit.callHeld = true;
					super.onCallHeld(call);
				}
				
				@Override
				public void onCalling(SipAudioCall call) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onRinging(SipAudioCall call, SipProfile caller) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void onRingingBack(SipAudioCall call) {
					// TODO Auto-generated method stub
					
					mP.setAudioStreamType(AudioManager.STREAM_MUSIC);
					mP.setLooping(true);
					mP.start();
				}
				
				@Override
				public void onError(SipAudioCall call, int errorCode,
						String errorMessage) {
					// TODO Auto-generated method stub
					finish();
				}
				
				@Override
				public void onCallBusy(SipAudioCall call) {
					// TODO Auto-generated method stub
					finish();
				}
				
				@Override
				public void onCallEnded(SipAudioCall call) {
					// TODO Auto-generated method stub
					try
					{
						SipInit.speakerPhone = false;
						SipInit.stopTime  = SystemClock.elapsedRealtime();
						long miliSecs = SipInit.stopTime - SipInit.startTime;
						int secs = (int)(miliSecs / 1000) % 60;
						int mins = (int)((miliSecs) / (1000*60)) %60;
						int hours = (int)((miliSecs) / (1000*60*60));
						
						StringBuilder sb = new StringBuilder(64);
						if(hours > 0)
                        {
                            sb.append(hours);
                            if(hours > 1)
                            	sb.append(" hrs ");
                            else sb.append("hr");                              
                        }
						sb.append(mins);
                        if(mins > 1) sb.append(" mins ");
                        else sb.append(" min ");
                        sb.append(secs);
                        if(secs > 1) sb.append(" secs ");
                        else sb.append(" sec ");
                        SipInit.callDuration = sb.toString();
                        SipInit.startTime = 0;
                        
                        am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                        am.setMode(AudioManager.MODE_NORMAL);
                        am.setSpeakerphoneOn(false);
                        
                        SaveHisto(SipInit.sipTarget.getUserName(),
                        		SipInit.sipTarget.getDisplayName(), 
                        		SipInit.callDate, SipInit.callDuration, true, false);
                        
					}
					catch(Exception e)
					{}
					finish();
				}
			};
			SipInit.sipTarget = new SipProfile.Builder(Keyboard.numCall, 
					SipInit.profile.getSipDomain()).build();
			SipInit.call = SipInit.sipManager.makeAudioCall(
					SipInit.profile.getUriString(), 
					SipInit.sipTarget.getUriString(), 
					SipInit.listener, 30);
			SipInit.call.setListener(SipInit.listener, true);
		}
		catch(Exception e)
		{
			if(SipInit.profile != null)
			{
				try
				{
					SipInit.sipManager.close(SipInit.profile.getUriString());
					Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
//					finish();
				}
				catch(Exception ex)
				{}
				if(SipInit.call != null)
				{
					SipInit.call.close();
					Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_LONG).show();
				}
			}
			finish();
		}
	}
	
	public void initEvent()
	{
		listenerEnd = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				finish();
			}
		};
		
		listenerHold = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(SipInit.holdIsPressed)
				{
					btnHold.setImageResource(R.drawable.play);
					try
					{
						if(SipInit.call != null && SipInit.call.isInCall())
						{
							SipInit.call.holdCall(30);
						}
					}
					catch(Exception e)
					{}
				}
				else
				{
					btnHold.setImageResource(R.drawable.pause);
					try
					{
						SipInit.call.continueCall(30);
					}
					catch(Exception e)
					{}
				}
				SipInit.holdIsPressed = !SipInit.holdIsPressed;
			}
		};
		
		listenerSpeaker = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(SipInit.speakerIsPressed)
				{
					btnSpeaker.setImageResource(R.drawable.speaker);
					if(am == null)
					{
						am = (AudioManager)getApplicationContext()
								.getSystemService(Context.AUDIO_SERVICE);
					}
					am.setSpeakerphoneOn(true);
					SipInit.speakerPhone = true;
					Toast.makeText(getApplicationContext(), "Loudspeaker on", Toast.LENGTH_SHORT).show();
				}
				else
				{
					btnSpeaker.setImageResource(R.drawable.speakerx);
					if(am == null)
					{
						am = (AudioManager)getApplicationContext()
								.getSystemService(Context.AUDIO_SERVICE);
					}
					am.setSpeakerphoneOn(false);
					SipInit.speakerPhone = false;
					Toast.makeText(getApplicationContext(), "Loudspeaker off", Toast.LENGTH_SHORT).show();
				}
				SipInit.speakerIsPressed = !SipInit.speakerIsPressed;
			}
		};
	}
	
	public void SaveHisto(String sipAddr, String name, 
			String callDate, String callDuration,
			boolean outgoingCall, boolean missedCall)
	{
		HistoryInfo info = new HistoryInfo(sipAddr, name, 
				callDate, callDuration, outgoingCall, missedCall);
        HistoryActivity.results.add(0,info);
        HistoryActivity.adapter.notifyDataSetChanged();
        
        SipInit.file = new File(SipInit.PATH, SipInit.FILENAME);
        try
        {
	        if(!SipInit.file.exists())
	        {
	        	SipInit.fos = openFileOutput(SipInit.FILENAME, Context.MODE_APPEND);
	        	SipInit.oos = new ObjectOutputStream(SipInit.fos);
	        }
	        else
	        {
	        	SipInit.fos = openFileOutput(SipInit.FILENAME, Context.MODE_APPEND);
	        	SipInit.oos = new AppendableObjectOutputStream(SipInit.fos);
	        }
	        SipInit.oos.writeObject(info);
	        SipInit.oos.flush();
	        SipInit.fos.close();
	        SipInit.oos.close();
        }
        catch(Exception e)
        {}
	}
	
	@Override
	public void onBackPressed() {}
	
	public void initWiget()
	{
		btnEndCall = (Button)findViewById(R.id.btnEndCall);
		btnHold = (ImageButton)findViewById(R.id.btnHold);
		btnSpeaker = (ImageButton)findViewById(R.id.btnSpeaker);
	}
	
	
}
