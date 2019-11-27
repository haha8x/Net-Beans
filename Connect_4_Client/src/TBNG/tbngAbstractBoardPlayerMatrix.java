package TBNG;

public abstract class tbngAbstractBoardPlayerMatrix extends tbngAbstractPlayerMatrix
{
	
	public tbngAbstractBoardPlayerMatrix ()
	{
	}

	public boolean isCellEmpty (int row, int column)
	{
		return boardMatrix[row][column] == EMPTY;
	}
	
	public abstract boolean isWinner (int row, int column, int piece);
	
	public void resetMatrix ()
	{
		
		for (int row = 0; row < boardMatrix.length; row++)
		{
			
			for (int column = 0; column < boardMatrix[row].length; column++)
			{
				boardMatrix[row][column] = EMPTY;
			}
			
		}
		
	}
	
	public void setCellContents (int row, int column, int piece)
	{
		boardMatrix[row][column] = piece;
	}
	
}