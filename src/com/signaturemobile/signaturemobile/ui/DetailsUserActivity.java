package com.signaturemobile.signaturemobile.ui;

import java.util.Date;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.signaturemobile.signaturemobile.Constants;
import com.signaturemobile.signaturemobile.R;
import com.signaturemobile.signaturemobile.io.NotificationCenter.NotificationListener;
import com.signaturemobile.signaturemobile.model.UserDB;
import com.signaturemobile.signaturemobile.utils.Tools;

/**
 * SignUserActivity activity sign user application
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class DetailsUserActivity extends BaseActivity implements NotificationListener {
	
    /**
     * Accept button
     */
    private Button acceptButton;
	
    /**
     * Delete button
     */
    private Button deleteButton;
    
    /**
     * User twitter image view
     */
    private ImageView userTwitterImageView;
    
    /**
     * User text view
     */
    private TextView userTextView;
    
    /**
     * User twitter text view
     */
    private TextView userTwitterTextView;
    
    /**
     * User create text view
     */
    private TextView userCreateTextView;
    
    /**
     * Last sign user
     */
    private TextView lastSignUserTextView;
    
    /**
     * Join asignature with user db
     */
    private UserDB userDB;
    
    /**
     * Handler UI
     */
    private Handler handlerUI;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_detailsuser, getString(R.string.title_list_details_sign_title), "", 0);
        
        initiliceUI();
        setup();

        // Register notifications
    	registerNotifications();
	}
    
    /**
     * Initialize UI
     */
    private void initiliceUI(){
    	handlerUI = new Handler();
        userTwitterImageView = (ImageView) findViewById(R.id.userTwitterImageView);
        userTextView = (TextView) findViewById(R.id.userTextView);
        userCreateTextView = (TextView) findViewById(R.id.userCreateTextView);
        lastSignUserTextView = (TextView) findViewById(R.id.lastSignUserTextView);
        userTwitterTextView = (TextView) findViewById(R.id.userTwitter);
        acceptButton = (Button) findViewById(R.id.acceptButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        
        acceptButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
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
				userTextView.setText(userDB.getUsername());
				userCreateTextView.setText(getString(R.string.user_create_sign) + " " + Tools.formatDate(userDB.getDateCreateUser()));
				Date date = userDB.getDateLastSignUser();
				if ((date != null) && (!(Tools.isInitDate(date)))) {
					lastSignUserTextView.setText(getString(R.string.user_last_sign) + " " + Tools.formatDate(date));
				} else {
					lastSignUserTextView.setVisibility(View.GONE);
				}
				
				userTwitterTextView.setText(getString(R.string.user_twitter_sign) + " " + userDB.getUserTwitter());

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
	 * Register all notifications
	 */
    @Override
    public void unRegisterNotifications(){
    	super.unRegisterNotifications();

    	// Unregister activity notification
    	toolbox.getNotificationCenter().unregisterListener(Constants.kHideInfoDialog, this);
    }
    
    /**
     * On item click event
     */
	public void onClick(View v) {
		if (v.equals(acceptButton)){
			goBack();
		} else if (v.equals(deleteButton)){
			if (toolbox.getDaoUserSQL().deleteUser(userDB)) {
				if (toolbox.getDaoJoinAsignatureWithUser().deleteJoinAsignatureWithUser(userDB.getIdUser())){
					if (toolbox.getDaoJoinClassWithUser().deleteJoinClassWithUser(userDB.getIdUser())){
						showInfoMessage(getString(R.string.user_delete_ok), true);
					} else {
						showInfoMessage(getString(R.string.user_delete_ko), true);
					}
				} else {
					showInfoMessage(getString(R.string.user_delete_ko), true);
				}
			} else {
				showInfoMessage(getString(R.string.user_delete_ko), true);
			}
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