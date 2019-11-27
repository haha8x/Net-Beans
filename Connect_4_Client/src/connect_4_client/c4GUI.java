package connect_4_client;

import TBNG.tbngStandardStyleGUI;

public class c4GUI extends tbngStandardStyleGUI
{
	
	private final int WIDTH = 750, HEIGHT = 420, CHAT_ROWS = 17;
	
	public c4GUI ()
	{
		
		gameCanvas = new c4Game(this);
		init(WIDTH, HEIGHT);
		panChatArea.setChatRows(CHAT_ROWS);
		
	}
	
}