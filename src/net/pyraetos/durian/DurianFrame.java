package net.pyraetos.durian;

import javax.swing.JFrame;

public class DurianFrame extends JFrame{

	public DurianFrame(){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(500, 100, Durian.FRAME_WIDTH + 6, Durian.FRAME_HEIGHT + 28);
		setTitle("Battle");
		setLayout(null);
		setResizable(false);
		setVisible(true);
		setFocusable(true);
		add(new Durian(this));
	}
}
