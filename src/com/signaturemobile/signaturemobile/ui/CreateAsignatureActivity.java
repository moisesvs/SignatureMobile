package com.signaturemobile.signaturemobile.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.signaturemobile.signaturemobile.Constants;
import com.signaturemobile.signaturemobile.R;
import com.signaturemobile.signaturemobile.io.NotificationCenter.NotificationListener;

/**
 * SignatureMobileActivity activity create user application
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class CreateAsignatureActivity extends BaseActivity implements NotificationListener {
    
    /**
     * Name user edit text
     */
    private EditText nameAsignatureEditext;

    /**
     * Create user button
     */
    private Button createAsignatureButton;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_createasignature, getString(R.string.create_asignature_main), "", 0);
        
        nameAsignatureEditext = (EditText) findViewById(R.id.nameAsignatureEditText);
        createAsignatureButton = (Button) findViewById(R.id.createAsignatureButton);
        
        createAsignatureButton.setOnClickListener(this);
    }
    
    /**
     * Called when the activity on resumed
     */
	@Override
	protected void onResume() {
		super.onResume();
		registerNotifications();
	}

    /**
     * Called when the activity on paused
     */
	@Override
	protected void onPause() {
		super.onPause();
        unRegisterNotifications();
	}
    
    /**
     * On item click event
     */
	public void onClick(View v) {
		if (v.equals(createAsignatureButton)){

			if (nameAsignatureEditext != null){
				
				String nameAsignature = nameAsignatureEditext.getText().toString();
				
				if ((nameAsignature != null) && (!(nameAsignature.equals("")))){
					nameAsignature = nameAsignature.trim();

					try {
						toolbox.getDaoAsignatureSQL().createAsignature(nameAsignature, 0);
						showInfoMessage(getString(R.string.create_asignature_ok), true);
					} catch (Exception e) {
						showErrorMessage(getString(R.string.create_asignature_ko));
					}

				} else {
					showErrorMessage(getString(R.string.name_asignature_empty));
				}
				
			}
			
		} else {
			super.onClick(v);
		}
	}
    
	/**
	 * Register notification
	 */
    @Override
    public void registerNotifications(){
    	super.registerNotifications();
    	
    	// Register activity notification dialog
    	toolbox.getNotificationCenter().registerListener(Constants.kHideInfoDialog, this);
    }
    
	/**
	 * Unregister of the notifications to device
	 */
    @Override
    public void unRegisterNotifications(){
    	super.registerNotifications();
    	
    	// Register activity notification dialog
    	toolbox.getNotificationCenter().unregisterListener(Constants.kHideInfoDialog, this);
    }
    
    /**
     * Notification received 
     * @param notification the notification name
     * @param info the notification info
     */
	public void notificationPosted(String notification, Object info) {
		if (notification.equals(Constants.kHideInfoDialog)){
				// finish activity
				finish();
		}
	}
    
}