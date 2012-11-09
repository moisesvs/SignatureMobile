package com.signaturemobile.signaturemobile.nfc;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.util.Log;

/**
 * NFCIntent intent defined the characteristics
 * 
 * @author <a href="mailto:moisesvs@gmail.com">Moisés Vázquez Sánchez</a>
 */
public class NFCIntent {
	
	/**
	 * List read
	 */
    private List<String> read;
    
    /**
     * Ndef formateable
     */
    private boolean ndefFormatable;
    
    /**
     * Flag ndef
     */
    private boolean ndef;

    /**
     * Tag from intent
     */
    private Tag tagFromIntent;

    /**
     * Create a new NFCIntent based on the Intent sent to the app by the NFC broadcast.
     *
     * @param intent source intent
     */
    public NFCIntent(Intent intent) {
        tagFromIntent = intent.getParcelableExtra( NfcAdapter.EXTRA_TAG );
        for(String tech : tagFromIntent.getTechList()) {
            if(tech.equals(Ndef.class.getName())) {
                Log.d( "NFCIntent", "Ndef tech discovered");
                ndef = true;
            } else if(tech.equals(NdefFormatable.class.getName())) {
                Log.d("NFCIntent", "NdefFormatable tech discovered");
                ndefFormatable = true;
            }
        }
    }

    /**
     * Try to read plain text information from the Tag. Upon successful read the getRead() method can be invoked to
     * retrieve a list of Strings with the Ndef records contents.
     *
     * @throws FormatException on tag format errors
     * @throws IOException on errors reading from the tag
     */
    public void read() throws FormatException, IOException {
        if(ndef) {
            readNdefTag();
        }
    }

    /**
     * Get the plain text read from the tag.
     *
     * @return List of Strings or null if no text was read
     */
    public List<String> getRead() {
        return read;
    }

    /**
     * Read ndef tag
     * @throws IOException throws IOException
     * @throws FormatException throws FormatException
     */
    private void readNdefTag() throws IOException, FormatException {
        final Ndef ndef = Ndef.get( tagFromIntent );
        
        try {
            ndef.connect();
            NdefMessage msg = ndef.getNdefMessage();
            NdefRecord[] records = msg.getRecords();
            if(records != null) {
                read = new Vector<String>( records.length );
                for(final NdefRecord record : records){
                    String text = getText( record.getPayload() );
                    if(text != null){
                        read.add( text );
                    }
                }
            } else {
                Log.d( "NFCIntent", "Tag is empty" );
            }
        } finally {
            try{
            	ndef.close();
            } catch (Exception ignored){
        		// Error
            }
        }
    }

    /**
     * the First Byte of the payload contains the "Status Byte Encodings" field,
     * per the NFC Forum "Text Record Type Definition" section 3.2.1.
     *
     * Bit_7 is the Text Encoding Field.
     * * if Bit_7 == 0 the the text is encoded in UTF-8
     * * else if Bit_7 == 1 then the text is encoded in UTF16
     * Bit_6 is currently always 0 (reserved for future use)
     * Bits 5 to 0 are the length of the IANA language code.
     *
     * Based on code from O'Reilly book "Programming Android":
     * http://ofps.oreilly.com/titles/9781449389697/ch18_id300804.html
     */
    private String getText(final byte[] payload) {
        if(payload != null) {
            try {
                String textEncoding = ((payload[0] & 0x80 ) == 0) ? "UTF-8" : "UTF-16";
                int languageCodeLength = payload[0] & 0x3f;
                return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
            } catch (Exception e) {
                Log.e( "NFCIntent", "Error getting text from payload", e );
            }
        }
        return null;
    }
    
    /**
     * Write some text to the tag.
     *
     * @param text text to write
     * @param locale locale the text uses
     * @param encodeInUtf8 if true use UTF-8 else use UTF-16
     * @throws IOException on error writing to the tag
     * @throws FormatException on error with tag format
     */
    public void writePlain(final String text, final Locale locale, boolean encodeInUtf8) throws IOException, FormatException {
        
    	NdefRecord[] records = {newTextRecord(text, locale, encodeInUtf8)};
        NdefMessage message = new NdefMessage(records);

        if (ndef) {
        	Ndef ndef = Ndef.get(tagFromIntent);
            try {
                ndef.connect();
                ndef.writeNdefMessage(message);
            } finally {
                try {
                	ndef.close();
                } catch(Exception ignored){
            		// Error
                }
            }
        } else if (ndefFormatable) {
            NdefFormatable ndefFormatable = NdefFormatable.get( tagFromIntent );
            try {
                ndefFormatable.connect();
                ndefFormatable.format( message );
            } finally {
                try {
            		ndefFormatable.close();
            	} catch (Exception ignored){
            		// Error
            	}
            }
        }
    }

    /**
     * From the Android developer reference.
     * @param text text record
     * @param locale locale 
     * @param encodeInUtf8 enconded itn utf 8
     */
    public static NdefRecord newTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes( Charset.forName( "US-ASCII" ));

        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }
    
}
