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
import android.widget.RelativeLayout;

/**
 * Basic list item for the applications. It contains a background image view and a selected background image view
 * to set the cell backgrounds, and an accesory image view. Subclasses must create the three images view
 * 
 * @author <a href="mailto:info@movilok.com">Movilok Interactividad Movil S.L.</a>
 */
public class SignatureListItemView extends RelativeLayout {

	public SignatureListItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public SignatureListItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SignatureListItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
}