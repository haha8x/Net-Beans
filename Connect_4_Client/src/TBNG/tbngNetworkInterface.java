package TBNG;

import java.net.InetAddress;

public interface tbngNetworkInterface
{	
	
	public void networkConnectionMade (InetAddress ipAddress, int port);
	public void networkNewGame ();
	public void networkRecMessage (String strMessage);	
	public void networkRecMove (String strMove);
	public void networkConnectionClosed (boolean wasTimeout);
	public void networkConnectionError (String strError);
	
}