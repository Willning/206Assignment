package guiAuthoring;


import java.io.IOException;
import java.util.Observable;

public class CreationBuilder{
	
	 
	private String name;
	
	
	public CreationBuilder() {
		
	}

	
	public void setName(String name) {
		this.name=name;
	}

	public void buildCreation() throws IOException{		

		String[] command={"/bin/bash", "./creationScript.sh", this.name};
		//calls a pre-written script to both build the ffmpeg video as well as merge the audio with the created video 

		Process process=Runtime.getRuntime().exec(command);
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("interrupted");
		} //this process is done when it fully executes,

	}
	
	
	public void recordSound() throws IOException{
		
		String[] command={"/bin/bash", "./recordScript.sh", this.name};	
		//Calls a pre-written script that does arecord
		Process initRecord=Runtime.getRuntime().exec(command);
		try {
			initRecord.waitFor();
		} catch (InterruptedException e) {
			System.out.println("interrupted");
			
		}

		
	}
	

	


}
