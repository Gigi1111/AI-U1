package s0571269;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

public class PathFinder {
	
	private final float infinity = 99999999999.f;

	private Graph graph;
	
	private HashMap<Point, Float> distances;
	private ArrayList<Point> unvisited;
	private ArrayList<Point> visited;
	private HashMap<Point, Point> shortestApproach;

	public PathFinder(Graph graph) {
		this.graph = graph;
	}
	
	public ArrayList<Point> getShortestWay(Point start, Point destination) {
		visited = new ArrayList<Point>();
		shortestApproach = new HashMap<>();
		float currentDistance = 0;
		Point currentPoint = start;
		distances = new HashMap<>();
		unvisited = graph.getAllNodes();
		
		for (Point node : unvisited) {

			distances.put(node, infinity);
		}
		distances.put(start, 0.f);
		while (!currentPoint.equals(destination)) {
			for (Point neighbour : graph.getNeighbours(currentPoint)) {
				if (unvisited.contains(neighbour)) {
					float distanceToCurrentPoint = (float) currentPoint.distance(neighbour);
					float tentativeDistance = distanceToCurrentPoint + currentDistance;
					float previousDistance = distances.get(neighbour);
					if (previousDistance == infinity || previousDistance > tentativeDistance) {
						distances.put(neighbour, tentativeDistance);
						shortestApproach.put(neighbour, currentPoint);
					}
				}
			}
			unvisited.remove(currentPoint);
			visited.add(currentPoint);
			currentPoint = selectClosestNode();
			if (currentPoint == null || !unvisitedPointsReachable()) {
				return null;
			} else {
				currentDistance = distances.get(currentPoint);
			}
		}
    	return generateShortestPath(start, destination);

	}
	
	private ArrayList<Point> generateShortestPath(Point start, Point destination) {
		ArrayList<Point> path = new ArrayList<>();
		Point currentPoint = destination;
		path.add(destination);
		while (! currentPoint.equals(start)) {
			currentPoint = this.shortestApproach.get(currentPoint);
			if (currentPoint == null) {
				System.out.println("yo");
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
		expectedPath.add(start);
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