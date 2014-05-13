package net.pyraetos.durian;

import javax.swing.JApplet;

public class DurianApplet extends JApplet{
	
	public DurianApplet(){
		setSize(698, 725);
		setLayout(null);
		setVisible(true);
		setFocusable(true);
		add(new Status());
		add(new Durian(this));
	}
}