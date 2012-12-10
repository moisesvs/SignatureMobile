package com.signaturemobile.signaturemobile.ui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.csvreader.CsvWriter;
import com.signaturemobile.signaturemobile.Constants;
import com.signaturemobile.signaturemobile.R;
import com.signaturemobile.signaturemobile.io.NotificationCenter.NotificationListener;
import com.signaturemobile.signaturemobile.model.AsignatureDB;

/**
 * SignatureMobileActivity activity main application
 *
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class SignClassHomeActivity extends BaseActivity implements NotificationListener {
	
    /**
     * Create user button
     */
    private Button createClassButton;
    
    /**
     * Signature user button
     */	
    private Button selectClassButton;
	
    /**
     * Create user button
     */
    private Button createUserButton;
    
    /**
     * File user button
     */
    private Button fileUserButton;
    
    /**
     * Asignature text view
     */
    private TextView asignatureTextView;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_signclass_home, getString(R.string.title_sign_class_home_mobile), "", 0);
        
        createClassButton = (Button) findViewById(R.id.createClassButton);
        selectClassButton = (Button) findViewById(R.id.selectClassButton);
        createUserButton = (Button) findViewById(R.id.createUserButton);
        fileUserButton = (Button) findViewById(R.id.fileUserButton);
        asignatureTextView = (TextView) findViewById(R.id.asignatureTextView);
        
        // ui listener
        createClassButton.setOnClickListener(this);
        selectClassButton.setOnClickListener(this);
        createUserButton.setOnClickListener(this);
        fileUserButton.setOnClickListener(this);

        // get asignature
        AsignatureDB asignature = toolbox.getSession().getSelectAsignature();
        if (asignature != null) {
        	asignatureTextView.setText(asignature.getNameAsignature());
        }
    }
    
    @Override
    public void onResume(){
    	super.onResume();

    	// Register notifications
    	registerNotifications();
    	
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	
    	// Unregister notifications
    	unRegisterNotifications();
    }
    
    /**
     * On item click event
     */
	public void onClick(View v) {
		if (v == createClassButton){
            Intent intentCreateUser = new Intent(SignClassHomeActivity.this, CreateClassActivity.class);
            SignClassHomeActivity.this.startActivity(intentCreateUser);    
		} else if (v == selectClassButton){
            Intent intentSignUser = new Intent(SignClassHomeActivity.this, SelectListClassActivity.class);
            SignClassHomeActivity.this.startActivity(intentSignUser);    
		} else if (v == createUserButton){
            Intent intentCreateUser = new Intent(SignClassHomeActivity.this, CreateUserActivity.class);
            SignClassHomeActivity.this.startActivity(intentCreateUser);    
		} else if (v == fileUserButton){
			createFileFromData();
			sendEmail();
		} else {
			super.onClick(v);
		}
	}
    
    /**
     * Create file from data
     */
    private boolean createFileFromData(){
		
    	boolean result = false;
    	
        File rootPath = android.os.Environment.getExternalStorageDirectory(); 
        File dir = new File (rootPath.getAbsolutePath() + "/" + Constants.NAME_FOLDER_APPLICATION);
        
        // intent create directory
        dir.mkdirs();
    	
		// before we open the file check to see if it already exists
//		boolean alreadyExists = new File(dir + "/" + Constants.NAME_FILE_CSV).exists();
			
		try {
			// use FileWriter constructor that specifies open for appending
			CsvWriter csvOutput = new CsvWriter(new FileWriter(dir + "/" + Constants.NAME_FILE_CSV, false), ',');
			
			// if the file didn't already exist then we need to write out the header line
//			if (!alreadyExists) {
				csvOutput.write("id");
				csvOutput.write("name");
				csvOutput.endRecord();
//			}
			// else assume that the file already has the correct header line
			
			// write out a few records
			csvOutput.write("1");
			csvOutput.write("Bruce");
			csvOutput.endRecord();
			
			csvOutput.write("2");
			csvOutput.write("John");
			csvOutput.endRecord();
			
			csvOutput.close();
			result = true;
			
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}
		
		return result;
    }
    
    /**
     * Send mail 
     */
	public boolean sendEmail() {
		
		boolean result = false;
		
		try {
			
	        File rootPath = android.os.Environment.getExternalStorageDirectory(); 
	        File dir = new File (rootPath.getAbsolutePath() + "/" + Constants.NAME_FOLDER_APPLICATION);
	        
			File file = new File(dir, Constants.NAME_FILE_CSV);
			if (!file.exists()) {
				file.mkdirs();
			}
			
			final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

			String subject = getString(R.string.email_subject);
			String emailtext = getString(R.string.email_content);

			emailIntent.setType("plain/text");
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailtext);
			emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file));

			result = true;
			
		} catch (Throwable t) {
			// nothing
			result = false;
		}
		
		return result;
	}

}