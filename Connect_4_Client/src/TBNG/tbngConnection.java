package TBNG;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.io.IOException;

public class tbngConnection implements Runnable
{
	//How long in miliseconds to wait before sending a packet. This gives time for the other computer to start listening
	//If you send a packet while the other computer is not listening, then the packet might get discarded
	private final long WAIT_BEFORE_SENDING_TIME = 30L; //Wait this long before sending ANY packet
	private final long WAIT_BETWEEN_TURNS_TIME = 1000L; //Once the connection is established, wait this long before taking turn to send a packet
	//The port number the host will be listening to for incoming packets. The client should send packets to this port
	private int HOST_PORT_NUMBER = 4445;

	private InetAddress ipAddress = null;
	private int port = 0;
	private boolean meHost = false;
	
	private DatagramSocket socket = null;

	private boolean disconnect = false;
	private boolean newGame = false;
	private boolean sendingMove = false;
	private boolean sendingMessage = false;
	
	private String strMove = null;
	private String strMessage = null;
	
	private tbngNetworkInterface parent = null;
	private Thread networkThread = null;
	
	private void waitBeforeSending ()
	{
		
		try
		{
			networkThread.sleep(WAIT_BEFORE_SENDING_TIME);
		}
		catch (InterruptedException ex)
		{
		}
		
	}
	
	private void sendPacket (tbngProtocol data) throws IOException
	{
		
		waitBeforeSending();
		
		byte[] buff = data.encode();
		DatagramPacket packet = new DatagramPacket(buff, buff.length, ipAddress, port);
		socket.send(packet);
		
	}
	
	private tbngProtocol receivePacket (boolean recordConnectionDetails) throws IOException
	{
		
		byte[] buff = new byte[256];
		DatagramPacket packet = new DatagramPacket(buff, buff.length);
		socket.receive(packet);
		
		if (recordConnectionDetails)
		{
			
			ipAddress = packet.getAddress();
			port = packet.getPort();
			
		}
		
		return new tbngProtocol(packet.getData());
		
	}
	
	private tbngProtocol receivePacket () throws IOException
	{
		return receivePacket(false);
	}
	
	public tbngConnection (tbngNetworkInterface parent)
	{		
		this.parent = parent;	
	}
	//User can change the port the host is listening to, or the client is sending to
	public void hostOnPort (int HOST_PORT_NUMBER) throws Exception
	{
		
		if (networkThread != null)
		{
			throw new Exception("Can not change port because the connection is still active");
		}
		
		this.HOST_PORT_NUMBER = HOST_PORT_NUMBER;
		
	}	
	
	public void hostNetworkGame () throws Exception
	{
		
		if (networkThread != null)
		{
			throw new Exception("A connection is still active and must be stopped first");
		}
	
		meHost = true;
		networkThread = new Thread(this, "Game Host");
		networkThread.start();
		
	}
	
	public void joinNetworkGame (String strIP) throws Exception
	{
		
		if (networkThread != null)
		{
			throw new Exception("A connection is still active and must be stopped first");
		}
		
		try
		{
			ipAddress = InetAddress.getByName(strIP);
		}
		catch (UnknownHostException ex)
		{
			throw new Exception("Invalid IP address syntax.\nExample of IPv4 address 192.168.1.3\nExample of IPv6 address FE80::5445:5245:444F");
		}			
			
		meHost = false;
		networkThread = new Thread(this, "Game Join");
		networkThread.start();
		
	}
	
	public void stop ()
	{
		
		if (networkThread != null)
		{
			
			networkThread.interrupt();
			networkThread = null;
			
		}
		
	}
	
	public void sendMove (String strMove)
	{

		this.strMove = strMove;
		sendingMove = true;
		
	}
	
	public void sendMessage (String strMessage)
	{

		this.strMessage = strMessage;
		sendingMessage = true;
		
	}
	
	public void disconnect ()
	{

		disconnect = true;
	}
	
	public void newGame ()
	{
		newGame = true;
	}
	
	public void run ()
	{
		
		Thread runningThread = networkThread;
		tbngProtocol data = null;
		
		try
		{
			
			if (meHost)
			{
				socket = new DatagramSocket(HOST_PORT_NUMBER);				
			}
			else
			{

				port = HOST_PORT_NUMBER;				
				socket = new DatagramSocket();
							
			}
			
			socket.setSoTimeout(20000);
			
		}
		catch (SocketException ex)
		{
			
			parent.networkConnectionError("The port is already in use");
			return;
			
		}
		
		try
		{
		
			if (meHost)
			{
				
				data = receivePacket(true);
					
				if (data.getType() == tbngProtocol.TYPE_CONNECTION && data.getOption() == tbngProtocol.CONNECTION_REQUEST_JOIN)
				{
				//Good request					
				}
				else
				{
				//Bad request
				}
		
				data = new tbngProtocol(tbngProtocol.TYPE_CONNECTION, tbngProtocol.CONNECTION_ACCEPT_JOIN);
				sendPacket(data);
				
			}
			else
			{

				data = new tbngProtocol(tbngProtocol.TYPE_CONNECTION, tbngProtocol.CONNECTION_REQUEST_JOIN);
				sendPacket(data);				
				data = receivePacket();
				
				if (data.getType() == tbngProtocol.TYPE_CONNECTION && data.getOption() == tbngProtocol.CONNECTION_ACCEPT_JOIN)
				{
				//Good join
				}
				else
				{
				//Bad join						
				}
				
			}
			
			parent.networkConnectionMade(ipAddress, port);
			boolean firstTime = true; //At the start the host sends and the client listens
			socket.setSoTimeout(5000);
			
			while (runningThread == networkThread)
			{
			
				if (!firstTime || meHost)
				{
					
					try
					{
						networkThread.sleep(WAIT_BETWEEN_TURNS_TIME);
					}
					catch (InterruptedException ex)
					{
					}

					if (disconnect)
					{

						data = new tbngProtocol(tbngProtocol.TYPE_CONNECTION, tbngProtocol.CONNECTION_DISCONNECT);
						disconnect = false;
						networkThread = null;
						
					}
					else if (newGame)
					{
						
						data = new tbngProtocol(tbngProtocol.TYPE_GAME, tbngProtocol.GAME_NEW_GAME);
						sendingMove = false;
						newGame = false;
						
					}
					else if (sendingMove)
					{
			
						data = new tbngProtocol(tbngProtocol.TYPE_GAME, tbngProtocol.GAME_SEND_MOVE, strMove);
						sendingMove = false;

					}
					else if (sendingMessage)
					{

						data = new tbngProtocol(tbngProtocol.TYPE_MESSAGE, tbngProtocol.MESSAGE_SEND_MESSAGE, strMessage);
						sendingMessage = false;

					}
					else
					{
						data = new tbngProtocol(tbngProtocol.TYPE_CONNECTION, tbngProtocol.CONNECTION_KEEP_ALIVE);
					}
					
					sendPacket(data);
					
				}
				
				if (networkThread == null)
				{
					break;
				}
				
				firstTime = false;

				data = receivePacket();
				
				if (data.getType() == tbngProtocol.TYPE_CONNECTION)
				{
					
					if (data.getOption() == tbngProtocol.CONNECTION_KEEP_ALIVE)
					{
						continue;
					}
					else if (data.getOption() == tbngProtocol.CONNECTION_DISCONNECT)
					{						

						parent.networkConnectionClosed(false);
						networkThread = null;
						
					}
					
				}
				else if (data.getType() == tbngProtocol.TYPE_GAME)
				{
					
					if (data.getOption() == tbngProtocol.GAME_SEND_MOVE)
					{
						parent.networkRecMove(data.getData());
					}
					else if (data.getOption() == tbngProtocol.GAME_NEW_GAME)
					{

						sendingMove = false;
						parent.networkNewGame();
						
					}
					
				}
				else if (data.getType() == tbngProtocol.TYPE_MESSAGE)
				{
					
					if (data.getOption() == tbngProtocol.MESSAGE_SEND_MESSAGE)
					{
						parent.networkRecMessage(data.getData());
					}
					
				}
				
			}
			
		}
		catch (UnknownHostException ex)
		{
			parent.networkConnectionError("Could not locate and connect to the other computer\nOne reason is that both computers are behind NAT (Network Address Translation) routers");
		}
		catch (SocketException ex)
		{
			parent.networkConnectionError("The socket sending and receiving data threw an error\n" + ex.getMessage());
		}
		catch (SocketTimeoutException ex)
		{
			parent.networkConnectionClosed(true);
		}
		catch (IOException ex)
		{
			parent.networkConnectionError(ex.getMessage());
		}
		
		if (socket != null)
		{
			socket.close();
		}

		networkThread = null;
		
	}		
	
}	