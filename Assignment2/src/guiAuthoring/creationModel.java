package guiAuthoring;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.DefaultListModel;

@SuppressWarnings("serial")
public class CreationModel  {
	//The Model portion of our MVC pattern. Singleton this later on

	private static CreationModel instance= null;
	private CreationModel(){
		//This is done to prevent multiple instantiation.
	}

	public static CreationModel getInstance(){
		if (instance==null){
			instance=new CreationModel();
		}
		return instance;

	}


	DefaultListModel<File> fileList=new DefaultListModel<File>();
	//List of all the files

	ArrayList<String> nameList=new ArrayList<String>();
	//String list of all the creations, used for checking for duplicated upon creation

	ArrayList<File> iterableList = Collections.list(fileList.elements());


	private File creationFiles=new File("./creations"); //this is the relative path to the creations.
	private File videoAssets=new File("./videoAssets");
	private File soundAssets=new File("./soundAssets");

	public void createFiles() {
		//This is a helper method used to create the directory files if they are needed.

		if (!creationFiles.exists()) {	        
			creationFiles.mkdir();
			//If the creation directory does not exist, create it. 
		}

		if(!videoAssets.exists()) {
			videoAssets.mkdir();
		}

		if(!soundAssets.exists()) {
			soundAssets.mkdir();
		}

	}

	public void updateList(){
		//have a filter here for only .mp4 files

		fileList.clear();
		nameList.clear();
		//clear the list before refreshing
		for(File creation:creationFiles.listFiles())			
			if (!fileList.contains(creation)){
				//if there is no other file in this list with the same name, we are clear to add.
				//parse out the .mp4 here				
				fileList.addElement(creation);
				nameList.add(creation.getName());				

			}

		this.iterableList = Collections.list(fileList.elements());
	}


	public void deleteElement(File file){
		//issue here is that only updateList should be touching directory internals.
		for (File creation: iterableList){
			if(creation.equals(file)){
				creation.delete();
			}
		}
		updateList();
	}

	public String playElement(File file){
		for (File creation: iterableList){
			if(creation.equals(file)){
				return creation.getPath();
			}
		}
		return null;
	}

	public void createElement(String name) {		

		if(!nameList.contains(name+".mp4")) {
			//find a way to parse out.mp4
			CreationBuilder builder=new CreationBuilder(name);

			BackGroundWorker work=new BackGroundWorker(builder);
			work.execute();		

		}
	}


	public DefaultListModel<File> outputFileList() {
		return fileList;
	}

}
