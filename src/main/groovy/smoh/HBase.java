package main.groovy.smoh;

import main.java.com.utils.NodeType;

public class HBase implements IBase {
	public String name;
	public String unit;
	public Integer value;
	public NodeType type;
	public Integer speed;
	public SpeedUnit speedUnit;
	public Integer LinkLength;
	
	public Integer getLinkLength() {
		return LinkLength;
	}

	public void setLinkLength(Integer linkLength) {
		LinkLength = linkLength;
	}

	private String caption;
	private String connected;
	

	public <T> void connect(Class<T> input){};
	
	public HBase(String caption)
	{
		this.setCaption(caption);
	}
	
	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	public String getConnectionSpeed(){
		String connSpeed = "";
		
		try{
			connSpeed = Integer.toString(speed) + speedUnit;
		}
		catch(Exception ex){
			connSpeed = null;
		}
		
		return connSpeed;
	}
	
	public String getConnected() {
		// TODO Auto-generated method stub
		return connected;
	}

	public void setConnected(String connected) {
		// TODO Auto-generated method stub
		this.connected = connected;
	}
}
