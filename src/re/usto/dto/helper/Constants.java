package re.usto.dto.helper;



public class Constants
{
	public static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds
	
	//public static String SERVER_URL = "http://50.112.199.157:8080";
	public static String SERVER_URL = "http://u1.usto.re:8080";
	
	public static final String APP_SHARED_PREFS = "re.usto.preferences";
	
	public static final String PREFERENCE_LOGIN = "login";
	public static final String PREFERENCE_PASS = "password";
	public static final String PREFERENCE_URL = "url";
	
	public static final String PARAM_LOGIN_USER = "user";
	public static final String PARAM_LOGIN_PASS = "pass";
	public static final String LOGIN_MESSAGE_EMPTY = "Verify if exists any empty field before click sign in.";
	public static final String LOGIN_MESSAGE_FAIL = "Sorry!! Incorrect Username or Password.";
	
	public static final String DIALOG_LOGIN_TITLE = "Sign in...";

	public static final String PARAM_PEER_ID = "peerId";

	public static final String DIALOG_LISTING_FILES_TITTLE = "Requesting files...";

	public static final String LOG_TAG = "TAG";

	public static final String REQUEST_TIMEOUT_EXCEPTION = "Your request timed out. Try again.";
	
	
	public static final String LABEL_LOGIN = "login";
	public static final String LABEL_PASSWORD = "password";
	public static final String REQUEST_DIALOG_MESSAGE = "Progress...";

	public static final String FILENAME_POINT_EXTENSION_STRING = ".";
	
	public static final String FILE_EXTENSIONS_IMAGE = "jpg,jpeg,png,bmp,gif,tif,thm";
	public static final String FILE_EXTENSIONS_ZIPFILE = "jar,zip,rar,7z,tar,tar.gz,gz,rpm,pkg,sit,six,deb,zipx";
	public static final String FILE_EXTENSIONS_PDF = "pdf";
	public static final String FILE_EXTENSIONS_MUSIC = "mp3,aif,iff,m3u,m4a,mid,mpa,ra,wav,wma";
	public static final String FILE_EXTENSIONS_VIDEO = "3g2,3gp,asf,asx,avi,flv,mov,mp4,mpg,rm,swf,vob,wmv";
	public static final String FILE_EXTENSIONS_FILE = "exe,bin,bat,ini,txt";
	
	public static final String SERVER_MESSAGE_FAIL = "Server is unavailable. Try again later.";
	
	//IP_DO_HOST:8080/p2pWS/search/[PEGA ISSO DO TEXTEDIT E TIRA OS COLCHETES]/_FILE_SEPARATOR_/[PEGA O authCode E TIRA OS COLCHETES]
	public static final String REQUEST_SEARCH_FILE_SEPARATOR = "_FILE_SEPARATOR_";
	public static final String REQUEST_SEARCH_URL = "/p2pWS/search/";//GET
	public static final String REQUEST_LOGIN_URL = "/p2pWS/login";
	public static final String REQUEST_LOGOUT_URL = "/p2pWS/login/authentication/logout";//{authcode}
	public static final String REQUEST_LISTFILES_URL = "/p2pWS/login/listFiles";
	public static final String REQUEST_DELETEFILE_URL = "/p2pWS/login/deleteFile/";//{fileId}
	public static final String REQUEST_DOWNLOADFILE_URL = "/p2pWS/login/restoreFile/";//{fileName}/{authcode}
	public static final String REQUEST_UPLOADFILE_URL = "/p2pWS/login/backup/";//{file}/{login}/{authCode}/{length}
	public static final String REQUEST_SHAREFILE_URL = "/p2pWS/login/shareFile/";//POST fileId, userLogin, and authCode

	public static final int REQUEST_CODE_FILE_UPLOAD_DIALOG = 120;
	public static final int REQUEST_CODE_FILE_ADD_FOLDER_DIALOG = 102;
	public static final int REQUEST_CODE_SHARE_FILE_DIALOG = 103;
	
	public static final String PARAM_AUTHCODE = "authCode";
	public static final String PARAM_AUTHCODE2 = "authenticationCode";
	public static final String PARAM_AUTHCODE3 = "authenticationcode";
	public static final String PARAM_FILEID = "fileId";
	public static final String PARAM_FILENAME = "fileName";
	public static final String PARAM_FILE = "file";
	public static final String PARAM_LOGIN = "login";
	public static final String PARAM_LENGTH = "length";
	public static final String PARAM_SEARCH = "query";//GET
	public static final String PARAM_UPLOAD_PATH = "path";
	public static final String PARAM_LOGIN_SHARE = "userLogin";

	public static final String DIALOG_REQUEST_UPLOAD_TITLE = "Uploading file...";

	public static final String SERVER_SEARCH_FOR_ERROR = "error";
	

	public static final String DIALOG_DOWNLOAD_TITLE = "Download";
	public static final String DIALOG_DOWNLOAD_MESSAGE = "Do you want download this file?";
	public static final String DIALOG_DOWNLOAD_MESSAGE_COMPLETE = "The download was completed.";
	public static final String DIALOG_DOWNLOAD_TITLE_EXECUTING = "Downloading";
	public static final String DIALOG_DOWNLOAD_BUTTON_OK = "ok";
	public static final String DIALOG_DOWNLOAD_BUTTON_CANCEL = "cancel";
	

	public static final CharSequence DIALOG_ERROR_UPLOAD_TITLE = "Error";
	public static final CharSequence DIALOG_ERROR_BUTTON_OK = "ok";
	public static final CharSequence DIALOG_ERROR_UPLOAD_MESSAGE = "Occurred an error when the application tried to upload the file.\r\n";

	public static final String DIALOG_DELETE_TITLE = "Delete";
	public static final String DIALOG_DELETE_MESSAGE = "Do you really want delete this file?";
	public static final String DIALOG_DELETE_BUTTON_OK = "ok";
	public static final String DIALOG_DELETE_BUTTON_CANCEL = "cancel";


	public static final String DIALOG_LOGOUT_TITLE = "Logout";
	public static final CharSequence DIALOG_LOGOUT_MESSAGE = "Do you want logout the system?";
	public static final CharSequence DIALOG_LOGOUT_BUTTON_OK = "yes";
	public static final CharSequence DIALOG_LOGOUT_BUTTON_CANCEL = "no";
	
	
	public static final String DIALOG_SEARCH_TITLE = "Search";
	public static final String DIALOG_SEARCH_NO_RESULTS = "No results from search";
	public static final String DIALOG_SEARCH_BUTTON_OK = "ok";
	
	public static final String DIALOG_SHARE_TITLE= "Share";
	public static final String DIALOG_SHARE_SELECT_A_FILE_PLEASE= "Please, select file(s) to share";
	public static final String DIALOG_SHARE_SELECT_A_LOGIN = "Please, insert a login";
	public static final String DIALOG_SHARE_SHARE_SUC = "File(s) shared successfully!";
	public static final String DIALOG_SHARE_BUTTON_OK = "ok";

	public static final CharSequence FILE_DIALOG_FILE_DO_NOT_EXISTS = "Does not exist.";

	public static final String FOLDER_SLASH = "/";
	public static final String FOLDER_ROOT = "/";

	public static final String ORDER_REFRESH_LIST = "refreshList";
	
	public static final int FILE_NAME_LENGTH_OF_SIZE_MORE_DATE = 450;
	
	public static final String TASK_DOWNLOAD_COMPLETE_RESULT = "download complete";
	
	public static final long DOUBLE_CLICK_INTERVAL = 250;

	public static final String PARAM_FILEPATH = "path";

	public static final String ORDER_SET_FILE_LIST = "set list";
	
	public static final String EMPTY_SEARCH = "empty search";




	
}
