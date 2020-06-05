package s0571269;

import java.awt.Point;
import java.awt.Polygon;

public class Grid {

	private int cellSize;
	private int gridHeight;
	private int gridWidth;
	private boolean freeCells[][];

	public Grid(int trackHeight, int trackWidth, Polygon[] obstacle, int cellSize) {
		this.cellSize = cellSize;
		gridHeight = trackHeight / cellSize;
		gridWidth = trackWidth / cellSize;
		freeCells = new boolean[trackHeight][trackWidth];
		calculateFreeCells(obstacle);
	}

	Graph getGraph() {
		Graph graph = new Graph();
		for (int i = 0; i < gridHeight; i++) {
			for (int j = 0; j < gridWidth; j++) {
				if (freeCells[i][j]) {
					graph.addNode(pointFromGridCoordinates(i, j));
					addNeigboursForPoint(graph, i, j);
				}
			}
		}
		return graph;
	}

	private void calculateFreeCells(Polygon[] obstacles) {
		for (int i = 0; i < gridHeight; i++) {
			for (int j = 0; j < gridWidth; j++) {
				int x = i * cellSize;
				int y = j * cellSize;
				boolean isCellFree = true;
				for (Polygon obstacle : obstacles) {
					isCellFree &= !(obstacle.intersects(x, y, cellSize, cellSize));
				}
				freeCells[i][j] = isCellFree;
			}
		}
	}

	Point pointFromGridCoordinates(int x, int y) {
		return new Point(x * cellSize + cellSize / 2, y * cellSize + cellSize / 2);
	}

	
	void addNeigboursForPoint(Graph graph, int x, int y) {
		boolean left = isFree(x-1, y);
		boolean right = isFree(x+1, y);
		boolean up = isFree(x, y+1);
		boolean down = isFree(x, y-1);
		boolean upleft = isFree(x-1, y+1);
		boolean upright = isFree(x+1, y+1);
		boolean downleft = isFree(x-1, y-1);
		boolean downright = isFree(x+1, y-1);

		if (left) {
			graph.addNeigbourNode(pointFromGridCoordinates(x, y), pointFromGridCoordinates(x - 1, y));
		}
		if (down) {
			graph.addNeigbourNode(pointFromGridCoordinates(x, y), pointFromGridCoordinates(x, y - 1));
		}
		if (right) {
			graph.addNeigbourNode(pointFromGridCoordinates(x, y), pointFromGridCoordinates(x + 1, y));
		}
		if (up) {
			graph.addNeigbourNode(pointFromGridCoordinates(x, y), pointFromGridCoordinates(x, y + 1));
		}
		if (up && left && upleft) {
			graph.addNeigbourNode(pointFromGridCoordinates(x, y), pointFromGridCoordinates(x-1, y + 1));
		}
		if (up && right && upright) {
			graph.addNeigbourNode(pointFromGridCoordinates(x, y), pointFromGridCoordinates(x+1, y + 1));
		}
		if (down && left && downleft) {
			graph.addNeigbourNode(pointFromGridCoordinates(x, y), pointFromGridCoordinates(x-1, y - 1));
		}
		if (down && right && downright) {
			graph.addNeigbourNode(pointFromGridCoordinates(x, y), pointFromGridCoordinates(x+1, y - 1));
		}
	}
	
	

	private boolean isFree(int x, int y) {
		if (x >= 0 && x < this.gridWidth && y >= 0 && y < this.gridHeight) {
			return this.freeCells[x][y];
		} else {
			return false;
		}
	}

	public Point pointToNode(Point point) {
		int gridX, gridY;
		gridX = (int) point.x / this.cellSize;
		gridY = (int) point.y / this.cellSize;
		return this.pointFromGridCoordinates(gridX, gridY);
	}

	public boolean isSameCell(Point s, Point t) {
		return pointToCell(s).equals(pointToCell(t));
	}

	private Point pointToCell(Point point) {
		int gridX, gridY;
		gridX = (int) point.x / this.cellSize;
		gridY = (int) point.y / this.cellSize;
		return new Point(gridX, gridY);
	}

}
