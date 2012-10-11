package re.usto.dto.object;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.simpleframework.xml.Element;

import android.R.bool;
import android.view.View;

import re.usto.dto.helper.StringUtils;

@Element(name = "filelist")
public class Path {

	public Path() {
	}

	public Path(String name, long size) {
		this.name = name;
		this.length = size;
	}

	@Element
	private int id;
	@Element
	private String name;
	@Element
	private long length;
	@Element
	private String lastUpdate;
	private boolean directory;
	private View mView = null;
	//puts the gray background on row
	private boolean selected = false;

	public Path set(boolean yes) {
		this.directory = yes;
		return this;
	}

	public String getFullName() throws UnsupportedEncodingException {
		if (StringUtils.isEmpty(this.name))
			return null;
		if (!PathParser.containsAnySeparator(this.name))
			return this.name;
		return URLEncoder.encode(this.name, "UTF-8");
	}

	public String getExibitionName() {
		String name = this.name;

		if (String.valueOf(name.charAt(0)).equals(
				PathParser.separator(this.name)))
			name = name.substring(1);

		return reduceFileNameLenght(name);
	}

	public boolean isDirectory() {
		if (StringUtils.isEmpty(this.name))
			return false;
		return ((this.name.length() - 1) == (this.name.lastIndexOf(PathParser
				.separator(this.name))));
	}

	public String getDirectoryName() {
		if (StringUtils.isEmpty(this.name))
			return null;

		int lastindex = -1;
		lastindex = this.name.lastIndexOf(PathParser.separator(this.name)) + 1;

		if (lastindex == -1)
			return PathParser.separator(this.name);
		return this.name.substring(0, lastindex);
	}

	public String getFileName() {
		if (StringUtils.isEmpty(this.name))
			return null;
		if (!PathParser.containsAnySeparator(this.name))
			return reduceFileNameLenght(this.name);

		String[] exp = StringUtils.split(this.name,
				PathParser.separator(this.name));
		String result = exp[exp.length - 1];
		result = reduceFileNameLenght(result);
		return result;
	}

	public String reduceFileNameLenght(String result) {
		// if ( result.length() > 27 ) {
		// result = result.substring(0, 27);
		// }
		return result;
	}

	public String getSize() {
		return length / 1024 + " KB";
	}

	@Override
	public String toString() {
		return (this.name != null) ? this.name : super.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Path other = (Path) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public long getLength() {
		return length;
	}

	public String getLastUpdate() {
		
		return lastUpdate.substring(0, 10); // Is 10 because the date value has
											// ten chars
	}

	public void setDirectory(boolean directory) {
		this.directory = directory;
	}

	public void setView(View view) {
		
		this.mView = view;

	}
	
	public View getView()
	{
		return this.mView;
	}
	
	public boolean isSelected(){
		return this.selected;
	}
	
	public void setSelected(boolean selected){
		this.selected = selected;
	}

}