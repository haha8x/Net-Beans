package TBNG;

public class tbngProtocol
{
	//packet type
	public static final int TYPE_CONNECTION = 0;
	public static final int TYPE_GAME = 1;
	public static final int TYPE_MESSAGE = 2;
	
	//Connection options
	public static final int CONNECTION_KEEP_ALIVE = 0;
	public static final int CONNECTION_REQUEST_JOIN = 1;
	public static final int CONNECTION_ACCEPT_JOIN = 2;
	public static final int CONNECTION_ACCEPTED_SOMEONE_ELSE = 3;
	public static final int CONNECTION_ACK_ACCEPT_JOIN = 4;
	public static final int CONNECTION_DISCONNECT = 5;
	public static final int CONNECTION_ERROR = 6;
	
	//Game options
	public static final int GAME_NEW_GAME = 0;
	public static final int GAME_SEND_MOVE = 1;
	public static final int GAME_ACK_MOVE = 2;
	public static final int GAME_END_GAME = 3;
	
	//Message options
	public static final int MESSAGE_SEND_MESSAGE = 0;
	
	private int type = 0;
	private int option = 0;
	private String strData = "";
	
	public void tbngProtocol ()
	{
	}
	
	public tbngProtocol (int type, int option)
	{
		
		this.type = type;
		this.option = option;

	}
	
	public tbngProtocol (int type, int option, String strData)
	{
		
		this.type = type;
		this.option = option;
		this.strData = strData;
		
	}
	
	public tbngProtocol (byte[] data)
	{
		decode(data);
	}
	
	public int getType ()
	{
		return type;
	}
	
	public int getOption ()
	{
		return option;
	}
	
	public String getData ()
	{
		return strData;
	}
	
	public byte[] encode ()
	{
		
		String strSend = "type=" + type + "&option=" + option + "&data=" + strData;		
		return strSend.getBytes();
		
	}
	
	private int getNullDataPoint (byte[] data)
	{
		
		for (int i = 0; i < data.length; i++)
		{
			
			if (data[i] == 00000000)
			{
				return i;
			}
			
		}
		
		return data.length;
		
	}
	
	private void decode (byte[] rawData)
	{
		
		int nullDataPoint = getNullDataPoint(rawData);
		byte[] data = new byte[nullDataPoint];
		String strRec = null;
		String[] strField = null;
		String[] strKeyValue = null;
		
		for (int i = 0; i < data.length; i++)
		{
			data[i] = rawData[i];
		}
		
		strRec = new String(data);
		strField = strRec.split("&", 3);
		
		for (int i = 0; i < 3; i++)
		{
			
			strKeyValue = strField[i].split("=", 2);
			
			if (strKeyValue[0].equals("type"))
			{
				type = Integer.parseInt(strKeyValue[1]);
			}
			else if (strKeyValue[0].equals("option"))
			{
				option = Integer.parseInt(strKeyValue[1]);
			}
			else if (strKeyValue[0].equals("data"))
			{
				strData = strKeyValue[1];
			}
			
		}
		
	}
	
}