package re.usto.dto.object;

import java.io.File;

import re.usto.dto.helper.StringUtils;

public class PathParser {
	
	public static String separator(String name) {
		if ( StringUtils.isEmpty(name) ) return String.valueOf( SEPARATOR_DEFAULT );
		if ( !StringUtils.containsAnyIgnoreCase(name, SEPARATORS) ) return File.separator;
		
		char separator = ( containsDefaultSeparator(name) ) 
				? SEPARATOR_DEFAULT : SEPARATOR_ANOTHER;
		return String.valueOf( separator );
	}
	
	public static boolean containsDefaultSeparator(String name) {
		if ( StringUtils.isEmpty( name ) ) return false;
		return StringUtils.contains( name, String.valueOf(SEPARATOR_DEFAULT) );
	}
	
	public static boolean containsAnySeparator(String name) {
		if ( StringUtils.isEmpty( name ) ) return false;
		return ( StringUtils.containsAnyIgnoreCase(name, SEPARATORS) );
	}
	
	public static boolean isRoot(String basedir) {
		if ( StringUtils.isEmpty(basedir) ) return true;
		if ( basedir.length() != 1 ) return false;
		
		for (char shar : SEPARATORS) {
			if ( basedir.charAt(0) == shar ) return true;
		}
		return false;
	}
	
	private static final char 	SEPARATOR_DEFAULT = '/';
	private static final char 	SEPARATOR_ANOTHER = '\\';
	private static final char[] SEPARATORS = new char[] { SEPARATOR_DEFAULT, SEPARATOR_ANOTHER};
	
}