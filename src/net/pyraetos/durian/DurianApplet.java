package net.pyraetos.durian;

import javax.swing.JApplet;

public class DurianApplet extends JApplet{
	
	public DurianApplet(){
		setLayout(null);
		setVisible(true);
		setFocusable(true);
		add(new Status(true));
		add(new Durian(this));
	}
}