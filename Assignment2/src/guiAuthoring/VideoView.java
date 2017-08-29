package guiAuthoring;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.sun.jna.NativeLibrary;
import com.sun.jna.Native;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.media.Media;
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
		JPanel videoPanel=new JPanel(new BorderLayout());
		JPanel controlPanel=new JPanel(new FlowLayout());

		videoPanel.setBackground(Color.BLACK);

		CreationModel model=CreationModel.getInstance();

		//Model part of MVC 

		model.createFiles();
		model.updateList();
		//Upon starting, the file directories will be checked and then updated into the gui.

		JList<File> list=new JList<File>(model.outputFileList());
		list.setCellRenderer(new FileRenderer());
		//Make this update everytime List is pressed.

		JScrollPane scrollPane=new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(200,30));
		//TODO, get rid of magic numbers

		JScrollBar bar = scrollPane.getVerticalScrollBar();
		bar.setPreferredSize(new Dimension(10, 0));


		//Use a listModel and a JList to display all the creations

		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		final EmbeddedMediaPlayer video = mediaPlayerComponent.getMediaPlayer();

		video.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			@Override
			public void finished(MediaPlayer mediaPlayer) {
				videoPanel.getComponent(0).setVisible(false);
				//on finishing, hide the videoPanel

			}
		});

		videoPanel.setPreferredSize(new Dimension(480,360));

		videoPanel.add(mediaPlayerComponent, BorderLayout.CENTER);
		contentPanel.add(videoPanel,BorderLayout.CENTER);
		ListPanel.add(scrollPane,BorderLayout.WEST);
		ListPanel.add(bar,BorderLayout.EAST);





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
				if (list.getSelectedValue()!=null) {
					videoPanel.getComponent(0).setVisible(true);
					video.playMedia(model.playElement((list.getSelectedValue()))); // fix this

				}
			}		
		});
		controlPanel.add(playButton);

		JButton destroyButton = new JButton("Delete");		
		destroyButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedValue()!=null) {

					model.deleteElement(list.getSelectedValue());	

				}
			}
		});		
		controlPanel.add(destroyButton);			

		




		JButton createButton=new JButton("Create");
		createButton.addActionListener(new ActionListener(){
			
			String name=null;
			@Override
			public void actionPerformed(ActionEvent e){				
                String name = JOptionPane.showInputDialog(videoPanel,
                        "Enter Creation Name", null);
                
				if (!name.isEmpty()) {
					model.createElement(name);
					//this whole top part willl be swingworkered
										
				}			
			}

		});
		controlPanel.add(createButton);


		//Later do checks on this textfield to see if the string inside is a valid name
		


		

		contentPanel.add(controlPanel, BorderLayout.SOUTH);
		contentPanel.add(ListPanel,BorderLayout.WEST);

		frame.setContentPane(contentPanel);

		frame.setLocation(100, 100);
		frame.setSize(1050, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				
		frame.setVisible(true);
	}


}


