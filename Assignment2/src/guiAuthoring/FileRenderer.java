package guiAuthoring;

import java.awt.Component;
import java.io.File;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.filechooser.FileSystemView;

public class FileRenderer extends DefaultListCellRenderer{
	private static final long serialVersionUID = 1L;
	
	//This class is used with our JList in order to parse out the extension name in our list.

	public Component getListCellRendererComponent(
			JList list,
			Object value,
			int index,
			boolean isSelected,
			boolean cellHasFocus) {

		Component c = super.getListCellRendererComponent(
				list,value,index,isSelected,cellHasFocus);
		JLabel l = (JLabel)c;
		File f = (File)value;
		l.setText(removeExtension(f.getName())); //parse out the .mp4 and then we are done
		l.setIcon(FileSystemView.getFileSystemView().getSystemIcon(f));

		return l;
	}
	
	private String removeExtension(String name){
		if (name.contains(".mp4")){
			return name.substring(0, name.lastIndexOf("."));
		}
		return name;
		
	}
	
	
}


