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
		this.setPoint(new Point(p.x,p.y)); 
		this.setCost(cost); 
	} 
	
	@Override
	public int compare(Edge e1, Edge e2) 
	{ 
	   if (e1.cost < e2.cost) 
	       return -1; 
	   if (e1.cost > e2.cost) 
	       return 1; 
	   return 0; 
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public Point getPoint() {
		return null;
	} 
	public void setPoint(Point point) {
		this.point = point;
	} 
} 
