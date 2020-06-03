package s0571269;

import java.awt.Point;
import java.awt.Polygon;

public class Grid {
	
	private int cellSize = 5;
	private int gridHeight;
	private int gridWidth;
	private boolean freeCells[][];
	
	
	public Grid(int trackHeight, int trackWidth, Polygon[] obstacle) {
		gridHeight = trackHeight / cellSize;
		gridWidth = trackWidth / cellSize;
		freeCells = new boolean[trackHeight][trackWidth];
		getFreeCells(obstacle);
	}
	
	
	public boolean[][] getFreeCells(Polygon[] obstacles) {
		int x = 0;
		int y = 0;
		Polygon[] obstacle = obstacles;
			for (int i = 0; i < gridHeight; i++) {
				for (int j = 0; j < gridWidth; j++) {					
					boolean isCellFree = false;
					x = i * cellSize;
					y = j * cellSize;
					int middle = cellSize / 2;
					for (int k = 0; k < obstacles.length; k++) {
						isCellFree = obstacle[k].intersects(x, y, cellSize, cellSize);
						if (isCellFree) {
							freeCells[i][j] = true;
						}
					}
				}
			}
		return freeCells;
	}
	
	Point pointFromGridCoordinates(int x, int y) {
		return new Point(x * cellSize + cellSize / 2, y * cellSize + cellSize / 2);
	}
	
	Graph getGraph() {
		Graph graph = new Graph();
		for (int i = 0; i < gridHeight; i++) {
			for (int j = 0; j < gridWidth; j++) {	
				if (freeCells[i][j]) {
					graph.addNode(pointFromGridCoordinates(i,j));
					addNeigboursForPoint(graph, i, j);
				}
			}
		}
		return graph;
	}
	
	void addNeigboursForPoint(Graph graph, int x, int y) {
		//left neighbour 
		if (x > 0 && freeCells[x - 1][y]) {
			graph.addNeigbourNode(pointFromGridCoordinates(x, y),pointFromGridCoordinates(x - 1, y));
		}
		//upper neighbour
		if (y > 0 && freeCells[x][y-1]) {
			graph.addNeigbourNode(pointFromGridCoordinates(x, y),pointFromGridCoordinates(x, y-1));
		}
		//right neighbour
		if (x < gridWidth && freeCells[x + 1][y]) {
			graph.addNeigbourNode(pointFromGridCoordinates(x, y),pointFromGridCoordinates(x + 1, y));
		}
		// lower neighbour
		if (y > gridHeight && freeCells[x][y + 1]) {
			graph.addNeigbourNode(pointFromGridCoordinates(x, y),pointFromGridCoordinates(x, y+ 1));
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
