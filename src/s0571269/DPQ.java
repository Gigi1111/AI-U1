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
	private final float infinity = 99999999999.f;
	public static boolean debug = false;
	public static Point src;
	public static Point dst;
	 public Map<Point,Float> dist; 
	 public static Map<Point,Point> weg; //shortest
	 private ArrayList<Point> visited; //visited
	 private ArrayList<Point> unvisited; //visited
	 private PriorityQueue<Edge> pq; 
	 private int numOfPoints; // Number of vertices 
	 Graph_DPQ graph;
//	 static Map<Point,List<Edge>> adj; 
	
	 public DPQ(int numofp, Graph_DPQ g) 
	 { 
		 src = g.source;
		 dst = g.destination;
	     this.numOfPoints = numofp; 
	     this.graph = g;
	     //dist should store goal, total dist ANNNND route
	     dist = new HashMap<Point,Float>(); 
	     weg = new HashMap<Point,Point>();
	     visited = new  ArrayList<Point>(); 
	     unvisited = new  ArrayList<Point>(); 
	     pq = new PriorityQueue<Edge>(numofp, new Edge()); 
	 } 

//	 private void printPq() {
//		 System.out.print("pq size:"+pq.size());
//         System.out.print("pq:");
//         for(Edge entry: pq) {
//        	 System.out.print("("+entry.point.x+","+entry.point.y+")"+"[dist:"+entry.cost+"]");
//         }
//         System.out.println();
//	}
//
//	 void printDist() {
//		 System.out.print("dist:");
//         for(Map.Entry<Point, Float> entry:dist.entrySet()) {
//        	 System.out.print("("+entry.getKey().x+","+entry.getKey().y+")"+"[dist:"+entry.getValue()+"]");
//         }
//         System.out.println();
//	 }
//	 void printWeg() {
//		 System.out.print("Weg:");
//         for(Map.Entry<Point, List<Point>> entry:weg.entrySet()) {
//        	 System.out.print("("+entry.getKey().x+","+entry.getKey().y+")"+":WEG:");
//        	 for(Point p: entry.getValue()) {
//            	 System.out.print("("+p.x+","+p.y+")"+"->");
//             }
//        	 System.out.println();
//         }
//         System.out.println();
//	 }
	 // Function for Dijkstra's Algorithm 
	 
	 public ArrayList<Point> getShortestWay(Point start, Point destination) {
			visited = new ArrayList<Point>();
			weg = new HashMap<>();
			float currentDistance = 0;
			Point currentPoint = start;
			dist = new HashMap<>();
			if(graph==null) {
				System.out.println("graph is null");
				return null;
			}
			if(graph.getAllNodes()!=null) {
				unvisited = (ArrayList<Point>) graph.getAllNodes();
				
				
				for (Point node : unvisited) {
	
					dist.put(node, infinity);
				}
				dist.put(start, 0.f);
				while (!currentPoint.equals(destination)) {
					
	//				for (Point neighbour : graph.getNeighbours(currentPoint)) {
	//					if (unvisited.contains(neighbour)) {
	//						float distanceToCurrentPoint = (float) currentPoint.distance(neighbour);
	//						float tentativeDistance = distanceToCurrentPoint + currentDistance;
	//						float previousDistance = dist.get(neighbour);
	//						if (previousDistance == infinity || previousDistance > tentativeDistance) {
	//							dist.put(neighbour, tentativeDistance);
	//							weg.put(neighbour, currentPoint);
	//						}
	//					}
	//				}
					if(graph.getNeighbours(currentPoint)==null) {
						System.out.println("raph.getNeighbours(currentPoint)==null");
					}
					
					for (Edge neighbour : graph.getNeighbours(currentPoint)) {
						if(neighbour==null) {
							System.out.println("neighbour==null");
						}
						if (unvisited.contains(neighbour.point)) {
							float distanceToCurrentPoint = (float) neighbour.cost;
							float tentativeDistance = distanceToCurrentPoint + currentDistance;
							float previousDistance = dist.get(neighbour.point);
							if (previousDistance == infinity || previousDistance > tentativeDistance) {
								dist.put(neighbour.point, tentativeDistance);
								weg.put(neighbour.point, currentPoint);
							}
						}
					}
					unvisited.remove(currentPoint);
					visited.add(currentPoint);
					currentPoint = selectClosestNode();
					if (currentPoint == null || !unvisitedPointsReachable()) {
						return null;
					} else {
						currentDistance = dist.get(currentPoint);
					}
				}
				
		    	return generateShortestPath(start, destination);
			}
			System.out.println("not ready yet");
			return null;

		}
		private ArrayList<Point> generateShortestPath(Point start, Point destination) {
			ArrayList<Point> path = new ArrayList<>();
			Point currentPoint = destination;
			path.add(destination);
			while (! currentPoint.equals(start)) {
				currentPoint = this.weg.get(currentPoint);
				if (currentPoint == null) {
					System.out.println("ERROR in generate shortest path!!!");
				}
				path.add(0, currentPoint);
				
			}
			return path;
		}

	 private Point selectClosestNode() {
			if (unvisited.isEmpty()) {
				return null;
			}
			Point shortestPoint = unvisited.get(0);
			for (Point point : unvisited) {
				if (dist.get(point) != infinity && dist.get(point) < dist.get(shortestPoint)) {
					shortestPoint = point;
				}
			}
			return shortestPoint;
		}

		
		private boolean unvisitedPointsReachable() {
			for (Point point : unvisited) {
				if (dist.get(point) != infinity) {
					return true;
				}
			}
			return false;
		}

	 
//	 public void dijkstra() 
//	 { 
//	     for (Map.Entry<Point,List<Edge>> entry : g.adjVertices.entrySet()) {
//	    	 dist.putIfAbsent(entry.getKey(),Float.MAX_VALUE);
//	    		weg.putIfAbsent(entry.getKey(),new ArrayList<Point>());
//	     }
//	
//	     // Add source Edge to the priority queue 
//	     pq.add(new Edge(src, 0f)); 
//	
//	     // Distance to the source is 0 
//	     //here
//	     dist.replace(src,0f); 
//	     weg.get(src).add(src);
////	     if(debug) {
////	     printDist();
////	     printWeg();
////	     }
//	     while (visited.size() < numOfPoints) { 
//	
//	         // remove the minimum distance Edge  
//	         // from the priority queue 
//	    	 if(debug)
//	    		 System.out.println("----while---");
//	    	 
//	    	 Point u = null;
//	    	 if(pq.size()>0)
//	    		 u = pq.remove().point; 
//	    	 else {
//	    		 break;
//	    	 }
////	         if(debug) {
////		         printPq();
////		         System.out.println();
////		         System.out.println("u"+u.x+","+u.y);
////	         }
//	         // adding the Edge whose distance is 
//	         // finalized 
//	         visited.add(u); 
//	         if(debug)
//	        	 System.out.println("setteled:"+visited);
//	         e_Neighbours(u); 
//	     } 
//	 } 
//	 private void e_Neighbours(Point u) 
//	 { 
//	     float edgeDistance = -1; 
//	     float newDistance = -1; 
//
//	     for (Edge v: g.getEdgeList(u)) { 
//	    	
////	    	 if(debug) {
////	    	 printWeg();
////	    	 g.printEdgeList(u);
////	    	 }
//	         // If current Edge hasn't already been processed 
//	         if (!visited.contains(v.point)) { 
//	             edgeDistance = v.cost; 
//	             newDistance = dist.get(u) + edgeDistance; 
//	             List<Point> newl = new ArrayList<Point>();
//	             for(Point p : weg.get(u))
//	             	newl.add(p);
//	             if(!newl.contains(u)) {
//	            	 newl.add(u);
//	             }
//	             if(debug)
//	            	 System.out.println("u/v:"+"("+u.x+","+u.y+")"+"("+v.point.x+","+v.point.y+")"+"edgeDist:"+edgeDistance+", newDist"+newDistance+", distOfV:"+dist.get(v.point));
//	
//	             if (dist.containsKey(v.point) && dist.get(v.point)!=null&& newDistance < dist.get(v.point)) {
//	                 dist.replace(v.point,newDistance);
//	                 if(!v.equals(src))
//	                	 weg.replace(v.point,newl);
//	                 if(debug) {
//	                	 System.out.println("-----replace----");
////	                 printWeg();
//	                 }
//	             }
//	             if(debug)
////	            	 printDist();
//	             
//	             // Add the current Edge to the queue 
//	             if (dist.containsKey(v.point) && dist.get(v.point)!=null){
//	            	 pq.add(new Edge(v.point, dist.get(v.point))); 
//	             }
//	             if(debug) {
////	             printPq();
//	             System.out.println("----");
//	             }
//	         } 
//	     } 
//	 } 
//	 static List<Point> getShortestPathTo(Point p) {
//		 if(p==null) {
//			 p = dst;
//		 }
//		 List<Point> li = weg.get(p);
//		 if(li!=null && !li.isEmpty() ) {
//			 if(debug)
////				 System.out.print("Shortest weg: ");
//			 for(Point pp : li) {
////				 System.out.print("("+pp.x+","+pp.y+")"+"->");
//			 }
////			 System.out.println("("+p.x+","+p.y+")");
//			 li.add(p);
//			 return li;
//		 }else {
////			 System.out.println("No weg from "+"("+src.x+","+src.y+")"+"to ("+p.x+","+p.y+")");
//			 return null;
//		 }
//	 }
	 
	 ////private final float infinity = 99999999999.f;


		
}