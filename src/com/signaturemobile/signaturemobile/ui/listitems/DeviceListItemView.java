/*
 * Copyright (c) 2011 CEPSA. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * CEPSA ("Confidential Information").  You shall not disclose such 
 * Confidential Information and shall use it only in accordance with 
 * the terms of the license agreement you entered into with CEPSA.
 */
package com.signaturemobile.signaturemobile.ui.listitems;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.signaturemobile.signaturemobile.R;

/**
 * List item to display an action. It contains an action image, an action text, and an action indicator, wich can be
 * a right arrow when the action is inmediate, or an up/down arrow when sub-actions can be displayed. The action level
 * is also displayed as an image and text indent. Second level actions should be below a first level with an up/down
 * arrow
 * 
 * @author <a href="mailto:info@movilok.com">Movilok Interactividad Movil S.L.</a>
 */
public class DeviceListItemView extends SignatureListItemView {
    
    /**
     * Id device text view
     */
    private TextView idDeviceTextView;

    /**
     * Telephone text view
     */
    private ImageView checkImageView;
    
    /**
     * Device item
     */
    private BluetoothDevice device;
    
    /**
     * Default constructor
     * @param context the context application
     * @param attrs attributes list item device
     * @param defStyle
     */
    public DeviceListItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public DeviceListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DeviceListItemView(Context context) {
		super(context);
	}

	@Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        idDeviceTextView = (TextView) findViewById(R.id.idDeviceTextView);
        checkImageView = (ImageView) findViewById(R.id.checkImageView);
    }
    
    /**
     * Sets telephone in view
     * 
     * @param device The device item associate
     * @param checked If the device is checked or not
     */
    public void setContent(BluetoothDevice device, boolean checked) {
    	if (device != null){
    		this.device = device;
    		String idDevice = device.getName();
        	if (idDevice != null)
        		idDeviceTextView.setText(idDevice);
        	
        	setChecked(checked);
    	}

    }
    
    public void setChecked (boolean checked){
    	if (checked)
    		checkImageView.setVisibility(View.VISIBLE);
    	else
    		checkImageView.setVisibility(View.GONE);
    }

    /**
	 * @return the device
	 */
	public BluetoothDevice getDevice() {
		return device;
	}

	/**
	 * @param device the device to set
	 */
	public void setDevice(BluetoothDevice device) {
		this.device = device;
	}
}
