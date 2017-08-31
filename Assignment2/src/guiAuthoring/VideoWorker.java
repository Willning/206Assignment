package guiAuthoring;

import javax.swing.SwingWorker;

public class VideoWorker extends SwingWorker<Object, Object> {
	//Edit the objects 
	private CreationBuilder builder;
	
	
	public VideoWorker (CreationBuilder builder) {
		this.builder=builder;
		
	}

	@Override
	protected Object doInBackground() throws Exception {
		builder.buildCreation();
		return null;
	}
	
	protected void done() {
		CreationModel model=CreationModel.getInstance();
		model.creationFinished();
	
		
	}

}
