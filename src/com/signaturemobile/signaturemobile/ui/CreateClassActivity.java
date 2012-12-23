package com.signaturemobile.signaturemobile.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.signaturemobile.signaturemobile.Constants;
import com.signaturemobile.signaturemobile.R;
import com.signaturemobile.signaturemobile.io.NotificationCenter.NotificationListener;
import com.signaturemobile.signaturemobile.model.AsignatureDB;
import com.signaturemobile.signaturemobile.model.ClassDB;

/**
 * SignatureMobileActivity activity create user application
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class CreateClassActivity extends BaseActivity implements NotificationListener {
    
    /**
     * Name class edit text
     */
    private EditText nameClassEditext;

    /**
     * Create user button
     */
    private Button createClassButton;
    
    /**
     * Asignature db
     */
    private AsignatureDB asignatureDB;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_createclass, getString(R.string.create_class_title), "", 0);
        
        nameClassEditext = (EditText) findViewById(R.id.nameClassEditText);
        createClassButton = (Button) findViewById(R.id.createClassButton);
        
        createClassButton.setOnClickListener(this);
        asignatureDB = toolbox.getSession().getSelectAsignature();
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
		if (v.equals(createClassButton)){

			if (nameClassEditext != null){
				
				String nameClass = nameClassEditext.getText().toString();
				
				if ((nameClass != null) && (!(nameClass.equals("")))){
					nameClass = nameClass.trim();
					try {
						ClassDB classDb = toolbox.getDaoClassSQL().searchClassFromNameClass(nameClass);
						if (classDb == null){
							int idClass = toolbox.getDaoClassSQL().createClass(nameClass, 0);
							toolbox.getDaoJoinAsignatureWithClass().createJoinAsignatureWithClass(asignatureDB.getIdAsignature(), idClass);
							showInfoMessage(getString(R.string.create_class_ok), true);
						} else {
							showErrorMessage(getString(R.string.create_class_ko));
						}
					} catch (Exception e) {
						showErrorMessage(getString(R.string.create_class_ko));
					}

				} else {
					showErrorMessage(getString(R.string.name_class_empty));
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