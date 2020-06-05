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
		 long startTime = System.nanoTime();
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
	    	 
	    	 Point u = null;
	    	 if(pq.size()>0)
	    		 u = pq.remove().point; 
	    	 else {
	    		 break;
	    	 }
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
	     long endTime = System.nanoTime();
	     long totalTime = endTime - startTime;
	     System.out.println("-------------total time---------"+totalTime);
	 } 
	
	 // Function to process all the neighbours  
	 // of the passed Edge 
	 private void e_Neighbours(Point u) 
	 { 
	     float edgeDistance = -1; 
	     float newDistance = -1; 

	     for (Edge v: g.getEdgeList(u)) { 
	    	
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
//	             if(v.point==null) {
//	            	 System.out.println("v.point");
//	             }
//	             else if(dist.get(v.point)==null) {
//	            	 System.out.println("u: "+u);
//	            	 System.out.println("edge list size of u: "+g.getEdgeList(u).size());
//	            	 System.out.println("v.point: "+v.point);
//	            	 System.out.println("dist: "+dist);
//	            	 System.out.println("dist.get(v.point)");
//	             }
	             if (dist.containsKey(v.point) && dist.get(v.point)!=null&& newDistance < dist.get(v.point)) {
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
	             if (dist.containsKey(v.point) && dist.get(v.point)!=null){
	            	 pq.add(new Edge(v.point, dist.get(v.point))); 
	             }
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
			 if(debug)
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
}