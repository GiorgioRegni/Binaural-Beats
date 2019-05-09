package com.ihunda.android.binauralbeat;

import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public abstract class OnEditTextFocusChange implements OnFocusChangeListener {

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus == false) {
			EditText et = (EditText) v;
			String t = et.getText().toString();
			
			onTextChange(et, t);
		}
	}
	
	abstract void onTextChange(EditText v, String text);

}
