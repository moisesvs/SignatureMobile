package com.signaturemobile.signaturemobile.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.signaturemobile.signaturemobile.Constants;
import com.signaturemobile.signaturemobile.R;
import com.signaturemobile.signaturemobile.SignatureMobileApplication;
import com.signaturemobile.signaturemobile.io.NotificationCenter;
import com.signaturemobile.signaturemobile.io.NotificationCenter.NotificationListener;

/**
 * The splash activity the first activity launcher
 *  
 * @author <a href="mailto:moisesvs@gmail.com">Mooisés Vázquez Sánchez</a>
 */
public class SplashActivity extends Activity implements OnClickListener, NotificationListener{
    
    /**
     * A reference to the application
     */
    protected SignatureMobileApplication application;
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (SignatureMobileApplication)getApplication();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        SignatureMobileApplication app = (SignatureMobileApplication) this.getApplication();
        NotificationCenter notificationCenter = app.getToolBox().getNotificationCenter();
        notificationCenter.registerListener(Constants.kInitializationEnds, this);
        if (application.isInitialized()) {
        	openSignatureActivity();
        } else if (application.hasFailed()) {
//            app.showErrorMessage(this, app.getString(R.string.unable_to_configure_application), this);
        }
    }
    
    /**
     * Called when the activity on resume
     */
    @Override
    protected void onResume() {
        super.onResume();
        ((SignatureMobileApplication) this.getApplication()).setCurrentActivity(this);
    }

    /**3
     * Called when the activity on stop
     */
    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
    }
    
    /**
     * Called when the activity on destroy
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Notification posted
     */
	public void notificationPosted(String notification, Object info) {
	    if (Constants.kInitializationEnds.equals(notification)) {
	    	SignatureMobileApplication app = (SignatureMobileApplication) this.getApplication();
	        if (app.hasFailed()) {
	            app.showErrorMessage(this, app.getString(R.string.unable_to_configure_application), this);
	        } else {
	        	openSignatureActivity();
	        }
	    }
    }
	
	/**
	 * Open signature activity
	 */
	private void openSignatureActivity() {
		Intent mainIntent = new Intent(SplashActivity.this, SignAsignatureActivity.class);	 	    	
       	SplashActivity.this.startActivity(mainIntent);
	    
        this.finish();
	}
	
	/**
	 * Error dialog clicked
	 * 
	 * @param dialog dialog clicked
	 * @param which button clicked
	 */
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        openSignatureActivity();
    }
}
