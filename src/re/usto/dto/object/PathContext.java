package re.usto.dto.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import re.usto.dto.helper.Message;
import re.usto.dto.helper.StringUtils;

@Root
public class PathContext {
	
	@ElementList(entry="fileList", name="fileList", inline=true) public List<Path> paths;
	
	public PathContext() {}
	public PathContext(List<Path> paths) {
		this.paths = paths;
	}

	public boolean containsFile(String fileName) {
		if (this.paths != null && this.paths.size() > 0)
			for (Path path : this.paths) {
				if ( path.getName().equals(fileName) )
					return true;
			}
		return false;
	}
	
	public Map<String, List<Path>> getMap() {
		
		Map<String, List<Path>> map = 
			new HashMap<String, List<Path>>();
		
		for (Path filedata : this.paths) {
			String key = filedata.getDirectoryName();
			List<Path> files = map.get( key );
			
			files = ( files == null ) ? new ArrayList<Path>() : files;
			files.add( filedata );
			map.put(key, files );
		}
		
		return map;
	}
	
	public List<Path> getView(String baseDir) {
		List<Path> list  = new ArrayList<Path>();
		if ( this.paths == null || this.paths.isEmpty() ) 
			return list;
		
		String separator = PathParser.separator( baseDir );
		Map<String, List<Path>> map = this.getMap();
		for ( Entry<String, List<Path>> entry : map.entrySet() ) {
			if ( entry.getKey().contains(separator) )
				addIntermediateDirectories(entry, list, baseDir);
		}

		listFiles(baseDir, list, separator, map);
		listFiles("", list, separator, map);
		return list;
	}
	
	private void listFiles(String baseDir, List<Path> list, String separator, Map<String, List<Path>> map) {
		List<Path> files = map.get(baseDir);
		if ( files == null ) return;
		for (Path path : files) {
			if ( path.getName().equals(baseDir) ) continue;
			if ( path.getName().equals( baseDir + separator) ) continue;
			
			char shar = path.getName().charAt( path.getName().length() -1 );
			if ( shar == '\\' || shar == '/' ) continue;
			
			list.add( path );
		}
		
		boolean notContainsSeparator = !PathParser.containsAnySeparator(baseDir);
		boolean notEqualAnySeparator = !baseDir.equals( PathParser.separator(baseDir) ); 
		if ( notContainsSeparator || notEqualAnySeparator )
			return;
			
		files = map.get( "" ); // apenas quando na raiz. motivo: arquivos sem o barra no come?o
		if ( files == null ) return; 
		for (Path path : files) {
			if ( path.getName().equals( baseDir + separator) ) continue;
			list.add( path );	
		}
	}
	
	private void addIntermediateDirectories(Entry<String, List<Path>> entry, List<Path> list, String baseDir) {
		
		if ( !entry.getKey().contains( baseDir ) ) return;
		if ( entry.getKey().equals( baseDir) ) return;
		
		String separator = PathParser.separator(baseDir);
		String[] term = StringUtils.split(entry.getKey(), separator);
		if ( term.length == 0 ) return;
		
		String dir = null;
		if ( baseDir.equals(separator) ) {
			if ( already(list, term[0] + separator ) ) return;
			dir = term[0] + separator;
		} else {
			String word = StringUtils.replace(entry.getKey(), baseDir, StringUtils.EMPTY);
			String[] exp = StringUtils.split(word, separator);
			dir = baseDir + exp[0] + separator;
		}
		
		Path wrapper = new Path(dir, 0).set(true);
		if ( !list.contains(wrapper) )
			list.add( wrapper );
	}
	
	private boolean already(List<Path> list, String dir) {
		for (Path path : list) {
			if ( path.getName().equals(dir) ) return true;
		}
		return false;
	}
	
}