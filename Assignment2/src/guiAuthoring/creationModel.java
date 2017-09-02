package guiAuthoring;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;

@SuppressWarnings("serial")
public class CreationModel extends Observable{
	//The Model portion of our MVC pattern. Singleton this later on

	private static CreationModel instance= null;	
	private CreationBuilder builder=new CreationBuilder();



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


	private File creationFiles=new File("./creations");
	//these are the relative path to the creations and also assetFiles.
	private File videoAssets=new File("./videoAssets");
	private File soundAssets=new File("./soundAssets");
	private File pictureAssets=new File("./pictureAssets");

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

		if(!pictureAssets.exists()) {
			pictureAssets.mkdir();
		}

	}

	public void updateList(){

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
		cleanAssets();

		this.iterableList = Collections.list(fileList.elements());
	}

	private void cleanAssets() {
		//this method will clean the videoAsset and soundAsset as well as picture Asset files for files that aren't in the model
		for (File sound: soundAssets.listFiles()) {
			String soundName=sound.getName();

			if (soundName.contains(".wav")){
				if (soundName.lastIndexOf(".")>0) {
					soundName= soundName.substring(0, soundName.lastIndexOf("."));
				}
			}			
			if (!nameList.contains(soundName+".mp4")) {
				sound.delete();
			}
		}
		
		for (File pic: pictureAssets.listFiles()) {
			String pictureName=pic.getName();
			
			if (pictureName.contains(".jpg")){
				if (pictureName.lastIndexOf(".")>0) {
					pictureName= pictureName.substring(0, pictureName.lastIndexOf("."));
				}
			}
			
			if(!nameList.contains(pictureName+".mp4")) {
				pic.delete();
			}			
		}
		
		for (File video:videoAssets.listFiles()) {
			if (!nameList.contains(video.getName())) {
				video.delete();
			}
		}
	}


	public void deleteElement(File file){		
		String fileName=file.getName();
		//issue here is that only updateList should be touching directory internals.
		for (File creation: iterableList){
			if(creation.equals(file)){
				creation.delete();
			}
		}

		for (File video: videoAssets.listFiles()) {
			//Delete video assets
			if (video.getName().equals(file.getName()))
				video.delete();
		}

		for (File sound:soundAssets.listFiles()) {
			//Delete sound Assets
			String soundName=sound.getName();


			if (soundName.contains(".wav")){
				if(soundName.lastIndexOf(".")>0) {
					soundName= soundName.substring(0, soundName.lastIndexOf("."));
				}
			}

			if (file.getName().contains(".mp4")){
				if(fileName.lastIndexOf(".")>0) {
					fileName=fileName.substring(0, fileName.lastIndexOf("."));
				}
			}

			if (soundName.equals(fileName)) {
				sound.delete();
			}
		}

		for (File thumbnail:pictureAssets.listFiles()) {
			//Delete picture Assets
			String picName=thumbnail.getName();

			if (picName.contains(".jpg")) {
				picName=picName.substring(0, picName.lastIndexOf("."));
			}
			if(picName.equals(fileName)) {
				thumbnail.delete();
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

	public void createElement(String name) throws IOException, InterruptedException {		
		builder.setName(name);
		if(!nameList.contains(name+".mp4")) {			

			VideoWorker work=new VideoWorker(this.builder);
			work.execute();
		}
	}

	public void record(String name) {
		builder.setName(name);

		if(!nameList.contains(name+".mp4")) {
			//overwriten in the script

			RecordWorker record=new RecordWorker(this.builder);
			record.execute();
		}
	}

	public String playSound(String name) {		

		for (File wav:soundAssets.listFiles()) {			
			if(wav.getName().equals(name+".wav")) {				
				return wav.getPath();
			}
		}
		return null;
	}

	public String getPreview(String name) {
		String parsedName=name;

		if (name.contains(".mp4")&&name.contains("/")) {
			parsedName=name.substring(name.lastIndexOf("/")+1, name.lastIndexOf("."));
		}

		for (File preview:pictureAssets.listFiles()) {

			if (preview.getName().equals(parsedName+".jpg")) {

				return preview.getPath();
			}
		}
		return null;

	}

	public void finishedRecord() {
		this.setChanged();
		this.notifyObservers("halfFinished");
	}

	public void creationFinished() {
		//this notifies differently. 
		this.updateList();
		this.setChanged();
		this.notifyObservers("finished");
	}


	public DefaultListModel<File> outputFileList() {
		return fileList;
	}

	public boolean isInNameList(String name){
		//check if the name is in the nameList
		return nameList.contains(name+".mp4");
	}


}
