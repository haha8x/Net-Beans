package TBNG;

import java.awt.Graphics;
import java.awt.Color;

public abstract class tbngAbstractGrid extends tbngAbstractPlayingArea
{
	
	protected Color clrGridBackground = new Color(255, 255, 255);
	
	protected int numRowCells = 0, numColumnCells = 0;
	protected int gridWidth = 0, gridHeight = 0; //The game might contain more then a grid, thus this is the size of the grid only
	protected int gridStartX = 0, gridStartY = 0; //Where the grid starts, important if the game contains more then the grid
	protected int CELLSIZE = 50; //Size of a cell (one dimension)
	
	public tbngAbstractGrid (tbngGameInterface parent, int rows, int columns)
	{
		
		super(parent);
		setNumCells(rows, columns);
			
	}
	
	protected void drawGrid (Graphics g)
	{
		
		drawFrame(g);
		
		int fromX = gridStartX + borderSize;
		int fromY = gridStartY + borderSize;

		g.setColor(clrGridBackground);
		g.fillRect(fromX, fromY, gridWidth, gridHeight);
		
	}

	public void setCellSize (int CELLSIZE)
	{
		
		this.CELLSIZE = CELLSIZE;
		setupGridSize();
		
	}
	
	private void setupGridSize ()
	{
		
		gridWidth = CELLSIZE * numColumnCells;
		gridHeight = CELLSIZE * numRowCells;		
		
		if (gridHeight > gameHeight || gridWidth > gameWidth)
		{
			
			gameWidth = gridWidth;
			gameHeight = gridHeight;			
			gridStartX = 0;
			gridStartY = 0;
			
		}
		
		setupCanvasSize();
		
	}
	
	protected void setNumCells (int numRowCells, int numColumnCells)
	{
		
		this.numRowCells = numRowCells;
		this.numColumnCells = numColumnCells;
		
		setupGridSize();
		
	}
	
}