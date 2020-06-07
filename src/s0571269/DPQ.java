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

	 
	 public ArrayList<Point> getShortestWay(Point start, Point destination) {
			visited = new ArrayList<Point>();
			weg = new HashMap<>();
			float currentDistance = 0;
			Point currentPoint = start;
			dist = new HashMap<>();
			if(graph==null) {
				return null;
			}
			if(graph.getAllNodes()!=null) {
				unvisited = (ArrayList<Point>) graph.getAllNodes();
				
				
				for (Point node : unvisited) {
	
					dist.put(node, infinity);
				}
				dist.put(start, 0.f);
				while (!currentPoint.equals(destination)) {
					
					
					
					for (Edge neighbour : graph.getNeighbours(currentPoint)) {
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
			return null;

		}
		private ArrayList<Point> generateShortestPath(Point start, Point destination) {
			ArrayList<Point> path = new ArrayList<>();
			Point currentPoint = destination;
			path.add(destination);
			while (! currentPoint.equals(start)) {
				currentPoint = this.weg.get(currentPoint);

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
		
}