package net.pyraetos.durian;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Status extends JPanel{

	private static Status instance;
	private JLabel label;
	
	public Status(boolean applet){
		instance = this;
		int width = applet ? 680 : DurianFrame.FRAME_WIDTH;
		label = new JLabel();
		setLayout(new BorderLayout());
		this.add(label);
		setBounds(0, width - 50, width, 50);
		setBackground(Color.BLACK);
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Courier New", Font.PLAIN, 16));
	}
	
	public static void update(int y, int width){
		instance.setBounds(0, y, width, instance.getHeight());
	}
	
	public static void set(String status){
		instance.label.setText("  " + status);
	}
}
