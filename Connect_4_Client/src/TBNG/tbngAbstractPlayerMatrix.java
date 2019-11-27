package TBNG;

public abstract class tbngAbstractPlayerMatrix
{

	public static final int EMPTY = 0;
	protected int[][] boardMatrix = null;
	protected boolean boardLocked = false;
	
	public int getCellContents (int row, int column)
	{
		return boardMatrix[row][column];
	}		

	public boolean isBoardFull ()
	{
		
		for (int row = 0; row < boardMatrix.length; row++)
		{
			
			for (int column = 0; column < boardMatrix[row].length; column++)
			{
				
				if (boardMatrix[row][column] == EMPTY)
				{
					return false;
				}
				
			}
			
		}
		
		return true;
		
	}
	
	public boolean isBoardLocked ()
	{
		return boardLocked;
	}
	
	public abstract boolean isLegalMove (int row, int column, int piece);
	public abstract boolean isWinner (int row, int column, int piece);	
	public abstract void resetMatrix ();
	public abstract void setCellContents (int row, int column, int piece);
	
	public void setBoardLocked (boolean boardLocked)
	{
		this.boardLocked = boardLocked;
	}
	
}