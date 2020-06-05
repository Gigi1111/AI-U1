//package s0571269;
//
//import static org.lwjgl.opengl.GL11.*;
//
//import java.awt.Point;
//import java.awt.Polygon;
//import java.awt.geom.Line2D;
//import java.awt.geom.Point2D;
//import java.util.Map;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import org.lwjgl.util.vector.Vector2f;
//
//import lenz.htw.ai4g.ai.AI;
//import lenz.htw.ai4g.ai.DriverAction;
//import lenz.htw.ai4g.track.Track;
//
//public class MyClass extends AI {
//
//    // max 1 min -1
//    float angularAcc;
//    float throttle;
//    Vector2f velocity = info.getVelocity();
//    Track track = info.getTrack();
//    Polygon[] obstacles = track.getObstacles();
//    int wunschZeit = 100;
//    float orientatin = info.getOrientation(); // Blickrichtung zwischen -PI und +PI
//    float angularVelocity = info.getAngularVelocity(); // aktuelle Drehgeschwindigkeit
//    int trackWidth = track.getWidth();
//    int trackHeight = track.getHeight();
//    Point currentPos = new Point((int)info.getX(),(int)info.getY());
//    Point checkpoint = info.getCurrentCheckpoint();
//    float desiredSpeed = distance(currentPos, checkpoint);
//    float currentSpeed = info.getVelocity().length();
//    float dist = distance(currentPos, checkpoint);
//    float acceleration;
//    Point prevCheckpoint;
//    Graph g;
//    boolean debug = false;
//
//    List<Point> shortList = null;
//    boolean getShortest = true;
//    DPQ dpq=null;
//    
//    public boolean onemore =false;
//    int listPointCounter = 1;
//    
//    Point prevPos = null;
//    public MyClass(lenz.htw.ai4g.ai.Info info) {
//	   	 super(info);
//	   	 enlistForTournament(549481, 571269);
//	   	 //also get current checkpoint and current
//	   	prevCheckpoint = new Point(info.getCurrentCheckpoint());
//	   	initGraph();
//	   	onemore =false;
//	    listPointCounter = 1;
//	    prevPos = new Point((int)info.getX(),(int)info.getY());	   	 
//    }
//    float get_ori(Point p) {
//      	 return getAtan2(
//      			p.x,
//      			 p.y,
//      			 info.getX(),
//      			 info.getY()
//      	 );
//     }
//    
//    private void getGraphEdges() {
//    	//not connect to four corners  //now
//    	//polygon intersection
//		//create array list of  vertex
//		int i = 0;
//		while(g.getPoint(i)!= null ) {
//			int j=i+1;
//			Point p1 = new Point(g.getPoint(i).x, g.getPoint(i).y);
//			while(g.getPoint(j)!=null) {
//				Point q1 = new Point( g.getPoint(j).x,g.getPoint(j).y);
//				if(!intersectWithObs(p1,q1)) {
//					g.addEdge(p1,q1);	
//				}
//				j++;
//			}
//			i++;
//		}
//		
//	}
//
//	private boolean intersectWithObs(Point p1, Point q1) {
//		// TODO innerpoints still connected(the obstacle points to itself)
//		//first check if they belong to same obstacle
//		//to types of check  (1)contain(to check if it's two points from same obs) (2)intersect (to see if there's other obs in between)
//		
//		for(int i = 0 ; i <obstacles.length;i++) {
//			
//    		for(int j = 0; j<obstacles[i].npoints; j++) {
//    			int N = obstacles[i].npoints;
//    			
//				Point p2 = new Point(obstacles[i].xpoints[j],obstacles[i].ypoints[j]);
//				Point q2 = new Point(obstacles[i].xpoints[(j+1)%N],obstacles[i].ypoints[(j+1)%N]);
//				
//				//first check they are not next to each other
//				if(!p1.equals(p2) && !p1.equals(q2) && !q1.equals(p2) && !q1.equals(q2)) {
//					//only checking line, not checking content
//					if(doIntersect(p1, q1, p2, q2) ) {
//						return true;
//					}
//					// checking if mid point contain
//					if(obstacles[i].contains(new Point((p1.x+q1.x)/2,(p1.y+q1.y)/2))) 
//						return true;
//				}
//    		}
//    	}
//		return false;
//	}
//
//	private void getGraphNodes(Polygon[] obs) {
//		//TODO add in current location and checkpoint
//		//TODO isReflex not working yet
//		//not include  four corners 
//		
//		//only add point if it's reflect vertex, don't count border(first 2 obstacles)
//		//check if the two next to it are making this node protruding
//		
//		
//		//getStartPosition & checkpoint
//		Point startPoint = new Point((int)info.getX(),(int)info.getY());
//		 g.addPoint(startPoint);//0
//		 g.addPoint(info.getCurrentCheckpoint());//1
//		
//		
//    	//now i=2 to avoid corner
//    	for(int i = 0 ; i <obs.length;i++) {
////    		allObsPoints[i] = new Vector2f[obs[i].npoints];
//    		for(int j = 0; j<obs[i].npoints; j++) {
//    			//if is reflex vertex(not concave) add to node
//    			if(isReflexecke(obs[i],j)) {
//    				//then check outer border
//	    			if(!isBoarder(obs[i],j)) {
//		    			g.addPoint(new Point(obs[i].xpoints[j],obs[i].ypoints[j]));
//		    		}
//    			}
//    		}
//    	}
//	}
//
//
//	private boolean isBoarder(Polygon ob, int j) {
//		// TODO Auto-generated method stub
//		return ob.xpoints[j]==0 || ob.xpoints[j]==1000 || ob.ypoints[j]==0 || ob.ypoints[j]==1000;
//	}
//
//	//** still not working well, get ang but when is ang over 
//	private boolean isReflexecke(Polygon ob, int j) {
//	
//		//get two vectors from j to j-1 and j to j+1, get the smaller angle if middle to smaller angle is contained or intersect the obstacle, it's reflexive
//		
//		//first get angle bw three points with p1 as center
//		int prev = j>0? j-1:ob.npoints-1;
//		int next = j<ob.npoints-1? j+1:0;
//		Point p0 = new Point(ob.xpoints[j],ob.ypoints[j]);
//		Point p1 = new Point(ob.xpoints[prev],ob.ypoints[prev]);
//		Point p2 = new Point(ob.xpoints[next],ob.ypoints[next]);
//		Vector2f M = new Vector2f(p1.x-p0.x,p1.y-p0.y);
//		Vector2f C = new Vector2f(p2.x-p0.x,p2.y-p0.y);
//		double dot = Vector2f.dot(M,C);
//		double det = ((C.getX()*M.getY()) - (C.getY()*M.getX()));
//		double angle = Math.toDegrees(Math.atan2(det, dot));
//		if(debug)
//			System.out.println("ang: "+angle);
//		if(angle>0) {
//			return true;
//		}
//		return false;
//	}
//
//	private float getAtan2(float cx, float cy, float x, float y) {
//   	 return (float) Math.atan2(cy - y, cx - x);
//    }
//
//    private float getSlope(float cx, float cy, float x, float y) {
//   	 return (cy - y) / (cx - x);
//    }
//
//    @Override
//    public String getName() {
//   	 return "XAEA-12";
//    }
//   
//    int i=0;
//   
//
//    @Override
//    public DriverAction update(boolean wasResetAfterCollision) {
////    	System.out.println("update");
//    
//    i++;
//    if(i%10==0) {
//    	i=0;
//    	prevPos = new Point((int)info.getX(),(int)info.getY());
//    }
//    if(info.getCurrentCheckpoint().x != prevCheckpoint.x || info.getCurrentCheckpoint().y != prevCheckpoint.y ) {
//    	if(debug)
//    		System.out.println("get graph again");
//    	initGraph();
//    	listPointCounter=1;
//		prevCheckpoint = new Point(info.getCurrentCheckpoint());
//    }
//    
//    Point p = info.getCurrentCheckpoint();
//    
//    
//  //check if getting further away, init again
//    //prev position is closer to checkpoint then currenct
//    if(debug)
//    	System.out.println("prev:"+prevPos.x+","+prevPos.y);
//    int buffer = 0;
//    if(distance(prevPos,p)+buffer<distance(null,p)) {// || distance(prevPos,p)==distance(null,p)) {
//    	if(debug)
//    		System.out.println("!!!!!!!!!!!!!init!!!!!!!");
//    	
//    	initGraph();
//    	listPointCounter=1;
//    	
//    }
//    
//    if(shortList!=null) {
//    
//   	 p = shortList.get(listPointCounter);
//    	 if(distance(null,p)<20 && listPointCounter < shortList.size()-1 && !p.equals(info.getCurrentCheckpoint())) {
//    		 if(debug)
//    			 System.out.println("!!!!!!!!!!");
//    	     
//    		 listPointCounter++;
//    	   }
//    if(debug) {
//   	  System.out.println("goal:"+p);
//   	   System.out.println("pos:"+info.getX()+","+info.getY());
//   	   System.out.println("-----dist: "+distance(null,p)+"-i:"+listPointCounter+"---shlistsize:"+shortList.size()+"-----equals: "+p.equals(info.getCurrentCheckpoint())+"---");
//    }
//    }
//  
//    
//    angularAcc = turnToCheckpoint(p);
//  	 acceleration = getThrottle(p);
//   	 for (int i = 0; i < obstacles.length; i++) {
//   		 avoidObstacles(obstacles[i]);
//   	 }
//   	 return new DriverAction(acceleration, angularAcc);
//    }
//    
//
//	private void initGraph() {
//		getShortest = true;
//    	g = new Graph(new Point((int)info.getX(),(int)info.getY()),info.getCurrentCheckpoint());
//    	getGraphNodes(obstacles);
//	   	 getGraphEdges();
//	     
//	}
//	private void updateGraphWithNewStartPoint() {
//		getShortest = true;
//    	g = new Graph(new Point((int)info.getX(),(int)info.getY()),info.getCurrentCheckpoint());
//    	getGraphNodes(obstacles);
//	   	 getGraphEdges();
//	     
//	}
////	private void updateGraphWithNewCheckPoint() {
////		getShortest = true;
////    	g = new Graph(new Point((int)info.getX(),(int)info.getY()),info.getCurrentCheckpoint());
////    	getGraphNodes(obstacles);
////	   	 getGraphEdges();
////	     
////	}
//	private void avoidObstacles(Polygon obs) {
//   	 if (isObstacleThere(0.0, obs) ||
//   		 isObstacleThere(-0.3, obs)) {
//   	 	angularAcc = .5f;
//   	 	acceleration = -0.0f;
//   	 }
//   	 if (isObstacleThere(0.3, obs)) {
//   		 angularAcc = -.5f;
//   	 	acceleration = -0.0f;
//   	 }
//
//    }
//
//    boolean isObstacleThere(double angularOffset, Polygon obstacle) {
//   	 // iterate through x/y of in a specific distance
//   	 for (int j = 0; j < 40; j++) {
//
//   		 Point futurePosition = calculateFuturePosition(j, angularOffset);
//
//   		 if (obstacle.contains(futurePosition)) {
////   			 System.out.println(futurePosition);
//   			 return true;
//   		 }
//   	 }
//   	 return false;
//
//    }
//
//    String showArray(int[] array, int length) {
//   	 String result = "";
//   	 for (int i = 0; i < length; i++) {
//   		 result = result + array[i] + ", ";
//   	 }
//   	 return result;
//    }
//
//    private Point calculateFuturePosition(int stepCount, double angularOffset) {
//   	 double xNew = info.getX() + (double) stepCount * Math.cos(info.getOrientation() + angularOffset);
//   	 double yNew = info.getY() + (double) stepCount * Math.sin(info.getOrientation() + angularOffset);
//   	 Point futurePos = new Point();
//   	 futurePos.setLocation(xNew, yNew);
//   	 return futurePos;
//    }
//
//    private float getThrottle(Point p) {
//   	 float angle_off_target = info.getOrientation() - get_ori(p);
//   	 if (Math.abs(angle_off_target) < 0.02) {
//   		 return 1f;
//   	 } else {
//   		 return 0.5f;
//   	 }
//    }
//
//    private float distance(Point p, Point g) {
//   	 Point goal = g;
//   	 if(p==null)
//   		 p = new Point((int)info.getX(),(int)info.getY());
//   	 Point p0 = p;
//   	 float dist = (float) Math.sqrt(Math.pow(goal.x - p0.x, 2) + Math.pow(goal.y - p0.y, 2));
//   	 return dist;
//    }
//
//    private float turnToCheckpoint(Point p) {
//   	 float frictionValue = 1.f;
//   	 float currentAcceleration = 0f;
//   	 float currentDirection = info.getOrientation();
//   	 float angleDifference = get_ori(p) - currentDirection;
//   	 
//   	 //make sure we turn the smaller circle
//   	 if(Math.abs(angleDifference) > Math.PI) {
//   		angleDifference = (float)(Math.PI - Math.abs(get_ori(p)) + (Math.PI-Math.abs(currentDirection)));
//   		//make sure turning towards goal direction 
//   		if(get_ori(p)>0 && currentDirection<0) {
//   			angleDifference = -angleDifference;
//   		 }
//   	 }
////   	 System.out.println("goal_ori: " + get_ori(info.getCurrentCheckpoint()) + " currentDirectoion: " + currentDirection+" angleDIff: "+angleDifference);
//   	 
//   	 float frictionAcceleration = (- info.getAngularVelocity()) * frictionValue;
//   	 
//   	 if (
//   			 (Math.abs(angleDifference) < 0.2) &&
//   		 	(Math.abs(info.getAngularVelocity()) > info.getMaxAbsoluteAngularVelocity() * 0.3f)
//   			 ) {
//   		 currentAcceleration = - Math.signum(info.getAngularVelocity());
//   	 }
//   	 currentAcceleration = currentAcceleration + (Math.signum(angleDifference) * 1.f);
//   	 
//   	 return currentAcceleration + frictionAcceleration;
//    }
//
//    @Override
//    public String getTextureResourceName() {
//    	return "/s0571269/car2.png";
//    }
//    @Override
//	public void doDebugStuff() {
//   	 glBegin(GL_LINES);
//   	 glColor3f(1, 0, 0);
//   	 glVertex2f(info.getX(), info.getY());
//   	 glVertex2d(info.getCurrentCheckpoint().getX(), info.getCurrentCheckpoint().getY());
//   	 glEnd();
//   	
//     for (Map.Entry<Point, List<Edge>> entry : g.adjVertices.entrySet()) {
//    	 for(Edge v : entry.getValue()) {
//	    	 glBegin(GL_LINES);
//	         glColor3f(1,1,1);
//			 glVertex2f(entry.getKey().x,entry.getKey().y);
//			 glVertex2f(v.point.x,v.point.y);
//			 glEnd();
//    	 }
//     }
//     if(getShortest) {
// 		 dpq = new DPQ(g.getNumOfPoints(),g); 
// 	     dpq.dijkstra(); 
// 	     shortList = dpq.printShortestPathTo(dpq.dst);
//// 	     System.out.println(shortList.size());
// 	     getShortest = false;
// 	    
//     }
//     else {
//	    	 for(int i=0; i<shortList.size()-1;i++) {
////		  		 System.out.print("("+shortList.get(i).x+","+shortList.get(i).y+")"+"->"+"("+shortList.get(i+1).x+","+shortList.get(i+1).y+")");
////		  		 System.out.println();
//		  		 glBegin(GL_LINES);
//		          glColor3f(1,0,1);
//		  		 glVertex2f(shortList.get(i).x,shortList.get(i).y);
//		  		 glVertex2f(shortList.get(i+1).x,shortList.get(i+1).y);
//		  		 glEnd();
//		  	 }
//	     
//     	
//     }
//     
//     
//	}
//    static boolean onSegment(Point p, Point q, Point r) 
//    { 
//        if (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) && 
//            q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y)) 
//        return true; 
//      
//        return false; 
//    } 
//      
//    static int orientation(Point p, Point q, Point r) 
//    { 
//        // See https://www.geeksforgeeks.org/orientation-3-ordered-points/ 
//        // for details of below formula. 
//        int val = (q.y - p.y) * (r.x - q.x) - 
//                (q.x - p.x) * (r.y - q.y); 
//      
//        if (val == 0) return 0; // colinear 
//      
//        return (val > 0)? 1: 2; // clock or counterclock wise 
//    } 
//      
//    // The main function that returns true if line segment 'p1q1' 
//    // and 'p2q2' intersect. 
//    static boolean doIntersect(Point p1, Point q1, Point p2, Point q2) 
//    { 
//        // Find the four orientations needed for general and 
//        // special cases 
//        int o1 = orientation(p1, q1, p2); 
//        int o2 = orientation(p1, q1, q2); 
//        int o3 = orientation(p2, q2, p1); 
//        int o4 = orientation(p2, q2, q1); 
//      
//        // General case 
//        if (o1 != o2 && o3 != o4) 
//            return true; 
//      
//        // Special Cases 
//        // p1, q1 and p2 are colinear and p2 lies on segment p1q1 
//        if (o1 == 0 && onSegment(p1, p2, q1)) return true; 
//      
//        // p1, q1 and q2 are colinear and q2 lies on segment p1q1 
//        if (o2 == 0 && onSegment(p1, q2, q1)) return true; 
//      
//        // p2, q2 and p1 are colinear and p1 lies on segment p2q2 
//        if (o3 == 0 && onSegment(p2, p1, q2)) return true; 
//      
//        // p2, q2 and q1 are colinear and q1 lies on segment p2q2 
//        if (o4 == 0 && onSegment(p2, q1, q2)) return true; 
//      
//        return false; // Doesn't fall in any of the above cases 
//    } 
//}
//
//
