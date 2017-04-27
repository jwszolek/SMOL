package SMOH

import com.utils.NodeType;

public class MainNode extends HBase {	
	
	public MainNode(String caption)
	{
		super(caption);
		this.type = NodeType.CN
	}
}
