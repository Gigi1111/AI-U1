package s0571269;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import lenz.htw.ai4g.ai.AI;
import lenz.htw.ai4g.ai.DriverAction;
import lenz.htw.ai4g.track.Track;

public class MyClass3 extends AI {

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
    Point prevCheckpoint;
    Graph g;
    boolean debug = false;

    List<Point> shortList = null;
    boolean getShortest = true;
    DPQ dpq=null;
    
    public MyClass3(lenz.htw.ai4g.ai.Info info) {
	   	 super(info);
	   	 enlistForTournament(549481, 571269);
	   	 //also get current checkpoint and current
	   	prevCheckpoint = new Point(info.getCurrentCheckpoint());
	   	initGraph();
	   	 
    }
    float goal_ori() {
      	 return getAtan2(
      			 info.getCurrentCheckpoint().x,
      			 info.getCurrentCheckpoint().y,
      			 info.getX(),
      			 info.getY()
      	 );
     }
    
    private void getGraphEdges() {
    	//not connect to four corners  //now
    	//polygon intersection
		//create array list of  vertex
		int i = 0;
		while(g.getPoint(i)!= null ) {
			int j=i+1;
			Point p1 = new Point(g.getPoint(i).x, g.getPoint(i).y);
			while(g.getPoint(j)!=null) {
				Point q1 = new Point( g.getPoint(j).x,g.getPoint(j).y);
				if(!intersectWithObs(p1,q1)) {
					g.addEdge(p1,q1);	
				}
				j++;
			}
			i++;
		}
		
	}

	private boolean intersectWithObs(Point p1, Point q1) {
		// TODO innerpoints still connected(the obstacle points to itself)
		//first check if they belong to same obstacle
		//to types of check  (1)contain(to check if it's two points from same obs) (2)intersect (to see if there's other obs in between)
		
		for(int i = 0 ; i <obstacles.length;i++) {
			
    		for(int j = 0; j<obstacles[i].npoints; j++) {
    			int N = obstacles[i].npoints;
    			
				Point p2 = new Point(obstacles[i].xpoints[j],obstacles[i].ypoints[j]);
				Point q2 = new Point(obstacles[i].xpoints[(j+1)%N],obstacles[i].ypoints[(j+1)%N]);
				
				//first check they are not next to each other
				if(!p1.equals(p2) && !p1.equals(q2) && !q1.equals(p2) && !q1.equals(q2)) {
					//only checking line, not checking content
					if(doIntersect(p1, q1, p2, q2) ) {
						return true;
					}
					// checking if mid point contain
					if(obstacles[i].contains(new Point((p1.x+q1.x)/2,(p1.y+q1.y)/2))) 
						return true;
				}
    		}
    	}
		return false;
	}

	private void getGraphNodes(Polygon[] obs) {
		//TODO add in current location and checkpoint
		//TODO isReflex not working yet
		//not include  four corners 
		
		//only add point if it's reflect vertex, don't count border(first 2 obstacles)
		//check if the two next to it are making this node protruding
		
		
		//getStartPosition & checkpoint
		Point startPoint = new Point((int)info.getX(),(int)info.getY());
		 g.addPoint(startPoint);//0
		 g.addPoint(info.getCurrentCheckpoint());//1
		
		
    	//now i=2 to avoid corner
    	for(int i = 0 ; i <obs.length;i++) {
//    		allObsPoints[i] = new Vector2f[obs[i].npoints];
    		for(int j = 0; j<obs[i].npoints; j++) {
    			//if is reflex vertex(not concave) add to node
    			if(isReflexecke(obs[i],j)) {
    				//then check outer border
	    			if(!isBoarder(obs[i],j)) {
		    			g.addPoint(new Point(obs[i].xpoints[j],obs[i].ypoints[j]));
		    		}
    			}
    		}
    	}
	}


	private boolean isBoarder(Polygon ob, int j) {
		// TODO Auto-generated method stub
		return ob.xpoints[j]==0 || ob.xpoints[j]==1000 || ob.ypoints[j]==0 || ob.ypoints[j]==1000;
	}

	//** still not working well, get ang but when is ang over 
	private boolean isReflexecke(Polygon ob, int j) {
	
		//get two vectors from j to j-1 and j to j+1, get the smaller angle if middle to smaller angle is contained or intersect the obstacle, it's reflexive
		
		//first get angle bw three points with p1 as center
		int prev = j>0? j-1:ob.npoints-1;
		int next = j<ob.npoints-1? j+1:0;
		Point p1 = new Point(ob.xpoints[j],ob.ypoints[j]);
		Point p0 = new Point(ob.xpoints[prev],ob.ypoints[prev]);
		Point p2 = new Point(ob.xpoints[next],ob.ypoints[next]);
		float a = (p1.x-p0.x)*(p1.x-p0.x) + (p1.y-p0.y)*(p1.y-p0.y);
		float b = (p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y);
		float c = (p2.x-p0.x)*(p2.x-p0.x) + (p2.y-p0.y)*(p2.y-p0.y);
		float result = (float) (Math.acos( (a+b-c) / Math.sqrt(4*a*b) ) * 180/Math.PI);
		//only returns 0-180
		if(debug)
			System.out.println("result: "+result);
		
		if(debug)
			System.out.println("result: "+result);
		double mx = (double)(p2.x+p0.x)/2;
		double my = (double)(p2.y+p0.y)/2;
		if(debug)
			 System.out.println("mid: "+mx+","+my);
		//get slope between mid and p1
		double m = (double)((my-p1.y) / (mx-p1.x));
		if(debug)
			System.out.println("m: "+m);
		//get center p1 to go slightly inward towards mid along the slope
		int i =0;
		if(mx < p1.x) {
			i=-1;
		}
		double nx = (double)p1.x+i;
		double ny =(double)(p1.y+(m *i));
		if(debug)
			System.out.println("new: "+nx+","+ny);
		if(ob.contains(nx,ny))
			return true;
		//if concave, meaning we don't add it to graph node, return false
		return false;
	}

	private float getAtan2(float cx, float cy, float x, float y) {
   	 return (float) Math.atan2(cy - y, cx - x);
    }

    private float getSlope(float cx, float cy, float x, float y) {
   	 return (cy - y) / (cx - x);
    }

    @Override
    public String getName() {
   	 return "Nadja&Fan";
    }
   

    
public boolean onemore =false;
    
    @Override
    public DriverAction update(boolean wasResetAfterCollision) {
//    	System.out.println("update");
    if(info.getCurrentCheckpoint().x != prevCheckpoint.x || info.getCurrentCheckpoint().y != prevCheckpoint.y ) {
    	System.out.println("get graph again");
    	initGraph();
    	
    	drawShortestPath();
    	
		prevCheckpoint = new Point(info.getCurrentCheckpoint());
    }
   	 angularAcc = turnToCheckpoint();

   	 acceleration = getThrottle();
   	 for (int i = 0; i < obstacles.length; i++) {
   		 avoidObstacles(obstacles[i]);
   	 }
   	 return new DriverAction(acceleration, angularAcc);
    }
    
    
    private void drawShortestPath() {
    	
		
	}
	private void initGraph() {
		getShortest = true;
    	g = new Graph(new Point((int)info.getX(),(int)info.getY()),info.getCurrentCheckpoint());
    	getGraphNodes(obstacles);
	   	 getGraphEdges();
	     
	}
	private void avoidObstacles(Polygon obs) {
   	 if (isObstacleThere(0.0, obs) ||
   		 isObstacleThere(-0.3, obs)) {
   	 	angularAcc = .5f;
   	 	acceleration = -0.0f;
   	 }
   	 if (isObstacleThere(0.3, obs)) {
   		 angularAcc = -.5f;
   	 	acceleration = -0.0f;
   	 }

    }

    boolean isObstacleThere(double angularOffset, Polygon obstacle) {
   	 // iterate through x/y of in a specific distance
   	 for (int j = 0; j < 40; j++) {

   		 Point futurePosition = calculateFuturePosition(j, angularOffset);

   		 if (obstacle.contains(futurePosition)) {
//   			 System.out.println(futurePosition);
   			 return true;
   		 }
   	 }
   	 return false;

    }

    String showArray(int[] array, int length) {
   	 String result = "";
   	 for (int i = 0; i < length; i++) {
   		 result = result + array[i] + ", ";
   	 }
   	 return result;
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
   	 if (Math.abs(angle_off_target) < 0.02) {
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
   	 
   	 //make sure we turn the smaller circle
   	 if(Math.abs(angleDifference) > Math.PI) {
   		angleDifference = (float)(Math.PI - Math.abs(goal_ori()) + (Math.PI-Math.abs(currentDirection)));
   		//make sure turning towards goal direction 
   		if(goal_ori()>0 && currentDirection<0) {
   			angleDifference = -angleDifference;
   		 }
   	 }
//   	 System.out.println("goal_ori: " + goal_ori() + " currentDirectoion: " + currentDirection+" angleDIff: "+angleDifference);
   	 
   	 float frictionAcceleration = (- info.getAngularVelocity()) * frictionValue;
   	 
   	 if (
   			 (Math.abs(angleDifference) < 0.2) &&
   		 	(Math.abs(info.getAngularVelocity()) > info.getMaxAbsoluteAngularVelocity() * 0.3f)
   			 ) {
   		 currentAcceleration = - Math.signum(info.getAngularVelocity());
   	 }
   	 currentAcceleration = currentAcceleration + (Math.signum(angleDifference) * 1.f);
   	 
   	 return currentAcceleration + frictionAcceleration;
    }

    @Override
    public String getTextureResourceName() {
    	return "/s0571269/car2.png";
    }
    @Override
	public void doDebugStuff() {
   	 glBegin(GL_LINES);
   	 glColor3f(1, 0, 0);
   	 glVertex2f(info.getX(), info.getY());
   	 glVertex2d(info.getCurrentCheckpoint().getX(), info.getCurrentCheckpoint().getY());
   	 glEnd();
   	
     for (Map.Entry<Point, List<Edge>> entry : g.adjVertices.entrySet()) {
    	 for(Edge v : entry.getValue()) {
	    	 glBegin(GL_LINES);
	         glColor3f(1,1,1);
			 glVertex2f(entry.getKey().x,entry.getKey().y);
			 glVertex2f(v.point.x,v.point.y);
			 glEnd();
    	 }
     }
     if(getShortest) {
 		 dpq = new DPQ(g.getNumOfPoints(),g); 
 	     dpq.dijkstra(); 
 	     shortList = dpq.printShortestPathTo(dpq.dst);
// 	     System.out.println(shortList.size());
 	     getShortest = false;
 	    
     }
     else {
	    	 for(int i=0; i<shortList.size()-1;i++) {
//		  		 System.out.print("("+shortList.get(i).x+","+shortList.get(i).y+")"+"->"+"("+shortList.get(i+1).x+","+shortList.get(i+1).y+")");
//		  		 System.out.println();
		  		 glBegin(GL_LINES);
		          glColor3f(1,0,1);
		  		 glVertex2f(shortList.get(i).x,shortList.get(i).y);
		  		 glVertex2f(shortList.get(i+1).x,shortList.get(i+1).y);
		  		 glEnd();
		  	 }
	     
     	
     }
     
     
	}
    static boolean onSegment(Point p, Point q, Point r) 
    { 
        if (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) && 
            q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y)) 
        return true; 
      
        return false; 
    } 
      
    static int orientation(Point p, Point q, Point r) 
    { 
        // See https://www.geeksforgeeks.org/orientation-3-ordered-points/ 
        // for details of below formula. 
        int val = (q.y - p.y) * (r.x - q.x) - 
                (q.x - p.x) * (r.y - q.y); 
      
        if (val == 0) return 0; // colinear 
      
        return (val > 0)? 1: 2; // clock or counterclock wise 
    } 
      
    // The main function that returns true if line segment 'p1q1' 
    // and 'p2q2' intersect. 
    static boolean doIntersect(Point p1, Point q1, Point p2, Point q2) 
    { 
        // Find the four orientations needed for general and 
        // special cases 
        int o1 = orientation(p1, q1, p2); 
        int o2 = orientation(p1, q1, q2); 
        int o3 = orientation(p2, q2, p1); 
        int o4 = orientation(p2, q2, q1); 
      
        // General case 
        if (o1 != o2 && o3 != o4) 
            return true; 
      
        // Special Cases 
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1 
        if (o1 == 0 && onSegment(p1, p2, q1)) return true; 
      
        // p1, q1 and q2 are colinear and q2 lies on segment p1q1 
        if (o2 == 0 && onSegment(p1, q2, q1)) return true; 
      
        // p2, q2 and p1 are colinear and p1 lies on segment p2q2 
        if (o3 == 0 && onSegment(p2, p1, q2)) return true; 
      
        // p2, q2 and q1 are colinear and q1 lies on segment p2q2 
        if (o4 == 0 && onSegment(p2, q1, q2)) return true; 
      
        return false; // Doesn't fall in any of the above cases 
    } 
}


