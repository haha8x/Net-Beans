package TBNG;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

public abstract class tbngAbstractPlayingArea extends Canvas implements MouseListener, MouseMotionListener
{
	
	public static final int PLAYER_ONE = -1, PLAYER_TWO = 1;
	
	protected int meNetworkPlayer = 0;
	protected int currentPlayer = 0;
	protected int firstTurnPlayer = 0;
	
	protected boolean isConnected = false;
	protected boolean firstGame = true;
	protected boolean firstNetworkGame = false;
	
	protected Color clrBackground = new Color(255, 255, 255);
	protected Color clrBorder = new Color(0, 0, 0);

	protected int borderSize = 10;
	protected int canvasWidth = 0, canvasHeight = 0; //The total size
	protected int gameWidth = 0, gameHeight = 0; //The game size, not including the border
	
	protected tbngGameInterface parent = null;
	protected tbngAbstractPlayerMatrix playerMatrix = null;
	
	public tbngAbstractPlayingArea (tbngGameInterface parent)
	{
		
		this.parent = parent;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
			
	}
	
	public abstract void decodeNetworkMove (String strMove);
	
	protected void displayCurrentPlayer ()
	{

		if (isConnected)
		{

			if (currentPlayer == meNetworkPlayer)
			{
				parent.gameDisplayCurrentPlayer("Your turn to move");
			}
			else
			{
				parent.gameDisplayCurrentPlayer("Waiting for oppoment to move");
			}

		}
		else
		{
			
			if (currentPlayer == PLAYER_ONE)
			{
				parent.gameDisplayCurrentPlayer("Player one's turn");
			}
			else
			{
				parent.gameDisplayCurrentPlayer("Player two's turn");
			}
			
		}

	}
	
	protected void drawFrame (Graphics g)
	{
		
		g.setColor(clrBackground);
		g.fillRect(0, 0, canvasWidth, canvasHeight);//fillRect(x, y, width, height);
		
		if (borderSize > 0) //If there is a border to draw
		{
		
			g.setColor(clrBorder);
			g.fillRect(0, 0, canvasWidth, borderSize); //NW - NE BORDER
			g.fillRect(0, 0, borderSize, canvasHeight); //NW - SW BORDER
			g.fillRect(0, (gameHeight + borderSize), canvasWidth, borderSize); //SW - SE BORDER
			g.fillRect((gameWidth + borderSize), 0, borderSize, canvasHeight); //NE - SE BORDER
			
		}
		
	}
	
	protected void gameDraw ()
	{	
			
		parent.gameDisplayCurrentPlayer("Its a draw");
		playerMatrix.setBoardLocked(true);
				
	}	
	
	protected void gameWon ()
	{
		
		if (isConnected)
		{				

			if (currentPlayer == meNetworkPlayer)
			{
				parent.gameDisplayCurrentPlayer("You Win");
			}
			else
			{
				parent.gameDisplayCurrentPlayer("You Lose");
			}
			
		}
		else
		{
			
			if (currentPlayer == PLAYER_ONE)
			{
				parent.gameDisplayCurrentPlayer("Player One Wins!");
			}
			else
			{
				parent.gameDisplayCurrentPlayer("Player Two Wins!");
			}
			
		}
		
		playerMatrix.setBoardLocked(true);
		
	}
	
	public int getMeNetworkPlayer ()
	{
		return meNetworkPlayer;
	}
	
	public Dimension getMinimumSize ()
	{
		return getPreferredSize();
	}
	
	public Dimension getPreferredSize ()
	{
		return new Dimension(canvasWidth, canvasHeight);
	}
	
	protected void moveMadeSwitchPlayers ()
	{

		if (currentPlayer == PLAYER_ONE)
		{
			currentPlayer = PLAYER_TWO;
		}
		else
		{
			currentPlayer = PLAYER_ONE;
		}

		displayCurrentPlayer();

	}
	
	public abstract void newGame ();	
	public abstract void paint (Graphics g);
	
	protected void resetGame ()
	{
		
		if ((isConnected && firstNetworkGame) || (!isConnected && firstGame))
		{
			
			firstTurnPlayer = PLAYER_ONE;
			firstGame = false;
			firstNetworkGame = false;
			
		}
		else if (firstTurnPlayer == PLAYER_ONE)
		{
			firstTurnPlayer = PLAYER_TWO;
		}
		else
		{
			firstTurnPlayer = PLAYER_ONE;
		}
		
		currentPlayer = firstTurnPlayer;
		displayCurrentPlayer();
		playerMatrix.setBoardLocked(false);
		playerMatrix.resetMatrix();
		
	}
	
	public void setupCanvasSize ()
	{
		
		canvasWidth = gameWidth + (borderSize * 2);
		canvasHeight = gameHeight + (borderSize * 2);
		
	}
	
	public void setConnected (boolean isConnected)
	{

		this.isConnected = isConnected;
		firstNetworkGame = isConnected;

	}
	
	public void setMeNetworkPlayer (int meNetworkPlayer)
	{
		this.meNetworkPlayer = meNetworkPlayer;
	}
		
	public void update (Graphics g)
	{
		paint(g);
	}
	
	public void mousePressed (MouseEvent e)
	{
	}

	public void mouseReleased (MouseEvent e)
	{
	}

	public void mouseEntered (MouseEvent e)
	{
	}

	public void mouseExited (MouseEvent e)
	{
	}

	public void mouseClicked (MouseEvent e)
	{
	}
	
	public void mouseMoved (MouseEvent e)
	{
	}
	
	public void mouseDragged (MouseEvent e)
	{
	}
	
}