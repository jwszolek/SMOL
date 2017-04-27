package SMOH

import com.utils.NodeType;

class SANode extends HBase {
	
	public SANode(String caption)
	{
		super(caption);
		this.type = NodeType.SA;
	}
	
	public <T> void connect(T obj, Integer connectionspeed, SpeedUnit speedunt, Integer linkLgth)
	{
		connected = obj.caption;
		speed = connectionspeed;
		speedUnit = speedunt;
		linkLength = linkLgth;
	}
		
}
