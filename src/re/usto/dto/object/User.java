package re.usto.dto.object;

import java.io.Serializable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import re.usto.dto.helper.StringUtils;

@Root
public class User implements Serializable {
	
	private static final long serialVersionUID = -1986843667784191063L;
	
	@Element public String id;
	@Element public String login;
	@Element public String authenticationCode;

	public boolean isUnauthorized() {
		if ( StringUtils.isEmpty(authenticationCode) ) return true;
		long auth = Long.valueOf( authenticationCode );
		return ( auth == -1 );
	}	

}