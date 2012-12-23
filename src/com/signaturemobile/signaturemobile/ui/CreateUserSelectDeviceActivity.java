package com.signaturemobile.signaturemobile.ui;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
 * CreateUserSelectDeviceActivity activity create user select device application
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class CreateUserSelectDeviceActivity extends BaseActivity implements NotificationListener, OnItemClickListener{
	
    /**
     * The activity TAG
     */
    private static final String TAG = "CreateUserSelectDeviceActivity";
    
    /**
     * Device text view
     */
    private TextView devicesTextView;
    
    /**
     * Devices list view
     */
    private ListView devicesListView;
    
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
        super.onCreate(savedInstanceState, R.layout.activity_createuser_selectdevice, getString(R.string.searching_devices_bluetooth_title), "", 0);
        
        devicesTextView = (TextView) findViewById(R.id.devicesTextView);
        devicesListView = (ListView) findViewById(R.id.deviceListView);
        
        deviceAdapter = new DevicesListAdapter(null);
        devicesListView.setAdapter(deviceAdapter);
        devicesListView.setOnItemClickListener(this);
        
        listDevices = new ArrayList<BluetoothDevice>();

        // Register notifications
    	registerNotifications();
    	
    	// update devices
    	updateListDevices();
    }
    
    /**
     * Call when the activity was destroy
     */
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	// Unregister notifications
    	unRegisterNotifications();
    	toolbox.getBluetoohInvoker().endDiscoveryDevices();
    }
    
    /**
     * On item click event
     */
	public void onClick(View v) {
		super.onClick(v);
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
     * Called when push into item list view
     */
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
		// synchronized with list devices
		synchronized (devicesListView) {
	        // the device header
	        if (position <= devicesListView.getCount() & (view instanceof DeviceListItemView)) {
	        	DeviceListItemView deviceListView = (DeviceListItemView) view;
	        	BluetoothDevice bluetoohDevice = deviceListView.getDevice();
	        	if (bluetoohDevice != null){
	        		Intent intentResult = new Intent();
	        		intentResult.putExtra(Constants.PARAMETERS_SELECT_ACTIVITY, bluetoohDevice);
	        		setResult(Constants.RESULT_CODE_OK, intentResult);
	        		goBack();
	        	}
	        }  
		}
	}

    /**
     * Update list devices
     */
    private void updateListDevices (){
    	// update devices    	
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
		                Intent intentSignAcceptUser = new Intent(CreateUserSelectDeviceActivity.this, SignAcceptUserActivity.class);
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
	    	showErrorMessage(messageErrorBluetooth, true);
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
			
			Tools.logLine(TAG, "Received finish request device found devices");
			deviceAdapter.setListDevices(listDevices);
			if (!listDevices.isEmpty()){
				devicesTextView.setText(getString(R.string.select_user));
				deviceAdapter.setListDevices(new ArrayList<BluetoothDevice>(listDevices));
				
				// synchronized with list devices
				synchronized (devicesListView) {
					deviceAdapter.notifyDataSetChanged();
				}
				
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