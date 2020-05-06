
public class Accumalation {
	private int[][] occupy;	// each value is type
	
	public Accumalation()
	{
		occupy = new int[40][10];
		for (int r = 0; r < 40; r++)
		{
			for (int c = 0; c < 10; c++)
			{
				occupy[r][c] = -1;
			}
		}
	}
	
	public void receievePiece(Piece p)
	{
		boolean[][] occupy_p = p.getOccupy();
		for (int r = 0; r < occupy_p.length; r++)
		{
			for (int c = 0; c < occupy_p[0].length; c++)
			{
				if (occupy_p[r][c])
				{
					occupy[r + p.getRow()][c + p.getCol()] = p.getType();
				}
			}
		}
	}
	
	public int clearedLines()
	{
		int linesCleared = 0;
		for (int r = 0; r < 40; r++)
		{
			boolean fullLine = true;
			for (int c = 0; c < 10; c++)
			{
				fullLine = fullLine && (occupy[r][c] != -1);
			}
			if (fullLine)
			{
				linesCleared++;
				int[] freshLine = new int[10];
				for (int i = 0; i < 10; i++)
				{
					freshLine[i] = -1;
				}
				for (int rr = r; rr >= 0; rr--)
				{
					for (int cc = 0; cc < 10; cc++)
					{
						if (rr == 0)
							occupy[rr][cc] = freshLine[cc];
						else
							occupy[rr][cc] = occupy[rr - 1][cc];
					}
				}
				
			}
		}
		return linesCleared;
	}
	
	public int[][] getOccupy()
	{
		return occupy;
	}
}
