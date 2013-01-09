package com.signaturemobile.signaturemobile.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.signaturemobile.signaturemobile.ui.listitems.UserChargedListItemView;

/**
 * ListClassActivity activity list class signature application
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class SelectListUsernameActivity extends BaseActivity implements NotificationListener, OnItemClickListener {
    
    /**
     * Device text view
     */
    private TextView usernameTextView;
    
    /**
     * Devices list view
     */
    private ListView usernameListView;
    
    /**
     * Adapter devices
     */
    private DevicesListAdapter usernameAdapter;
    
    /**
     * List users
     */
    private List<String> listUsers;

    /**
     * Select asignagure
     */
    private AsignatureDB selectAsignature;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_listusername, getString(R.string.title_list_username_title), "", 0);
        initializeUI();
    }
    
    /**
     * Initialize UI
     */
    private void initializeUI(){
        usernameTextView = (TextView) findViewById(R.id.classTextView);
        usernameListView = (ListView) findViewById(R.id.classListView);
        
        listUsers = new ArrayList<String>();
        usernameAdapter = new DevicesListAdapter(null);
        usernameListView.setAdapter(usernameAdapter);
        usernameListView.setOnItemClickListener(this);
        
        selectAsignature = toolbox.getSession().getSelectAsignature();
    }
    
    /**
     * Called when the activity on resume
     */
    public void onResume(){
    	super.onResume();
    	// list bluetooth devices
    	HashMap <String, List<String>> listAsignatureWithJoin = toolbox.getSession().getChargeUsers();
    	if ((listAsignatureWithJoin != null) && (! (listAsignatureWithJoin.isEmpty()))) {
    		listUsers.clear();
    		List<String> listUsername = listAsignatureWithJoin.get(selectAsignature.getNameAsignature());
    		if (listUsername != null) {
        		for (String username : listUsername) {
        			if (username != null) {
        	        	listUsers.add(username);
        			}
        		}
    		}
    	}
    	
    	if ((listUsers != null) && (! (listUsers.isEmpty()))){
            usernameAdapter.setListDevices(listUsers);
            usernameAdapter.notifyDataSetChanged();
        	usernameTextView.setText(getString(R.string.list_devices_username_empty));
    	} else {
            usernameAdapter.setListDevices(listUsers);
            usernameAdapter.notifyDataSetChanged();
        	usernameTextView.setText(getString(R.string.list_devices_username_empty));
    	}
    }
    
    /**
     * On item click event
     */
	public void onClick(View v) {
		super.onClick(v);
	}
    
    /**
     * Called when push into item list view
     */
	public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {    
		// the device header
		if (position <= usernameListView.getCount() && (view instanceof UserChargedListItemView)) {
			UserChargedListItemView deviceListView = (UserChargedListItemView) view;
			String username = deviceListView.getUsername();
			if (username != null) {
				Intent intentResult = new Intent();
        		intentResult.putExtra(Constants.PARAMETERS_SELECT_USERNAME, username);
        		setResult(Constants.RESULT_CODE_OK, intentResult);
        		goBack();
			} else {
				showInfoMessage(getString(R.string.unable_sign_accept_class), false);
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
    	 * List class 
    	 */
    	private List <String> listUsers;
    	
    	/**
    	 * Default constructor
    	 * @param listUsers the list users
    	 */
    	public DevicesListAdapter (List<String> listUsers){
    		this.listUsers = listUsers;
    	}
    	
        /**listClass
         * How many items are in the data set represented by this Adapter.
         * 
         * @return count of items
         */
        public int getCount() {
            if (listUsers != null)
                return listUsers.size();
             
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
            UserChargedListItemView result;
            if (convertView != null) {
                result = (UserChargedListItemView) convertView;
            } else {
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                result = (UserChargedListItemView) layoutInflater.inflate(R.layout.listitem_username_charged, parent, false);
            }
            
            int row = position;
            if (listUsers != null){
            	String users = listUsers.get(row);
                if (users != null){
                    result.setContent(users);
                }
            }
            
            return result;
        }

		/**
		 * @param listClassDB the listClass to set
		 */
		public void setListDevices(List<String> listUsersDB) {
			this.listUsers = listUsersDB;
		}
        
    }

}