package s0571269;

import java.awt.Point;
import java.util.Comparator;

public class Edge2 implements Comparator<Edge2> { 
	public Point point; 
	public float cost; 
	
	public Edge2() 
	{ 
	} 
	
	public Edge2(Point p, float cost) 
	{ 
	   this.point = new Point(p.x,p.y); 
	   this.cost = cost; 
	} 
	
	@Override
	public int compare(Edge2 e1, Edge2 e2) 
	{ 
	   if (e1.cost < e2.cost) 
	       return -1; 
	   if (e1.cost > e2.cost) 
	       return 1; 
	   return 0; 
	} 
} 
