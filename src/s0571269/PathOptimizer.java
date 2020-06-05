package s0571269;

import java.awt.Point;
import java.util.ArrayList;

public class PathOptimizer {
	
	ArrayList<Point> originalPath, newPath;
	
	public ArrayList<Point> optimize(ArrayList<Point> path) {
		this.originalPath = path;
		newPath = new ArrayList<>();
		for (Point currentNode : originalPath) {
			Point previousNode = getPreviousNode(currentNode);
			Point secondPreviousNode = getSecondPreviousNode(currentNode);
			if (previousNode == null || secondPreviousNode == null) {
				newPath.add(currentNode);
				continue;
			}
			if (currentNode.x == secondPreviousNode.x || currentNode.y == secondPreviousNode.y) {
				removePreviousNode(currentNode);
			}
		}
		return newPath;
	}

	private void removePreviousNode(Point node) {
		int index = this.newPath.indexOf(node);
		if (index > 0) {
			this.newPath.remove(index);
		}
	}

	private Point getSecondPreviousNode(Point node) {
		return getPreviousNode(getPreviousNode(node));
	}

	private Point getPreviousNode(Point node) {
		if (node == null) {
			return null;
		}
		int index = this.originalPath.indexOf(node);
		if (index > 0) {
			return originalPath.get(index - 1);
		} else {
			 return null;
		}
	}
}
