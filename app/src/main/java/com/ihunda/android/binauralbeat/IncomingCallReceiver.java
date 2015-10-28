package com.ihunda.android.binauralbeat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class IncomingCallReceiver extends BroadcastReceiver {

	private static  String TAG = "BBT-CALL";
	
    @Override
    public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            
            if(null == bundle)
                    return;
            
            BBeat app = BBeat.getInstance();
            
            Log.i(TAG,bundle.toString());
            
            String state = bundle.getString(TelephonyManager.EXTRA_STATE);
                            
            Log.i(TAG, "State: "+ state);
            
            if (app == null)
            	return;
            
            if (!app.isInProgram())
            	return;
            
            if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING) ||
            		state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
            	if (app.isPaused() == false)
            		app.pauseOrResume();
            } else if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)) {
            	if (app.isPaused() == true)
            		app.pauseOrResume();
            }
            
            /*
            if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING))
            {
                    String phonenumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                            
                    Log.i("IncomingCallReceiver","Incomng Number: " + phonenumber);
                    
                    String info = "Detect Calls sample application\nIncoming number: " + phonenumber;
                    
                    Toast.makeText(context, info, Toast.LENGTH_LONG).show();
            }
            */
    }

}