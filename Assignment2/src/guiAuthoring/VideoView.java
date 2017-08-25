package guiAuthoring;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.sun.jna.NativeLibrary;
import com.sun.jna.Native;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;


public class VideoView{
	//TODO get listing to list
	//TODO playbutton
	//TODO, create button
	//TODDO quit button


	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;


	public static void main(String[] args) {

		boolean found = new NativeDiscovery().discover();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new VideoView();
			}
		});
	}


	public VideoView(){


		JFrame frame= new JFrame("Math Authoring Aid");
		JPanel contentPanel = new JPanel(new BorderLayout());
		JPanel ListPanel=new JPanel(new BorderLayout());

		creationModel model = new creationModel();
		//Model part of MVC 

		model.createFiles();
		model.updateList();
		//Upon starting, the file directories will be checked and then updated into the gui.

		JList list=new JList(model.outputList());
		//Make this update everytime List is pressed.

		JScrollPane scrollPane=new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(200,30));
		//TODO, get rid of magic numbers

		JScrollBar bar = scrollPane.getVerticalScrollBar();
		bar.setPreferredSize(new Dimension(10, 0));

		//view, change this so it displays blank

		//Use a listModel and a JList to display all the creations

		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		final EmbeddedMediaPlayer video = mediaPlayerComponent.getMediaPlayer();

		contentPanel.add(mediaPlayerComponent, BorderLayout.CENTER);
		ListPanel.add(scrollPane,BorderLayout.WEST);
		ListPanel.add(bar,BorderLayout.EAST);


		JPanel controlPanel=new JPanel(new FlowLayout());

		JButton listButton = new JButton("Refresh List");		
		listButton.addActionListener(new ActionListener() {
			//later make buttons seperate classes responsible for their own thing
			//List will update the creation list, 
			@Override
			public void actionPerformed(ActionEvent e) {				
				model.updateList();
				
			}
		});		
		controlPanel.add(listButton);

		
		JButton playButton=new JButton("Play");
		playButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}		
		});
		controlPanel.add(playButton);

		JButton destroyButton = new JButton("Delete");		
		destroyButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedValue()!=null) {
					model.deleteElement(list.getSelectedValue().toString());	
					
				}
			}
		});		
		controlPanel.add(destroyButton);
		

		contentPanel.add(controlPanel, BorderLayout.SOUTH);
		contentPanel.add(ListPanel,BorderLayout.WEST);

		frame.setContentPane(contentPanel);

		frame.setLocation(100, 100);
		frame.setSize(1050, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				
		frame.setVisible(true);
	}


}


