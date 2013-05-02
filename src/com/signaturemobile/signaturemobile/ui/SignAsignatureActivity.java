package com.signaturemobile.signaturemobile.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.signaturemobile.signaturemobile.R;
import com.signaturemobile.signaturemobile.io.NotificationCenter.NotificationListener;

/**
 * SignatureMobileActivity activity main application
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class SignAsignatureActivity extends BaseActivity implements NotificationListener {
	
    /**
     * Create class button
     */
    private Button createClassButton;
    
    /**
     * Select class button
     */	
    private Button selectClassButton;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_signaturemobile, getString(R.string.title_signature_mobile), "", 0);
        
        createClassButton = (Button) findViewById(R.id.createClassButton);
        selectClassButton = (Button) findViewById(R.id.selectClassButton);
        
        // ui listener
        createClassButton.setOnClickListener(this);
        selectClassButton.setOnClickListener(this);
        
        actionBar.setDisplayHomeAsUpEnabled(false);

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
		if (v == createClassButton){
            Intent intentCreateClass = new Intent(SignAsignatureActivity.this, CreateAsignatureActivity.class);
            SignAsignatureActivity.this.startActivity(intentCreateClass);    
		} else if (v == selectClassButton){
            Intent intentListClass = new Intent(SignAsignatureActivity.this, SelectListAsignatureActivity.class);
            SignAsignatureActivity.this.startActivity(intentListClass);    
		}
	}
    
	/**
	 * Register all notifications
	 */
    @Override
    public void registerNotifications(){
    	super.registerNotifications();
    }
    
    @Override
    public void unRegisterNotifications(){
    	super.registerNotifications();
    }

}