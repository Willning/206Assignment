package guiAuthoring;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
	private final EmbeddedMediaPlayer video;


	public static void main(String[] args) {

		boolean found = new NativeDiscovery().discover();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new VideoView();				
			}
		});
	}

	private String creationName=null;
	CreationModel model=CreationModel.getInstance();


	public VideoView(){


		JFrame frame= new JFrame("Math Authoring Aid");
		JPanel contentPanel = new JPanel(new BorderLayout());

		JPanel ListPanel=new JPanel(new BorderLayout());
		JPanel videoPanel=new JPanel(new BorderLayout());
		JPanel controlPanel=new JPanel(new FlowLayout());

		videoPanel.setBackground(Color.BLACK);


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
		video = mediaPlayerComponent.getMediaPlayer();

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

					video.playMedia(model.playElement((list.getSelectedValue()))); 

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


		CreationDialog creationDialog=new CreationDialog(frame);
		//creation dialog menu used to build creations. Closing this without finishing will reset this and delete all recordings made 
		//that were unassigned to a video 
		creationDialog.setLocationRelativeTo(controlPanel);
		creationDialog.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				creationDialog.reset();
				model.updateList();
				
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});


		JButton createButton=new JButton("Create");
		createButton.addActionListener(new ActionListener(){				
			@Override
			public void actionPerformed(ActionEvent e){				
				if (!creationDialog.isShowing()) {
					creationDialog.setVisible(true);
				}

			}

		});
		controlPanel.add(createButton);


		
		contentPanel.add(controlPanel, BorderLayout.SOUTH);
		contentPanel.add(ListPanel,BorderLayout.WEST);

		frame.setContentPane(contentPanel);

		frame.setLocation(100, 100);
		frame.setSize(1050, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				
		frame.setVisible(true);
	}



	@SuppressWarnings("serial")
	class CreationDialog extends JDialog implements Observer{


		JLabel recordingStatus;
		JPanel creationPanel = new JPanel();
		JTextField nameField;


		CreationDialog(JFrame frame){
			super(frame, "Creation Menu");

			model.addObserver(this);

			nameField=new JTextField("Enter Creation Name");
			nameField.selectAll();

			JPanel recordPanel=new JPanel();

			recordPanel.setLayout(new BoxLayout(recordPanel, BoxLayout.PAGE_AXIS));


			JButton record=new JButton("record");
			record.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (nameField.getText()!=null&&!nameField.getText().isEmpty()) {
						if (!nameField.getText().contains(" ")){
							if (!model.isInNameList((nameField.getText()))){
								recordingStatus.setText("Recording");
								creationName=nameField.getText();
								nameField.setEnabled(false);
								model.record(creationName);
							}
						}

					}
				}
			});
			record.setAlignmentX(Component.CENTER_ALIGNMENT);

			recordingStatus= new JLabel("Press record to record creation");
			recordingStatus.setVisible(true);			

			recordingStatus.setAlignmentX(Component.CENTER_ALIGNMENT);


			recordPanel.add(record);
			recordPanel.add(recordingStatus);

			//make a record record button and a status tracking message

			JButton keep=new JButton("keep");
			keep.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					if (creationName!=null&&!creationName.isEmpty()) {
						try {							
							model.createElement(creationName);
						} catch (IOException | InterruptedException e1) {
							JOptionPane.showMessageDialog(new JButton("OK"), "Creation Was interrupted");
						}
					}
				}
			});

			JButton playRecording=new JButton("play Recording");
			playRecording.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					if (model.playSound(creationName)!=null) {						
						video.playMedia(model.playSound(creationName));
					}
				}
			});

			creationPanel.add(keep);
			creationPanel.add(playRecording);

			creationPanel.setVisible(false);
			//contains the buttons for keep and play recording, should only be visible when there is a recording


			//creationDialog=new JDialog(frame, "Creation");
			this.setSize(new Dimension(300,100));			
			this.add(creationPanel);
			this.add(recordPanel,BorderLayout.SOUTH);
			this.add(nameField,BorderLayout.NORTH);

		}
		
		public void reset() {
			nameField.setEnabled(true);
			nameField.setText("Enter Creation Name");
			nameField.selectAll();
			
			recordingStatus.setText("Press record to record creation");
			creationPanel.setVisible(false);
		}

		@Override
		public void update(Observable arg0, Object state) {

			if ((String)state=="halfFinished") {
				
				recordingStatus.setText("Done: Press record again to rerecord");
				creationPanel.setVisible(true);
			}

			if ((String)state=="finished") {
				reset();
			}

		}
	}


}


