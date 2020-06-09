package s0571269;

import java.awt.Point;
import java.util.Comparator;

public class Edge implements Comparator<Edge> { 
	
	private Point point; 
	private float cost; 
	
	public Edge() 
	{ 
	} 
	
	public Edge(Point p, float cost) 
	{ 
	   this.setPoint(p); 
	   this.setCost(cost); 
	} 
	
	@Override
	public int compare(Edge e1, Edge e2) 
	{ 
	   if (e1.getCost() < e2.getCost()) 
	       return -1; 
	   if (e1.getCost() > e2.getCost()) 
	       return 1; 
	   return 0; 
	} 
	public boolean containsPoint(Point p) {
		if(p.equals(p))
			return true;
		
		return false;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}
} 