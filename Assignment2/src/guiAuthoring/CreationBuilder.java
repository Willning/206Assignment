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

	public void buildCreation(String name) throws IOException{
				
		String[] command={"ffmpeg -nostats -f lavfi -i color=color=blue"+				
				"-t 3 -s 320x240 $name.mp4 &>>output.txt"};
				
		ProcessBuilder builder=new ProcessBuilder(command);
		builder.directory(new File("./creations"));
		
		Process process=builder.start();
	
	}	

}