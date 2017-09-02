package guiAuthoring;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;


public class VideoView{


	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private final EmbeddedMediaPlayer video;
	
	private boolean isPlaying=false;


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
	
	private JFrame frame; //frame  holding everything in the main gui
	private JPanel contentPanel; //panel of everything
	private JPanel controlPanel; //panel with main buttons
	private JPanel listPanel; //panel with the list of creations
	private JPanel videoPanel; //panel with the video player
	
	private ImageIcon preview;


	public VideoView(){

		frame= new JFrame("Math Authoring Aid");
		contentPanel = new JPanel(new BorderLayout());
		listPanel=new JPanel(new BorderLayout());
		videoPanel=new JPanel(new BorderLayout());
		controlPanel=new JPanel(new FlowLayout());

		videoPanel.setBackground(Color.BLACK);


		//Model part of MVC 

		model.createFiles();
		model.updateList();
		
		//Upon starting, the file directories will be checked and then updated into the gui
		

		
		
		//Add a thumbnail Jpanel to the listPanel at the top.
		JLabel thumbnail=new JLabel();
				
		
		
		//Use a listModel and a JList to display all the creations
		JList<File> list=new JList<File>(model.outputFileList());
	
		list.setCellRenderer(new FileRenderer());
		
		list.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				if (list.getSelectedValue()!=null&&list.getValueIsAdjusting()==false) {
					preview=new ImageIcon(model.getPreview(list.getSelectedValue().toString()));
										
					thumbnail.setIcon(preview);
				}
				
			}
			
		});
		
				

		JScrollPane scrollPane=new JScrollPane(list);
		scrollPane.setPreferredSize(new Dimension(200,30));
		

		JScrollBar bar = scrollPane.getVerticalScrollBar();
		bar.setPreferredSize(new Dimension(10, 0));
		

		
		listPanel.add(thumbnail, BorderLayout.NORTH);
		listPanel.add(scrollPane,BorderLayout.WEST);
		listPanel.add(bar,BorderLayout.EAST);		


		

		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		video = mediaPlayerComponent.getMediaPlayer();

		video.addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
			@Override
			public void finished(MediaPlayer mediaPlayer) {
				isPlaying=false;
				videoPanel.getComponent(0).setVisible(false);
				//on finishing, hide the videoPanel

			}
		});

		videoPanel.setPreferredSize(new Dimension(480,360));

		videoPanel.add(mediaPlayerComponent, BorderLayout.CENTER);
		contentPanel.add(videoPanel,BorderLayout.CENTER);
	



		JButton listButton = new JButton("Refresh List");		
		listButton.addActionListener(new ActionListener() {
			//later make buttons seperate classes responsible for their own thing
			//List will update the creation list, 
			@Override
			public void actionPerformed(ActionEvent e) {				
				model.updateList();
				//hitting RefreshList will deselect all elements and will empty the thumbnail
				preview=null;					
				thumbnail.setIcon(preview);	
				

			}
		});		
		controlPanel.add(listButton);


		JButton playButton=new JButton("Play");
		playButton.addActionListener(new ActionListener() {
			//Button responsible for playing the creation
			@Override
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedValue()!=null) {
					videoPanel.getComponent(0).setVisible(true);
					isPlaying=true;
					video.playMedia(model.playElement((list.getSelectedValue()))); 					

				}
			}		
		});
		controlPanel.add(playButton);

		JButton destroyButton = new JButton("Delete");	
		//Button used to destroy creations, will destroy all relevant assets as well.
		destroyButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (list.getSelectedValue()!=null) {
					
					if(!isPlaying) {
					//modules cannot be deleted if video is in a playing state
					preview=null;					
					thumbnail.setIcon(preview);	
					
					model.deleteElement(list.getSelectedValue());
					}

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
		contentPanel.add(listPanel,BorderLayout.WEST);

		frame.setContentPane(contentPanel);

		frame.setLocation(100, 100);
		frame.setSize(1050, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				
		frame.setVisible(true);
	}

	public void lockControl() {
		//this will lock the creationPanel so as not to disturb the recording
		//this is as all the buttons will prematurely clear the soundAsset if pressed while recording
		for (Component component:controlPanel.getComponents()) {
			component.setEnabled(false);
		}

	}

	public void unlockControl() {
		//this will unlock the creationPanel
		for (Component component:controlPanel.getComponents()) {
			component.setEnabled(true);
		}

	}


	@SuppressWarnings("serial")
	class CreationDialog extends JDialog implements Observer{
		//This is a helper class in video view which is used to start recordngs and to make creations


		JLabel recordingStatus;
		JPanel creationPanel = new JPanel();
		JTextField nameField;
		JButton record=new JButton("record");


		CreationDialog(JFrame frame){
			super(frame, "Creation Menu");
			
			model.addObserver(this);


			nameField=new JTextField("Enter Creation Name");
			nameField.selectAll();

			JPanel recordPanel=new JPanel();

			recordPanel.setLayout(new BoxLayout(recordPanel, BoxLayout.PAGE_AXIS));


			record.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (nameField.getText()!=null&&!nameField.getText().isEmpty()) {
						if (!nameField.getText().contains(" ")){
							if (!model.isInNameList((nameField.getText()))){							
								
								recordingStatus.setText("Recording"); //text will change to say recording;
								creationName=nameField.getText(); //this is done so changes to nameField will not effect the process/merge later on
								nameField.setEnabled(false); //Extra layer of safety to absolutely ensure the nameField cannot change
								
								creationPanel.setVisible(false); //hide the keep and Play Recording buttons while recording.
								lockControl(); //lock control with the main buttons in the gui as some of them can delete the file mid recording
								model.record(creationName); //start the process from model
							}
						}

					}
				}
			});
			
			record.setAlignmentX(Component.CENTER_ALIGNMENT);

			recordingStatus= new JLabel("Press record to record creation");
			recordingStatus.setVisible(true);		

			recordingStatus.setAlignmentX(Component.CENTER_ALIGNMENT);

			recordPanel.add(recordingStatus);
			recordPanel.add(record);			

			//make a record record button and a status tracking message

			JButton keep=new JButton("Finish Creation");
			//button used to Finish the creation, on press should unlock the main buttonsin the gui as well as reset the dialog so another creation can be built
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
			this.setSize(new Dimension(300,150));			
			this.add(creationPanel);
			this.add(recordPanel,BorderLayout.SOUTH);
			this.add(nameField,BorderLayout.NORTH);

		}

		public void reset() {
			//resets the dialog to the startng state as well as unlocking the main buttons
			
			nameField.setEnabled(true);
			nameField.setText("Enter Creation Name");
			nameField.selectAll();
			unlockControl();

			recordingStatus.setText("<html>Press record to record creation<br></html>");
			creationPanel.setVisible(false);
		}

		@Override
		public void update(Observable arg0, Object state) {

			if ((String)state=="halfFinished") {

				recordingStatus.setText("<html>Finished Recording:<br> To re-record, press record<br>"
						+ "To finish creation, press Finish Creation</html>");
				creationPanel.setVisible(true);
			}else if ((String)state=="finished") {
				reset();
			}

		}
	}


}


