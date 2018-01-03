package main.java.com.utils;

import java.util.Comparator;


public class CustomEdge implements Comparator<CustomEdge> {
	private LinkBase link;
	private NodeBase nodeStart;
	private NodeBase nodeStop;
	private String type;
	private String connSpeed;
	private Integer linkLength;
	private int Id;
	
	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public Integer getLinkLength() {
		return linkLength;
	}

	public void setLinkLength(Integer linkLength) {
		if(linkLength != null)
			this.linkLength = linkLength;
		else
			this.linkLength = 0;
	}
	
	public CustomEdge(){
//		this.link = link;
//		this.nodeStart = nodeStart;
//		this.nodeStop = nodeStop;
//		this.setType(type);
//		this.setConnSpeed(connSpeed);
	}	
	
	public CustomEdge(LinkBase link, NodeBase nodeStart, NodeBase nodeStop, String type, String connSpeed){
		this.link = link;
		this.nodeStart = nodeStart;
		this.nodeStop = nodeStop;
		this.setType(type);
		this.setConnSpeed(connSpeed);
	}	
	
	public CustomEdge(LinkBase link, NodeBase nodeStart, NodeBase nodeStop, String type, String connSpeed, Integer linkLength){
		this.link = link;
		this.nodeStart = nodeStart;
		this.nodeStop = nodeStop;
		this.setType(type);
		this.setConnSpeed(connSpeed);
		this.setLinkLength(linkLength);
	}
	
	public LinkBase getLink() {
		return link;
	}

	public void setLink(LinkBase link) {
		this.link = link;
	}

	public NodeBase getNodeStart() {
		return nodeStart;
	}

	public void setNodeStart(NodeBase nodeStart) {
		this.nodeStart = nodeStart;
	}

	public NodeBase getNodeStop() {
		return nodeStop;
	}

	public void setNodeStop(NodeBase nodeStop) {
		this.nodeStop = nodeStop;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getConnSpeed() {
		return connSpeed;
	}

	public void setConnSpeed(String connSpeed) {
		this.connSpeed = connSpeed;
	}

	public int compare(CustomEdge arg0, CustomEdge arg1) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static Comparator<CustomEdge> LengthComparator = new Comparator<CustomEdge>() {
        public int compare(CustomEdge e1, CustomEdge e2) {
            return e1.getLinkLength() - e2.getLinkLength();
        }
    };
	
}
