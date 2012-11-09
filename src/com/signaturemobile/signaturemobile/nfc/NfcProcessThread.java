package com.signaturemobile.signaturemobile.nfc;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.os.Vibrator;

/**
 * SignatureMobileActivity activity create user application
 * 
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class NfcProcessThread extends Thread {
	
	/**
	 * Time vibrate when appears device nfc
	 */
	private int TIME_VIBRATE_DISCOVERED_NFC = 300;
	
	/**
	 * Context activity
	 */
	private Context contextActivity;
	
	/**
	 * Intent received from activity
	 */
    private Intent intent;
    
    /**
     * Text to NFC
     */
    private String textToNFC;

    /**
     * Default constructor NFC Thread
     * @param intent received activity
     */
    public NfcProcessThread(Context contextActivity, Intent intent, String textToNFC) {
    	this.contextActivity = contextActivity;
        this.intent = intent;
        this.textToNFC = textToNFC;
    }

    /**
     * Execute the thread nfc process
     */
    public void run() {
    	
        Looper.prepare();
        final NFCIntent nfcIntent = new NFCIntent(intent);
        
        try {
        	nfcIntent.writePlain(textToNFC, new Locale("es"), true);
        	vibrate(300);
        } catch(Exception e) {
            vibrate(TIME_VIBRATE_DISCOVERED_NFC);
        }
    }
    
    public void vibrate(int miliseconds)  {    	
    	Vibrator v = (Vibrator) contextActivity.getSystemService(Context.VIBRATOR_SERVICE);            	 
    	v.vibrate(miliseconds);
    }
}
