package s0571269;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.util.vector.Vector2f;
import lenz.htw.ai4g.ai.AI;
import lenz.htw.ai4g.ai.DriverAction;
import lenz.htw.ai4g.track.Track;

public class MySuperClass extends AI {

	// max 1 min -1
	float angularAcc;
	float throttle;
	Vector2f velocity = info.getVelocity();
	Track track = info.getTrack();
	Track currentTrack;
	Polygon[] obstacles = track.getObstacles();
	int desireTime = 100;
	float orientatin = info.getOrientation(); // Blickrichtung zwischen -PI und +PI
//	float desiredSpeed = distance();
	float currentSpeed = info.getVelocity().length();
	float acceleration;
	Point checkpoint = info.getCurrentCheckpoint();
	Point lastCheckpoint;
	private PathFinder pathFinder;
	private Grid grid;
	private Graph graph;
	private ArrayList<Point> currentPath = new ArrayList<>();
	private PathOptimizer pathOptimizer = new PathOptimizer();
	private int cellSize = 20;
	private Point spawnPoint;

	public MySuperClass(lenz.htw.ai4g.ai.Info info) {
		super(info);
		enlistForTournament(549481, 571269);
		grid = new Grid(track.getHeight(), track.getWidth(), obstacles, cellSize);
		graph  =  grid.getGraph();
		pathFinder = new PathFinder(graph);
		spawnPoint = new Point(500, 500);
		lastCheckpoint = null;
	}

	@Override
	public DriverAction update(boolean wasResetAfterCollision) {
		Point targetPoint;
		
		if (waypointReached()) {
			selectNextWaypoint();
		}
		
		if (hasExploded()) {
			System.out.println("boom");
			calculatePath();			
		}

		if (checkpointHasChanged()) {
			onCheckpointChange();
		}
		
		targetPoint = getNextWaypoint();
		angularAcc = turnToPoint(targetPoint);
		acceleration = getThrottle(targetPoint);
		return new DriverAction(acceleration, angularAcc);
	}

	private boolean hasExploded() {
		return getCurrentLocation().equals(spawnPoint);
	}

	private void selectNextWaypoint() {
		if (! (this.currentPath == null) && !this.currentPath.isEmpty()) {
			this.currentPath.remove(0);
		}
	}

	private boolean waypointReached() {
		return this.grid.pointToNode(getCurrentLocation()).equals(
				this.grid.pointToNode(getNextWaypoint())
				);
	}

	private void onCheckpointChange() {
		if (this.lastCheckpoint != null) {
			this.spawnPoint = new Point(this.lastCheckpoint);
		}
		calculatePath();
		this.lastCheckpoint = new Point(info.getCurrentCheckpoint());
	}
	
	private void calculatePath() {
		Point currentNode = this.grid.pointToNode(getCurrentLocation());
		Point targetNode = this.grid.pointToNode(info.getCurrentCheckpoint());
		currentPath = this.pathFinder.getShortestWay(currentNode, targetNode);
		if (currentPath == null || currentPath.isEmpty()) {
			System.out.println("WARNING: Could not calculate path");
		} else {
			System.out.println("INFO: Calculated new path");
		}
		// currentPath = this.pathOptimizer.optimize(currentPath);
	}

	private boolean checkpointHasChanged() {
		Point currentCheckpoint = info.getCurrentCheckpoint();
		return ! currentCheckpoint.equals(lastCheckpoint);
	}

	@Override
	public String getName() {
		return "FAN NADJA";
	}

	@Override
	public String getTextureResourceName() {
		return "car.png";
	}

	private float getAtan2(float cx, float cy, float x, float y) {
		return (float) Math.atan2(cy - y, cx - x);
	}

	String showArray(int[] array, int length) {
		String result = "";
		for (int i = 0; i < length; i++) {
			result = result + array[i] + ", ";
		}
		return result;
	}
	private float getThrottle(Point targetPoint) {
		float angle_off_target = info.getOrientation() - getGoalOrientation(targetPoint);
		if (Math.abs(angle_off_target) < 0.2) {
			return 1f;
		} else {
			return 0.01f;
		}
	}

	private float turnToPoint(Point targetPoint) {
		float orientationDifference = info.getOrientation() - getGoalOrientation(targetPoint);
		if (orientationDifference >= 0.001) {
			return 0.f;
		} else if (info.getAngularVelocity() < 0.1f) {
			return info.getMaxAbsoluteAngularAcceleration() * Math.signum(orientationDifference);
		}
		float remainingTimeToTurn = 2.f * orientationDifference / info.getAngularVelocity();
		float throttle = info.getAngularVelocity() * info.getAngularVelocity() / 4.f / orientationDifference;
		System.out.println( "throttle: " + throttle);
		System.out.println("rttt: " + remainingTimeToTurn);
		return throttle;
		
	}

	private float getGoalOrientation(Point targetPoint) {
		return acceleration = getAtan2(
				targetPoint.x, targetPoint.y, info.getX(), info.getY());
	}
	
	private Point getNextWaypoint() {
		if (this.currentPath == null || this.currentPath.isEmpty()) {
			return info.getCurrentCheckpoint();
		} else {
			return this.currentPath.get(0);
		}
	}

	private void drawPath() {
		for (Point point : currentPath) {
			this.drawRectangle(point.x - 3, point.y - 3, 6, 6, 0, 1, 0);
		}
	}

	private Point getCurrentLocation() {
		Point point = new Point();
		point.setLocation(info.getX(), info.getY());
		return point;
	}

	public void drawRectangle(int x, int y, int width, int height, float red, float green, float blue) {
		glBegin(GL_QUADS);
		glColor3f(red, green, blue);
		glVertex2f(x, y);
		glVertex2d(x + width, y);
		glVertex2d(x + width, y + height);
		glVertex2d(x, y + height);
		glEnd();		
	}
	
	private void drawGrid() {
		ArrayList<Point> freespace = new ArrayList<>(graph.getAllNodes());
		for (Point point : freespace ) {
			drawRectangle(point.x - cellSize/2 + 1, point.y - cellSize/2 + 1, cellSize - 2, cellSize -2, 1, 0, 0);
		}
	}
	
	@Override
	public void doDebugStuff() {
		drawGrid();
		drawNeighbours();
		drawPath();
		
		glBegin(GL_LINES);
		glColor3f(0, 0, 1);
		glVertex2f(info.getX(), info.getY());
		glVertex2d(info.getCurrentCheckpoint().getX(), info.getCurrentCheckpoint().getY());
		glEnd();
	}

	private void drawNeighbours() {
		for (Point cell: new ArrayList<Point>(graph.getAllNodes())) {
			for (Point neighbour : this.graph.getNeighbours(cell)) {
				drawLine(cell, neighbour, 1.f, 0.f, 0.f);
				
			}
		}
		
	}

	private void drawLine(Point s, Point t, float r, float g, float b) {
		glBegin(GL_LINES);
		glColor3f(r, g, b);
		glVertex2f(s.x, s.y);
		glVertex2d(t.x, t.y);
		glEnd();
	}

}
