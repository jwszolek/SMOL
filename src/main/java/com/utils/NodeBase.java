package main.java.com.utils;

public class NodeBase {
	private int id;
	private String name; 
	private NodeType type;
	private String connSpeed;
	private Double x;
	private Double y;
	
	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public String getConnLabel() {
		return connSpeed;
	}

	public void setConnLabel(String connLabel) {
		this.connSpeed = connLabel;
	}
	

	public NodeBase() {
//		this.setId(id);
//		this.setName(name);
//		this.setType(type);
//		this.setConnLabel(connSpeed);
	}
	
	
	public NodeBase(int id, String name, NodeType type, String connSpeed) {
		this.setId(id);
		this.setName(name);
		this.setType(type);
		this.setConnLabel(connSpeed);
	}
	

	public String toString() {
		//return "V" + getId();
		//return "name_"+ getName();
		return getType() + "_" + getId();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public NodeType getType() {
		return type;
	}

	public void setType(NodeType type) {
		this.type = type;
	}
}
