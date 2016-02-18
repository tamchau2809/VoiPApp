package chau.voipapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContactActivity extends Fragment 
{
	View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{	
		setHasOptionsMenu(true);
		rootView = inflater.inflate(R.layout.activity_history, container, false);
		
		initWiget();

		
		return rootView;
	}
	
	@Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        
		inflater.inflate(R.menu.contact, menu);
		
        super.onCreateOptionsMenu(menu,inflater);
        
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
}
