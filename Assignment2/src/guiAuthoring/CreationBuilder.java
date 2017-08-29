package guiAuthoring;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CreationBuilder {
	
	//Make this have a swingworker. 
	private String name;
	
	public CreationBuilder(String name){
		this.name=name;
	}

	public void buildCreation() throws IOException, InterruptedException{		

		String[] command={"/bin/bash", "./creationScript.sh", this.name};
		//this probably needs to be backgrounded also needs to be fixed to actually display text

		Process process=Runtime.getRuntime().exec(command);
		process.waitFor(); //this process is done when it fully executes,
		//later catch the exceptions and deal with them accordingly

	}
	
	
	public void recordSound(String name) throws IOException{
		//Only aRecord is done in the ED thread.
		String[] command={"/bin/bash", "./recordScript.sh", this.name};		
		Process initRecord=Runtime.getRuntime().exec(command);
	}

}
