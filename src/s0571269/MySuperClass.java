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
	private double oldCheckpointX;
	private double oldCheckpointY;
	private PathFinder pathFinder;;
	private Grid grid = new Grid(track.getHeight(), track.getWidth(), obstacles);
	private Graph graph;
	private ArrayList<Point> shortestWay;



	public MySuperClass(lenz.htw.ai4g.ai.Info info) {
		super(info);
		enlistForTournament(549481, 571269);

		oldCheckpointX = info.getCurrentCheckpoint().getX();
		oldCheckpointY = info.getCurrentCheckpoint().getY();
		graph  =  grid.getGraph();
		

		// calculate free space once
	}

	@Override
	public DriverAction update(boolean wasResetAfterCollision) {
		Point targetPoint;
		int lastCheckPointX = 0, lastCheckpointY = 0;

		// nach coolision beachten wieder erster checkpoint
		if (info.getCurrentCheckpoint().x != lastCheckPointX || info.getCurrentCheckpoint().y != lastCheckpointY) {
		}

		if (oldCheckpointX == info.getCurrentCheckpoint().getX()
				&& oldCheckpointY == info.getCurrentCheckpoint().getY()) {
			//getshortestway
			Point startPoint = new Point((int)info.getX(), (int)info.getY());
			Point currentCheckP = new Point((int)info.getCurrentCheckpoint().x, (int)info.getCurrentCheckpoint().y);
			pathFinder = new PathFinder(graph);
			
			oldCheckpointX = info.getCurrentCheckpoint().getX();
			oldCheckpointY = info.getCurrentCheckpoint().getY();		
		}
		targetPoint = getNextWaypoint();
		angularAcc = turnToPoint(targetPoint);
		acceleration = getThrottle(targetPoint);
		// for (int i = 0; i < obstacles.length; i++) {
		// avoidObstacles(obstacles[i]);
		// }
		return new DriverAction(acceleration, angularAcc);
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
			return 0.5f;
		}
	}

	private float turnToPoint(Point targetPoint) {
		float frictionValue = 1.f;
		float currentAcceleration = 0f;
		float currentDirection = info.getOrientation();
		float angleDifference = getGoalOrientation(targetPoint) - currentDirection;
		if (Math.abs(angleDifference) > Math.PI) {
			angleDifference = (float) (Math.PI - Math.abs(getGoalOrientation(targetPoint))
					+ (Math.PI - Math.abs(currentDirection)));
			if (getGoalOrientation(targetPoint) > 0 && currentDirection < 0) {
				angleDifference = -angleDifference;
			}
		}

		float frictionAcceleration = (-info.getAngularVelocity()) * frictionValue;
		if ((Math.abs(angleDifference) < 0.2)
				&& (Math.abs(info.getAngularVelocity()) > info.getMaxAbsoluteAngularVelocity() * 0.3f)) {
			currentAcceleration = -Math.signum(info.getAngularVelocity());
		}
		currentAcceleration = currentAcceleration + (Math.signum(angleDifference) * 1.f);
		return currentAcceleration + frictionAcceleration;
	}

	private float getGoalOrientation(Point targetPoint) {
		return acceleration = getAtan2(
				targetPoint.x, targetPoint.y, info.getX(), info.getY());
	}
	
	private Point getNextWaypoint() {
		boolean carInCellOfCheckpoint = this.grid.isSameCell(getCurrentLocation(), info.getCurrentCheckpoint());
		if (carInCellOfCheckpoint) {
			return info.getCurrentCheckpoint();
		} else {
			ArrayList<Point> path;
			Point currentLocation = getCurrentLocation();
			Point targetNode = this.grid.pointToNode(info.getCurrentCheckpoint());
			path = this.pathFinder.getShortestWay(currentLocation, targetNode);
			return path.get(0);
		} 
	}

	private Point getCurrentLocation() {
		Point point = new Point();
		point.setLocation(info.getX(), info.getY());
		return point;
	}

	@Override
	public void doDebugStuff() {
		
		int cellSize = 30;
		ArrayList<Point> freespace = new ArrayList<>(graph.getAllNodes());
		for (Point point : freespace ) {
			glBegin(GL_QUADS);
			glColor3f(1, 0, 0);
			glVertex2f(point.x, point.y);
			glVertex2d(point.x + cellSize, point.y);
			glVertex2d(point.x + cellSize, point.y + cellSize);
			glVertex2d(point.x, point.y + cellSize );
			glEnd();
		}
		
		ArrayList<Point> bestWay = new ArrayList<>(graph.getAllNodes());
		for (Point point : freespace ) {
			glBegin(GL_LINES);
			glColor3f(1, 0, 0);
			glVertex2f(point.x, point.y);
			glVertex2f(point.x, point.y);
			glEnd();
		}

		glBegin(GL_LINES);
		glColor3f(0, 0, 1);
		glVertex2f(info.getX(), info.getY());
		glVertex2d(info.getCurrentCheckpoint().getX(), info.getCurrentCheckpoint().getY());
		glEnd();
	}

}
