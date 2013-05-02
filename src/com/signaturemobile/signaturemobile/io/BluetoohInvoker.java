package com.signaturemobile.signaturemobile.io;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.signaturemobile.signaturemobile.Constants;
import com.signaturemobile.signaturemobile.R;
import com.signaturemobile.signaturemobile.SignatureMobileApplication;
import com.signaturemobile.signaturemobile.utils.Tools;

/**
 * BluetoohInvoker opetation bluetooh and send notification when operation finish
 *
 * @author <a href="mailto:moisesvs@gmail.com">Mooisés Vázquez Sánchez</a>
 */
public class BluetoohInvoker {

	    /**
	     * The activity TAG
	     */
	    private static final String TAG = "BluetoohInvoker";
	
		/**
		 * Adapter bluetooh singleton
		 */
		private BluetoothAdapter bluetoothAdapter;
		
		/**
		 * Handler receives notification bluetooh
		 */
		private BroadcastReceiverUpdater bluetoohReceiver;
		
		/**
		 * Notification bluetooh center
		 */
		private NotificationCenter notificationCenter;

		/**
		 * Application reference
		 */
		private SignatureMobileApplication application;
		
		/**
		 * Is discovering devices or not
		 */
		private boolean isDiscovering;
		
		/**
		 * Default constructor Bluetooh invoker
		 * @param application the application context
		 * @param notificationCenter the notification center
		 */
		public BluetoohInvoker (Application application, NotificationCenter notificationCenter){

			this.application = (SignatureMobileApplication) application;
			this.notificationCenter = notificationCenter;
			
			this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			this.bluetoohReceiver = new BroadcastReceiverUpdater();
			
			// Register application intent filter
			registerApplication();
		}

		/**
		 * Register application to bluetooth discovery
		 */
		public void registerApplication (){
		    // Register for broadcasts when a device is discovered
			IntentFilter filterFoundDevices = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		    application.registerReceiver(bluetoohReceiver, filterFoundDevices);

		    // Register for broadcasts when discovery has finished
		    IntentFilter filterActionDiscoveryFinish = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		    application.registerReceiver(bluetoohReceiver, filterActionDiscoveryFinish);
		}
		
		/**
		 * Begin to discovery devices bluetooth discovery
		 */
		public void beginDiscoveryDevices (){
			if (!isDiscovering){
				if (this.bluetoothAdapter != null) {
					boolean startDiscovery = this.bluetoothAdapter.startDiscovery();
					
					if (startDiscovery){
						isDiscovering = true;
				        Tools.logLine(TAG, "Start discovery devices...");
						this.notificationCenter.postNotification(Constants.kRequestStartOkFoundReceived, null);
						
					} else {
						isDiscovering = false;
				        Tools.logLine(TAG, "¡Failed! Start discovery devices...");
						this.notificationCenter.postNotification(Constants.kRequestStartFailedFoundReceived, null);
					}
				} else {
					String messageNotBluetoothDevie = application.getString(R.string.no_bluetooth_device);
					application.getCurrentActivity().showErrorMessage(messageNotBluetoothDevie);
				}
				
			}
		}
		
		/**
		 * End discovery devices Bluetooth
		 */
		public void endDiscoveryDevices (){
			if (isDiscovering){
				if (this.bluetoothAdapter != null) {
					boolean cancelDiscovery = this.bluetoothAdapter.cancelDiscovery();
					
					if (cancelDiscovery){
						isDiscovering = false;
				        Tools.logLine(TAG, "End force discovery devices...");
						this.notificationCenter.postNotification(Constants.kRequestFinishFoundForceOkReceived, null);
						
					} else {
						isDiscovering = true;
				        Tools.logLine(TAG, "¡Failed! End discovery devices...");
						this.notificationCenter.postNotification(Constants.kRequestFinishFoundForceFailedReceived, null);
					}
				} else {
					String messageNotBluetoothDevie = application.getString(R.string.no_bluetooth_device);
					application.getCurrentActivity().showErrorMessage(messageNotBluetoothDevie);
				}
			}
		}
		
		// The BroadcastReceiver that listens for discovered devices and
		// changes the title when discovery is finished		
		private class BroadcastReceiverUpdater extends BroadcastReceiver{
			
		    @Override
		    public void onReceive(Context context, Intent intent) {
		        String action = intent.getAction();

		        // When discovery finds a device
		        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
		        	
		            // Get the BluetoothDevice object from the Intent
		            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		            
		            // If it's already paired, skip it, because it's been listed already
		            if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
				        Tools.logLine(TAG, "Device: " + device.getName() + "   Address:" + device.getAddress());
				        notificationCenter.postNotification(Constants.kDeviceFoundReceived, device);
		            }
		            
		        // When discovery is finished, change the Activity title
		        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
					isDiscovering = false;
			        Tools.logLine(TAG, "End discovery devices");
					notificationCenter.postNotification(Constants.kFinishRequestDeviceFoundReceived, null);
		        }
		    }
		}
}
