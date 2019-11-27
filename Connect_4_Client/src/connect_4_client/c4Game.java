package connect_4_client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import TBNG.tbngAbstractGrid;
import TBNG.tbngGameInterface;

public class c4Game extends tbngAbstractGrid
{
	
	public static final int numOfRows = 6, numOfColumns = 8;
	
	private int cellBorderOffset = 5; //How far from a cell's border should the naught/cross be drawn from, the margin/padding
	private int coinHolderWidth = 7; //How big the coin holder is
	private int outsideGridBorder = 15; //How much gap is between the grid and the game canvas border

	private final Color clrRedPiece = new Color(255, 0, 0);
	private final Color clrBluePiece = new Color(0, 0, 255);
	private final Color clrCellBorder = new Color(6, 79, 167);

	private int numMovesMade = 0; //If get up to 48 then the game is a draw	
	private int columnMouseOver = -1;
	
	private boolean onlyPaintingHangingCoin = false; //When true, only paints the hanging coin, saves drawing time

	public c4Game (tbngGameInterface parent)
	{

		super(parent, numOfRows, numOfColumns);
		playerMatrix = new c4PlayerMatrix(numOfRows, numOfColumns);
		
		borderSize = 0;		
		clrGridBackground = new Color(75, 141, 221);
		
		gameHeight = gridHeight + (outsideGridBorder * 2);
		gameWidth = gridWidth + (outsideGridBorder * 2);
		gridStartX = outsideGridBorder;
		gridStartY = outsideGridBorder;
		
		setupCanvasSize();

	}	
		
	private boolean canMove ()
	{
		
		if (isConnected && (currentPlayer != meNetworkPlayer))
		{
			return false;
		}
		else if (playerMatrix.isBoardLocked())
		{
			return false;
		}
		
		return true;
		
	}	

	public void decodeNetworkMove (String strMove)
	{

		String[] strKeyValue = strMove.split("=");
		doMove(Integer.parseInt(strKeyValue[1]));

	}

	private void doMove (int column)
	{

		numMovesMade++;
		playerMatrix.setCellContents(-1, column, currentPlayer);
		boolean win = playerMatrix.isWinner(-1, column, currentPlayer);

		//If this move was made by you, then send it to the other player
		if (isConnected && currentPlayer == meNetworkPlayer)
		{
			parent.gameEncodedMove("column=" + column);
		}

		if (win)
		{
			gameWon();			
		}
		else if (numMovesMade >= 48)
		{
			gameDraw();
		}
		else
		{
			moveMadeSwitchPlayers();
		}
		
		repaint();

	}
	
	private void drawCell (int fromX, int fromY, Graphics g)
	{
		
		g.setColor(clrCellBorder);
		
		g.fillRect(fromX, fromY, CELLSIZE, coinHolderWidth); //NW - NE
		g.fillRect(fromX, (fromY + (CELLSIZE - coinHolderWidth)), CELLSIZE, coinHolderWidth); //SW - SE
		g.fillRect(fromX, fromY, coinHolderWidth, CELLSIZE); //NW - SW
		g.fillRect((fromX + (CELLSIZE - coinHolderWidth)), fromY, coinHolderWidth, CELLSIZE); //NE - SE
		
	}
	
	private void drawPiece (int fromX, int fromY, Graphics g, int piece)
	{
		
		if (piece == PLAYER_ONE)
		{
			g.setColor(clrRedPiece);
		}
		else
		{
			g.setColor(clrBluePiece);
		}
		
		int size = CELLSIZE - (cellBorderOffset * 2);
		g.fillRoundRect((fromX + cellBorderOffset), (fromY + cellBorderOffset), size, size, size, size);

	}
	
	private boolean canDrawHangingCoin ()
	{
		
		if (!canMove() || columnMouseOver == -1)
		{
			return false;
		}
		
		return true;
		
	}		
		
	private void drawHangingCoin (Graphics g)
	{
		
		if (!canDrawHangingCoin())
		{
			return;
		}
		else if (!playerMatrix.isLegalMove(-1, columnMouseOver, 0))
		{
			return;
		}
		
		int fromX = gridStartX + borderSize + (columnMouseOver * CELLSIZE);
		int fromY = gridStartY - outsideGridBorder;
		
		drawPiece(fromX, fromY, g, currentPlayer);
		
	}		
	
	private int getMouseOverColumn (int x, int y)
	{
		//If not inside the grid
		if (y < (gridStartY + borderSize) || y > (gridStartY + borderSize + gridHeight))
		{
			return -1;
		}
		
		int fromX = gridStartX + borderSize;		
		
		for (int column = 0; column < numOfColumns; column++)
		{
			
			if (x > fromX && x < (fromX + CELLSIZE))
			{
				return column;
			}
			
			fromX += CELLSIZE;
			
		}
		
		return -1;
		
	}

	public void mouseMoved (MouseEvent e)
	{
		
		int column = getMouseOverColumn(e.getX(), e.getY());
		
		if (column == columnMouseOver)
		{
			return;
		}
		
		columnMouseOver = column;
		
		if (canDrawHangingCoin())
		{
			
			onlyPaintingHangingCoin = true;
			repaint();	
					
		}
		
	}

	public void mouseClicked (MouseEvent e)
	{	
		
		columnMouseOver = getMouseOverColumn(e.getX(), e.getY());
		
		if (canDrawHangingCoin() && playerMatrix.isLegalMove(-1, columnMouseOver, 0))
		{
			doMove(columnMouseOver);
		}

	}

	public void newGame ()
	{
		
		numMovesMade = 0;		
		resetGame();
		repaint();

	}

	public void paint (Graphics g)
	{
		
		int fromX = gridStartX + borderSize;
		int fromY = gridStartY + borderSize;
		int cellContents = 0;
		
		if (onlyPaintingHangingCoin)
		{
			g.setClip(0, 0, canvasWidth, (fromX + CELLSIZE));				
		}
		
		drawGrid(g);	
		drawHangingCoin(g);
		
		for (int row = 0; row < numRowCells; row++)
		{
						
			for (int column = 0; column < numColumnCells; column++)
			{
				
				cellContents = playerMatrix.getCellContents(row, column);				
				
				if (cellContents != 0)
				{
					drawPiece(fromX, fromY, g, cellContents);
				}
				
				drawCell(fromX, fromY, g);
				fromX += CELLSIZE;
				
			}
			
			if (onlyPaintingHangingCoin)
			{
				break;
			}
			
			fromX = gridStartX + borderSize;
			fromY += CELLSIZE;
			
		}
		
		onlyPaintingHangingCoin = false;
		
	}
	
}