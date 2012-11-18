package com.signaturemobile.signaturemobile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.signaturemobile.signaturemobile.Constants;
import com.signaturemobile.signaturemobile.R;
import com.signaturemobile.signaturemobile.io.NotificationCenter.NotificationListener;

/**
 * SignatureMobileActivity activity main application
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class SignUserHomeActivity extends BaseActivity implements NotificationListener {
	
    /**
     * Create user button
     */
    private Button createUserButton;
    
    /**
     * Signature user button
     */	
    private Button signUserButton;
    
    /**
     * List user button
     */
    private Button listUserButton;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_signuser_home, getString(R.string.title_sign_user_home_mobile), "", 0);
        
        createUserButton = (Button) findViewById(R.id.createUserButton);
        signUserButton = (Button) findViewById(R.id.signUserButton);
        listUserButton = (Button) findViewById(R.id.listUserButton);
        
        // ui listener
        createUserButton.setOnClickListener(this);
        signUserButton.setOnClickListener(this);
        listUserButton.setOnClickListener(this);
        
    }
    
    @Override
    public void onResume(){
    	super.onResume();

    	// Register notifications
    	registerNotifications();
    	
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	
    	// Unregister notifications
    	unRegisterNotifications();
    }
    
    /**
     * On item click event
     */
	public void onClick(View v) {
		if (v == createUserButton){
            Intent intentCreateUser = new Intent(SignUserHomeActivity.this, CreateUserActivity.class);
            SignUserHomeActivity.this.startActivity(intentCreateUser);    
		} else if (v == signUserButton){
            Intent intentSignUser = new Intent(SignUserHomeActivity.this, SignUserActivity.class);
            SignUserHomeActivity.this.startActivity(intentSignUser);    
		} else if (v == listUserButton){
            Intent intentListUser = new Intent(SignUserHomeActivity.this, ListUsersSignActivity.class);
            SignUserHomeActivity.this.startActivity(intentListUser);    
		} else {
			super.onClick(v);
		}
	}
    
	/**
	 * Register all notifications
	 */
    @Override
    public void registerNotifications(){
    	super.registerNotifications();

    	// Register activity notification discovering
    	toolbox.getNotificationCenter().registerListener(Constants.kRequestStartOkFoundReceived, this);
    	toolbox.getNotificationCenter().registerListener(Constants.kRequestStartFailedFoundReceived, this);
    	
    	// Register activity notification devices
    	toolbox.getNotificationCenter().registerListener(Constants.kDeviceFoundReceived, this);
    	toolbox.getNotificationCenter().registerListener(Constants.kFinishRequestDeviceFoundReceived, this);
    }
    
    @Override
    public void unRegisterNotifications(){
    	super.registerNotifications();

    	// Register activity notification discovering
    	toolbox.getNotificationCenter().unregisterListener(Constants.kRequestStartOkFoundReceived, this);
    	toolbox.getNotificationCenter().unregisterListener(Constants.kRequestStartFailedFoundReceived, this);
    	
    	// Register activity notification devices
    	toolbox.getNotificationCenter().unregisterListener(Constants.kDeviceFoundReceived, this);
    	toolbox.getNotificationCenter().unregisterListener(Constants.kFinishRequestDeviceFoundReceived, this);
    }

}