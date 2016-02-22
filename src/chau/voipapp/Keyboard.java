package chau.voipapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class Keyboard extends Fragment  implements OnClickListener
{
	private Button btnOne;
	private Button btnTwo;
	private Button btnThree;
	private Button btnFour;
	private Button btnFive;
	private Button btnSix;
	private Button btnSeven;
	private Button btnEIGHT;
	private Button btnNine;
	private Button btnZero;
	private Button btnStar;
	private Button btnHash;
	
	private StringBuilder sb;
	
//	private onKeyBoardEvent keyboardEventListener;
	
	private EditText edNumInput;
	
	int maxLength = 15;
	int currentLength;
	
//	public static Keyboard newInstance(String value)
//	{
//		Keyboard kb = new Keyboard();
//		Bundle bundle = new Bundle();
//		bundle.putString("et_value", value);
//		kb.setArguments(bundle);
//		return kb;
//	}
	
//	@SuppressWarnings("deprecation")
//	@Override
//	public void onAttach(Activity activity) 
//	{
//		try
//		{
//			keyboardEventListener = (onKeyBoardEvent)activity;
//		}
//		catch(ClassCastException e)
//		{}
//		super.onAttach(activity);
//	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container, Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.keyboard, container, false);
//		sb=new StringBuilder(getArguments().getString("et_value"));
//        currentLength=sb.length();
        edNumInput = (EditText)rootView.findViewById(R.id.edNumInput);
		
        btnOne = (Button)rootView.findViewById(R.id.btnOne);
        btnOne.setOnClickListener(this);
//        
        btnTwo = (Button)rootView.findViewById(R.id.btnTwo);
        btnTwo.setOnClickListener(this);
//        btnTwo.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				add("2");
//			}
//		});
//        
        btnThree = (Button)rootView.findViewById(R.id.btnThree);
//        btnThree.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				add("3");
//			}
//		});
//        
        btnFour = (Button)rootView.findViewById(R.id.btnFour);
//        btnFour.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				add("3");
//			}
//		});
//        
//        btnFive = (Button)rootView.findViewById(R.id.btnFive);
//        btnFive.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				add("5");
//			}
//		});
        
		return rootView;
	}
	
//	public interface onKeyBoardEvent
//    {
//        public void numberIsPressed(String total);
//        public void backLongPressed();
//        public void backButtonPressed(String total);
//    }
//	
	public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Button b1 = (Button)v;
		edNumInput.setText(edNumInput.getText().toString() + b1.getText().toString());
	}
}
