package SMOH

import com.utils.NodeType;

class CanNode extends HBase {
	
	public CanNode(String caption)
	{
		super(caption);
		this.type = NodeType.CAN;
	}
	
	public <T> void connect(T obj, Integer connectionspeed, SpeedUnit speedunt, Integer linkLgth)
	{
		connected = obj.caption;
		speed = connectionspeed;
		speedUnit = speedunt;
		linkLength = linkLgth;
	}
}
