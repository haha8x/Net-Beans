package connect_4_client;

import TBNG.tbngAbstractPlayerMatrix;

public class c4PlayerMatrix extends tbngAbstractPlayerMatrix
{
	
	private int totalRows = 6, totalColumns = 8;
	private int columnHeights[];
	
	public c4PlayerMatrix (int rows, int columns)
	{
		
		boardMatrix = new int[rows][columns];
		columnHeights = new int[columns];
		resetMatrix();
		
	}	
		
	private boolean checkBackwardDig (int row, int column, int piece)
	{
		
		int foundCoins = 0;
		int startRow = 0;
		int startColumn = 0;
		
		if (row == column)
		{
			
			startColumn = 0;
			startRow = 0;
			
		}
		else if (row < column)
		{
			
			startColumn = column - row;
			startRow = 0;
			
		}
		else //row > column
		{
			
			startColumn = 0;
			startRow = row - column;
			
		}
		
		//Check NW - SE dig	
		for (; startRow < totalRows && startColumn < totalColumns; startRow++, startColumn++)
		{
			
			if (boardMatrix[startRow][startColumn] == piece)
			{
				foundCoins++;
			}
			else
			{
				foundCoins = 0;
			}
			
			if (foundCoins >= 4)
			{
				return true;
			}
			
		}
		
		return false;
		
	}
	
	private boolean checkColumn (int row, int column, int piece)
	{
		
		int foundCoins = 0;
		
		for (int chkRow = 0; chkRow < totalRows; chkRow++)
		{
			
			if (boardMatrix[chkRow][column] == piece)
			{
				foundCoins++;
			}
			else
			{
				foundCoins = 0;
			}
			
			if (foundCoins >= 4)
			{
				return true;
			}
			
		}
		
		return false;
		
	}
	
	private boolean checkForwardDig (int row, int column, int piece)
	{
		
		int foundCoins = 0;
		int startRow = 0;
		int startColumn = 0;
		
		if (row == column)
		{

			startColumn = 0;
			startRow = (totalRows - 1);
			
		}
		else if ((totalRows - 1) - row < column)
		{

			startColumn = column - ((totalRows - 1) - row);
			startRow = (totalRows - 1);
			
		}
		else //row > column
		{

			startColumn = 0;
			startRow = row + column;
			
		}		
	
		//Check SW - NE dig
		for (; startRow >= 0 && startColumn < totalColumns; startRow--, startColumn++)
		{

			if (boardMatrix[startRow][startColumn] == piece)
			{			
				foundCoins++;
			}
			else
			{
				foundCoins = 0;
			}
			
			if (foundCoins >= 4)
			{
				return true;
			}
			
		}
		
		return false;
		
	}
	
	private boolean checkRow (int row, int column, int piece)
	{
		
		int foundCoins = 0;
		
		for (int chkColumn = 0; chkColumn < totalColumns; chkColumn++)
		{
			
			if (boardMatrix[row][chkColumn] == piece)
			{
				foundCoins++;
			}
			else
			{
				foundCoins = 0;
			}
			
			if (foundCoins >= 4)
			{
				return true;
			}
			
		}
		
		return false;
		
	}	
	
	public boolean isLegalMove (int row, int column, int piece)
	{
		return columnHeights[column] < totalRows;
	}
	
	public boolean isWinner (int row, int column, int piece)
	{
		
		row = (totalRows - 1) - (columnHeights[column] - 1);		
		return (checkRow(row, column, piece) || checkColumn(row, column, piece) || checkForwardDig(row, column, piece) || checkBackwardDig(row, column, piece));
		
	}

	public void resetMatrix ()
	{
		
		for (int row = 0; row < totalRows; row++)
		{
			
			for (int column = 0; column < totalColumns; column++)
			{
				boardMatrix[row][column] = EMPTY;
			}
			
		}
		
		for (int column = 0; column < columnHeights.length; column++)
		{
			columnHeights[column] = 0;
		}
		
	}
	
	public void setCellContents (int row, int column, int piece)
	{
		
		row = (totalRows - 1) - columnHeights[column];		
		boardMatrix[row][column] = piece;
		columnHeights[column]++;
		
	}
	
}