package TBNG;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class tbngAbstractConnectionPanel extends JPanel
{
	
	protected JButton cmdHostGame, cmdJoinGame, cmdDisconnect;
	protected JLabel lblNetworkStatus;
	
	public tbngAbstractConnectionPanel ()
	{
	}
	
	public abstract void connectionChanged (boolean isConnected);	
	
	public void enableButtons (boolean isConnected)
	{
		
		cmdHostGame.setEnabled(!isConnected);
		cmdJoinGame.setEnabled(!isConnected);
		cmdDisconnect.setEnabled(isConnected);
		
	}
		
	public abstract String getIPAddress ();
	public abstract void setMessage (String strMsg);
	
}	