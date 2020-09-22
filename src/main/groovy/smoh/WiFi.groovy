package main.groovy.smoh

import main.java.com.utils.NodeType;

class WiFi extends HBase {

	public WiFi(String caption)
	{
		super(caption);
		this.type = NodeType.SENSOR
	}

	public <T> void connect(T obj, Integer connectionspeed, SpeedUnit speedunt)
	{
		connected = obj.caption;
		speed = connectionspeed;
		speedUnit = speedunt;
	}
}