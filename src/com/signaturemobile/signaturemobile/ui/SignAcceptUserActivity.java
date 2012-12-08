package com.signaturemobile.signaturemobile.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.signaturemobile.signaturemobile.Constants;
import com.signaturemobile.signaturemobile.R;
import com.signaturemobile.signaturemobile.io.NotificationCenter.NotificationListener;
import com.signaturemobile.signaturemobile.model.UserDB;

/**
 * SignUserActivity activity sign user application
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class SignAcceptUserActivity extends BaseActivity implements NotificationListener {
	
    /**
     * Accept button
     */
    private Button acceptButton;
    
    /**
     * Cancel button
     */
    private Button cancelButton;
    
    /**
     * Password edidtext
     */
    private EditText passwordEditText;
    
    /**
     * User twitter image view
     */
    private ImageView userTwitterImageView;
    
    /**
     * Confirm title text view
     */
    private TextView confirmTitleTextView;
    
    /**
     * User db
     */
    private UserDB userDB;
    
    /**
     * The handler ui
     */
    private Handler handlerUI;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_signacceptuser, getString(R.string.title_sign_accept_user_title), "", 0);
        
        initiliceUI();
        setup();
    }
    
    /**
     * Initialize UI
     */
    private void initiliceUI(){
        handlerUI = new Handler();
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        acceptButton = (Button) findViewById(R.id.acceptButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        userTwitterImageView = (ImageView) findViewById(R.id.userTwitterImageView);
        confirmTitleTextView = (TextView) findViewById(R.id.confirmTitleTextView);
        
        acceptButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }
    
    /**
     * setup activity
     */
    private void setup(){
    	Intent intent = this.getIntent();
    	if (intent != null) {
			userDB = (UserDB) intent.getExtras().getSerializable(Constants.PARAMETERS_SIGN_USER);
			if (userDB != null) {
				String userDBUrl = userDB.getUserTwitter();
				String urlTwitter = String.format(Constants.URL_TWITTER_FORMAT_STRING, userDBUrl);
				String titleSign = String.format("%s%s%s", getString(R.string.confirm_title_device_text_1), userDB.getUsername(), "? " + getString(R.string.confirm_title_device_text_2));
				confirmTitleTextView.setText(titleSign);
				donwloadImage(urlTwitter, userTwitterImageView);
			}
    	}
    }
    
	/**
	 * Register all notifications
	 */
    @Override
    public void registerNotifications(){
    	super.registerNotifications();

    	// Register activity notification
    	toolbox.getNotificationCenter().registerListener(Constants.kHideInfoDialog, this);
    }
    
    /**
     * On item click event
     */
	public void onClick(View v) {
		if (v.equals(acceptButton)){
			String passEditText = passwordEditText.getText().toString();
			if (userDB != null){
				String macUser = userDB.getMac();
				int tickets = userDB.getTickets();
				String userPass = userDB.getPassword();
				if ((passEditText != null) && (passEditText.equals(userPass))){
					toolbox.getDaoUserSQL().updateTicketsFromMAC(macUser, tickets + 1);
					showInfoMessage(getString(R.string.sign_accept_user), true);
				} else {
					showInfoMessage(getString(R.string.password_not_equals_sign_accept_user), false);
				}

			} else {
				showInfoMessage(getString(R.string.unable_sign_accept_user), false);
			}
				
		} else if (v.equals(cancelButton)){
			goBack();
		} else {
			super.onClick(v);
		}
	}
	
    /**
     * Download image
     * @param url to download image
     */
    private void donwloadImage (final String url, final ImageView image){
    	if (url != null){
    		Thread thread = new Thread(new Runnable() {
				public void run() {
					try {
						final Bitmap imageTwitter = toolbox.getUpdater().downloadImage(url);
						if ((image != null) && (imageTwitter != null)){
							handlerUI.post(new Runnable() {
								public void run() {
									image.setImageBitmap(imageTwitter);
							    	showProgressNotification(false);
								}
							});
						}
					} catch (Throwable e) {
						e.printStackTrace();
					}					
				}
			});
    		
    		thread.start();
    		thread = null;
			
		}
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