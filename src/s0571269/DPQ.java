package s0571269;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class DPQ{
	public static boolean debug = false;
	public static Point src;
	public static Point dst;
	 public Map<Point,Float> dist; 
	 public static Map<Point,List<Point>> weg; 
	 private Set<Point> settled; 
	 private PriorityQueue<Edge> pq; 
	 private int numOfPoints; // Number of vertices 
	 Graph g;
//	 static Map<Point,List<Edge>> adj; 
	
	 public DPQ(int V, Graph g) 
	 { 
		 src = g.source;
		 dst = g.destination;
	     this.numOfPoints = V; 
	     this.g = g;
	     //dist should store goal, total dist ANNNND route
	     dist = new HashMap<Point,Float>(); 
	     weg = new HashMap<Point,List<Point>>();
	     settled = new HashSet<Point>(); 
	     pq = new PriorityQueue<Edge>(V, new Edge()); 
	 } 

	 private void printPq() {
		 System.out.print("pq size:"+pq.size());
         System.out.print("pq:");
         for(Edge entry: pq) {
        	 System.out.print("("+entry.point.x+","+entry.point.y+")"+"[dist:"+entry.cost+"]");
         }
         System.out.println();
	}

	 void printDist() {
		 System.out.print("dist:");
         for(Map.Entry<Point, Float> entry:dist.entrySet()) {
        	 System.out.print("("+entry.getKey().x+","+entry.getKey().y+")"+"[dist:"+entry.getValue()+"]");
         }
         System.out.println();
	 }
	 void printWeg() {
		 System.out.print("Weg:");
         for(Map.Entry<Point, List<Point>> entry:weg.entrySet()) {
        	 System.out.print("("+entry.getKey().x+","+entry.getKey().y+")"+":WEG:");
        	 for(Point p: entry.getValue()) {
            	 System.out.print("("+p.x+","+p.y+")"+"->");
             }
        	 System.out.println();
         }
         System.out.println();
	 }
	 // Function for Dijkstra's Algorithm 
	 public void dijkstra() 
	 { 
	     for (Map.Entry<Point,List<Edge>> entry : g.adjVertices.entrySet()) {
	    	 dist.putIfAbsent(entry.getKey(),Float.MAX_VALUE);
	    		weg.putIfAbsent(entry.getKey(),new ArrayList<Point>());
	     }
	
	     // Add source Edge to the priority queue 
	     pq.add(new Edge(src, 0f)); 
	
	     // Distance to the source is 0 
	     //here
	     dist.replace(src,0f); 
	     weg.get(src).add(src);
	     if(debug) {
	     printDist();
	     printWeg();
	     }
	     while (settled.size() < numOfPoints) { 
	
	         // remove the minimum distance Edge  
	         // from the priority queue 
	    	 if(debug)
	    		 System.out.println("----while---");
	         Point u = pq.remove().point; 
	         if(debug) {
	         printPq();
	         System.out.println();
	         System.out.println("u"+u.x+","+u.y);
	         }
	         // adding the Edge whose distance is 
	         // finalized 
	         settled.add(u); 
	         if(debug)
	        	 System.out.println("setteled:"+settled);
	         e_Neighbours(u); 
	     } 
	 } 
	
	 // Function to process all the neighbours  
	 // of the passed Edge 
	 private void e_Neighbours(Point u) 
	 { 
	     float edgeDistance = -1; 
	     float newDistance = -1; 

	     for (Edge v:g.getEdgeList(u)) { 
	    	
	    	 if(debug) {
	    	 printWeg();
	    	 g.printEdgeList(u);
	    	 }
	         // If current Edge hasn't already been processed 
	         if (!settled.contains(v.point)) { 
	             edgeDistance = v.cost; 
	             newDistance = dist.get(u) + edgeDistance; 
	             List<Point> newl = new ArrayList<Point>();
	             for(Point p : weg.get(u))
	             	newl.add(p);
	             if(!newl.contains(u)) {
	            	 newl.add(u);
	             }
	             if(debug)
	            	 System.out.println("u/v:"+"("+u.x+","+u.y+")"+"("+v.point.x+","+v.point.y+")"+"edgeDist:"+edgeDistance+", newDist"+newDistance+", distOfV:"+dist.get(v.point));
	
	             // If new distance is cheaper in cost 
	             if (newDistance < dist.get(v.point)) {
	                 dist.replace(v.point,newDistance);
	                 if(!v.equals(src))
	                	 weg.replace(v.point,newl);
	                 if(debug) {
	                	 System.out.println("-----replace----");
	                 printWeg();
	                 }
	             }
	             if(debug)
	            	 printDist();
	             
	             // Add the current Edge to the queue 
	             pq.add(new Edge(v.point, dist.get(v.point))); 
	             if(debug) {
	             printPq();
	             System.out.println("----");
	             }
	         } 
	     } 
	 } 
	 static List<Point> printShortestPathTo(Point p) {
		 if(p==null) {
			 p = dst;
		 }
		 List<Point> li = weg.get(p);
		 if(li!=null && !li.isEmpty() ) {
			 System.out.print("Shortest weg: ");
			 for(Point pp : li) {
				 System.out.print("("+pp.x+","+pp.y+")"+"->");
			 }
			 System.out.println("("+p.x+","+p.y+")");
			 li.add(p);
			 return li;
		 }else {
			 System.out.println("No weg from "+"("+src.x+","+src.y+")"+"to ("+p.x+","+p.y+")");
			 return null;
		 }
	 }
//	 public static void main(String arg[]) 
//	 { 
//	     int V = 5; //number of nodes
////	     Point source = 0; 
//	
//	     // Adjacency list representation of the  
//	     // connected edges 
////	     //src, dest
////	     Point p0 = new Point(0,0);
////	     Point p1 = new Point(0,1);
////	     Point p2 = new Point(0,2);
////	     Point p3 = new Point(0,3);
////	     Point p4 = new Point(0,4);
////	     Graph g = new Graph(p0,p4); 
////	
////	     // Initialize list for every node 
////	    g.addPoint(p0);
////	    g.addPoint(p1);
////	    g.addPoint(p2);
////	    g.addPoint(p3);
////	    g.addPoint(p4);
////	   
////	     // Inputs for the DPQ graph 
////	    g.addEdge(p0,p3);
////	    g.addEdge(p0,p1);
////	    g.addEdge(p1,p2);
////	    g.addEdge(p2,p3);
////	    g.addEdge(p1,p3);
////	    g.addEdge(p2,p4);
////	    g.addEdge(p1,p4);
//
//	     //src, dest
//	     Point p0 = new Point(0,0);
//	     Point p1 = new Point(0,10);
//	     Point p2 = new Point(10,0);
//	     Point p3 = new Point(10,10);
//	     
//	     Point p4 = new Point(2,2);
//	     Point p5 = new Point(2,8);
//	     Point p6 = new Point(8,2);
//	     Point p7 = new Point(8,8);
//	     
//	     Point p8 = new Point(9,9);
//	     Graph g = new Graph(p0,p8); 
//	
//	     // Initialize list for every node 
//	    g.addPoint(p0);
//	    g.addPoint(p1);
//	    g.addPoint(p2);
//	    g.addPoint(p3);
//	    g.addPoint(p4);
//	    g.addPoint(p5);
//	    g.addPoint(p6);
//	    g.addPoint(p7);
//	    g.addPoint(p8);
//	   
//	    g.addEdge(p2,p5);
//	    g.addEdge(p0,p4);
//	    g.addEdge(p6,p1);
//	    g.addEdge(p3,p7);
//	    g.addEdge(p0,p5);
//	    g.addEdge(p0,p6);
//	    g.addEdge(p5,p4);
//	    g.addEdge(p4,p6);
//	    g.addEdge(p7,p6);
//	    g.addEdge(p8,p3);
//	    g.addEdge(p8,p2);
//	    g.addEdge(p8,p1);
//	    g.addEdge(p8,p7);
//	    g.addEdge(p8,p5);
//	    g.addEdge(p8,p6);
//	    
//	    g.printGraph();
//	    
//	    
//	     // Calculate the single source shortest path 
//	     DPQ dpq = new DPQ(4,g); 
//	     dpq.dijkstra(); 
//	
//	     // Print the shortest path to all the nodes 
//	     // from the source node 
//	     System.out.println("The shorted path from node :"); 
////	     System.out.print("dist:");
////         for(Map.Entry<Point, Float> entry:dpq.dist.entrySet()) {
////        	 System.out.print("("+entry.getKey().x+","+entry.getKey().y+")"+"[dist:"+entry.getValue()+"]");
////         }
//         System.out.println();
//         printShortestPathTo(p8);
//	     for ( Map.Entry<Point,Float> entry:dpq.dist.entrySet()) 
//	         System.out.println("("+p0.x+","+p0.y+")" + " to " +  "("+entry.getKey().x+","+entry.getKey().y+")"
//	        		 				+ " is "
//	                            + entry.getValue()); 
//	     //and is how??
//	 }
}