package re.usto.dto.helper;

import static android.content.Context.TELEPHONY_SERVICE;
import android.content.Context;
import android.telephony.TelephonyManager;

public class Device {
	
	private Context context;
	
	public Device(Context context) {
		this.context = context;
	}
	
	private static final String colon = ":";
	public String getGeneralInfo() {
		String phone_info = ( hasPhoneNumber() ) ? phone_number() : sim_serial_number();
		return sdk_version() + colon + imei() + colon + phone_info;
	}
	
	private String imei() {
		return manager().getDeviceId();
	}
	
	private String sim_serial_number() {
		return manager().getSimSerialNumber();
	}
	
	private String phone_number() {
		return manager().getLine1Number();
	}	
	
	private TelephonyManager manager() {
		return ((TelephonyManager) this.context.getSystemService(TELEPHONY_SERVICE));
	}

	private String sdk_version() {
		return String.valueOf( android.os.Build.VERSION.SDK_INT );
	}
	
	private boolean hasPhoneNumber() {
		String number = phone_number();
		return !( number == null || number.isEmpty() );
	}
	
}