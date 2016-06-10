package net.pyraetos.durian;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

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
		setBounds(500, 100, FRAME_WIDTH + 18, FRAME_HEIGHT + 95);
		setTitle(OFFLINE);
		setLayout(null);
		setVisible(true);
		setFocusable(true);
		add(new Status(false));
		add(new Durian(this));
		addComponentListener(this);
		componentResized(null);
	}

	public static void modifyTitle(String title){
		instance.setTitle(title);
	}
	
	@Override
	public void componentResized(ComponentEvent e){
		Durian.setGameSize(getWidth() - 18, getHeight() - 95);
		Status.update(getHeight() - 95, getWidth() - 18);
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
