package TBNG;

import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;

public class tbngStandardStyleConnectionPanel extends tbngAbstractConnectionPanel
{
	
	private JTextField txtIPAddress;
	private JPanel panRoot, panOptions;
	
	public tbngStandardStyleConnectionPanel (ActionListener parent)
	{
		
		this.setLayout(new BorderLayout());
		
		cmdHostGame = new JButton("Host Game");
		cmdJoinGame = new JButton("Join Game");
		cmdDisconnect = new JButton("Disconnect");
		
		cmdHostGame.addActionListener(parent);
		cmdJoinGame.addActionListener(parent);
		cmdDisconnect.addActionListener(parent);
		
		cmdHostGame.setActionCommand("hostGame");
		cmdJoinGame.setActionCommand("joinGame");
		cmdDisconnect.setActionCommand("disconnect");
		
		cmdHostGame.setToolTipText("Host a game for other computers to join");
		cmdJoinGame.setToolTipText("Join a game being hosted at the specified IP address");
		cmdDisconnect.setToolTipText("Close the connection");
		
		txtIPAddress = new JTextField("192.168.1.3");
		txtIPAddress.setToolTipText("Address of the computer hosting the game");
				
		lblNetworkStatus = new JLabel("Not Connected", JLabel.CENTER);
		
		panRoot = new JPanel(new BorderLayout());
		panOptions = new JPanel(new GridLayout(2, 2, 2, 2));
		
		this.add(panRoot, BorderLayout.WEST);
			panRoot.add(lblNetworkStatus, BorderLayout.NORTH);
			panRoot.add(panOptions, BorderLayout.SOUTH);
				panOptions.add(txtIPAddress);
				panOptions.add(cmdJoinGame);
				panOptions.add(cmdDisconnect);
				panOptions.add(cmdHostGame);			
			
		panRoot.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		lblNetworkStatus.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
				
	}
	
	public void connectionChanged (boolean isConnected)
	{
		
		enableButtons(isConnected);
		txtIPAddress.setEnabled(!isConnected);
		
	}
	
	public void setMessage (String strMsg)
	{
		lblNetworkStatus.setText(strMsg);
	}
	
	public String getIPAddress ()
	{
		return txtIPAddress.getText();
	}
	
}