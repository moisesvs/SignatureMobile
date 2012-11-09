package com.signaturemobile.signaturemobile.persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Persistence provides an interface to the application to store and load files from internal storage
 * @author <a href="mailto:moisesvs@gmail.com">Mooisés Vázquez Sánchez</a>
 */
public class Persistence {
	
	/**
	 * A reference to the application context
	 */
	private Context context;
	
	/**
	 * Default constructor
	 * @param context reference to the application context
	 */
	public Persistence(Context context) {
		this.context = context;
	}
	
	/**
	 * Load a bitmap from disk
	 * @param filename the file name
	 * @return the bitmap contained in the file, or null if the file does not exist or
	 * it does not contain a bitmap
	 * @throws FileNotFoundException 
	 */
	public Bitmap loadBitmap(String filename) throws FileNotFoundException {
		Bitmap bitmap = null;
		String localFileName = buildLocalFilename(filename);
		FileInputStream in;

		in = context.openFileInput(localFileName);
        
		try {
			bitmap = BitmapFactory.decodeStream(in); 
		} finally {
			try {
                in.close();
            } catch (IOException e) {
            	// empty
            }
		}
		return bitmap;
	}

	/**
	 * Load a text from a text file in disk
	 * @param filename the file name
	 * @return the text contained in the file, or null if the file does not exist or
	 * it does not contain a text
	 * @throws IOException 
	 */
	public String loadTextFile(String filename) throws IOException {
		String text = null;
		String localFileName = buildLocalFilename(filename);
		FileInputStream in;
        in = context.openFileInput(localFileName);
        
		try {
			InputStreamReader reader = new InputStreamReader(in);
			try {
				int bytesRead = 0;
				char[] buffer = new char[1024];
				StringBuffer sb = new StringBuffer();
				do {
					bytesRead = reader.read(buffer);
					if (bytesRead > 0) {
						sb.append(buffer);
					}
				} while (bytesRead > 0);
				text = sb.toString();
			} finally {
				try {
                    reader.close();
                } catch (IOException e) {
                	// empty
                }
			}
		} finally {
			try {
                in.close();
            } catch (IOException e) {
            	// empty
            }
		}
		return text;
    }
	
	/**
	 * Save a bitmap in the disk
	 * @param filename the file name 
	 * @param bitmap the bitmap to store
	 * @return the local file name where the file is actually stored
	 * @throws IOException 
	 */
	public String saveBitmap(String filename, Bitmap bitmap) throws IOException {
		String localFileName = buildLocalFilename(filename);
		// store the new local image file
        FileOutputStream out = context.openFileOutput(localFileName, Context.MODE_PRIVATE);
        try {
        	bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } finally {
        	try {
                out.close();
            } catch (IOException e) {
            	// empty
            }
        }
        return localFileName;
	}
	
	/**
	 * Save a text in a file in the disk
	 * @param filename the file name 
	 * @param bitmap the text to store
	 * @return the local file name where the file is actually stored
	 * @throws IOException 
	 */
	public String saveTextFile(String filename, String content) throws IOException {
		String localFileName = buildLocalFilename(filename); 
		// store the new local image file
        FileOutputStream out = context.openFileOutput(localFileName, Context.MODE_PRIVATE);
        try {
        	OutputStreamWriter writer = new OutputStreamWriter(out);
            try {
            	writer.write(content);
            } finally {
                try {
                    writer.close();
                } catch (IOException e) {
                	// empty
                }
            }
        } finally {
            try {
                out.close();
            } catch (IOException e) {
            	// empty
            }
        }

        return localFileName;
	}
	
	/**
	 * Save a raw content in a file
	 * @param filename the file name where the raw content is stored
	 * @param in the input stream where the content is read
	 * @throws IOException 
	 */
	public void saveRawContent(String filename, InputStream in) throws IOException {
		String localFileName = buildLocalFilename(filename); 
		// store the new local image file
        FileOutputStream out = context.openFileOutput(localFileName, Context.MODE_PRIVATE);
        try {            	
        	byte[] buffer = new byte[1024];
        	int bytesRead = 0;                
        	do {
        		bytesRead = in.read(buffer);
        		if (bytesRead > 0) {
        			out.write(buffer, 0, bytesRead);
        		}
        	} while (bytesRead > 0);                
        } finally {
            try {
                out.close();
            } catch (IOException e) {
            	// empty
            }
        }
	}
	
	/**
	 * Build a filename to store from the original file name
	 * @param filename the original file name
	 * @return a local file name
	 */
	private String buildLocalFilename(String filename) {		
		return filename;
	}
	
	/**
	 * Delete a file from disk
	 * @param filename the file name to delete
	 */
	public void deleteFile(String filename) {
		String name = buildLocalFilename(filename);
		this.context.deleteFile(name);
	}
}
