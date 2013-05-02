package com.signaturemobile.signaturemobile;


/**
 * Constants provides a set of static values used throughout the application
 *  
 * @author <a href="mailto:moisesvs@gmail.com">Mooisés Vázquez Sánchez</a>
 */
public class Constants {

	/**
	 * Version application
	 */
	public static final String VERSION_APPLICATION = "1.0";
	
	/**
	 * Version data base user application
	 */
	public static final int VERSION_DB_USER = 1;
	
	/**
	 * Version data base asignature application
	 */
	public static final int VERSION_DB_ASIGNATURE = 1;

	/**
	 * Version data base class application
	 */
	public static final int VERSION_DB_CLASS = 1;
	
	/**
	 * Version data base join user with asignature application
	 */
	public static final int VERSION_DB_JOIN_USER_ASIGNATURE = 1;
	
	/**
	 * Version data base join user with class application
	 */
	public static final int VERSION_DB_JOIN_USER_CLASS = 1;
	
	/**
     * Trace flag (disable for production)
     */
    public static final boolean TRACE_ALLOWED = false;

    /**
     * Trace operations flag (disable for production)
     */
    public static final boolean TRACE_OPERATIONS = true;
    
    /**
     * Without advertising
     */
    public static final boolean WITHOUT_ADVERTISING = false;

    /**
     * CEPSA Infrastructure
     */
    public static final int SIGNATURE_MOBILE = 0;
    
    /**
     * Target infrastructure 
     */
    public static int TARGET = SIGNATURE_MOBILE;

    /**
     * Production environment identifier
     */
    public static final int PRODUCTION_ENVIRONMENT = 0;

    /**
     * Pre-production environment identifier
     */
    public static final int PRE_PRODUCTION_ENVIRONMENT = 1;
    
    /**
     * Development environment identifier
     */
    public static final int DEVELOPMENT_ENVIRONMENT = 2;
    
    /**
     * Target environment 
     */
    public static int ENVIRONMENT = DEVELOPMENT_ENVIRONMENT;

    /**
     * Trust all certificates (just for testing, disable for production)
     */
    public static final boolean TRUST_ALL_CERTIFICATES = (ENVIRONMENT != PRODUCTION_ENVIRONMENT);
    
    /**
     * The operations
     */
    public static String[][][] TARGET_OPERATION = new String[][][] {
        new String[][] {
            new String[] {                    
                    "https://api.twitter.com/1/users/profile_image?screen_name=moisesvs&size=bigger",            			// op0

            },
            new String[] {                    
                    "https://api.twitter.com/1/users/profile_image?screen_name=moisesvs&size=bigger",            			// op0
                    
        },
        new String[] {                    
            		"https://api.twitter.com/1/users/profile_image?screen_name=moisesvs&size=bigger",                // op0

            }
        },
        new String[][] {
                new String[] {
                     "https://api.twitter.com/1/users/profile_image?screen_name=moisesvs&size=bigger",                           // op0
                    
                },
                new String[] {                    
                     "https://api.twitter.com/1/users/profile_image?screen_name=moisesvs&size=bigger",                           // op0
                        
                },
                new String[] {                    
                     "https://api.twitter.com/1/users/profile_image?screen_name=moisesvs&size=bigger",                           // op0
                }
            }
    	};
    
   /**
    * Operation codes
    */
   public static final int SEARCH_POI_AROUND_POINT_OPERATION = 0;
   public static final int SEARCH_POI_NEAR_ROUTE_OPERATION = 1;
   

   public static final int DOWNLOAD_RESOURCE_OPERATION = 500;

    /**
     * The application identifier for Spain
     */
    public static final String APPLICATION_IDENTIFIER_ES = "ES";

    /**
     * Default parameter encoding used in communications
     */
	public static final String DEFAULT_PARAMETER_ENCODING = "UTF-8";
    
    /**
     * Notifications codes (Notification Center)
     */
    public static final String kInitializationEnds = "kInitializationEnds";
    public static final String kAllResourcesLoadingEnds = "kAllResourcesLoadingEnds";
    public static final String kConfigurationDataLoadingFailed = "kConfigurationDataLoadingFailed";
    
    /**
     * Notifications dialogs
     */
    public static final String kHideInfoDialog = "kHideInfoDialog";
    public static final String kHideErrorDialog = "kHideErrorDialog";
    
    /**
     * Notifications codes (Notification Bluetooh Center)
     */
    public static final String kRequestStartOkFoundReceived = "kRequestStartOkFoundReceived";
    public static final String kRequestStartFailedFoundReceived = "kRequestStartFailedFoundReceived";
    public static final String kRequestFinishFoundForceOkReceived = "kRequestFinishFoundForceOkReceived";
    public static final String kRequestFinishFoundForceFailedReceived = "kRequestFinishFoundForceFailedReceived";
    public static final String kDeviceFoundReceived = "kDeviceFoundReceived";
    public static final String kFinishRequestDeviceFoundReceived = "kFinishRequestDeviceFoundReceived";

////////////////////////////////////////////////////////////////
//Activity request codes
////////////////////////////////////////////////////////////////
    /**
     * Default tickets user
     */
    public static final String URL_TWITTER_FORMAT_STRING = "https://api.twitter.com/1/users/profile_image?screen_name=%s&size=bigger";
    
    
////////////////////////////////////////////////////////////////
//Activity request codes
////////////////////////////////////////////////////////////////
	/**
	 * Default tickets user
	 */
	public static final String NAME_FOLDER_APPLICATION = "signatureMobile";
	
	/**
	 * Default name file output csv
	 */
	public static final String NAME_FILE_OUTPUT_CSV = "users.csv";
	
	/**
	 * Default name file input csv
	 */
	public static final String NAME_FILE_INPUT_CSV = "input_users.csv";
	
	/**
	 * Separator file CSV
	 */
	public static final char SEPARATOR_FILE_CSV = ';';
	
	/**
	 * Charset file
	 */
	public static final String ENCODING_TEXT = "ISO-8859-1";

	
////////////////////////////////////////////////////////////////
//Activity request codes
////////////////////////////////////////////////////////////////
    /**
     * Default tickets user
     */
    public static final int DEFAULT_TICKETS_USER = 0;
    
    /**
     * Default null values
     */
    public static final int NULL_VALUES = -1;
    
////////////////////////////////////////////////////////////////
//Configuration Bug Sense
////////////////////////////////////////////////////////////////
    
    /**
     * Bug sense api key
     */
    public static final String BUG_SENSE_API_KEY = "77e5f841";
    
////////////////////////////////////////////////////////////////
//Parameters Activity
////////////////////////////////////////////////////////////////
    /**
     * Parameters activity
     */
    public static final String PACKAGE_APPLICATION = "com.signaturemobile.signaturemobile";
    
    /**
     * Parameters activity
     */
    public static final String PARAMETERS_SIGN_USER = "com.signaturemobile.parameter.siguser";
    
    /**
     * Parameters activity
     */
    public static final String PARAMETERS_SELECT_ASIGNATURE = "com.signaturemobile.parameter.selectasignature";
    
    /**
     * Parameters activity
     */
    public static final String PARAMETERS_SELECT_CLASS = "com.signaturemobile.parameter.selectclass";
    
    /**
     * Parameters activity
     */
    public static final String PARAMETERS_SELECT_ACTIVITY = "com.signaturemobile.parameter.selectdevice";
    
    /**
     * Parameters activity
     */
    public static final String PARAMETERS_SELECT_USERNAME = "com.signaturemobile.parameter.selectusername";
    
////////////////////////////////////////////////////////////////
//Result code
////////////////////////////////////////////////////////////////
    /**
     * Parameters activity for result
     */
    public static final int RESULT_CODE_OK = 0;
    
    /**
     * Parameters activity for result
     */
    public static final int RESULT_CODE_KO = 1;
}