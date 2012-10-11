package re.usto.dto.dialog;

import java.io.File;
import java.util.List;

import re.usto.dto.R;
import re.usto.dto.helper.Constants;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Path;
import android.view.View;
import android.widget.ListView;

public class FileDialog extends BaseFileDialog {
    
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		File file = new File(path.get(position));

		if (!file.exists()) {
		    new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher)
                .setTitle(Constants.FILE_DIALOG_FILE_DO_NOT_EXISTS)
                .setMessage(file.getName())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
		    
		    return;
		}
		
		selectView(v);
		
		if (file.isDirectory()) {
			selectButton.setEnabled(false);
			if (file.canRead()) {
			    // Save the scroll position so users don't get confused when they come back
				lastPositions.put(currentPath, this.getListView().getFirstVisiblePosition());
				getDir(path.get(position));
			} else {
				new AlertDialog.Builder(this)
						.setIcon(R.drawable.ic_launcher)
						.setTitle(
								"[" + file.getName() + "] "
										+ getText(R.string.cant_read_folder))
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).show();
			}
		}
		else {
			//removeBeforeSelection(path);
			selectedFile = file;
			v.setSelected(true);
			selectButton.setEnabled(true);
			
			if (options.oneClickSelect) {
			    selectButton.performClick();
			}
		}
	}
	
	/*private void removeBeforeSelection(List<String> path){
		
	}*/
}