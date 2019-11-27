/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package connect_4_server;

/**
 *
 * @author Mr Haha
 */
import javax.swing.*;
	import javax.swing.filechooser.*;
	import javax.swing.border.*;

import java.awt.*;
	import java.awt.event.*;
	import java.awt.image.*;
	import java.awt.geom.*;

import java.util.*;
import java.net.*;
import java.io.*;

public class Connect_4_Server
{	public static void main(String[] inArgs)
	{	MainFrame x = new MainFrame();
		x.setVisible(true);
	}
}

class MainFrame extends JFrame implements Serializable 	 
{	JPanel mainPan;
		InputPanel inPanel;
		ImagePanel imagePanel;

	Game game;	
		GameState gameState;				// Stores saome game related variables - could be subsumed into the Game class

	private static final long serialVersionUID = 1L;	// seriaisable - put in to avoid overly prescriptive compilers thowing a wobbly
	private int data;

	public MainFrame()
	{	gameState = new GameState();	
		game = new Game(gameState);		// initiates a game on construction

		mainPan = new JPanel();						

		imagePanel = new ImagePanel(game, gameState, (new ProgBorder()).getProgBorder());
		inPanel = new InputPanel(game, (new ProgBorder()).getProgBorder(), imagePanel);

			mainPan.setLayout(new BorderLayout(5,5));
			mainPan.add(imagePanel, BorderLayout.CENTER);
			mainPan.add(inPanel, BorderLayout.EAST);

		getContentPane().add(mainPan);
		setSize(800, 600);
		setResizable(false);
		setLocation(100, 50);
		setVisible(true);
	}

	public void SerializeMe (int data) 
	{        this.data = data;
    	}

    	public int getData() 
	{        return data;
    	}
}

class ImagePanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener, Serializable 
{	BackgroundImage panelBGImage;
	MainFrame scopeObject;

	Game game;
		GameState gameState;
		BoardAnalysis boardAnalysisX;
		C4Board board;

//  -------------------------------------------for drawing the grid:
	int columnGap;
	int rowGap;
	int boardWidth;
	int boardHeight;
//  -------------------------------------------

// -------------------------------------------below for animating coin drop:
	boolean animatingCoinDrop;
	int animationColumn;
	int animationRow;
	int animationFrame;
	int animationStartPosition;
	double animationFrameAcceleration;
// -------------------------------------------

//  -------------------------------------------below control mouse pointer which is the ball about to be dropped - pointer position is limited.
	boolean drawPointerOnMouse = false;			// when mouse is in critical zone a piece is shown around it.
	int mousePointerHorizontalPosition = 0;			// 
	int mousePointerVerticalPosition = 0;			// this and above are all set on mouseMove
//  -------------------------------------------

//  -------------------------------------------below are used to monitor the game situation and animate accordingly
		int winAnimationFrame;
		int ovalWidth;
		int ovalWidthModifier = 1;
		boolean winFirstDetection = true;

	int moveResult;
//  -------------------------------------------

//  -------------------------------------------below to constructor for sundry draw relted variables:
	Image dbImage;           				// for double buffering
	boolean useDoubleBuffering = true;			// hard-coded for now, but is built to be switchable (cos I like comparing!).

//  -------------------------------------------gradient used for drawing pointers.  There are 3 different pointer fills:
	GradientPaint gpBlue;					//	for the blue player's moves
	GradientPaint gpRed;					//	for the red player's moves
	GradientPaint gpGreen;					// 	for instances where the pointer is shown but no valid move can be made

// --------------------------------------------------------seriaisable:
	private static final long serialVersionUID = 1L;
	private int data;

	public ImagePanel(Game gm, GameState gs, Border bord)
	{	game = gm;
			gameState = gs;
			boardAnalysisX = game.getBoardAnalysis();
			board = game.getC4Board();

		setLayout(new GridLayout(1,1,5,5));	// a single cell
		setBorder(bord);
		addMouseListener(this);
		addMouseMotionListener(this);
		panelBGImage = new BackgroundImage (this.getToolkit());
		setBoardParameters();						// imposes min width / height and sets ovalWidth, rowGap and columnGap 
		//ovalWidth = 4*(columnGap/5)-2;					// NOTE - leave here - else set in its own method.
		initiateAnimationState();			// set the initial parameters for the coin animation

	}

	private void setWinFirstDetection(boolean w)
	{	winFirstDetection = w;
	}

	public void initiateAnimationState()
	{	ovalWidth = 4*(columnGap/5)-2;					// NOTE - leave here - else set in its own method.
		setAnimationState(false, 0,0,0);
		winFirstDetection = true;
		winAnimationFrame=0;
	}

	public void setAnimationState(boolean b, int col, int rowBegin, int a)
	{	//------------------------------------------- variables below are reset everytime a coin is dropped:
		animationFrame = 0;
		animationFrameAcceleration = 1;
		//-------------------------------------------

		animationColumn = col;
		animatingCoinDrop = b;
		animationStartPosition = rowBegin;				// this is the position of the mouse pointer - drops from the pointer
		animationRow = a;						// last move in Game is the one we are animating.
	}

	public void effectDoubleBuffering(Graphics2D g2)
	{	if (dbImage == null ) dbImage = createImage(getWidth(),getHeight());

		Graphics2D offG = (Graphics2D)(dbImage.getGraphics()); 		//clear the screen
		makeNextFrame(offG);					            	// Put the dbImage image on the screen.
		g2.drawImage(dbImage , 0, 0, null);
	}

	public void paint(Graphics g) 
	{	Graphics2D g2 = (Graphics2D)g;
		if(useDoubleBuffering) effectDoubleBuffering(g2);
		else makeNextFrame(g2);
		repaint();
	}

	public void makeNextFrame(Graphics2D g2)
	{	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);	
		paintImageOnBoard(g2);
		drawGameBoard(g2);
	}

	public void paintImageOnBoard(Graphics2D gr2)
	{	gr2.setPaint(panelBGImage.getBackgroundPaint());
		gr2.fillRect(0,0,getWidth(),getHeight());
		(panelBGImage.getBufferedImage()).createGraphics().drawImage(panelBGImage.getBackgroundImage(),0,0,null);
	}

	private void drawGameBoard(Graphics2D g2)
	{	setBoardParameters();

		if(animatingCoinDrop) animateCoinDrop(g2);
		if(drawPointerOnMouse) drawMousePointer(g2);

		drawGridLines(g2);
		drawCounters(g2);

		if((gameState.getWinDetected())&&(!animatingCoinDrop))				// wait until counter has dropped before animating win
		{	game.setAnimatingWin(true);
		 	animateWin(g2);			
		}

		if(winAnimationFrame==GameConstants.WIN_ANIMATION_FRAMES)
		{	gameState.setGameOver(true);
			game.setAnimatingWin(false);				// used to deactivate Reset button during win animation
		}
	}

	private void animateWin(Graphics2D g2)
	{	int textAnimationInt;		// used to control movement of text and text box across screen (want to stop in middle)
		int counterAnimationInt;	

		// we need to remove the winning counters from the board (so they don't get drawn there as well as here):
		game.clearWinningPiecesFromBoard();

		if(winAnimationFrame>250) textAnimationInt = 250;
		else textAnimationInt = winAnimationFrame;

//		g2.setPaint(getGreenGradientPaint());

		if(game.whoseMove()==GameConstants.PLAYER1)	g2.setPaint(getRedGradientPaint());	
		else g2.setPaint(getBlueGradientPaint());


		// we want the winning pieces to spin - code below sorts this:-----------------------------------------------
		// ovalWidthModifier begins at 1 but ovalWidth begins at (4*cg/5), so first if is bypassed and ovalWidthMod is set to 0
		// oval width increases until oval is full sized (4*cg/5) - 60 at present.
		if((ovalWidth== 4*(columnGap/5))&&(winAnimationFrame<GameConstants.WIN_ANIMATION_FRAMES)) ovalWidthModifier = 1;

		// when oval width hits (4*cg/5) = 60 the width of the oval will decrease until it again hits 60.
		if(ovalWidth== 0) ovalWidthModifier = 0;


		if(ovalWidthModifier==1)	ovalWidth = winAnimationFrame%(4*(columnGap/5));
		if(ovalWidthModifier==0)	ovalWidth = 4*(columnGap/5) - winAnimationFrame%(4*(columnGap/5));


		if (ovalWidth==60) ovalWidth = 0;
		else if(ovalWidth==0) ovalWidth = 60;

		//-----------------------------------------------------------------------------------------------------------

		for(int drawLoop = 0; drawLoop< game.getNumberOfWinningPieces(); drawLoop++)
		{
			g2.fillOval
			(	columnGap/2 + columnGap*(game.getWinNodeCol(drawLoop)) + columnGap/14 + (columnGap/2-ovalWidth/2),	
				(GameConstants.BOARD_TOP_BORDER+rowGap*(GameConstants.NUMBER_OF_ROWS 
					- 1 - game.getWinNodeRow(drawLoop)) + rowGap/7) ,
				ovalWidth,
				(4*rowGap/5)
			);

		}

		if(winAnimationFrame==GameConstants.WIN_ANIMATION_FRAMES)	// when game is won a token is added to winning pieces
		{	for(int drawLoop = 0; drawLoop< game.getNumberOfWinningPieces(); drawLoop++)
			{	g2.setPaint(Color.white);
				g2.setFont(new Font("Arial", Font.BOLD, 12));
				g2.drawString(	"Win!",
					columnGap/2 + columnGap*(game.getWinNodeCol(drawLoop)) + columnGap/14 + columnGap/2 - 10, 
					GameConstants.BOARD_TOP_BORDER+rowGap*(GameConstants.NUMBER_OF_ROWS 
					- 1 - game.getWinNodeRow(drawLoop)) + 4*rowGap/7);
			} 

		}
	
		// below a hacky solution to the issue of flicker in the ovals - modular div letting me down on boundaries.  Below
		// ensures that there is a smooth transition between boundaries.
		if (ovalWidth==0) ovalWidth = 60;
		else if((ovalWidth==60)&&(winAnimationFrame<GameConstants.WIN_ANIMATION_FRAMES)) ovalWidth = 0;
		//----------------------------------------------------------------------------------------------------------------
	
		animateGameOverText(g2, textAnimationInt);
		if(winAnimationFrame<GameConstants.WIN_ANIMATION_FRAMES) winAnimationFrame++;
	}

	private void animateGameOverText(Graphics2D g2, int textAnimationInt)
	{	// Game Over copy below - moves from right to left until it is centralised:---------------------
		g2.setPaint(Color.gray);
		g2.fillRect(0+textAnimationInt,250, 200, 100);
		g2.setPaint(Color.white);
		g2.setFont(new Font("Arial", Font.BOLD, 28));
		g2.drawString("Game over", 28+textAnimationInt, 280);
		if(game.whoseMove()==GameConstants.PLAYER1)	g2.setPaint(getRedGradientPaint());	
		else g2.setPaint(getBlueGradientPaint());
		g2.setFont(new Font("Arial", Font.BOLD, 22));
		g2.drawString(getMoveResultText(moveResult), 18+textAnimationInt, 320);
	}

	private void animateCoinDrop(Graphics2D g2)		// shows the coin dropping to its resting point.  As coin drops user input is frozen.
	{
		if(gameState.getGameOver()) 
		{	game.resetLastMove();	
			game.setAnimatingWin(false);
		}
		else
			{
			animatingCoinDrop = (animationStartPosition - rowGap/2 + animationFrame < GameConstants.BOARD_TOP_BORDER+(-1*(animationRow-4))*rowGap);

			if(animatingCoinDrop)
			{	animationFrameAcceleration = 1.1*animationFrameAcceleration;
				animationFrame = animationFrame + ((int)(animationFrameAcceleration));

				if(game.whoseMove()==GameConstants.PLAYER1)	g2.setPaint(getRedGradientPaint());		// colours here appear 'inverted' - this is because the move
				else 						g2.setPaint(getBlueGradientPaint());	// has actually been made, so animation is for last move
				//------- now draw the coin:
				g2.fillOval((columnGap/2 + columnGap*animationColumn+ columnGap/7), 
						(animationStartPosition - rowGap/2+ animationFrame),
						 4*columnGap/5,4*rowGap/5);
				}
			else  game.resetLastMove();
			}
	}

	private GradientPaint getBlueGradientPaint()		
	{	if(gpBlue==null) gpBlue = new GradientPaint(75,75, new Color(0,0,255), 100,100, new Color(0,0,200), true);
		return gpBlue;
	}

	private GradientPaint getRedGradientPaint()
	{	if(gpRed==null) gpRed = new GradientPaint(75,75, new Color(220,0,0), 100,100, new Color(150,0,0), true);
		return gpRed;
	}

	private GradientPaint getGreenGradientPaint()
	{	if(gpGreen==null) gpGreen = new GradientPaint(75,75, new Color(0,255,0), 100,100, new Color(0,150,0), true);
		return gpGreen;
	}


	public void drawMousePointer(Graphics2D g2)
	{	g2.setPaint(Color.white);
		g2.drawOval(mousePointerHorizontalPosition*columnGap+columnGap/2 + columnGap/7, 
				mousePointerVerticalPosition-rowGap/2,4*columnGap/5,4*rowGap/5);

		if(!animatingCoinDrop)
		{

			if(game.whoseMove()==GameConstants.PLAYER1) g2.setPaint(getBlueGradientPaint());
			if(game.whoseMove()==GameConstants.PLAYER2) g2.setPaint(getRedGradientPaint());

//----------------------Solid colours for the counters below:------------------------------------------------------
//			if(game.whoseMove()==GameConstants.PLAYER1)			g2.setPaint(Color.blue);
//			if(game.whoseMove()==GameConstants.PLAYER2)			g2.setPaint(Color.red);
//-----------------------------------------------------------------------------------------------------------------

			if (!(game.isSpaceInColumn(mousePointerHorizontalPosition))) 	g2.setPaint(Color.orange);
			g2.fillOval(mousePointerHorizontalPosition*columnGap+columnGap/2 + columnGap/7, 
				mousePointerVerticalPosition-rowGap/2,4*columnGap/5,4*rowGap/5);
		}
	}

	private void drawCounters(Graphics2D g2)	
	{	for(int colLoop = 0; colLoop< GameConstants.NUMBER_OF_COLS; colLoop++)	// draw all lines and the coins that have already been placed
		{	for(int rowLoop = 0; rowLoop< GameConstants.NUMBER_OF_ROWS ; rowLoop++)
			{	if(game.getPlayerIDAt(colLoop,GameConstants.NUMBER_OF_ROWS-1-rowLoop)>0)
				{	//------- first set the colour:
					if(game.getPlayerIDAt(colLoop,GameConstants.NUMBER_OF_ROWS-1-rowLoop)==GameConstants.PLAYER1)	
					g2.setPaint(getBlueGradientPaint());
				else 
					g2.setPaint(getRedGradientPaint());	
					//------- now draw the coin:
					g2.fillOval((columnGap/2 + columnGap*colLoop+ columnGap/7),	
						(GameConstants.BOARD_TOP_BORDER+rowGap*rowLoop + rowGap/7) ,4*columnGap/5,4*rowGap/5);
				}
			}
		}
	}

	private void drawGridLines(Graphics2D g2)
	{	for(int colLoop = 0; colLoop< GameConstants.NUMBER_OF_COLS; colLoop++)	// draw all lines and the coins that have already been placed
		{	drawLines(g2, colLoop, GameConstants.VERTICAL_LINES);
			for(int rowLoop = 0; rowLoop< GameConstants.NUMBER_OF_ROWS ; rowLoop++)
			{	if(colLoop==0)	drawLines(g2, rowLoop, GameConstants.HORIZONTAL_LINES);	// only draw them once
			}
		}
	}

	private void drawLines(Graphics2D g2, int locationID, int lineType)
	{	if (lineType == GameConstants.VERTICAL_LINES)
		{	g2.setPaint(Color.black);
			g2.fillRect((columnGap/2 + columnGap*locationID),GameConstants.BOARD_TOP_BORDER/2 , 
					7, this.getHeight() - 2*GameConstants.BOARD_TOP_BORDER/3);
			g2.fillRect((columnGap/2 + columnGap*GameConstants.NUMBER_OF_COLS),	
					GameConstants.BOARD_TOP_BORDER/2 , 7, this.getHeight() - 2*GameConstants.BOARD_TOP_BORDER/3);
			// The latter fillRect draws the last line - it does draw too many times though
		}

		if (lineType == GameConstants.HORIZONTAL_LINES)
		{	g2.setPaint(Color.gray);	
			g2.fillRect(columnGap/2,GameConstants.BOARD_TOP_BORDER+rowGap*locationID,GameConstants.NUMBER_OF_COLS*columnGap,6);
		}
	}


	public String getMoveResultText(int moveResult)
	{	switch(moveResult)
		{	case(GameConstants.LEGAL_MOVE_GAME_ON):		return "legal move made - game on";
			case(GameConstants.ILLEGAL_MOVE):		return "illegal move attempted ";
			case(GameConstants.GAME_OVER_PLAYER1_WINS):	return "Blue player wins";
			case(GameConstants.GAME_OVER_PLAYER2_WINS):	return "Red player wins";
			case(GameConstants.GAME_OVER_DRAW):		return "DRAW!";
		}
		return "legal move made - game on";
	}

	private void setBoardParameters()	// board is dynamic - based on size of panel , but minimum dimensions are required:
	{	if (this.getWidth()<250) boardWidth = 250;
		else boardWidth = this.getWidth();

		if (this.getHeight()<250) boardHeight = 250;
		else boardHeight = this.getHeight();

			setGaps(boardWidth, boardHeight);
	}

	private void setGaps(int w, int h)	// need to know location of columns to identify a move by the click.
	{	columnGap = w/(GameConstants.NUMBER_OF_COLS+1);
		rowGap = (h-GameConstants.BOARD_TOP_BORDER)/(GameConstants.NUMBER_OF_ROWS);
	//	ovalWidth = 4*(columnGap/5)-2;
	}

	public void changeImage(String s)		// NOTE - cannot be called as the input panel does not currently
	{	panelBGImage.setBackgroundImage(s);	// have scope of this panel.
	}




	public void mouseMoved(MouseEvent mm)	// if the mouse is in the upper part of the screen a piece will be shown
	{	drawPointerOnMouse = (mm.getY() < (GameConstants.BOARD_TOP_BORDER + columnGap/2));			
		mousePointerHorizontalPosition = ((mm.getX()-columnGap/2)/ columnGap);	// drawn in centre of column - not centred over the mouse
		mousePointerVerticalPosition = mm.getY();				// rem - must be fixed place per column (no overlapping allowed).
	}

	public void mouseDragged(MouseEvent md)
	{}

	public void mouseClicked(MouseEvent m1)
	{	// need to find out which column has been clicked on then post it as a move to Game.
		if(gameState.getWinDetected()) 
		{		//donothing
		}
		else
		{	if (!animatingCoinDrop )	// only want input when no coins are currently dropping.
			{	int colClicked = 0;
				moveResult = GameConstants.LEGAL_MOVE_GAME_ON;		// default assumption is move is legal (arbitrary though)
	
				// user must click around the top of the grid in the column they wish the coin to drop.
				// limited area but will be drawn on later.

				if(m1.getY() < GameConstants.BOARD_TOP_BORDER + columnGap/2)			// large area at top for clicking to make move
				{	colClicked = ((m1.getX()-columnGap/2)/ columnGap);
	
					if (game.isSpaceInColumn(colClicked )) 	moveResult = game.makeAMove(colClicked);

					if (!(moveResult == GameConstants.ILLEGAL_MOVE))
					{	if((moveResult == GameConstants.GAME_OVER_PLAYER1_WINS)||(moveResult == GameConstants.GAME_OVER_PLAYER2_WINS)) 
						gameState.setWinDetected(true);
						setAnimationState(true, colClicked, m1.getY(), game.getLastMoveRow()); 	// i.e. is legal so animate the drop		
					}
				}
			}
		}
	}
	public void mouseEntered(MouseEvent m2)		{}
	public void mouseExited(MouseEvent m3)		{}
	public void mousePressed(MouseEvent m4)		{}
	public void mouseReleased(MouseEvent m5)	{}
	public void keyPressed(KeyEvent e)		{}
	public void keyReleased(KeyEvent e) 		{}
	public void keyTyped(KeyEvent e)  		{}

	public void SerializeMe (int data) 
	{        this.data = data;
    	}

    	public int getData() 
	{        return data;
    	}
}

class InputPanel extends JPanel implements ActionListener, Serializable
{	Game game;
	ImagePanel imagePanel;

// --------------------------------------------------------seriaisable:
	private static final long serialVersionUID = 1L;
	private int data;

	public InputPanel (Game g, Border bord, ImagePanel i)
	{	game = g;
		imagePanel = i;
		setBorder(bord);
		setLayout(new GridLayout(8,1,5,5));
		setBackground(new Color(255,255,255));

		JButton ex  = new JButton("Exit");
			ex.addActionListener(this);

		JButton reset  = new JButton("Reset");
			reset.addActionListener(this);

		add(ex);
		add(reset);
	}

	public void actionPerformed(ActionEvent ae)
	{	String buttonStr  = ((JButton)(ae.getSource())).getText();
		if (buttonStr.equals("Exit"))	System.exit(0);
		if (buttonStr.equals("Reset"))
			if(game.getAnimatingWin())
			{	// do nothing - reset deactivated
			}
			else
			{	imagePanel.initiateAnimationState();
				game.resetGame();
			}
	}

	public void SerializeMe (int data) 
	{        this.data = data;
    	}

    	public int getData() 
	{        return data;
    	}
}

class ProgBorder
{	Border progBord;

	public ProgBorder()
	{	Border inner = new CompoundBorder(BorderFactory.createLineBorder(Color.black),
				                      BorderFactory.createEmptyBorder(5,5,5,5));
		Border outer = new CompoundBorder(BorderFactory.createLoweredBevelBorder(),
							    BorderFactory.createEmptyBorder(5,5,5,5));
		progBord = new CompoundBorder(outer, inner);
	}

	public Border getProgBorder()
	{	return progBord;
	}
}


class BackgroundImage 
{	BufferedImage img;
 	TexturePaint pnt;
	Rectangle rect1;

	Image image;
	Toolkit kit;	

	public BackgroundImage(Toolkit t)
	{	kit = t;
		setBackgroundImage("images/defaultBG.jpg");
		makeRect();
	}

	public void makeRect()
	{	rect1 = new Rectangle(100,100);
        	img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
	 	pnt = new TexturePaint(img, rect1);
	}
	
	public void setBackgroundImage(String s)
	{	image = kit.getImage(s);
		makeRect();
	}

	public Image getBackgroundImage()
	{	return image;	
	}

	public Paint getBackgroundPaint()
	{	return pnt;
	}

	public BufferedImage getBufferedImage()
	{	return img;
	}
}


// Appearance elements of application sorted above - below are business objects:

//                       ___________________Game (parameters for the current game)
//                         	|				    |	
//                       Board (info about the board) ----- BoardAnalysis (reviews state of pieces on the board)
//
// Note - no concept of a piece.  This is because a 'piece' is actually just a property of a cell on the board
// after a game.  Hence a piece object is overkill - an integer array is enough (kiss).

class Game
{	int numberOfMoves;
	C4Board board;
	GameState gameState;
	BoardAnalysis boardAnalysis;

	int lastMoveRow = 0;
	int lastMoveColumn = 0;

	// below parameters are for animating win:--------------------------------------------------

	int numberOfWinningPieces;	// could be 4, 5, 6 or 7
	int winNodes[][];
	int winAnimationFrame;
	boolean animatingWin;

	//------------------------------------------------------------------------------------------

	public Game()
	{	this(new GameState());	// FOR NOW ONLY - one is created in MainPan and this constructor will become unneccesary
	}

	public Game(GameState g)
	{	gameState = g;
		initialiseGameClass();
	}

	public void initialiseGameClass()
	{	numberOfMoves = 0;
		board = new C4Board(gameState);
		boardAnalysis = new BoardAnalysis(board, this);
		animatingWin = false;
	}

	public void resetGame()
	{	initialiseGameClass();
		resetLastMove();
		animatingWin = false;
		gameState.resetGameState();

	}	

	public void setAnimatingWin(boolean b)
	{	animatingWin = b;
	}

	public boolean getAnimatingWin()
	{	return animatingWin;
	}

//	Methods below are called when a game is won - set up paramteres for the win animation:----------------------------------------

	public void setWinParamaters(int numPieces, int firstCol, int firstRow, int direction)
	{	
		resetWinAnimationFrame();
		setNumberOfWinningPieces(numPieces);
		setFirstWinNodeCol(firstCol);
		setFirstWinNodeRow(firstRow);
		populateWinNodes(direction);
	}

	public void setNumberOfWinningPieces(int a)
	{	numberOfWinningPieces = a;
		winNodes = new int[a][2];
	}

	public void setFirstWinNodeCol(int colVal)
	{	winNodes[0][0] = colVal;
	}

	public void setFirstWinNodeRow(int rowVal)
	{	winNodes[0][1] = rowVal;
	}	

	public void resetWinAnimationFrame()
	{	winAnimationFrame = 0;
	}

	public int getWinNodeCol(int where)
	{	return winNodes[where][0];
	}

	public int getWinNodeRow(int where)
	{	return winNodes[where][1];
	}

	public int getNumberOfWinningPieces()
	{	return numberOfWinningPieces;
	}

	public int getWinAnimationFrame()
	{	return winAnimationFrame;
	}

	public void incrementWinAnimationFrame()
	{	winAnimationFrame++;
	}

	public void populateWinNodes(int winDirection)
	{	switch(winDirection)
		{	case(GameConstants.WIN_DIRECTION_HORIZONTAL):
			for(int a = 1; a < numberOfWinningPieces; a++)
			{	winNodes[a][0] = (winNodes[0][0] + a);
				winNodes[a][1] = (winNodes[0][1]);
			}
			break;
		
			case(GameConstants.WIN_DIRECTION_VERTICAL):
			for(int a = 1; a < numberOfWinningPieces; a++)
			{	winNodes[a][1] = (winNodes[0][1] +a);
				winNodes[a][0] = (winNodes[0][0]);
			}
			break;

			case(GameConstants.WIN_DIRECTION_DIAGONAL_NE):
			for(int a = 1; a < numberOfWinningPieces; a++)
			{	winNodes[a][1] = (winNodes[0][1] +a);
				winNodes[a][0] = (winNodes[0][0] +a);
			}
			break;

			case(GameConstants.WIN_DIRECTION_DIAGONAL_SE):
			for(int a = 1; a < numberOfWinningPieces; a++)	
			{	winNodes[a][1] = (winNodes[0][1] -a);
				winNodes[a][0] = (winNodes[0][0] +a);
			}
			break;

		}
	}

	public void clearWinningPiecesFromBoard()
	{	for(int a = 0; a < numberOfWinningPieces; a++)
		{	board.clearNode(winNodes[a][0], winNodes[a][1]);
		}
	}

//	-----------------------------------------------------------------------------end of win animation specific methods

	public int getLastMoveRow()
	{	return lastMoveRow;
	}
	public void setLastMoveRow(int a)
	{	lastMoveRow = a;
	}

	public int getLastMoveColumn()
	{	return lastMoveColumn;
	}
	public void setLastMoveColumn(int a)
	{	lastMoveColumn = a;
	}


	public boolean isSpaceInColumn(int column)	// checks the top cell of each column to see if it is vacant (if so coin can be dropped in it)
	{	if (column > (GameConstants.NUMBER_OF_COLS-1)) return false;
		if(board.getPlayerIDAt(column, (GameConstants.NUMBER_OF_ROWS-1)) == 0) return true;
		return false;
	}

	public int makeAMove(int column)	// return 3 statuses - GameOver (WIN OR DRAW) and IllegalMove (both defined in constants)
	{	if(!board.makeMove(column, whoseMove(), this)) 
		{	System.out.print("Invalid move made");
			return GameConstants.ILLEGAL_MOVE;
		}
		numberOfMoves++;			// reached here means move was valid... did it win the game though
		if(checkGameOver())
		{//	System.out.println("4 in a row detected");
			if(whoseMove() == GameConstants.PLAYER1) return GameConstants.GAME_OVER_PLAYER2_WINS;
			return GameConstants.GAME_OVER_PLAYER1_WINS;
		// FOR NOW ONLY - FAILING TO CHECK FOR DRAW SITUATIONS.  Note - simply look for rows*columns number of moves.
		}
		return GameConstants.LEGAL_MOVE_GAME_ON;		
	}

	public int whoseMove()
	{	if((numberOfMoves%2)==1) return GameConstants.PLAYER1;
		return GameConstants.PLAYER2;		
	}

	public boolean checkGameOver()
	{	// checks if 4 in a row has been achieved by the last move.  If so game over is returned - systems knows who has won (the last player)
		return boardAnalysis.checkBoardFor4InALine();	
	}

	public void resetLastMove()
	{	lastMoveColumn = GameConstants.NUMBER_OF_COLS+1;
		lastMoveRow = GameConstants.NUMBER_OF_ROWS+1;
	}
	
	public int getPlayerIDAt(int c, int r)
	{	if((c == lastMoveColumn)&&(r == lastMoveRow ))	return 0;	// means this piece is in process of being added (animation of drop is occuring)
		else return board.getPlayerIDAt(c,r);
	}

	public BoardAnalysis getBoardAnalysis()		// used to allow drawing object access to winning conditions (for animating)
	{	return boardAnalysis;
	}

	public C4Board getC4Board()
	{	return board;
	}

}


class BoardAnalysis		// all methods to analyse moves / the positions of pieces on the board are located here
{	C4Board board;
	Game gameX;
	
//--------------------------------- Parameter below pertains to a won game (necessary for the special animation triggered when a player wins)	
	boolean gameWon;

//---------------------------------

//--------------------------------- Parameters below used in the check routines:	
	int maxRunSize;
	int runLoop;
	int runID;
	int baseLoop;
//---------------------------------

/// NB - when a game has bee won the winning pieces need to be removed from C4Board (to ensure that they are not animated like other pieces)

	public BoardAnalysis(C4Board b, Game g)
	{	board = b;
		gameX = g;
		gameWon = false;
	}


	// methods below check board in all possible directions for an unbroken run of 4 or more
	// if such a run is found information identifying the run (size, start point and direction) are added to win situation paramters in game


	public boolean checkBoardFor4InALine()
	{	for(int c = 0; c<board.getNumberOfColumns(); c++)
		{	if(checkWholeColumn(c)) return true;
			for(int r = 0; r<board.getNumberOfRows(); r++)	if(checkWholeRow(r)) return true;
		}

		for(int c = 0; c<board.getNumberOfColumns()-3; c++)
		{	for(int r = 0; r<board.getNumberOfRows()-3; r++)	if(checkDiagonalNE(c,r)) return true;
		}


		for(int c = 0; c<board.getNumberOfColumns()-3; c++)						// c runs from 0 to 4
		{	for(int r = 3; r<board.getNumberOfRows(); r++) if(checkDiagonalSE(c,r)) return true;	// r runs frmo 3 to 5
		}

		return false;
	}

	private boolean checkDiagonalNE(int colRef, int rowRef)
	{	initialiseRunCheck();

		while((baseLoop < board.getNumberOfRows()-3) && maxRunSize<4)
		{	runID = initialiseCheckLoop(colRef, baseLoop);

			if(runID>0) checkRunSize((board.getNumberOfRows() - baseLoop), colRef, 1, baseLoop, GameConstants.NORTH, runID);

			if(maxRunSize > 3) initiateWinSituation(colRef, baseLoop, GameConstants.WIN_DIRECTION_DIAGONAL_NE);
			else baseLoop++;				// else check next place for winning run:
		}
		return(maxRunSize > 3);		// true if a run of 4 OR OVER has been found
	}


	private boolean checkWholeRow(int rowRef)
	{	initialiseRunCheck();

		while((baseLoop < board.getNumberOfColumns()-3)&&maxRunSize<4)
		{	runID = initialiseCheckLoop(baseLoop,rowRef);

			if(runID>0) checkRunSize((board.getNumberOfColumns() - baseLoop), baseLoop, 1, rowRef, 0, runID);

			if(maxRunSize > 3) initiateWinSituation(baseLoop, rowRef, GameConstants.WIN_DIRECTION_HORIZONTAL);
			else baseLoop++;					// else check next place for winning run
		}
		return gameWon;		// true if a run of 4 OR OVER has been found
	}

	private boolean checkWholeColumn(int colRef)
	{	initialiseRunCheck();

		while((baseLoop < board.getNumberOfRows()-3) && maxRunSize<4)
		{	runID = initialiseCheckLoop(colRef, baseLoop);

			if(runID>0) checkRunSize((board.getNumberOfRows() - baseLoop), colRef, 0, baseLoop, 1, runID);

			if(maxRunSize > 3) initiateWinSituation(colRef, baseLoop, GameConstants.WIN_DIRECTION_VERTICAL);
			else baseLoop++;				// else check next place for winning run:
		}
		return(maxRunSize > 3);		// true if a run of 4 OR OVER has been found
	}


	private boolean checkDiagonalSE(int colRef, int rowRef)
	{	initialiseRunCheck();
		baseLoop = 3;

		while((baseLoop < board.getNumberOfRows()) && maxRunSize<4)
		{	runID = initialiseCheckLoop(colRef, baseLoop);

			if(runID>0) checkRunSize((board.getNumberOfRows()), colRef, 1, baseLoop, GameConstants.SOUTH, runID);

			if(maxRunSize > 3) initiateWinSituation(colRef, baseLoop, GameConstants.WIN_DIRECTION_DIAGONAL_SE);
			else baseLoop++;				// else check next place for winning run:
		}
		return(maxRunSize > 3);		// true if a run of 4 OR OVER has been found
	}


	private void checkRunSize(int boardBoundary, int col, int colModifier, int row, int rowModifier, int playerID)
	{	runLoop = 1;
		maxRunSize = 1;
		while	((runLoop < boardBoundary)
				&&
			(board.getPlayerIDAt((col+colModifier*runLoop), (row+rowModifier*runLoop)) == playerID)			
			)

				{	maxRunSize++;
					runLoop++;
				}
		// modifiers above are 1 or 0, depending on the direction that is being checked.
	}

	private void initialiseRunCheck()
	{	gameWon = false;
		maxRunSize = 1;
		runLoop = 0;
		runID = 0;
		baseLoop = 0;	
	}

	private int initialiseCheckLoop(int col, int row)	
	{	maxRunSize = 1; 
		return board.getPlayerIDAt(col, row);
	}

	public void initiateWinSituation(int col, int row, int winType)
	{	gameWon = true;
		gameX.setWinParamaters(maxRunSize, col, row, winType);
	}
}


class C4Board
{	int boardNodes[][];
	GameState gameState;

	public C4Board(GameState g)
	{	gameState = g;
		boardNodes = new int[GameConstants.NUMBER_OF_COLS][GameConstants.NUMBER_OF_ROWS];
		
		clearBoard();
	}

	public void clearBoard()
	{	for(int r = 0; r<GameConstants.NUMBER_OF_ROWS; r++)
		{	for(int c = 0; c<GameConstants.NUMBER_OF_COLS; c++)
			{	boardNodes[c][r] = 0;
			}
		}
	}

	public boolean makeMove(int column, int playerID, Game g)
	{	// adds the player ID to the first zero in the column.  Returns False (invalid move) if no spaces remain
		boolean moveIsLegal = true;	// assume a valid move until proved otherwise
		boolean rowCheck = true;
		int rowID = 0;
		if(GameConstants.NUMBER_OF_ROWS < 1) System.out.print("exit from while potentially blocked - not enough rows in board");

		while(rowCheck)
		{	if (boardNodes[column][rowID] == 0)	
			{	boardNodes[column][rowID] = playerID;		
				rowCheck = false;			
				g.setLastMoveColumn(column);
				g.setLastMoveRow(rowID);
			}

			if(rowID == GameConstants.NUMBER_OF_ROWS) 	// this bit only executed if all nodes are occupied
			{	moveIsLegal = false; 	
				rowCheck = false;	
			}
			rowID++;
		}

		return moveIsLegal;	// move was valid and has been registered
	}

	public void clearNode(int column, int row)	// zeroes the node passed in
	{	boardNodes[column][row] = 0;
	}

	public int getNumberOfRows()
	{	return GameConstants.NUMBER_OF_ROWS;
	}

	public int getNumberOfColumns()
	{	return GameConstants.NUMBER_OF_COLS;
	}

	public int getPlayerIDAt(int c, int r)
	{	if 	((c > (GameConstants.NUMBER_OF_COLS-1))	||
			 (r > (GameConstants.NUMBER_OF_ROWS-1))	||
			 (c<0)						||
			 (r<0))
		return 0;

		return boardNodes[c][r];
	}
}

// below is a container class for game specific variables.  These are part of the game and therefore could be located in the Game class.
class GameState
{
	boolean winDetected;
	boolean gameOver;

	public GameState()
	{	resetGameState();
	}

	public void resetGameState()
	{	winDetected = false;			// cues win animation
		gameOver = false;			// prevents moves post win.
	}

	public boolean getWinDetected()
	{	return 	winDetected;
	}

	public boolean getGameOver()
	{	return 	gameOver;
	}

	public void setWinDetected(boolean a)
	{	winDetected = a;
	}

	public void setGameOver(boolean a)
	{	gameOver = a;
	}
}

class GameConstants
{	public static final int PLAYER1 = 1;				// GameConstants.PLAYER1
	public static final int PLAYER2 = 2;				// GameConstants.PLAYER2

	public static final int NUMBER_OF_ROWS = 6;			// GameConstants.NUMBER_OF_ROWS
	public static final int NUMBER_OF_COLS = 8;			// GameConstants.NUMBER_OF_COLS

	// 5 POSSIBLE OUTCOMES OF A MOVE:
	public static final int LEGAL_MOVE_GAME_ON = 1;			// GameConstants.LEGAL_MOVE_GAME_ON
	public static final int ILLEGAL_MOVE = 2;			// GameConstants.ILLEGAL_MOVE
	public static final int GAME_OVER_PLAYER1_WINS = 3;		// GameConstants.GAME_OVER_PLAYER1_WINS
	public static final int GAME_OVER_PLAYER2_WINS = 4;		// GameConstants.GAME_OVER_PLAYER2_WINS
	public static final int GAME_OVER_DRAW = 5;			// GameConstants.GAME_OVER_DRAW

	public static final int BOARD_TOP_BORDER = 50;			// GameConstants.BOARD_TOP_BORDER
	public static final int BOARD_SIDE_BORDER = 30;			// GameConstants.BOARD_TOP_BORDER

	public static final int VERTICAL_LINES = 1;			// GameConstants.VERTICAL_LINES
	public static final int HORIZONTAL_LINES = 0;			// GameConstants.HORIZONTAL_LINES

	public static final int CHECK_HORIZONTAL = 1;			// GameConstants.CHECK_HORIZONTAL
	public static final int CHECK_VERTICAL = 0;			// GameConstants.CHECK_VERTICAL

	public static final int WIN_DIRECTION_HORIZONTAL = 0;		//  GameConstants.WIN_DIRECTION_HORIZONTAL
	public static final int WIN_DIRECTION_VERTICAL = 1;		//  GameConstants.WIN_DIRECTION_VERTICAL
	public static final int WIN_DIRECTION_DIAGONAL_SE = 2;		//  GameConstants.WIN_DIRECTION_DIAGONAL_SE
	public static final int WIN_DIRECTION_DIAGONAL_NE = 3;		//  GameConstants.WIN_DIRECTION_DIAGONAL_NE

	public static final int WIN_ANIMATION_FRAMES = 480;		//  GameConstants.WIN_ANIMATION_FRAMES

	public static final int NORTH = 1;				//  GameConstants.NORTH
	public static final int SOUTH = -1;				//  GameConstants.SOUTH

}