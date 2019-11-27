package TBNG;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class tbngAbstractChatPanel extends JPanel implements KeyListener
{
	
	private int charLimit = 80;
	
	protected JTextArea txtHistory;
	protected JTextField txtMessage;
	protected JButton cmdMessageSend;
	
	public void tbngAbstractChatPanel ()
	{		
	}
	
	public tbngAbstractChatPanel (int charLimit)
	{
		this.charLimit = charLimit;
	}
	
	public abstract String getText ();
	public abstract void append (String strMsg);
	
	protected int getCharLimit ()
	{
		return charLimit;
	}
	
	protected void setCharLimit (int charLimit)
	{
		this.charLimit = charLimit;
	}
	
	public void setChatRows (int chatRows)
	{
		txtHistory.setRows(chatRows);
	}
	
	public void keyPressed (KeyEvent e)
	{
	}
	
	public void keyReleased (KeyEvent e)
	{
	}
	
	public void keyTyped (KeyEvent e)
	{
		
		char c = e.getKeyChar();
		int numChars = txtMessage.getText().length();
		
		if (c == KeyEvent.VK_ENTER)
		{
			cmdMessageSend.doClick();
		}
		else if (numChars >= charLimit && !(c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE))
		{
			e.consume();
		}
		
	}
	
}