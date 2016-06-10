package net.pyraetos.durian;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
	
	public static void update(int width){
		instance.setPreferredSize(new Dimension(width, 55));
		instance.setMaximumSize(new Dimension(width, 55));
		instance.setMinimumSize(new Dimension(width, 55));
	}
	
	public static void set(String status){
		instance.label.setText("  " + status);
	}
}
