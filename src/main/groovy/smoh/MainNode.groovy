package main.groovy.smoh

import main.java.com.utils.NodeType;

class MainNode extends HBase {
	
	MainNode(String caption)
	{
		super(caption)
		this.type = NodeType.CN
	}
}
