package s0571269;

import java.awt.Point;
import java.util.Comparator;

public class Edge implements Comparator<Edge> { 
	public Point point; 
	public float cost; 
	
	public Edge() 
	{ 
	} 
	
	public Edge(Point p, float cost) 
	{ 
	   this.point = new Point(p.x,p.y); 
	   this.cost = cost; 
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
} 
