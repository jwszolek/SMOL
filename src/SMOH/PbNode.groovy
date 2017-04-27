package SMOH

import com.utils.NodeType;

class PbNode extends HBase {
	
	public PbNode(String caption)
	{
		super(caption);
		this.type = NodeType.PB;
	}
	
	public <T> void connect(T obj,Integer connectionspeed, SpeedUnit speedunt, Integer linkLgth)
	{
		connected = obj.caption;
		speed = connectionspeed;
		speedUnit = speedunt;
		linkLength = linkLgth;
	}
	
}
