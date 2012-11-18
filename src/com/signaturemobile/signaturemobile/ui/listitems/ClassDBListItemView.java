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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.signaturemobile.signaturemobile.R;
import com.signaturemobile.signaturemobile.model.ClassDB;

/**
 * List item to display an action. It contains an action image, an action text, and an action indicator, wich can be
 * a right arrow when the action is inmediate, or an up/down arrow when sub-actions can be displayed. The action level
 * is also displayed as an image and text indent. Second level actions should be below a first level with an up/down
 * arrow
 * 
 * @author <a href="mailto:info@movilok.com">Movilok Interactividad Movil S.L.</a>
 */
public class ClassDBListItemView extends SignatureListItemView {
    
    /**
     * Id class text view
     */
    private TextView nameClassTextView;

    /**
     * Telephone text view
     */
    private ImageView checkImageView;
    
    /**
     * Class db
     */
    private ClassDB classDB;
    
    /**
     * Default constructor
     * @param context the context application
     * @param attrs attributes list item device
     * @param defStyle
     */
    public ClassDBListItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ClassDBListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ClassDBListItemView(Context context) {
		super(context);
	}

	@Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        nameClassTextView = (TextView) findViewById(R.id.idClassTextView);
        checkImageView = (ImageView) findViewById(R.id.checkImageView);
    }
    
    /**
     * Sets telephone in view
     * 
     * @param device The device item associate
     * @param checked If the device is checked or not
     */
    public void setContent(ClassDB classDB, boolean checked) {
    	this.classDB = classDB;
    	if (classDB != null){
    		String nameClass = classDB.getNameClass();
        	if (nameClass != null)
        		nameClassTextView.setText(nameClass);
        	
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
	 * @return the classDB
	 */
	public ClassDB getClassDB() {
		return classDB;
	}

	/**
	 * @param classDB the classDB to set
	 */
	public void setClassDB(ClassDB classDB) {
		this.classDB = classDB;
	}
}
