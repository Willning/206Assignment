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
		//this probably needs to be backgrounded also needs to be fixed to actually display text

		Process process=Runtime.getRuntime().exec(command);
		try {
			process.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("interrupted");
		} //this process is done when it fully executes,

	}
	
	
	public void recordSound() throws IOException{
		//Only aRecord is done in the ED thread.
		String[] command={"/bin/bash", "./recordScript.sh", this.name};		
		Process initRecord=Runtime.getRuntime().exec(command);
		try {
			initRecord.waitFor();
		} catch (InterruptedException e) {
			System.out.println("interrupted");
			
		}

		
	}
	

	


}
