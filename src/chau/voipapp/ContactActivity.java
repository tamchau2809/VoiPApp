package chau.voipapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ContactActivity extends Fragment implements Keyboard.onKeyBoardEvent
{
	View rootView;
	Keyboard keyboard;
	
	private EditText et;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{	
		setHasOptionsMenu(true);
		rootView = inflater.inflate(R.layout.activity_contact, container, false);
		
		initWiget();

//		et=(EditText)rootView.findViewById(R.id.editText1);
//	    et.setOnClickListener(new View.OnClickListener() {
//
//	        @Override
//	        public void onClick(View v) {
//	            // TODO Auto-generated method stub
//	            if(keyboard==null)
//	            {
//	            	keyboard=Keyboard.newInstance(et.getText().toString());
//	            	getActivity().getSupportFragmentManager().beginTransaction().add(R.id.Key_board_container, keyboard).commit();
//	            }
//	            else
//	            {
//	                if(keyboard.isVisible())
//	                	 getActivity().getSupportFragmentManager().beginTransaction().hide(keyboard).commit();
//	                else
//	                {
//	                    keyboard=Keyboard.newInstance(et.getText().toString());
//	                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.Key_board_container, keyboard).commit();
//	                }
//	            }
//	        }
//	    });
		
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
			abc();
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void abc()
	{
		getActivity().getSupportFragmentManager().beginTransaction().show(keyboard).commit();
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

	@Override
	public void numberIsPressed(String total) {
		// TODO Auto-generated method stub
		et.setText(total);
	}

	@Override
	public void backLongPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void backButtonPressed(String total) {
		// TODO Auto-generated method stub
		
	}
}
