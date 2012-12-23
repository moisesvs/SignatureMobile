package com.signaturemobile.signaturemobile.ui;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.signaturemobile.signaturemobile.model.AsignatureDB;
import com.signaturemobile.signaturemobile.model.JoinAsignatureWithUserDB;
import com.signaturemobile.signaturemobile.model.UserDB;
import com.signaturemobile.signaturemobile.ui.listitems.UserDBListItemView;

/**
 * ListUsersSignActivity activity list user signature application
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class ListUsersAsignatureActivity extends BaseActivity implements NotificationListener, OnItemClickListener {
    
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
     * List users from asignature
     */
    private List<JoinAsignatureWithUserDB> listUserFromAsignature;

    /**
     * Select asignature
     */
    private AsignatureDB selectAsignature;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_listsignuser, getString(R.string.title_list_sign_title), "", 0);
        selectAsignature = toolbox.getSession().getSelectAsignature();
        initializeUI();
    }
    
    /**
     * Initialize UI
     */
    private void initializeUI(){
        devicesTextView = (TextView) findViewById(R.id.devicesTextView);
        devicesListView = (ListView) findViewById(R.id.deviceListView);
        
        deviceAdapter = new DevicesListAdapter(null);
        devicesListView.setAdapter(deviceAdapter);
        devicesListView.setOnItemClickListener(this);
    }
    
    /**
     * Called when the activity on resume
     */
    public void onResume(){
    	super.onResume();
    	// list bluetooth devices
    	if (selectAsignature != null) {
    		int idAsignature = selectAsignature.getIdAsignature();
        	listUserFromAsignature = toolbox.getDaoJoinAsignatureWithUser().listJoinAsignatureWithUser(idAsignature);
        	if ((listUserFromAsignature != null) && (! (listUserFromAsignature.isEmpty()))){
                deviceAdapter.setListDevices(listUserFromAsignature);
                deviceAdapter.notifyDataSetChanged();
            	devicesTextView.setText(getString(R.string.list_devices_sign));
        	} else {
                deviceAdapter.setListDevices(listUserFromAsignature);
                deviceAdapter.notifyDataSetChanged();
            	devicesTextView.setText(getString(R.string.list_devices_sign_empty));
        	}
    	}
    }
    
    /**
     * Called when push into item list view
     */
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {    
		// the device header
		if (position <= devicesListView.getCount() && (view instanceof UserDBListItemView)) {
			UserDBListItemView deviceListView = (UserDBListItemView) view;
			UserDB userDBDevice = deviceListView.getUserDB();
			if (userDBDevice != null) {
				Intent intentSignAcceptUser = new Intent(ListUsersAsignatureActivity.this, DetailsUserActivity.class);
				intentSignAcceptUser.putExtra(Constants.PARAMETERS_SIGN_USER, userDBDevice);
				startActivity(intentSignAcceptUser);
			} else {
				showInfoMessage(getString(R.string.unable_sign_accept_user), false);
			}
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
    	private List <JoinAsignatureWithUserDB> listDevices;
    	
    	/**
    	 * Default constructor
    	 * @param listDevices the list devices
    	 */
    	public DevicesListAdapter (List<JoinAsignatureWithUserDB> listDevices){
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
            UserDBListItemView result;
            if (convertView != null) {
                result = (UserDBListItemView) convertView;
            } else {
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                result = (UserDBListItemView) layoutInflater.inflate(R.layout.listitem_userdb, parent, false);
            }
            
            int row = position;
            if (listDevices != null){
            	JoinAsignatureWithUserDB userDBDevice = listDevices.get(row);
                if (userDBDevice != null){
                	UserDB userDB = toolbox.getDaoUserSQL().searchUserDeviceId(userDBDevice.getIdUser());
                    result.setContent(userDB, false);
            		List<UserDB> userDBList = toolbox.getDaoUserSQL().listFromDate();
            		for (UserDB user : userDBList){
                		if ((user != null) && (DateUtils.isToday(user.getDateLastSignUserTime()))){
                			result.setChecked(true);
                		} else {
                			result.setChecked(false);
                		}
            		}
                }
            }
            
            return result;
        }

		/**
		 * @param listDevicesUserDB the listDevices to set
		 */
		public void setListDevices(List<JoinAsignatureWithUserDB> listDevicesUserDB) {
			this.listDevices = listDevicesUserDB;
		}
        
    }

}