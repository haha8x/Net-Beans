package TBNG;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import java.net.InetAddress;

public abstract class tbngStandardStyleGUI extends JPanel implements ActionListener, tbngNetworkInterface, tbngGameInterface
{

	private boolean isConnected = false;
	
	protected tbngStandardStyleChatPanel panChatArea = null;
	protected tbngStandardStyleConnectionPanel panConnectionArea = null;
	protected tbngAbstractPlayingArea gameCanvas = null;
	protected tbngConnection networkConnection = null;	

	private JButton cmdNewGame, cmdExit;
	private JLabel lblGameStatus, lblNetworkStatus;
	private JPanel panGameArea, panPlayingArea;
	private JPanel panRoot, panWestSide, panEastSide, panGameButtons;

	public void connectionChanged ()
	{

		panChatArea.setEnabled(isConnected);
		panConnectionArea.connectionChanged(isConnected);
		gameCanvas.setConnected(isConnected);

	}
	
	public tbngStandardStyleGUI ()
	{
		super(new BorderLayout());
	}
	
	protected void init (int width, int height)
	{

		this.setPreferredSize(new Dimension(width, height));
		this.setOpaque(true);

		networkConnection = new tbngConnection(this);
		
		panChatArea = new tbngStandardStyleChatPanel(this);
		panConnectionArea = new tbngStandardStyleConnectionPanel(this);
		
		lblGameStatus = new JLabel("", JLabel.CENTER);

		cmdNewGame = new JButton("New Game");
		cmdExit = new JButton("Exit");
		
		cmdNewGame.setActionCommand("newGame");
		cmdExit.setActionCommand("exit");

		cmdNewGame.addActionListener(this);
		cmdExit.addActionListener(this);

		connectionChanged();
		
		panGameArea = new JPanel(new BorderLayout());
		panPlayingArea = new JPanel();
		
		panRoot = new JPanel(new BorderLayout());
		panWestSide = new JPanel(new BorderLayout());
		panEastSide = new JPanel(new BorderLayout());

		panGameButtons = new JPanel();

		this.add(panRoot, BorderLayout.CENTER);
			panRoot.add(panWestSide, BorderLayout.WEST);
				panWestSide.add(panConnectionArea, BorderLayout.NORTH);
				panWestSide.add(panChatArea, BorderLayout.SOUTH);
			panRoot.add(panEastSide, BorderLayout.EAST);
				panEastSide.add(panGameArea, BorderLayout.NORTH);
					panGameArea.add(lblGameStatus, BorderLayout.NORTH);
					panGameArea.add(panGameButtons, BorderLayout.SOUTH);
						panGameButtons.add(cmdNewGame);
						panGameButtons.add(cmdExit);					
				panEastSide.add(panPlayingArea, BorderLayout.SOUTH);
					panPlayingArea.add(gameCanvas);
				
		panRoot.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		panGameArea.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		
		gameCanvas.newGame();

	}

	public void actionPerformed (ActionEvent e)
	{
		
		String strAction = e.getActionCommand();

		if (strAction.equals("newGame"))
		{
			
			if (isConnected)
			{
				networkConnection.newGame();
			}
			
			gameCanvas.newGame();
			
		}
		else if (strAction.equals("exit"))
		{

			networkConnection.stop();
			System.exit(0);

		}
		else if (strAction.equals("hostGame"))
		{

			try
			{

				networkConnection.hostNetworkGame();
				gameCanvas.setMeNetworkPlayer(gameCanvas.PLAYER_ONE);

			}
			catch (Exception ex)
			{
				panChatArea.append("Error: " + ex.getMessage());
			}

		}
		else if (strAction.equals("joinGame"))
		{

			try
			{

				networkConnection.joinNetworkGame(panConnectionArea.getIPAddress());
				gameCanvas.setMeNetworkPlayer(gameCanvas.PLAYER_TWO);

			}
			catch (Exception ex)
			{
				panChatArea.append("Error: " + ex.getMessage());
			}

		}
		else if (strAction.equals("disconnect"))
		{
			
			networkConnection.disconnect();
			panConnectionArea.setMessage("Closed the connection");
			isConnected = false;
			connectionChanged();
			
		}
		else if (strAction.equals("message"))
		{
			
			String strMsg = panChatArea.getText();
			
			if (strMsg.equals(""))
			{
				return;
			}
			
			if (gameCanvas.getMeNetworkPlayer() == gameCanvas.PLAYER_ONE)
			{
				strMsg = "Player 1: " + strMsg;
			}
			else
			{
				strMsg = "Player 2: " + strMsg;
			}
			
			networkConnection.sendMessage(strMsg);			
			panChatArea.append(strMsg);

		}

	}

	public void networkRecMessage (String strMsg)
	{		
		panChatArea.append(strMsg);	
	}

	public void networkNewGame ()
	{
		gameCanvas.newGame();
	}

	public void networkRecMove (String strMove)
	{
		gameCanvas.decodeNetworkMove(strMove);
	}

	public void networkConnectionMade (InetAddress ipAddress, int port)
	{

		isConnected = true;
		panConnectionArea.setMessage("Connected to " + ipAddress.getHostAddress() + ":" + port);
		connectionChanged();
		gameCanvas.newGame();

	}

	public void networkConnectionClosed (boolean wasTimeout)
	{

		isConnected = false;
		connectionChanged();
		
		if (wasTimeout)
		{
			panConnectionArea.setMessage("Connection timeout occured");
		}
		else
		{
			panConnectionArea.setMessage("Connection closed by oponent");
		}

	}

	public void networkConnectionError (String strError)
	{
		panChatArea.append("Error: " + strError);
	}
	
	public void gameEncodedMove (String strMove)
	{
		networkConnection.sendMove(strMove);
	}
	
	public void gameDisplayCurrentPlayer (String strPlayer)
	{
		lblGameStatus.setText(strPlayer);
	}
	
}