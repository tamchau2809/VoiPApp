package chau.voipapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint({ "SimpleDateFormat", "InflateParams" })
public class HistoryActivity extends Fragment
{
	HistoryInfo contact;
	ArrayList<HistoryInfo> a;
	String name;
	int lvClickPos;
	
	View rootView;
	
	ListView lvHis;	
	AdapterView.OnItemClickListener listenerlvHis;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{	
		setHasOptionsMenu(true);
//		getActivity().supportInvalidateOptionsMenu();
		rootView = inflater.inflate(R.layout.activity_history, container, false);
		
		initWiget();
		initListener();
		
		registerForContextMenu(lvHis);
		
		ArrayList<HistoryInfo> a = getHistoryfromDevice();
		
		lvHis.setAdapter(new ListviewContactAdapter(getActivity(), a));
		lvHis.setOnItemClickListener(listenerlvHis);
		
		return rootView;
	}	

	private ArrayList<HistoryInfo> GetlistContact(){
	    ArrayList<HistoryInfo> contactlist = new ArrayList<HistoryInfo>();

	    contact = new HistoryInfo("aa", true);
	    contactlist.add(contact);

	    return contactlist; 	
	}
	
	@Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        
		inflater.inflate(R.menu.history_bottom_bar, menu);
		
        super.onCreateOptionsMenu(menu,inflater);
        
    }
	
	//-----Tạo Menu cho itemClick listview
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) 
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.history_click_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) 
	{
		switch(item.getItemId())
		{
		case R.id.itemHistorycall:
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	};
	
	//----------------------------------------
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) 
	{
		
		super.onPrepareOptionsMenu(menu);
		getActivity().invalidateOptionsMenu();
//		MenuItem filter = menu.findItem(R.id.bottomSearch).setVisible(false);
//		filter = menu.findItem(R.id.bottomStatus).setVisible(false);
//		filter.setVisible(false);
		
	}
	
	@SuppressWarnings("deprecation")
	private ArrayList<HistoryInfo> getHistoryfromDevice()
	{
		ArrayList<HistoryInfo> contactlist = new ArrayList<HistoryInfo>();
		Cursor mCursor = getActivity().managedQuery(CallLog.Calls.CONTENT_URI, 
				null, null, null, null);
		
		int num = mCursor.getColumnIndex(CallLog.Calls.NUMBER);
		int type = mCursor.getColumnIndex(CallLog.Calls.TYPE);
		int date = mCursor.getColumnIndex(CallLog.Calls.DATE);
		int duration = mCursor.getColumnIndex(CallLog.Calls.DURATION);
		boolean outgoingCall = false;
		boolean missedCall = false;
		
		mCursor.moveToLast();
		SimpleDateFormat sdf = new SimpleDateFormat("H:mmaa   EEEE, dd MM, yyyy");
		while(mCursor.moveToPrevious())
		{
			String phoneNum = mCursor.getString(num).replace(" ", "");
			String callType = mCursor.getString(type);
			String callDate = mCursor.getString(date);
			Date callDateTime = new Date(Long.valueOf(callDate));
			String dura = mCursor.getString(duration);
//			String dir = null;
			int dirCode = Integer.parseInt(callType);
			switch(dirCode)
			{
			case CallLog.Calls.OUTGOING_TYPE:
				outgoingCall = true;
				break;
			case CallLog.Calls.INCOMING_TYPE:
//				dir = "Incoming";
				break;
			case CallLog.Calls.MISSED_TYPE:
				missedCall = true;
				break;
			}
//			sb.append( "\nPhone Number:--- "+phoneNum +" \nCall Type:--- "+dir+" \nCall Date:--- "+callDateTime+" \nCall duration in sec :--- "+dura );
//			sb.append("\n----------------------------------");
			contactlist.add(new HistoryInfo(phoneNum, sdf.format(callDateTime), convertTime(dura), outgoingCall, missedCall));
			outgoingCall = false;
			missedCall = false;
		}
		return contactlist;
//		mCursor.close();
//		Toast.makeText(getBaseContext(), sb, Toast.LENGTH_LONG).show();
	}
	
	private String convertTime(String dura)
	{
		String time = null;
		int secs = Integer.valueOf(dura)%60;
		int mins = (Integer.valueOf(dura) / (60))%60;
		int hrs = (Integer.valueOf(dura) / (60*60));
		StringBuilder builder = new StringBuilder(64);
		if(hrs > 0)
		{
		    builder.append(hrs);
		    if(hrs > 1)
		        builder.append(" hrs ");
		    else builder.append("hr");                              
		}
		builder.append(mins);
		if(mins > 1) builder.append(" mins ");
		else builder.append(" min ");
		builder.append(secs);
		if(secs > 1) builder.append(" secs ");
		else builder.append(" sec ");
		time = builder.toString();
		return time;
	}
	
	public class ListviewContactAdapter extends BaseAdapter
	{
		private ArrayList<HistoryInfo> listContact;

		private LayoutInflater mInflater;

		public ListviewContactAdapter(Context photosFragment, ArrayList<HistoryInfo> results)
		{
		    listContact = results;
		    mInflater = LayoutInflater.from(photosFragment);
		}

		@Override
		public int getCount() 
		{
		    // TODO Auto-generated method stub
		    return listContact.size();
		}

		@Override
		public Object getItem(int arg0)
		{
		    // TODO Auto-generated method stub
		    return listContact.get(arg0);
		}

		@Override
		public long getItemId(int arg0) 
		{
		    // TODO Auto-generated method stub
		    return arg0;
		}

		public View getView(int position, View convertView, ViewGroup parent) 
		{
		    // TODO Auto-generated method stub
		    ViewHolder holder;
		    if(convertView == null){
		        convertView = mInflater.inflate(R.layout.activity_history_customview, null);
		        holder = new ViewHolder();
		        holder.txtname = (TextView) convertView.findViewById(R.id.sipaddr);   
		        convertView.setTag(holder);
		    } else {
		        holder = (ViewHolder) convertView.getTag();
		    }

		    holder.txtname.setText(listContact.get(position).getSipAddr());       
		    
		    holder.calldate = (TextView)convertView.findViewById(R.id.calldate);
		    holder.calldate.setText(listContact.get(position).getCallDate());
		    
		    holder.callduration = (TextView)convertView.findViewById(R.id.callduration);
		    holder.callduration.setText(listContact.get(position).getCallDuration());
		    
	        holder.imgCallStatus = (ImageView)convertView.findViewById(R.id.icon);
	        if(listContact.get(position).isOutgoingCall())
			{
				holder.imgCallStatus.setImageResource(R.drawable.out_call);
			}
			else if(listContact.get(position).isMissedCall())
				{
					holder.imgCallStatus.setImageResource(R.drawable.miss_call);
				}
			else holder.imgCallStatus.setImageResource(R.drawable.in_call);
		    
//	        holder.sipname = (TextView) convertView.findViewById(R.id.sipName);
//	        holder.sipname.setText(name);
	        
		    return convertView;
		}

		class ViewHolder
		{
		    TextView txtname, txtphone, calldate, callduration, sipname;
		    ImageView imgCallStatus;
		}
	}	
	
	/**
	 * Khởi tạo các thành phần
	 */
	public void initWiget()
	{
		lvHis = (ListView)	rootView.findViewById(R.id.lvHistory);
	}
	
	/**
	 * Tạo sự kiện click
	 */
	public void initListener()
	{
		listenerlvHis = new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, 
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				lvClickPos = arg2;
			}
		};
	}
}
