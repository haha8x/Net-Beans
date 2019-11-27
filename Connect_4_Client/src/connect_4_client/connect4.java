package connect_4_client;

import javax.swing.JFrame;
import javax.swing.JComponent;

public class connect4 extends JFrame
{
	
	public static void main (String[] args)
	{
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		
		JFrame frame = new JFrame();
		frame.setTitle("Connect 4");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		c4GUI panMain = new c4GUI();
		
		frame.setResizable(false);
		frame.setContentPane(panMain);
		
		frame.pack();
		frame.setVisible(true);
		
	}
	
}		