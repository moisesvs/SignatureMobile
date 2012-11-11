package com.signaturemobile.signaturemobile.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.signaturemobile.signaturemobile.Constants;
import com.signaturemobile.signaturemobile.R;
import com.signaturemobile.signaturemobile.SignatureMobileApplication;
import com.signaturemobile.signaturemobile.ToolBox;

/**
 * Base activity for all the ui screens. Will:
 * - manage the automatic log out after a time without user interaction.
 * - manage progress dialogs and alert dialogs
 * - perform the drawing of the tab bar
 * - perform the drawing of the gradient bar decoration
 * - perform the drawing of the title bar (plain or gradient, the text, the search button and the favorite button)
 *  
 * @author <a href="mailto:moisesvs@gmail.com">Mooisés Vázquez Sánchez</a>
 */
public class BaseActivity extends Activity implements OnClickListener{

	/**
     * Reference to the application
     */
    protected SignatureMobileApplication application;
    
    /**
     * The application toolbox
     */
    public ToolBox toolbox;
    
    /**
     * Main title
     */
    protected TextView mainTitle;
    
    /**
     * Is the activity paused? Will be set to true when entering onPaused and will be
     * set to false when entering onResume
     */
    private boolean paused = true;
    
    /**
     * Will be used to display a progress dialog
     */
    private Dialog progressDialog;
    
	/**
	 * The body layout
	 */
	protected LinearLayout bodyLayout;
    
	/**
	 * The arrow left layout
	 */
	protected LinearLayout arrowLeftLinear;
	
	/**
	 * Progress bar notification
	 */
	protected ProgressBar progressBarNotification;

	/**
	 * Image view main title
	 */
	protected ImageView maintitleImage;

	/**
	 * Image view main title
	 */
	protected ImageView arrowLeftImage;

	/**
     * To be called from derived activities when created.
     * @param savedInstanceState The bundle
     * @param layoutID The id of the layout to be shown in the body area
     * @param title The title for the top bar (only visible when using SHOW_TITLE_HEADER)
     * @param subtitle The subtitle for the top bar (only visible when using SHOW_TITLE_HEADER)
     * @param parameters One or multiple parameters from : SHOW_SEARCH_BUTTON, SHOW_FAVORITE_BUTTON, 
     * SHOW_GRADIENT_HEADER, SHOW_TITLE_HEADER, SHOW_TAB_BAR, SHOW_GRADIENT_BAR, separated by |
     */
    public void onCreate(Bundle savedInstanceState, int layoutID,
                         String title, String subtitle,
                         int parameters) {
        
        super.onCreate(savedInstanceState);       
        application = (SignatureMobileApplication) getApplication();
        toolbox = application.getToolBox();
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base);
        
        bodyLayout = (LinearLayout) findViewById(R.id.bodyLayout);
        arrowLeftLinear = (LinearLayout) findViewById(R.id.arrowLeftLinear);
        progressBarNotification = (ProgressBar) findViewById(R.id.progressBarNotification);
        maintitleImage = (ImageView) findViewById(R.id.maintitleImage);
        arrowLeftImage = (ImageView) findViewById(R.id.arrowLeftImage);
        mainTitle = (TextView) findViewById(R.id.mainTitle);
        
        // load received layout id in the body
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(layoutID, bodyLayout, true);
        
        if ((title != null) && (!(title.trim().equals("")))){
            mainTitle.setText(title);
        }
        
        arrowLeftLinear.setOnClickListener(this);

    }
	
    /**
     * Register notifications activity
     */
    public void registerNotifications() {
    	// Nothing
    }
    
    /**
     * Un register notifications activity
     */
    public void unRegisterNotifications(){
    	// Nothing
    }
    
    /**
     * Get application ToolBox
     * @return the toolbox 
     */
    protected ToolBox getToolBox() {
        return toolbox;
    }
    
    /**
     * The activity has been resumed
     */
    @Override
    protected void onResume() {
        super.onResume();        
        paused = false;

    }
    
    /**
     * The activity has been paused
     */
    @Override
    protected void onPause() {
        super.onPause();
        
        paused = true;
    }
    
    /**
     * Finish this activity
     */
    public void finishActivity() {
        finish();
    }
    
    /**
     * A key event has occurred. Reset the inactivity timer
     * @param event The key event
     * @return boolean True if the event has been managed; false otherwise
     */
//    public boolean dispatchKeyEvent(KeyEvent event) {        
//        application.resetActivityTime();        
//        return super.dispatchKeyEvent(event);
//    }

    /**
     * A touch event has occurred. Update the inactivity timer
     * @param ev The motion event
     * @return boolean True if the event has been managed; false otherwise
     */
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        application.resetActivityTime();
//        return super.dispatchTouchEvent(ev);
//    }
    
    /**
     * Go back application
     */
    protected void goBack(){
    	finish();
    }
    
    /**
     * Show or not the progress bar notification
     */
    public void showProgressNotification (boolean show){
    	if (show)
    		progressBarNotification.setVisibility(View.VISIBLE);
    	else 
    		progressBarNotification.setVisibility(View.GONE);
    }
    
    /**
     * Shows an alert that finishes the current activity when dismissed
     * @param message The message to be shown
     */
    public void showAlertActivityFinisher(String message) {        
        
        // show an alert, that will close activity when dismissed
        AlertDialog dialog = new AlertDialog.Builder(this).create();        
//        dialog.setMessage(message);
//        dialog.setButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                dialog.dismiss();                
//                finishActivity();                
//           }
//        });        
        dialog.show();
    }
    
    /**
     * Method that will show a progress dialog with no message
     */
    public void showProgressDialog(String titleText, String text) {
    	
        // if the dialog exists and is already open...
        if(progressDialog!=null && progressDialog.isShowing()) { 
            // end doing nothing
        } else {            
            progressDialog = ProgressDialog.show(this, titleText, text, true, true);
        }
        
//        setProgressing(true);
    }
    
    /**
     * Hide progress indicator
     */
    public void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        
//        setProgressing(false);
    }
    
    /**
     * Set the progressing flag
     * @param progressing The progressing flag
     */
//    public void setProgressing(boolean progressing) {
//        this.progressing = progressing;
//    }

    /**
     * Get the progressing flag
     * @return The progressing flag
     */
//    public boolean isProgressing() {
//        return progressing;
//    }

    /**
     * Shows an error message
     * @param message The message to be shown
     */
    public void showErrorMessage(String message) {
        showErrorMessage(message, true);
    }
    
    /**
     * Shows error message
     * @param message the message
     * @param notification the flag to indicate the availability of BBVA notification button
     */
    public void showErrorMessage(final String message, boolean notification) {
        showErrorMessage(null, message, notification);
    }
    
    /**
     * Shows error message
     * @param title the title
     * @param message the message
     * @param notification the flag to indicate the availability of BBVA notification button
     */
    public void showErrorMessage(final String title, final String message, final boolean notification) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();     
        dialog.setTitle(((title != null) && (title.trim().length() > 0)) ? title : getString(R.string.error));
        dialog.setMessage(message);
        dialog.setButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                if (notification) {
                	toolbox.getNotificationCenter().postNotification(Constants.kHideErrorDialog, null);
                }
           }
        });
        dialog.show();
    }

    /**
     * Shows an standard alert
     * @param message The message to be shown
     */
    public void showInfoMessage(String message, final boolean notification) {        
        AlertDialog dialog = new AlertDialog.Builder(this).create();     
        dialog.setTitle(getString(R.string.info));
        dialog.setMessage(message);
        dialog.setButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                if (notification) {
                	toolbox.getNotificationCenter().postNotification(Constants.kHideInfoDialog, null);
                }
           }
        });        
        dialog.show();
    }
    
    /**
     * Shows a dialog asking the user for confirmation of the log out action
     */
    public void showLogOutDialog() {        
        AlertDialog dialog = null;    
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(R.string.attention);
//        builder.setMessage(R.string.want_to_logout);
//        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                doLogOutAndGoLoginActivity();
//            }           
//        });
//        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }           
//        });
        dialog = builder.create();
        dialog.show();
    }
    
//    /**
//     * Performs a logout operation and goes to LoginActivity clearing all activities of stack
//     */
//    public void doLogOutAndGoLoginActivity() {
//        logOut();
//        Intent intent = new Intent(this, LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//    }    
    
    /**
     * Creates and shows an alert dialog informing that no location sensor is available
     * @param listener the object receiving the button click
     */
    public void showNoSensorAvailableAlert(DialogInterface.OnClickListener listener) {        
        AlertDialog dialog = null;       
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(R.string.no_location_sensor_dlg);
//        builder.setPositiveButton(R.string.activate_location_sensors_button, listener);
//        builder.setNegativeButton(R.string.do_not_activate_location_sensors_button, listener);
        dialog = builder.create();
        dialog.show();
    }
    
    /**
     * Initiate a phone call
     * @param phonenumber The phone number to be called
     */
    public void phoneCall(final String phonenumber) {
        Builder builder = new AlertDialog.Builder(this);
        TextView information = new TextView(this);        
//        information.setText(getString(R.string.call_dialog_message));
        information.setMaxLines(10);
        information.setTextSize(20.0f);
        information.setPadding(20, 0, 20, 20);
//        builder.setView(information);
//        builder.setTitle(getString(R.string.call_dialog_title) + " " + phonenumber);
//        builder.setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                dialog.dismiss();
//                try {
//                    String url = "tel:" + phonenumber;          
//                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
//                    startActivity(callIntent);
//                } catch (ActivityNotFoundException e) {
//                }                   
//            }
//        });
//        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                dialog.dismiss();
//            }
//        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Notification received 
     * @param notification the notification name
     * @param info the notification info
     */
    public void notificationPosted(String notification, Object info) {
    	// nothing
    }
    
    /**
     * This hook is called when the user signals the desire to start a search. 
     * @return True if search launched, and false if activity blocks it
     */
    public boolean onSearchRequested() {
        return false;
    }

    /**
     * Return if the activity is pause
     * @return if is paused
     */
    public boolean isPaused (){
        return paused;
    }

    /**
     * Called when a view has been clicked.
     * @param view The view clicked
     */
	public void onClick(View v) {
    	if (v.equals(arrowLeftLinear)){
    		goBack();
    	}
	}

}