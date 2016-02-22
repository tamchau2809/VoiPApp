package chau.voipapp;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ContactActivity extends Fragment
{
	View rootView;
	Keyboard keyboard;
	String name;
	String phoneNo;
	ListView lvContact;	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{	
		setHasOptionsMenu(true);
		rootView = inflater.inflate(R.layout.activity_contact, container, false);
		
		initWiget();
		keyboard = new Keyboard();
		
		ArrayList<ContactItem> list = getContact();
		
		lvContact = (ListView)rootView.findViewById(R.id.lvContact);
		lvContact.setAdapter(new ListviewContactAdapter(getActivity(), list));
		
		return rootView;
	}
	
	@Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        
		inflater.inflate(R.menu.contact, menu);
		
        super.onCreateOptionsMenu(menu,inflater);
        
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if(id == R.id.action_contact_search)
		{
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent);
		}
		if(id == R.id.action_contact_show_keyboard)
		{
            if(keyboard==null)
            {
            	item.setTitle("Bàn Phím");
            	keyboard = new Keyboard();
            	getActivity().getSupportFragmentManager().beginTransaction().add(R.id.Key_board_container, keyboard).commit();
            }
            else
            {
                if(keyboard.isVisible())
                {
                	item.setTitle("Bàn Phím");
                	getActivity().getSupportFragmentManager().beginTransaction().hide(keyboard).commit();
                }
                else
                {
                	item.setTitle("Bàn Phím");
                	keyboard = new Keyboard();
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.Key_board_container, keyboard).commit();
                }
            }
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) 
	{
		
		super.onPrepareOptionsMenu(menu);
		getActivity().invalidateOptionsMenu();
//		MenuItem filter = menu.findItem(R.id.bottomSearch).setVisible(false);
//		filter = menu.findItem(R.id.bottomStatus).setVisible(false);
//		filter.setVisible(false);
		
	}
	
	/**
	 * Khởi tạo các thành phần
	 */
	public void initWiget()
	{
		
	}
	
	private ArrayList<ContactItem> getContact()
	{
		ArrayList<ContactItem> list = new ArrayList<ContactItem>();
		ContentResolver cr = getActivity().getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
				null, null, null, null);
//		if(cur.getCount() > 0)
//		{
			while(cur.moveToNext())
			{
				String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
        		name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        		if (Integer.parseInt(cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                     Cursor pCur = cr.query(
                    		 ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
                    		 null, 
                    		 ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", 
                    		 new String[]{id}, null);
                     while (pCur.moveToNext()) {
                    	 phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    	 list.add(new ContactItem(name, phoneNo));
                     } 
      	        pCur.close();
        		}
			}
//		}
		return list;
	}
	
	public class ListviewContactAdapter extends BaseAdapter
	{
		private ArrayList<ContactItem> listContact;
		private LayoutInflater mInflater;
		
		public ListviewContactAdapter(Context contactFragment, ArrayList<ContactItem> list) 
		{
			// TODO Auto-generated constructor stub
			listContact = list;
			mInflater = LayoutInflater.from(contactFragment);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listContact.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return listContact.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int pos, View view, ViewGroup parent) 
		{
			// TODO Auto-generated method stub
			ViewHolder holder;
			if(view == null)
			{
				view = mInflater.inflate(R.layout.activity_contact_custom_view, null);
				holder = new ViewHolder();
				holder.contactName = (TextView)view.findViewById(R.id.contact_name);
				view.setTag(holder);
			}
			else
			{
				holder = (ViewHolder)view.getTag();
			}
			
			holder.contactName = (TextView)view.findViewById(R.id.contact_name);
			holder.contactName.setText(listContact.get(pos).getName());
			
			holder.contactNum = (TextView)view.findViewById(R.id.contact_number);
			holder.contactNum.setText(listContact.get(pos).getNum());
			
			return view;
		}
		class ViewHolder
		{
		    TextView contactName, contactNum;
		}
	}

//	@Override
//	public void numberIsPressed(String total) {
//		// TODO Auto-generated method stub
//		et.setText(total);
//	}
//
//	@Override
//	public void backLongPressed() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void backButtonPressed(String total) {
//		// TODO Auto-generated method stub
//		
//	}
}
