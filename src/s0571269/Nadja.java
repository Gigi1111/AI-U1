package s0571269;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.omg.CORBA.FREE_MEM;

import lenz.htw.ai4g.ai.AI;
import lenz.htw.ai4g.ai.DriverAction;
import lenz.htw.ai4g.track.Track;

public class Nadja extends AI {

	// max 1 min -1
	float angularAcc;
	float throttle;
	Vector2f velocity = info.getVelocity();
	Track track = info.getTrack();
	Polygon[] obstacles = track.getObstacles();
	int wunschZeit = 100;
	float orientatin = info.getOrientation(); // Blickrichtung zwischen -PI und +PI
	float angularVelocity = info.getAngularVelocity(); // aktuelle Drehgeschwindigkeit
	int trackWidth = track.getWidth();
	int trackHeight = track.getHeight();
	float desiredSpeed = distance();
	float currentSpeed = info.getVelocity().length();
	float dist = distance();
	float acceleration;
	Point checkpoint = info.getCurrentCheckpoint();
	static final int CELL_SIZE = 20;
	int cellHeight = trackHeight / CELL_SIZE;
	int cellWidth = trackWidth / CELL_SIZE;
	boolean freeSpaceGraph[][] = new boolean[trackHeight][trackWidth];
	List<Point> freeNeighbourNodes = new ArrayList<Point>(); //local

	public Nadja(lenz.htw.ai4g.ai.Info info) {
		super(info);
		enlistForTournament(549481, 571269);

		// fill up graph once
		 createFreeSpaceGraph(info.getTrack().getObstacles());		
	}

	
	private boolean[][] createFreeSpaceGraph(Polygon[] obstacles) {
		int x = 0;
		int y = 0;
			for (int i = 0; i < cellHeight; i++) {
				for (int j = 0; j < cellWidth; j++) {					
					boolean isCellFree = false;
					x = i * CELL_SIZE;
					y = j * CELL_SIZE;
					for (int k = 0; k < obstacles.length; k++) {
						isCellFree = track.getObstacles()[k].intersects(x, y, CELL_SIZE, CELL_SIZE);
						if (isCellFree) {
							freeSpaceGraph[i][j] = true;
							Point2D freeNode = new Point(x, y);
//							freeNodes.add(freeNode);
//					} else {
//						freeSpaceGraph[i][j] = false;
						}
					}
				}
			}
		return freeSpaceGraph;
	}
	
	

	private float getAtan2(float cx, float cy, float x, float y) {
		return (float) Math.atan2(cy - y, cx - x);
	}
	

	private float getSlope(float cx, float cy, float x, float y) {
		return (cy - y) / (cx - x);
	}
	

	@Override
	public String getName() {
		return "FAN NADJA";
	}

	
	@Override
	public DriverAction update(boolean wasResetAfterCollision) {
		int lastCheckPointX = 0, lastCheckpointY = 0;
		
		// nach coolision beachten wieder erster checkpoint
		if (info.getCurrentCheckpoint().x != lastCheckPointX || info.getCurrentCheckpoint().y != lastCheckpointY) {
			// int way = freeSpaceGraph.searchWay(info.getX(), info.getY(),
			// info.getCurrentCheckpoint().x,info.getCurrentCheckpoint().y);
		}

		Point checkPoint = info.getCurrentCheckpoint();// Zielposition

		angularAcc = turnToCheckpoint();

		acceleration = getThrottle();
		for (int i = 0; i < obstacles.length; i++) {
			avoidObstacles(obstacles[i]);
		}

		return new DriverAction(acceleration, angularAcc);
	}

	void searchWay(float currentX, float currentY, int aimX, int aimY) {

	}

	String showArray(int[] array, int length) {
		String result = "";
		for (int i = 0; i < length; i++) {
			result = result + array[i] + ", ";
		}
		return result;
	}

	private void avoidObstacles(Polygon obs) {
		if (isObstacleThere(0.0, obs) || isObstacleThere(-0.3, obs)) {
			angularAcc = .5f;
			acceleration = -0.0f;
		}
		if (isObstacleThere(0.3, obs)) {
			angularAcc = -.5f;
			acceleration = 0.5f;
		}

	}

	boolean isObstacleThere(double angularOffset, Polygon obstacle) {
		// iterate through x/y of in a specific distance
		for (int j = 0; j < 40; j++) {

			Point futurePosition = calculateFuturePosition(j, angularOffset);

			if (obstacle.contains(futurePosition)) {
				return true;
			}
		}
		return false;

	}

	private Point calculateFuturePosition(int stepCount, double angularOffset) {
		double xNew = info.getX() + (double) stepCount * Math.cos(info.getOrientation() + angularOffset);
		double yNew = info.getY() + (double) stepCount * Math.sin(info.getOrientation() + angularOffset);
		Point futurePos = new Point();
		futurePos.setLocation(xNew, yNew);
		return futurePos;
	}

	private float getThrottle() {
		float angle_off_target = info.getOrientation() - goal_ori();
		if (Math.abs(angle_off_target) < 0.2) {
			return 1f;
		} else {
			return 0.5f;
		}
	}

	private float distance() {
		Point goal = info.getCurrentCheckpoint();
		float dist = (float) Math.sqrt(Math.pow(goal.x - info.getX(), 2) + Math.pow(goal.y - info.getY(), 2));
		return dist;
	}

	private float turnToCheckpoint() {
		float frictionValue = 1.f;
		float currentAcceleration = 0f;
		float currentDirection = info.getOrientation();
		float angleDifference = goal_ori() - currentDirection;

		if (Math.abs(angleDifference) > Math.PI) {
			angleDifference = (float) (Math.PI - Math.abs(goal_ori()) + (Math.PI - Math.abs(currentDirection)));

			if (goal_ori() > 0 && currentDirection < 0) {
				angleDifference = -angleDifference;
			}
		}

		// System.out.println("angleDifff: " + angleDifference + " currentDirectoion: "
		// + currentDirection);
		float frictionAcceleration = (-info.getAngularVelocity()) * frictionValue;
		if ((Math.abs(angleDifference) < 0.2)
				&& (Math.abs(info.getAngularVelocity()) > info.getMaxAbsoluteAngularVelocity() * 0.3f)) {
			currentAcceleration = -Math.signum(info.getAngularVelocity());
		}
		currentAcceleration = currentAcceleration + (Math.signum(angleDifference) * 1.f);

		return currentAcceleration + frictionAcceleration;
	}

	// Adjazenzliste
	private float getAllObstaclePoints(float onePointOfObstacle) {

		System.out.println(onePointOfObstacle);

		return (Float) null;
	}

	@Override
	public String getTextureResourceName() {
		return "car.png";
	}

	float goal_ori() {
		return getAtan2(info.getCurrentCheckpoint().x, info.getCurrentCheckpoint().y, info.getX(), info.getY());
	}

	@Override
	public void doDebugStuff() {
		//draw freespace graph negative
		int x = 0;
		int y = 0;
		for (int i = 0; i < cellHeight; i++) {
			for (int j = 0; j < cellWidth; j++) {

				x = i * CELL_SIZE;
				y = j * CELL_SIZE;
				if (freeSpaceGraph[i][j]) {
					glBegin(GL_QUADS);
					glColor3f(1, 0, 0);
					glVertex2f(x, y);
					glVertex2f(x + CELL_SIZE, y);
					glVertex2f(x + CELL_SIZE, y + CELL_SIZE);
					glVertex2f(x, y + CELL_SIZE);
					glEnd();
//				} else {
//					glBegin(GL_QUADS);
//					glColor3f(0, 1, 0);
//					glVertex2f(x, y);
//					glVertex2f(x + CELL_SIZE, y);
//					glVertex2f(x + CELL_SIZE, y + CELL_SIZE);
//					glVertex2f(x, y + CELL_SIZE);
//					glEnd();
//					System.out.println("else");
//					// System.out.println(freeSpaceGraph[i][j]);
				}

			}
			
		}
		
//	    System.out.println(Arrays.deepToString(freeSpaceGraph));



		glBegin(GL_LINES);
		glColor3f(0, 0, 1);
		glVertex2f(info.getX(), info.getY());
		glVertex2d(info.getCurrentCheckpoint().getX(), info.getCurrentCheckpoint().getY());
		glEnd();
	}

	
	
}
