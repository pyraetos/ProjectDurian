package net.pyraetos.durian;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class DurianFrame extends JFrame implements ComponentListener{

	public static final int FRAME_WIDTH = 1000;
	public static final int FRAME_HEIGHT = 800;
	public static final String ONLINE = "Project Durian - ONLINE";
	public static final String OFFLINE = "Project Durian - OFFLINE";
	private static DurianFrame instance;
	
	public DurianFrame(){
		instance = this;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle(OFFLINE);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setVisible(true);
		setFocusable(true);
		Status s = new Status(false);
		getContentPane().add(new Durian(getContentPane()));
		getContentPane().add(s);
		addComponentListener(this);
		pack();
		componentResized(null);
		revalidate();
	}

	public static void modifyTitle(String title){
		instance.setTitle(title);
	}
	
	@Override
	public void componentResized(ComponentEvent e){
		Durian.setGameSize(getWidth() - 18, getHeight() - 125);
		Status.update(getWidth() - 18);
	}

	@Override
	public void componentMoved(ComponentEvent e){
		
	}

	@Override
	public void componentShown(ComponentEvent e){

	}

	@Override
	public void componentHidden(ComponentEvent e){

	}
}
