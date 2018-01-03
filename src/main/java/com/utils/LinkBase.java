package main.java.com.utils;

public class LinkBase {
	double capacity; 
	double weight; 
	int id;

	public LinkBase(double weight, double capacity) {
		//this.id = edgeCount++; // This is defined in the outer class.
		this.weight = weight;
		this.capacity = capacity;
	}

	public String toString() { // Always good for debugging
		return "E" + id;
	}
}
