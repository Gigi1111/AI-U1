package s0571269;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

public class PathFinder {
	
	private float infinity = -1.f;

	private Graph graph;
	
	private HashMap<Point, Float> distances;
	private ArrayList<Point> unvisited;
	private ArrayList<Point> visited;

	public PathFinder(Graph graph) {
		this.graph = graph;
	}
	
	public ArrayList<Point> getShortestWay(Point start, Point destination) {
		visited = new ArrayList<Point>();
		float currentDistance = 0;
		Point currentPoint = start;
		distances = new HashMap<>();
		unvisited = graph.getAllNodes();
		
		for (Point node : unvisited) {

			distances.put(node, infinity);
		}
		distances.put(start, 0.f);
		while (unvisited.contains(destination) && unvisitedPointsReachable()) {
			for (Point neighbour : graph.getNeighbours(currentPoint)) {
				if (unvisited.contains(neighbour)) {
					float tentativeDistance = 1 + currentDistance;
					float previousDistance = distances.get(neighbour);
					if (previousDistance == infinity || previousDistance > tentativeDistance) {
						distances.put(neighbour, tentativeDistance);
					}
				}
			}
			unvisited.remove(currentPoint);
			visited.add(currentPoint);
			currentPoint = selectClosestNode();
			if (currentPoint == null) {
				break;
			}
		}
    	visited.remove(0);
    	return visited;

	}
	
	private Point selectClosestNode() {
		if (unvisited.isEmpty()) {
			return null;
		}
		Point shortestPoint = unvisited.get(0);
		for (Point point : unvisited) {
			if (distances.get(point) != infinity && distances.get(point) < distances.get(shortestPoint)) {
				shortestPoint = point;
			}
		}
		return shortestPoint;
	}

	
	private boolean unvisitedPointsReachable() {
		for (Point point : unvisited) {
			if (distances.get(point) != infinity) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		Point start = new Point(1,1);
		Point extra = new Point(2,1);
		Point middle = new Point(1,2);
		Point destination = new Point(1,3);
		Graph graph = new Graph();
		graph.addNode(start);
		graph.addNode(middle);
		graph.addNode(destination);
		graph.addNode(extra);
		graph.addNeigbourNode(start, middle);
		graph.addNeigbourNode(middle, destination);
		graph.addNeigbourNode(start, extra);
		graph.addNeigbourNode(extra, middle);
		PathFinder pathfinder = new PathFinder(graph);
		ArrayList<Point> path = pathfinder.getShortestWay(start, destination);
		ArrayList<Point> expectedPath = new ArrayList<Point>();
		expectedPath.add(middle);
		expectedPath.add(destination);
		if (path.equals(expectedPath)) {
			System.out.println("ok");
		} else {
			System.out.println("ERROR");
			System.out.println("actual: " + path);
			System.out.println("expected: " + expectedPath);
		}
	}

}