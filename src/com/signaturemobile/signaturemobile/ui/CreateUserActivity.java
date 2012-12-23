package com.signaturemobile.signaturemobile.ui;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.signaturemobile.signaturemobile.Constants;
import com.signaturemobile.signaturemobile.R;
import com.signaturemobile.signaturemobile.io.NotificationCenter.NotificationListener;
import com.signaturemobile.signaturemobile.model.AsignatureDB;
import com.signaturemobile.signaturemobile.model.UserDB;
import com.signaturemobile.signaturemobile.ui.listitems.DeviceListItemView;
import com.signaturemobile.signaturemobile.utils.Tools;

/**
 * SignatureMobileActivity activity create user application
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class CreateUserActivity extends BaseActivity implements NotificationListener {
	
    /**
     * The activity TAG
     */
    private static final String TAG = "CreateUserActivity";
    
    /**
     * Request code result device
     */
    private static final int REQUEST_CODE_SELECT_DEVICE = 0;
    
    /**
     * Devices text view
     */
    private TextView devicesTextView;
    
    /**
     * User device name
     */
    private TextView userDeviceName;
    
    /**
     * Name user edit text
     */
    private EditText nameUserEditext;
    
    /**
     * Twitter user edit text
     */
    private EditText twitterUserEditext;
    
    /**
     * User Twitter image view
     */
    private ImageView userTwitterImageView;

    /**
     * Create user button
     */
    private Button createUserButton;
    
    /**
     * Update list devices button
     */
    private Button selectDevice;
    
    /**
     * Write nfc key button
     */
    private Button writeKeyNFCButton;
    
    /**
     * NFC adapter
     */
    private NfcAdapter nfcAdapter;
    
    /**
     * Adapter devices
     */
    private DevicesListAdapter deviceAdapter;
    
    /**
     * List devices
     */
    private List<BluetoothDevice> listDevices;
    
    /**
     * The handler ui
     */
    private Handler handlerUI;
    
    /**
     * String value time
     */
    private String valueStringTime;
    
    /**
     * Bluetooth device
     */
    private BluetoothDevice bluetoothDevice;
    
    /**
     * Asignature selected
     */
    private AsignatureDB asignatureSelected;
    
    /**
     * Select device linear layout
     */
    private LinearLayout selectDeviceLinearLayout;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_createuser, getString(R.string.create_user_main), "", 0);
        
        handlerUI = new Handler();
        devicesTextView = (TextView) findViewById(R.id.devicesTextView);
        nameUserEditext = (EditText) findViewById(R.id.nameUserEditText);
        twitterUserEditext = (EditText) findViewById(R.id.twitterUserEditText);
        userTwitterImageView = (ImageView) findViewById(R.id.userTwitterImageView);
        userDeviceName = (TextView) findViewById(R.id.userDeviceName);
        createUserButton = (Button) findViewById(R.id.createButton);
        selectDevice = (Button) findViewById(R.id.updateListDevicesButton);
        writeKeyNFCButton = (Button) findViewById(R.id.writeKeyNFCButton);
        selectDeviceLinearLayout = (LinearLayout) findViewById(R.id.selectDeviceLinearLayout);
        
        createUserButton.setOnClickListener(this);
        selectDevice.setOnClickListener(this);
        writeKeyNFCButton.setOnClickListener(this);
        selectDeviceLinearLayout.setOnClickListener(this);

        twitterUserEditext.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                	searchUserTwitter();
                }
                return false;
            }
        });
        
        twitterUserEditext.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
            	if (!hasFocus) {
            		searchUserTwitter();
            	}
            }
        });
        
        listDevices = new ArrayList<BluetoothDevice>();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        // get asignature
        asignatureSelected = toolbox.getSession().getSelectAsignature();
    }
    
    /**
     * Called when the activity on resumed
     */
	@Override
	protected void onResume() {
		super.onResume();
        
		registerNotifications();
		
        // Check NFC availability and status, then register as foreground receiver
        if( nfcAdapter != null ) {
            if( nfcAdapter.isEnabled()) {
            	PendingIntent mNfcPendingIntent = PendingIntent.getActivity(this, 0,
            		    new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
                nfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, null, null);
            }
        }

	}

    /**
     * Called when the activity on paused
     */
	@Override
	protected void onPause() {
		super.onPause();
        unRegisterNotifications();

        // Make sure we unregister
        if( nfcAdapter != null ){
            nfcAdapter.disableForegroundDispatch(this);
        }
        
	}
    
    /**
     * Called when an activity you launched exits, giving you the requestCode 
     * you started it with, the resultCode it returned, and any additional data from it. 
     * The resultCode will be RESULT_CANCELED if the activity explicitly returned that, 
     * didn't return any result, or crashed during its operation..
     * requestCode	The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
	 * resultCode	The integer result code returned by the child activity through its setResult().
	 * data	An Intent, which can return result data to the caller (various data can be attached to Intent "extras").     */
    public void onActivityResult (int requestCode, int resultCode, Intent data){
    	if (requestCode == REQUEST_CODE_SELECT_DEVICE){
    		if (resultCode == Constants.RESULT_CODE_OK){
    			if (data != null){
    				Bundle bundle = data.getExtras();
    				if (bundle != null){
    					bluetoothDevice = (BluetoothDevice) bundle.get(Constants.PARAMETERS_SELECT_ACTIVITY);
    					if (bluetoothDevice != null){
    						userDeviceName.setText(bluetoothDevice.getName());
    					} else {
    						String text = getString(R.string.nothing);
    						userDeviceName.setText(text);
    					}
    				}
    			} else {
    				bluetoothDevice = null;
					String text = getString(R.string.nothing);
					userDeviceName.setText(text);
				}
    		} else if (resultCode == Constants.RESULT_CODE_KO){
    			String errorMessage = getString(R.string.register_user_not_selected_device);
    			showErrorMessage(errorMessage);
    		}
    	}
    }
    
    /**
     * On item click event
     */
	public void onClick(View v) {
		if (v.equals(selectDevice) || (v.equals(selectDeviceLinearLayout))){

			Intent intentCreateUserSelect = new Intent(this, CreateUserSelectDeviceActivity.class);
			startActivityForResult(intentCreateUserSelect, REQUEST_CODE_SELECT_DEVICE);
			
		} else if (v.equals(writeKeyNFCButton)){
			
			if (nfcAdapter != null){
				// write nfc
				String titleText = getString(R.string.register_user_dialog_title_nfc);
				String descriptionText = getString(R.string.register_user_dialog_description_nfc);
				showProgressDialog(titleText, descriptionText);
			} else {
				showInfoMessage(getString(R.string.device_not_functionality_nfc), false);
			}
			
		} else if (v.equals(createUserButton)){

			if ((nameUserEditext != null) && (twitterUserEditext != null)){
				
				String username = nameUserEditext.getText().toString();
				String twitterUser = twitterUserEditext.getText().toString();
				
				if ((username != null) && (!(username.equals("")))){
					
					if (bluetoothDevice != null){
						
						String mac = bluetoothDevice.getAddress();
						if ((! (toolbox.getDaoJoinAsignatureWithUser().exitsAsignatureFromMacUser(asignatureSelected.getNameAsignature(), mac)) || (valueStringTime != null))){
							// create user in table SQL
							if (toolbox.getDaoUserSQL().createUser(username, "", twitterUser, mac, 0, new Date(), (new Date(0)), valueStringTime)){
								AsignatureDB asignature = toolbox.getSession().getSelectAsignature();
								if (asignature != null) {
									UserDB userDb = toolbox.getDaoUserSQL().searchUserDeviceMAC(mac);
									if ((userDb != null) && (toolbox.getDaoJoinAsignatureWithUser().createJoinAsignatureWithUser(asignature.getIdAsignature(), userDb.getIdUser(), userDb.getUsername()))){
										showInfoMessage(getString(R.string.register_user_ok), true);
									} else {
										showErrorMessage(getString(R.string.register_user_ko));
									}
								} else {
									showErrorMessage(getString(R.string.register_user_ko));
								}
							} else {
								showErrorMessage(getString(R.string.register_user_ko));
							}
						} else {
							showErrorMessage(getString(R.string.device_already_register));
						}

					} else {
						showErrorMessage(getString(R.string.device_user_empty));
					}
						
				} else {
					showErrorMessage(getString(R.string.name_user_empty));
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
     * Search user twitter
     */
    private void searchUserTwitter (){
    	if (twitterUserEditext != null){
			String userTwitter = twitterUserEditext.getText().toString();
			if (userTwitter != null){
		    	showProgressNotification(true);
				String urlTwitter = String.format(Constants.URL_TWITTER_FORMAT_STRING, userTwitter);
				donwloadImage(urlTwitter, userTwitterImageView);
			}
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
		if (notification.equals(Constants.kRequestStartOkFoundReceived)){
			
			Tools.logLine(TAG, "Received notification start found");
			listDevices.clear();
			
		} else if (notification.equals(Constants.kRequestStartFailedFoundReceived)){
			
	    	showProgressNotification(false);
	    	String messageErrorBluetooth = getString(R.string.not_connected_bluetooth);
	    	showErrorMessage(messageErrorBluetooth, true);
			Tools.logLine(TAG, "Received notification failed start discovering devices");

		} else if (notification.equals(Constants.kDeviceFoundReceived)){
			
			if (info instanceof BluetoothDevice){
				BluetoothDevice device = (BluetoothDevice) info;
				Tools.logLine(TAG, "Received found devices bluetooh");
				listDevices.add(device);
			}
			
		} else if (notification.equals(Constants.kFinishRequestDeviceFoundReceived)){
			
	    	showProgressNotification(false);

			Tools.logLine(TAG, "Received finish request device found devices");
			deviceAdapter.setListDevices(listDevices);
			if (!listDevices.isEmpty()){
				devicesTextView.setText(getString(R.string.select_user));
				deviceAdapter.notifyDataSetChanged();
			} else {
				devicesTextView.setText(getString(R.string.not_exits_devices_select_user));
			}
			
		} else if (notification.equals(Constants.kHideInfoDialog)){
				// finish activity
				finish();
		}
	}
	
	/**
	 * On new intent received
	 * @param intent received
	 */
    public void onNewIntent(Intent intent){
        // NFC read/write are blocking operations that must be performed on a separate thread
    	Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        writeTag(tag);

    	// hide progress
    	hideProgressDialog();
    	
    	String messageNFC = getString(R.string.register_user_dialog_nfc_write_ok);
    	showInfoMessage(messageNFC, false);
    }
    
    /**
     * Write tag to tag nfc
     * @param tag nfc tag
     * @return if the tag has been write on tag
     */
    private boolean writeTag(Tag tag) {
    	// record to launch Play Store if app is not installed
    	NdefRecord appRecord = NdefRecord.createApplicationRecord(Constants.PACKAGE_APPLICATION);
    	
    	// record that contains our custom "retro console" game data, using custom MIME_TYPE
    	valueStringTime = String.valueOf(System.currentTimeMillis());
    	byte[] payload = valueStringTime.getBytes();
        byte[] mimeBytes = Locale.getDefault().getLanguage().getBytes(Charset.forName("US-ASCII"));

    	NdefRecord cardRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);
    	NdefMessage message = new NdefMessage(new NdefRecord[] { cardRecord, appRecord});
    	
    	try {
    		// see if tag is already NDEF formatted
    		Ndef ndef = Ndef.get(tag);
    		if (ndef != null) {
    			ndef.connect();
    			
    			if (!ndef.isWritable()) {
    				return false;
    			}
    			// work out how much space we need for the data
    			int size = message.toByteArray().length;
    			if (ndef.getMaxSize() < size) {
    				return false;
    			}
    			
    			ndef.writeNdefMessage(message);
    			return true;
    		} else {
    			// attempt to format tag
    			NdefFormatable format = NdefFormatable.get(tag);
    			if (format != null) {
    				try {
    					format.connect();
    					format.format(message);
    					return true;
    				} catch (IOException e) {
    					return false;
    				}
    			} else {
    				return false;
    			}
    		}
    	} catch (Exception e) {
    		// exception
    	}

    	return false;
	}

    /**
     * Options list adapter class
     * 
     * @author <a href="mailto:info@movilok.com">Movilok Interactividad Movil S.L.</a>
     */
    public class DevicesListAdapter extends BaseAdapter {

        // ListAdapter interface methods
    	/**
    	 * List devices 
    	 */
    	private List <BluetoothDevice> listDevices;
    	
    	/**
    	 * Default constructor
    	 * @param listDevices the list devices
    	 */
    	public DevicesListAdapter (List<BluetoothDevice> listDevices){
    		this.listDevices = listDevices;
    	}
    	
        /**
         * How many items are in the data set represented by this Adapter.
         * 
         * @return count of items
         */
        public int getCount() {
            if (listDevices != null)
                return listDevices.size();
             
            return 0;
        }

        /**
         * Get the data item associated with the specified position in the data set.
         * 
         * @param position Position of the item whose data we want within the adapter's data set.
         * @return The data at the specified position.
         */
        public Object getItem(int position) {
            return getResources().getString(R.string.app_name);
        }

        /**
         * Get the row id associated with the specified position in the list.
         * 
         * @position The position of the item within the adapter's data set whose row id we want.
         */
        public long getItemId(int position) {
            return position;
        }

        /**
         * Get a View that displays the data at the specified position in the data set.
         * You can either create a View manually or inflate it from an XML layout file.
         * When the View is inflated, the parent View (GridView, ListView...)
         * will apply default layout parameters unless you use inflate(int, android.view.ViewGroup, boolean)
         * to specify a root view and to prevent attachment to the root.
         * 
         * @param position The position of the item within the adapter's data set of the item whose view we want.
         * @param convertView The old view to reuse, if possible. Note: You should check that this view is non-nulland of an appropriate type before using.
         * If it is not possible to convert this view to display the correct data, this method can create a new view.
         * Heterogeneous lists can specify their number of view types, so that this View is always of the right type (see getViewTypeCount() and getItemViewType(int)).
         * @param parent The parent that this view will eventually be attached to
         * @return A View corresponding to the data at the specified position.
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            DeviceListItemView result;
            if (convertView != null) {
                result = (DeviceListItemView) convertView;
            } else {
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                result = (DeviceListItemView) layoutInflater.inflate(R.layout.listitem_device, parent, false);
            }
            
            int row = position;
            if (listDevices != null){
            	BluetoothDevice device = listDevices.get(row);
                if (device != null)
                    result.setContent(device, false);
            }
            
            return result;
        }

		/**
		 * @param listDevices the listDevices to set
		 */
		public void setListDevices(List<BluetoothDevice> listDevices) {
			this.listDevices = listDevices;
		}
        
    }

}