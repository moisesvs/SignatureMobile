package com.signaturemobile.signaturemobile.ui;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

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
import com.signaturemobile.signaturemobile.model.ClassDB;
import com.signaturemobile.signaturemobile.model.JoinAsignatureWithClassDB;
import com.signaturemobile.signaturemobile.model.JoinAsignatureWithUserDB;
import com.signaturemobile.signaturemobile.model.JoinClassWithUserDB;

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
     * List user button
     */
    private Button listUserButton;
    
    /**
     * File user button
     */
    private Button fileUserButton;
    
    /**
     * Asignature text view
     */
    private TextView asignatureTextView;
    
    /**
     * Selected asignature
     */
    private AsignatureDB selectedAsignature;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_signclass_home, getString(R.string.title_sign_class_home_mobile), "", 0);
        
        createClassButton = (Button) findViewById(R.id.createClassButton);
        selectClassButton = (Button) findViewById(R.id.selectClassButton);
        createUserButton = (Button) findViewById(R.id.createUserButton);
        listUserButton = (Button) findViewById(R.id.listUserButton);
        fileUserButton = (Button) findViewById(R.id.fileUserButton);
        asignatureTextView = (TextView) findViewById(R.id.asignatureTextView);
        
        // ui listener
        createClassButton.setOnClickListener(this);
        selectClassButton.setOnClickListener(this);
        createUserButton.setOnClickListener(this);
        listUserButton.setOnClickListener(this);
        fileUserButton.setOnClickListener(this);

        // get asignature
        selectedAsignature = toolbox.getSession().getSelectAsignature();
        if (selectedAsignature != null) {
        	asignatureTextView.setText(selectedAsignature.getNameAsignature());
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
		} else if (v == listUserButton){
            Intent intentListUserAsignature = new Intent(SignClassHomeActivity.this, ListUsersAsignatureActivity.class);
            SignClassHomeActivity.this.startActivity(intentListUserAsignature);    
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
		
    	Charset charset = Charset.forName(Constants.ENCODING_TEXT);
    	boolean result = false;
    	
        File rootPath = android.os.Environment.getExternalStorageDirectory(); 
        File dir = new File (rootPath.getAbsolutePath() + "/" + Constants.NAME_FOLDER_APPLICATION);
        
        // intent create directory
        dir.mkdirs();
    	
		try {
			// use FileWriter constructor that specifies open for appending
			CsvWriter csvOutput = new CsvWriter((dir + "/" + Constants.NAME_FILE_OUTPUT_CSV), Constants.SEPARATOR_FILE_CSV, charset);
			
			List<JoinAsignatureWithClassDB> listJoinAsignatureWithClass = toolbox.getDaoJoinAsignatureWithClass().listJoinAsignatureWithClass(selectedAsignature.getIdAsignature());
			if (listJoinAsignatureWithClass != null){
				// write first line
				csvOutput.write(selectedAsignature.getNameAsignature());
				for (JoinAsignatureWithClassDB join : listJoinAsignatureWithClass){
					int idClass = join.getIdClass();
					ClassDB classDb = toolbox.getDaoClassSQL().searchClassFromIdClass(idClass);
					if (classDb != null) {
						csvOutput.write(classDb.getNameClass());
					}
				}
				csvOutput.endRecord();
				
				// get list users
				List <JoinAsignatureWithUserDB> listJoinAsignatureWithUser = toolbox.getDaoJoinAsignatureWithUser().listJoinAsignatureWithUser(selectedAsignature.getIdAsignature());
				for (JoinAsignatureWithUserDB joinUser : listJoinAsignatureWithUser){
					int idUser = joinUser.getIdUser();
					csvOutput.write(joinUser.getUserName());
					for (JoinAsignatureWithClassDB join : listJoinAsignatureWithClass){
						int idClass = join.getIdClass();
						JoinClassWithUserDB joinClassWithUser = toolbox.getDaoJoinClassWithUser().searchClassFromIdClassAndIdUser(idClass, idUser);
						if (joinClassWithUser != null) {
							csvOutput.write("1");
						} else {
							csvOutput.write("0");
						}
					}
					csvOutput.endRecord();
				}
			}
			
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
	        
			File file = new File(dir, Constants.NAME_FILE_OUTPUT_CSV);
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
			startActivity(Intent.createChooser(emailIntent, getString(R.string.select_application_send_file)));
			result = true;
			
		} catch (Throwable t) {
			// nothing
			result = false;
		}
		
		return result;
	}

}