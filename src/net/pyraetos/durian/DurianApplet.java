package net.pyraetos.durian;

import javax.swing.JApplet;

public class DurianApplet extends JApplet{
	
	public DurianApplet(){
		setSize(DurianFrame.FRAME_WIDTH + 18, DurianFrame.FRAME_HEIGHT + 45);
		setLayout(null);
		setVisible(true);
		setFocusable(true);
		add(new Status());
		add(new Durian(this));
	}
}