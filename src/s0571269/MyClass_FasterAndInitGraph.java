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
//public class MyClass_FasterAndInitGraph extends AI {
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
//    //faster
//    Vector2f curCheck;
//    Vector2f orientation;
//    Vector2f curPos;//currentposition
//    Vector2f destination=new Vector2f();
//    Vector2f midDetect=new Vector2f(),leftDetect=new Vector2f(),rightDetect=new Vector2f();
//    float distToDes;
//    float desiredAV;
//    boolean stillUpdating= false;
//    
//    boolean debug = false;
//
//    List<Point> shortList = null;
//    boolean getShortest = true;
//    DPQ dpq=null;
//    
//
//    int listPointCounter = 1;
//    
//    Point prevPos = null;
//    
//    public MyClass_FasterAndInitGraph(lenz.htw.ai4g.ai.Info info) {
//	   	 super(info);
//	   	 enlistForTournament(549481, 571269);
//	   	 
//	   	 //also get current checkpoint and current
//	   	prevCheckpoint = new Point(info.getCurrentCheckpoint());
//	   	
//	   	initGraph();
//	    listPointCounter = 1;
//	    //prevPos is for seeing if car is always approaching next node
//	    prevPos = new Point((int)info.getX(),(int)info.getY());	   	 
//    }
//	
//    @Override
//    public String getName() {
//   	 return "Fast";
//    }
//    
//
//    float get_ori(Point p) {
//      	 return getAtan2(
//      			p.x,
//      			 p.y,
//      			 info.getX(),
//      			 info.getY()
//      	 );
//     }
//    
//	private float getAtan2(float cx, float cy, float x, float y) {
//   	 return (float) Math.atan2(cy - y, cx - x);
//    }
//   
//    int i=0;
//    int s1 = 0,s2=0,s4=1;
//    boolean backedup=false;
//    Point spin1=null;
//    Point spin2=null;
//    Point spin4=null;
//    int countBackUp = 0;
//    @Override
//    public DriverAction update(boolean wasResetAfterCollision) {
//    
//    	//compare with prev point from 10 frames ago to see if car is approaching the next node, 
//    	//if not, need to recalculate graph
//    	if(getShortest) {
//    		
////    		System.out.println("==================try to getshortest==========================");
////    		if(!stillUpdating) {
////    			System.out.println("==================start get shortest==========================");
//	    		 System.out.println("get shortest ");
//	    		 dpq = new DPQ(g.getNumOfPoints(),g); 
//	    		 //here
//	    	     dpq.dijkstra(); 
//	    	     shortList = dpq.printShortestPathTo(dpq . dst);
//	    	     if(shortList !=null)
//	    	    	 getShortest = false;
//	    	     System.out.println("after sh ");
////    		}else {
//////    			System.out.println("==================still updating so cant get shortest==========================");
////    		}
//        }
//    	
//	    i++;
//	    if(i%5==0) {
//	    	i=0;
//	    	prevPos = new Point((int)info.getX(),(int)info.getY());
//	    }
//	    
//	    if(s1%10==0) {
//	    	s1=0;
//	    	spin1 = new Point((int)info.getX(),(int)info.getY());
//	    }
//	    if(s2%10==0) {
//	    	s2=0;
//	    	spin2 = new Point((int)info.getX(),(int)info.getY());
//	    }
//	    s4++;
//	    spin4 = null;
//	    if(s4%150==0) {
//	    	System.out.println("!!!!!!!!!!!!!!!!   spin4  !!!!!!!!!!!!!!!!!!!!!");
//	    	s4=0;
//	    	spin4 = new Point((int)info.getX(),(int)info.getY());
//	    }
//	    
//	    Point currentPosition = new Point((int)info.getX(),(int)info.getY());
//	    
//	    
//	    //recalculate graph if checkpoint change
////	    System.out.println("check:"+info.getCurrentCheckpoint());
////	    System.out.println("prevCheck:"+prevCheckpoint);
////	    System.out.println("equal:"+info.getCurrentCheckpoint().equals(prevCheckpoint ));
//	    if(!info.getCurrentCheckpoint().equals(prevCheckpoint )) {
//	    	System.out.println("------------------------------------------");
//	    	System.out.println("in cur check ponit not equal to prev checkpoint");
////	    	initGraph();
//	    	 System.out.println("in a ");
//	    	 updateGraphSrcAndDes();
//	    	 System.out.println("after ");
//	    	
//	    	listPointCounter=1;
//			prevCheckpoint = new Point(info.getCurrentCheckpoint());
//	    }
//	    Point curNode=null;
//	    if(shortList!=null && listPointCounter<shortList.size())
//	    	curNode =  shortList.get(listPointCounter);//info.getCurrentCheckpoint();
//	    if(shortList!=null) {
//	    	if(listPointCounter < shortList.size())
//	    		curNode = shortList.get(listPointCounter);
//	    }
//	    
////	    System.out.println("listPointCounter:"+listPointCounter);  
////	    System.out.println("dist to curNode:"+distance(null,curNode));
//	    
//	    //if already close to curNode(not checkpoint
//	    if(curNode!=null && !curNode.equals(info.getCurrentCheckpoint()) && distance(null,curNode)<50 && shortList!= null && listPointCounter+1 < shortList.size()) {
//	    	System.out.println("------------------------------------------");
//	    	System.out.println("close enuff so move on to next");
//		   	listPointCounter++;
//		   	
//		 }
//	    
//	    if(curNode!=null && intersectWithObs(currentPosition,curNode) ) {
//	    	System.out.println("------------------------------------------");
//	    	System.out.println("intersect so update");
//	    	updateGraphSrc();
//	    	listPointCounter=1;
////	    	updateGraphSrc();
//	    }
//	    
//	    
//	    //deal with restart, when restart, update Graphsrc
//	    //how to know restart?? prevPos is closer to checkpoint than new is
//	    if(prevPos !=null && distance(prevPos,info.getCurrentCheckpoint())+200<distance(currentPosition,info.getCurrentCheckpoint())) {
//	    	System.out.println("------------------------------------------");
//	    	System.out.println("new born or prev to check < than currentc to checkpoint");
//	    	updateGraphSrc();
//	    	listPointCounter=1;
//	    }
//	    
//	    //cuz shortest path not perfect if currPos and nextPath is open space move ot next
//	    if(shortList!=null && listPointCounter+1<shortList.size() && !intersectWithObs(currentPosition,shortList.get(listPointCounter+1))){
//	    	System.out.println("------------------------------------------");
//	    	System.out.println("no obs bewteen current pos and the next curCheck so jump to next");
//	    	listPointCounter++;
//	    }
//	      //check if getting further away, init again
//	    //prev position is closer to checkpoint then currenct
//	    int buffer = 10;
//	    //if prevpos closer to curNode than current position, means u r straying away, init again
////	    if(distance(prevPos,curNode)+buffer<distance(null,curNode) ) {//|| distance(currentPosition,prevPos)<0.5 ) {
////	    	System.out.println("in distance to curNode is already low");
////	    	initGraph();
//////	    	if(stillUpdating == false) {
//////		    	 System.out.println("in b ");
//////		    	 updateGraphSrc();
//////	    	     
//////		    	 System.out.println("after ");
////		    	
////		    	listPointCounter=1;	
//////	    	}
////	    }
//	   
//	    //
////	    if(shortList!=null) {
////	   	 curNode = shortList.get(listPointCounter);
////	    	 if(distance(null,curNode)<20 && listPointCounter < shortList.size()-1 && !curNode.equals(info.getCurrentCheckpoint())) {
////	    		 listPointCounter++;
////	    	   }
////	    }
//	    
//	    //faster
//	    curCheck = new Vector2f((float)info.getCurrentCheckpoint().getX(), (float)info.getCurrentCheckpoint().getY());
//	    if(shortList!=null) {
//	    	if(listPointCounter >= shortList.size())listPointCounter = shortList.size()-1;
//		   	 curNode = shortList.get(listPointCounter);
//		   	 curCheck = new Vector2f((float)curNode.x,(float)curNode.y);
//		    	 if(distance(null,curNode)<10 && listPointCounter < shortList.size()-1 && !curNode.equals(info.getCurrentCheckpoint())) {
//		    		 listPointCounter++;
//		    		 curCheck = new Vector2f( shortList.get(listPointCounter).x, shortList.get(listPointCounter).y);
//		    	   }
//		   
//		 }
//	    
//	    orientation = new Vector2f((float)(Math.cos(info.getOrientation())), (float) (Math.sin(info.getOrientation())));
//	    curPos = new Vector2f(info.getX(), info.getY());
//	    Vector2f.sub(curCheck, curPos, destination);
//	    distToDes =   (float) (Math.sqrt(Math.pow(curCheck.x - info.getX(), 2) + Math.pow(curCheck.y - info.getY(), 2)));
//	   
//	    
//	    align();
//	    avoidObstacle(50);
//	    
//	    
//	    float newAV = (desiredAV - info.getAngularVelocity()) / 1;
////	    if(distance(null,info.getCurrentCheckpoint())<20) {
////	    	newAV = newAV/2;
////	    }
//	    //how to be sure car is spinning around the checkpoint??
//	    if( countBackUp!=0 ||(spin4!=null &&  distance(null, info.getCurrentCheckpoint())<40 &&  distance(spin4, info.getCurrentCheckpoint())<40  &&  Math.abs(distance(spin4, info.getCurrentCheckpoint())-distance(null, info.getCurrentCheckpoint()))<10 )) {
//	    	System.out.println("------------------------------------------");
//	    	System.out.println("backup once");
//	    	countBackUp++;
//	    	countBackUp = countBackUp % 1;
//	    	return new DriverAction(-50,-50);
//	    }
//	    
//	    //take care of respawing  by comparing spin 4
//	    if(spin4!=null  && distance(spin4, info.getCurrentCheckpoint())+100<  distance(null, info.getCurrentCheckpoint())) {
//	    	prevCheckpoint = new Point(info.getCurrentCheckpoint());
//		   	
//		   	initGraph();
//		    listPointCounter = 1;
//	    }
//	    //slow it down within 100
//	    return new DriverAction(acceleration(arrive(3f,40f)),newAV);
//	    
////	    angularAcc = turnToCheckpoint(curNode);
////	  	 acceleration = getThrottle(curNode);
////	   	 for (int i = 0; i < obstacles.length; i++) {
////	   		 avoidObstacles(obstacles[i]);
////	   	 }
//	   	 
//	   	 
////	   	 return new DriverAction(acceleration, angularAcc);
//    }
//   
//    
//    public float arrive(float destinationRadius, float baseBreakRadius) {
//        if (distToDes >= info.getVelocity().length()/baseBreakRadius) return info.getMaxVelocity();
//        else {
//            //if (distanceToDest < destinationRadius) return info.getMaxVelocity();
//            return distToDes * info.getMaxVelocity() / baseBreakRadius;
//        }
//    }
//
//    public float acceleration(float speed) {
//        return speed - info.getVelocity().length() / 1;
//    }
//
//    public void align() {
//        float angleBetweenPosAndDest = Vector2f.angle(orientation, destination);
//        float tolerance = 0.000001f;
//        float dot = orientation.x * - destination.y + orientation.y *  destination.x;
//        if (dot > 0) angleBetweenPosAndDest = -angleBetweenPosAndDest;
//        if (Math.abs(angleBetweenPosAndDest) < Math.abs(info.getAngularVelocity())/2) {
//            desiredAV = (angleBetweenPosAndDest * info.getMaxAbsoluteAngularVelocity() / 2*Math.abs(info.getAngularVelocity())); //TODO: Tweak
//        } else desiredAV = (angleBetweenPosAndDest > tolerance) ? info.getMaxAbsoluteAngularVelocity() : -info.getMaxAbsoluteAngularVelocity();
//    }
//
//    public void avoidObstacle(float breakRad) {
//        Track track = info.getTrack();
//        Polygon[] obstacles = track.getObstacles(); //(Oberflaeche der) Hindernisse
//        float rayCastLength = info.getVelocity().length();
//        if (distance(null,info.getCurrentCheckpoint()) >= 3*breakRad) {
//            rayCastLength = 3 * info.getVelocity().length();
//        }
//        Vector2f orientationWithLength = (Vector2f)orientation.scale(rayCastLength);
//
//        //Single Ray (middle)
//        Vector2f.add(curPos, orientationWithLength, midDetect);
//
//        //turn orientation vector
//        float fov = (float)Math.PI/8; //TODO: Tweak
//        float ox = orientationWithLength.x;
//        float oy = orientationWithLength.y;
//
//        //TODO: evtl vereinfachen (math.pi*2)
//        //Ray Left
//        Vector2f rayLeftOrientation = new Vector2f((float)(Math.cos(fov) * ox - Math.sin(fov) * oy), (float)(Math.sin(fov) * ox + Math.cos(fov) * oy));
//        Vector2f.add(curPos, rayLeftOrientation, leftDetect);
//
//        //Ray Right
//        Vector2f rayRightOrientation = new Vector2f((float)(Math.cos(2*Math.PI-fov) * ox - Math.sin(2*Math.PI-fov) * oy), (float)(Math.sin(2*Math.PI-fov) * ox + Math.cos(2*Math.PI-fov) * oy));
//        Vector2f.add(curPos, rayRightOrientation, rightDetect);
//
//        for (int i = 2; i < obstacles.length; i++) {
//            if (obstacles[i].contains(leftDetect.x, leftDetect.y))
//                desiredAV = -info.getMaxAbsoluteAngularVelocity();
//            else if (obstacles[i].contains(rightDetect.x, rightDetect.y))
//                desiredAV = info.getMaxAbsoluteAngularVelocity();
//        }
//    }
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
//   		 Point futurePosition = calculateFuturePosition(j, angularOffset);
//   		 if (obstacle.contains(futurePosition)) {
//   			 return true;
//   		 }
//   	 }
//   	 return false;
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
//    if(g==null) {
//    	return 0;
//    }
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
//     if(shortList!=null) {
//     for(int i=0; i<shortList.size()-1;i++) {
//	  		 glBegin(GL_LINES);
//	          glColor3f(1,0,1);
//	  		 glVertex2f(shortList.get(i).x,shortList.get(i).y);
//	  		 glVertex2f(shortList.get(i+1).x,shortList.get(i+1).y);
//	  		 glEnd();
//	  	 }
//     }
//     
////     glBegin(GL_LINES);
////     glColor3f(1, 0, 0);
////     glVertex2f(info.getX(), info.getY());
////     glVertex2d(info.getCurrentCheckpoint().getX(), info.getCurrentCheckpoint().getY());
////     glEnd();
//     //draw ray cast
//     glBegin(GL_LINES);
//     glColor3f(0,1,0);
////     glVertex2f(info.getX(), info.getY());
////     glVertex2d(rayCastMiddle.x, rayCastMiddle.y);
//     glVertex2f(info.getX(), info.getY());
//     glVertex2d(midDetect.x, midDetect.y);
//     glVertex2f(info.getX(), info.getY());
//     glVertex2d(leftDetect.x, leftDetect.y);
//     glVertex2f(info.getX(), info.getY());
//     glVertex2d(rightDetect.x, rightDetect.y);
//     glEnd();
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
//    
////***************************Graph stuff **************************//
//
//	private void initGraph() {
//		System.out.println("in Git init");
//		getShortest = true;
//    	g = new Graph();//new Point((int)info.getX(),(int)info.getY()),info.getCurrentCheckpoint());
//    	System.out.println("beforeupdatesource");
//
//    	updateSource();
//    	updateDes();
//    	setupObsNodes(obstacles);
//    	setupObsEdges();
//    	
//	     
//	}
//	
//	//new 
//	private void updateGraphSrcAndDes() {
//		System.out.println("**************in updateGraphSrcAndDes********************");
//		getShortest = true;
//		System.out.println("numOfPoints before:"+g.getNumOfPoints());
//    		updateSource();
//    		updateDes();
//    		System.out.println("numOfPoints after:"+g.getNumOfPoints());
//    		
//	     
//	}
//
//	private void updateGraphSrc() {
//		System.out.println("************** - in updateGraphSrc - ********************");
//		getShortest = true;
//		System.out.println("-numOfPoints before:"+g.getNumOfPoints());
//		updateSource();
//		System.out.println("- - numOfPoints after:"+g.getNumOfPoints());
//	}
//
//	private void updateSource() {
//		stillUpdating = true;
//		Point startPoint = new Point((int)info.getX(),(int)info.getY());
//		//if it's an update, remove first then add
//		if(g.source!=null && !g.source.equals(startPoint) ) {
//			 System.out.println("remove prev source");
//			if(g.removeSrc()) {
//				 System.out.println("done");
//				stillUpdating = false;
//			}
//			
//		}else if (g.source==null) {
//			stillUpdating = false;
//		}
//		System.out.println("- - numOfPoints after:"+g.getNumOfPoints());
//		System.out.println("new startpoint");
//		 g.addSrc(startPoint);
//		 System.out.println(startPoint+","+g.source);
//		 
//		 //then add the edges with new source
//		 int i = 0;
//			while(g.getPoint(i)!= null ) {
//				//add edge to all nodes that are not startpoint
//				if(!startPoint.equals(g.getPoint(i))){
//						Point q1 = new Point( g.getPoint(i).x,g.getPoint(i).y);
//						if(!intersectWithObs(startPoint,q1)) {
//							g.addEdge(startPoint,q1);	
//						}
//				}
//				i++;
//			}
//	}
//	private void updateDes() {
//		stillUpdating = true;
//		 Point des = info.getCurrentCheckpoint();
//		 
//		//if it's an update, remove first then add
//			if(g.destination!=null && !g.destination.equals(des) ) {
//				 System.out.println("remove prev destination");
//				if(g.removeDes()) {
//					 System.out.println("done");
//					stillUpdating = false;
//				}
//				
//			}else if (g.destination==null) {
//				stillUpdating = false;
//			}
//			System.out.println("new destination");
//			 g.addDes(des);
//			 System.out.println(des+","+g.destination);
//		
//		 
//		
//			 
//			 //then add the edges with new source
//			 int i = 0;
//				while(g.getPoint(i)!= null ) {
//					//add edge to all nodes that are not startpoint
//					if(!des.equals(g.getPoint(i))){
//							Point q1 = new Point( g.getPoint(i).x,g.getPoint(i).y);
//							if(!intersectWithObs(des,q1)) {
//								g.addEdge(des,q1);	
//							}
//					}
//					i++;
//				}
//	}
//	
//	private void setupObsNodes(Polygon[] obs) {
//		//TODO add in current location and checkpoint
//		//TODO isReflex not working yet
//		//not include  four corners 
//		
//		//only add point if it's reflect vertex, don't count border(first 2 obstacles)
//		//check if the two next to it are making this node protruding
////		Track track = info.getTrack();
////		track.
//		
//		//getStartPosition & checkpoint
////		Point startPoint = new Point((int)info.getX(),(int)info.getY());
////		 g.addPoint(startPoint);//0
////		 g.addPoint(info.getCurrentCheckpoint());//1
//		
//		
//    	//now i=2 to avoid corner
//    	for(int i = 0 ; i <obs.length;i++) {
////    		allObsPoints[i] = new Vector2f[obs[i].npoints];
//    		for(int j = 0; j<obs[i].npoints; j++) {
//    			//if is reflex vertex(not concave) add to node
//    			if(isReflexecke(obs[i],j)) {
//    				if(!isBoarder(obs[i],j)) {
//    				//then check outer border
//    				
//	    				g.addPoint(pointPlusBuffer(obs[i],j,10));
////		    			g.addPoint(new Point(obs[i].xpoints[j],obs[i].ypoints[j]));
//		    		}
//    			}
//    		}
//    	}
//	}
//
//	
//    private void setupObsEdges() {
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
//	private Point pointPlusBuffer(Polygon ob, int j,int buffer) {
//		// TODO Auto-generated method stub
//		
//		//first get angle bw three points with p1 as center
//				int prev = j>0? j-1:ob.npoints-1;
//				int next = j<ob.npoints-1? j+1:0;
////				int prevPrev = j>1? j-2:ob.npoints-2;
////				int nextNext = j<ob.npoints-2? j+2:1;
//				Point p0 = new Point(ob.xpoints[j],ob.ypoints[j]);
////				Point pPrev = new Point(ob.xpoints[prev],ob.ypoints[prev]);
////				Point pNext = new Point(ob.xpoints[next],ob.ypoints[next]);
////				//get linePrev and lineNext, add buffer, extend both way slowly, get interset it's the correct point
////				Point[] LinePrev = {p0,pPrev};
////				Point[] LineNext = {p0,pNext};
////				//get mid point
////				Point prevMid = new Point((p0.x+pPrev.x)/2, (p0.y+pPrev.y)/2);
////				Point nextMid = new Point((p0.x+pNext.x)/2, (p0.y+pNext.y)/2);
////				//get distance between mid and p0
////				float distPrev =  distance(p0,prevMid);
////				float distNext =  distance(p0,nextMid);
////				//get c point for a 45-45-90 deg triangle
////				Point prevUp = new Point((p0.x+pPrev.x)/2, (p0.y+pPrev.y)/2);
//				
//				
//				
//				//get buffer drection from 
//				//before adding buffer, must know which side is contained by ob
////				ob.containsdoub/in
//				
//				
//				
////				Line2D.linesIntersect(double x1, double y1,
////	                      double x2, double y2,
////	                      double x3, double y3,
////	                      double x4, double y4)
//				
////				Point p0 = new Point(ob.xpoints[j],ob.ypoints[j]);
//				Point p1= new Point(ob.xpoints[prev],ob.ypoints[prev]);
//				Point p2 = new Point(ob.xpoints[next],ob.ypoints[next]);
//				
//				//get mid point of p1 and p2 : pM
//				Point pm =  new Point((p1.x+p2.x)/2,(p1.y+p2.y)/2);
//				//then get use pM and p0 to get a line
//				//then use this line to get p0+buffer = pB
//				float m = (pm.y-p0.y)/(pm.x-p0.x);
//				int bufferX=0,bufferY=0;
//				bufferX=buffer;
//				bufferY = (int)(buffer*m);
//				if(p0.x < pm.x) bufferX*=-1;
//				if(p0.y <pm.y && bufferY >0) bufferY*=-1; 
//				Vector2f M = new Vector2f(p1.x-p0.x,p1.y-p0.y);
//				Vector2f C = new Vector2f(p2.x-p0.x,p2.y-p0.y);
//				double dot = Vector2f.dot(M,C);
//				double det = ((C.getX()*M.getY()) - (C.getY()*M.getX()));
//				double angle = Math.toDegrees(Math.atan2(det, dot));
//				
//				
//				
//				return new Point((int)p0.x+bufferX,(int)(p0.y+bufferY));
//	}
////	private Vector2f rotate(Vector2f vector,double angle) { // angle in radians
////
////		  //normalize(vector); // No  need to normalize, vector is already ok...
////			angle = Math.toRadians(angle);
////		  float x1 = (float)(vector.x * Math.cos(angle) - vector.y * Math.sin(angle));
////
////		  float y1 = (float)(vector.x * Math.sin(angle) + vector.y * Math.cos(angle)) ;
////
////		  return new Vector2f(x1, y1);
////
////		}
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
//		
//		if(angle>0) {
//			return true;
//		}
//		return false;
//	}
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
