package TBNG;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import java.util.Calendar;
import java.text.DecimalFormat;

public class tbngStandardStyleChatPanel extends tbngAbstractChatPanel
{

	private final DecimalFormat decFormat = new DecimalFormat("00");
	private JPanel panRoot, panSendMessage;
	private JScrollPane chatHistory;
	
	public tbngStandardStyleChatPanel (ActionListener parent)
	{
		
		super(80);
		this.setLayout(new BorderLayout());
		
		txtHistory = new JTextArea("", 9, 22);
		txtHistory.setLineWrap(true);
		txtHistory.setWrapStyleWord(true);
		txtHistory.setEditable(false);
		txtHistory.setToolTipText("All the messages that have been sent");
		
		chatHistory = new JScrollPane(txtHistory, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		txtMessage = new JTextField("", 17);
		txtMessage.addKeyListener(this);
		txtMessage.setToolTipText("Type your message here");
		
		cmdMessageSend = new JButton("Send");
		cmdMessageSend.addActionListener(parent);
		cmdMessageSend.setActionCommand("message");
		cmdMessageSend.setToolTipText("Send the message to the other player");
		
		panRoot = new JPanel(new BorderLayout());
		panSendMessage = new JPanel(new BorderLayout());		
		
		this.add(panRoot, BorderLayout.WEST);
			panRoot.add(panSendMessage, BorderLayout.NORTH);
				panSendMessage.add(txtMessage, BorderLayout.WEST);
				panSendMessage.add(cmdMessageSend, BorderLayout.EAST);			
			panRoot.add(chatHistory, BorderLayout.SOUTH);
			
		panRoot.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		panSendMessage.setBorder(BorderFactory.createEmptyBorder(1, 1, 3, 1));
		
	}
	
	public void setEnabled (boolean state)
	{
		
		txtMessage.setEnabled(state);
		cmdMessageSend.setEnabled(state);
		
	}
	
	public String getText ()
	{
		
		String strMsg = txtMessage.getText();
		
		if (strMsg.length() > getCharLimit())
		{
			strMsg = strMsg.substring(0, (getCharLimit() - 1));
		}

		txtMessage.setText("");
		return strMsg;
		
	}
	public void append (String strMsg)
	{
		
		Calendar cal = Calendar.getInstance();
		String strHeader = decFormat.format(cal.get(Calendar.HOUR)) + ":" + decFormat.format(cal.get(Calendar.MINUTE)) + ":" + decFormat.format(cal.get(Calendar.SECOND)) + " ";		
		txtHistory.append(strHeader + strMsg + "\n");
		
	}

}