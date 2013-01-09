/*
 * Copyright (c) 2011 CEPSA. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * CEPSA ("Confidential Information").  You shall not disclose such 
 * Confidential Information and shall use it only in accordance with 
 * the terms of the license agreement you entered into with CEPSA.
 */
package com.signaturemobile.signaturemobile.ui.listitems;

import android.content.Context;
import android.util.AttributeSet;
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
public class UserChargedListItemView extends SignatureListItemView {
    
    /**
     * Id device text view
     */
    private TextView usernameTextView;
    
    /**
     * Dao join asignature with user db
     */
    private String username;

	/**
     * Default constructor
     * @param context the context application
     * @param attrs attributes list item device
     * @param defStyle define style
     */
    public UserChargedListItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

    /**
     * Default constructor
     * @param context the context application
     * @param attrs attributes list item device
     */
	public UserChargedListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

    /**
     * Default constructor
     * @param context the context application
     */
	public UserChargedListItemView(Context context) {
		super(context);
	}

	/**
	 * Terminated inflate
	 */
	@Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        usernameTextView = (TextView) findViewById(R.id.usernameTextView);
    }
    
    /**
     * Sets user name in view
     * 
     * @param device The device item associate
     * @param checked If the device is checked or not
     */
    public void setContent(String user) {
    	this.username = user;
    	if (username != null) {
    		usernameTextView.setText(username);
    	}
    }
    
    /**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
}
