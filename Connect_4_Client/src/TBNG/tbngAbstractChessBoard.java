package TBNG;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseListener;

public abstract class tbngAbstractChessBoard extends tbngAbstractPlayingArea
{
	
	protected final int totalRows = 8, totalColumns = 8;
	
	protected int CELLSIZE = 50;
	
	protected Color clrBlackSquare = new Color(0, 0, 0);
	protected Color clrWhiteSquare = new Color(255, 255, 255);
	
	public tbngAbstractChessBoard (tbngGameInterface parent)
	{
		
		super(parent);
		setupBoardSize();
		
	}

	//Since a chess board is a square, this method can be used to get
	//the row and column
	protected int getMouseCell (int coor)
	{
		
		int fromBorder = borderSize;
		
		for (int cell = 0; cell < totalRows; cell++)
		{
			
			if (coor > fromBorder && coor < (fromBorder + CELLSIZE))
			{
				return cell;
			}
			
			fromBorder += CELLSIZE;
			
		}
		
		return -1;
		
	}
	
	public void setCellSize (int CELLSIZE)
	{
		
		this.CELLSIZE = CELLSIZE;
		setupBoardSize();
		
	}
	
	private void setupBoardSize ()
	{
		
		gameHeight = CELLSIZE * totalRows;
		gameWidth = CELLSIZE * totalColumns;
		setupCanvasSize();
		
	}
	
	protected void drawEmptyBoard (Graphics g)
	{
		
		drawFrame(g);
		
		int fromX = borderSize;
		int fromY = borderSize;
		
		for (int row = 0; row < totalRows; row++)
		{
			
			for (int column = 0; column < totalColumns; column++)
			{

				drawTile(row, column, fromX, fromY, g);				
				fromX += CELLSIZE;
				
			}
			
			fromX = borderSize;
			fromY += CELLSIZE;
			
		}
		
	}
	
	protected void drawTile (int row, int column, int fromX, int fromY, Graphics g)
	{
		
		if ((column + 1) % 2 == (row + 1) % 2)		
		{
			g.setColor(clrWhiteSquare);
		}
		else
		{
			g.setColor(clrBlackSquare);
		}
		
		g.fillRect(fromX, fromY, CELLSIZE, CELLSIZE);
		
	}		
	
}