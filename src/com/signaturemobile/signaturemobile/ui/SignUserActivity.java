package com.signaturemobile.signaturemobile.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.signaturemobile.signaturemobile.Constants;
import com.signaturemobile.signaturemobile.R;
import com.signaturemobile.signaturemobile.io.NotificationCenter.NotificationListener;
import com.signaturemobile.signaturemobile.model.UserDB;
import com.signaturemobile.signaturemobile.ui.listitems.DeviceListItemView;
import com.signaturemobile.signaturemobile.utils.Tools;

/**
 * SignUserActivity activity sign user application
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class SignUserActivity extends BaseActivity implements NotificationListener {
	
    /**
     * The activity TAG
     */
    private static final String TAG = "SignUserActivity";
    
    /**
     * Device text view
     */
    private TextView devicesTextView;
    
    /**
     * Devices list view
     */
    private ListView devicesListView;
    
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
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_signuser, getString(R.string.title_sign_user_title), "", 0);
        
        devicesTextView = (TextView) findViewById(R.id.devicesTextView);
        devicesListView = (ListView) findViewById(R.id.deviceListView);
        
        deviceAdapter = new DevicesListAdapter(null);
        devicesListView.setAdapter(deviceAdapter);
        
        listDevices = new ArrayList<BluetoothDevice>();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        
        // Register notifications
    	registerNotifications();
    	
    	// update devices
    	updateListDevices();
    }
    
    @Override
    public void onResume(){
    	super.onResume();

        // Check NFC availability and status, then register as foreground receiver
        if( nfcAdapter != null ) {
            if( nfcAdapter.isEnabled()) {
            	PendingIntent mNfcPendingIntent = PendingIntent.getActivity(this, 0,
            		    new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
                nfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, null, null );
            }
        }
    }
    
    /**
     * Call when the activity was paused
     */
    public void onPause() {
        super.onPause();
        // Make sure we unregister
        if( nfcAdapter != null ){
            nfcAdapter.disableForegroundDispatch(this);
        }
    }
    
    /**
     * Call when the activity was destroy
     */
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	// Unregister notifications
    	unRegisterNotifications();
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
    
    	// hide notification
    	toolbox.getNotificationCenter().registerListener(Constants.kHideErrorDialog, this);
    }

    /**
	 * Unregister of the notifications to device
	 */
    @Override
    public void unRegisterNotifications(){
    	super.registerNotifications();

    	// Register activity notification discovering
    	toolbox.getNotificationCenter().unregisterListener(Constants.kRequestStartOkFoundReceived, this);
    	toolbox.getNotificationCenter().unregisterListener(Constants.kRequestStartFailedFoundReceived, this);
    	
    	// Register activity notification devices
    	toolbox.getNotificationCenter().unregisterListener(Constants.kDeviceFoundReceived, this);
    	toolbox.getNotificationCenter().unregisterListener(Constants.kFinishRequestDeviceFoundReceived, this);
    	
    	// hide notification
    	toolbox.getNotificationCenter().unregisterListener(Constants.kHideErrorDialog, this);
    }

    /**
     * Update list devices
     */
    private void updateListDevices (){  	
    	showProgressNotification(true);
        toolbox.getBluetoohInvoker().beginDiscoveryDevices();
    }
    
	/**
	 * On new intent received
	 * @param intent received
	 */
    @Override
    protected void onNewIntent(Intent intent) {
        // NDEF exchange mode
        if(intent.getType() != null) {
        	Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        	NdefMessage msg = (NdefMessage) rawMsgs[0];
        	NdefRecord cardRecord = msg.getRecords()[0];
        	String tokenNFC = new String(cardRecord.getPayload());
        	
        	if ((tokenNFC != null) && (!(tokenNFC.equals("")))) {
        		UserDB userDB = toolbox.getDaoUserSQL().searchUserDeviceTokenNFC(tokenNFC);
        		if (userDB != null){
        			if (! (DateUtils.isToday(userDB.getDateLastSignUserTime()))){
		                Intent intentSignAcceptUser = new Intent(SignUserActivity.this, SignAcceptUserActivity.class);
		                intentSignAcceptUser.putExtra(Constants.PARAMETERS_SIGN_USER, userDB);
		                startActivity(intentSignAcceptUser);
        			} else {
            			showInfoMessage(getString(R.string.unable_sign_same_day_user), false);
        			}
        		} else {
        			showInfoMessage(getString(R.string.unable_sign_accept_user), false);
        		}
        	}
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
			
		} else if (notification.equals(Constants.kRequestStartFailedFoundReceived)){
			
	    	showProgressNotification(false);
	    	String messageErrorBluetooth = getString(R.string.not_connected_bluetooth);
	    	showErrorMessage(messageErrorBluetooth);
			Tools.logLine(TAG, "Received notification failed start discovering devices");

		} else if (notification.equals(Constants.kDeviceFoundReceived)){
			
			if (info instanceof BluetoothDevice){
				BluetoothDevice device = (BluetoothDevice) info;
				Tools.logLine(TAG, "Received found devices bluetooh");
				if (! (listDevices.contains(device))) {
					listDevices.add(device);
				}
			}
			
		} else if (notification.equals(Constants.kFinishRequestDeviceFoundReceived)){
			
	    	showProgressNotification(false);

			Tools.logLine(TAG, "Received finish request device found devices");
			deviceAdapter.setListDevices(listDevices);
			if (!listDevices.isEmpty()){
				devicesTextView.setText(getString(R.string.devices_user_sign));
				deviceAdapter.notifyDataSetChanged();
			} else {
				devicesTextView.setText(getString(R.string.not_exits_devices_select_user));
			}
			// again begin searching for devices
			updateListDevices();
		} else if (notification.equals(Constants.kHideErrorDialog)){
			// finish activity
			finish();
		}
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
                if (device != null){
                    result.setContent(device, false);
            		UserDB userDB = toolbox.getDaoUserSQL().searchUserDeviceMAC(device.getAddress());
            		if (userDB != null){
            			if (DateUtils.isToday(userDB.getDateLastSignUserTime())){
            				result.setChecked(true);
            			} else {
            				int tickets = userDB.getNumberTickets();
            				if (toolbox.getDaoUserSQL().updateTicketsFromMAC(device.getAddress(), tickets + 1)){
                				result.setChecked(true);
            				} else {
                				result.setChecked(false);
            				}
            			}
            		} else {
        				result.setChecked(false);
            		}
                }
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