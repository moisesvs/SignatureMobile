package com.signaturemobile.signaturemobile.ui;

import java.util.ArrayList;
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
import com.signaturemobile.signaturemobile.model.ClassDB;
import com.signaturemobile.signaturemobile.model.JoinAsignatureWithClassDB;
import com.signaturemobile.signaturemobile.ui.listitems.ClassDBListItemView;

/**
 * ListClassActivity activity list class signature application
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class SelectListClassActivity extends BaseActivity implements NotificationListener, OnItemClickListener {
    
    /**
     * Device text view
     */
    private TextView classTextView;
    
    /**
     * Devices list view
     */
    private ListView classListView;
    
    /**
     * Adapter devices
     */
    private DevicesListAdapter deviceAdapter;
    
    /**
     * List asignature
     */
    private List<ClassDB> listClass;

    /**
     * Select asignagure
     */
    private AsignatureDB selectAsignature;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_listclass, getString(R.string.title_list_class_title), "", 0);
        initializeUI();
    }
    
    /**
     * Initialize UI
     */
    private void initializeUI(){
        classTextView = (TextView) findViewById(R.id.classTextView);
        classListView = (ListView) findViewById(R.id.classListView);
        
        listClass = new ArrayList<ClassDB>();
        deviceAdapter = new DevicesListAdapter(null);
        classListView.setAdapter(deviceAdapter);
        classListView.setOnItemClickListener(this);
        
        selectAsignature = toolbox.getSession().getSelectAsignature();
    }
    
    /**
     * Called when the activity on resume
     */
    public void onResume(){
    	super.onResume();
    	// list bluetooth devices
    	List<JoinAsignatureWithClassDB> listAsignatureWithJoin = toolbox.getDaoJoinAsignatureWithClass().listJoinAsignatureWithClass(selectAsignature.getIdAsignature());
    	if ((listAsignatureWithJoin != null) && (! (listAsignatureWithJoin.isEmpty()))) {
    		listClass.clear();
    		for (JoinAsignatureWithClassDB joinAsignatureWithClass : listAsignatureWithJoin) {
    			ClassDB nameClass = toolbox.getDaoClassSQL().searchClassFromIdClass(joinAsignatureWithClass.getIdClass());
    			if (nameClass != null) {
    	        	listClass.add(nameClass);
    			}
    		}
    	}
    	
    	if ((listClass != null) && (! (listClass.isEmpty()))){
            deviceAdapter.setListDevices(listClass);
            deviceAdapter.notifyDataSetChanged();
        	classTextView.setText(getString(R.string.list_devices_class));
    	} else {
            deviceAdapter.setListDevices(listClass);
            deviceAdapter.notifyDataSetChanged();
        	classTextView.setText(getString(R.string.list_devices_class_empty));
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
		if (position <= classListView.getCount() && (view instanceof ClassDBListItemView)) {
			ClassDBListItemView deviceListView = (ClassDBListItemView) view;
			ClassDB classDBDevice = deviceListView.getClassDB();
			if (classDBDevice != null) {
				
				// set asignature
				toolbox.getSession().setSelectClass(classDBDevice);
				
				Intent intentSignAcceptUser = new Intent(SelectListClassActivity.this, SignUserHomeActivity.class);
				intentSignAcceptUser.putExtra(Constants.PARAMETERS_SELECT_CLASS, classDBDevice);
				startActivity(intentSignAcceptUser);
				
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
    	private List <ClassDB> listClass;
    	
    	/**
    	 * Default constructor
    	 * @param listClass the list class
    	 */
    	public DevicesListAdapter (List<ClassDB> listClass){
    		this.listClass = listClass;
    	}
    	
        /**listClass
         * How many items are in the data set represented by this Adapter.
         * 
         * @return count of items
         */
        public int getCount() {
            if (listClass != null)
                return listClass.size();
             
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
            ClassDBListItemView result;
            if (convertView != null) {
                result = (ClassDBListItemView) convertView;
            } else {
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                result = (ClassDBListItemView) layoutInflater.inflate(R.layout.listitem_class, parent, false);
            }
            
            int row = position;
            if (listClass != null){
            	ClassDB classDB = listClass.get(row);
                if (classDB != null){
                    result.setContent(classDB, false);
                }
            }
            
            return result;
        }

		/**
		 * @param listClassDB the listClass to set
		 */
		public void setListDevices(List<ClassDB> listClassDB) {
			this.listClass = listClassDB;
		}
        
    }

}