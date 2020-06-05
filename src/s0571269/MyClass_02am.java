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
//public class MyClass_02am extends AI {
//
//   
//    float angularAcc;
//    Track track = info.getTrack();
//    Polygon[] obstacles = track.getObstacles();
//    Point pCurPosition = new Point((int)info.getX(),(int)info.getY());
//    Vector2f vCurPosition;
//    
//    Vector2f vCurCheckpoint;
//    Vector2f vOrientation;
//    Vector2f destination = new Vector2f();
//    Vector2f midDetect = new Vector2f(),leftDetect=new Vector2f(),rightDetect=new Vector2f();
//    float brakeRadius = 80f;
//    float distToCheckpoint = distance(pCurPosition, info.getCurrentCheckpoint());
//    float desiredAV;
//    float slowdown=0;
//    
//    //for controling if the update is finished
//    boolean stillUpdating= false;
//    boolean debug = false;
//
//    //keeping track of positions  at certain points
//    int prevPosInterval=0;//5 frames
//    int spinPosInterval = 0;//100 frames
//    Point spin=null;
//    Point prevCheckpoint;
//    
//    //graph and shortweg
//    Graph g;
//    int bufferFromObs = 10;
//    List<Point> shortList = null;
//    boolean getShortest = true;
//    DPQ dpq=null;
//    
//
//    int listPointCounter = 1;
//    
//    Point prevPos = null;
//    
//    public MyClass_02am(lenz.htw.ai4g.ai.Info info) {
//	   	 super(info);
//	   	 enlistForTournament(549481, 571269);
//	   	 
//	   	prevCheckpoint = new Point(info.getCurrentCheckpoint());
//	   	initGraph();
//	    listPointCounter = 1;
//	    //prevPos is for seeing if car is always approaching next node
//	    prevPos = new Point((int)info.getX(),(int)info.getY());	   	 
//    }
//	
//    @Override
//    public String getName() {
//   	 return "XAEA12";
//    }
//    
//    @Override
//    public String getTextureResourceName() {
//    	return "/s0571269/car2.png";
//    }
//    
//    @Override
//    public DriverAction update(boolean wasResetAfterCollision) {
//    	Point pCurCheck = info.getCurrentCheckpoint();
//    
//    	if(getShortest) {
//	    		 System.out.println("get shortest ");
//	    		 dpq = new DPQ(g.getNumOfPoints(),g); 
//	    		 //here
//	    	     dpq.dijkstra(); 
//	    	     shortList = dpq.printShortestPathTo(dpq . dst);
//	    	     if(shortList !=null)
//	    	    	 getShortest = false;
//	    	     System.out.println("after sh ");
//        }
//    	
//	    prevPosInterval++;
//	    if(prevPosInterval%5==0) {
//	    	 prevPosInterval=0;
//	    	prevPos = new Point((int)info.getX(),(int)info.getY());
//	    }
//	    
//	    Point currentPosition = new Point((int)info.getX(),(int)info.getY());
//	    
//	    
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
//	    if(curNode!=null && !curNode.equals(info.getCurrentCheckpoint()) && distance(null,curNode)<50 && shortList!= null && listPointCounter+1 < shortList.size()) {
//	    	System.out.println("------------------------------------------");
//	    	System.out.println("close enuff so move on to next");
//		   	listPointCounter++;
//		   	
//		 }
//	    if(prevPos !=null && distance(prevPos,info.getCurrentCheckpoint())+200<distance(currentPosition,info.getCurrentCheckpoint())) {
//	    	System.out.println("------------------------------------------");
//	    	System.out.println("new born or prev to check < than currentc to checkpoint");
//	    	updateGraphSrc();
//	    	listPointCounter=1;
//	    }
//	    
//	    
//	    
//	    
//	    vCurCheckpoint = new Vector2f((float)info.getCurrentCheckpoint().getX(), (float)info.getCurrentCheckpoint().getY());
//	    if(shortList!=null) {
//	    	if(listPointCounter >= shortList.size())listPointCounter = shortList.size()-1;
//		   	 curNode = shortList.get(listPointCounter);
//		   	vCurCheckpoint = new Vector2f((float)curNode.x,(float)curNode.y);
//		    	 if(distance(null,curNode)<10 && listPointCounter < shortList.size()-1 && !curNode.equals(info.getCurrentCheckpoint())) {
//		    		 listPointCounter++;
//		    		 vCurCheckpoint = new Vector2f( shortList.get(listPointCounter).x, shortList.get(listPointCounter).y);
//		    		 destination =  vCurCheckpoint;
//		    	   }
//		    	
//		 }
//	    
//	    //detect spinning around checkpoint and backup
//	    //can also use this if we stuck at a spot
//	    spinPosInterval++;
//	    if(spinPosInterval%100==0 ) {
//	    	
//	    	//if to curCheck is the same as spinning(150 frame ago), then slow it down
//	    	//if almost in same location
//	    	if(spin!=null && Math.abs(distance(spin,pCurCheck)-distance(null,pCurCheck))<7) {
//	    		
//	    		System.out.println("------------------------------------------");
//		    	System.out.println("in spin");
//	    		//if already super close to check
//	    		
//	    		if(distance(null,pCurCheck)<30 && Math.abs(info.getAngularVelocity())>0.2) {
//	    			System.out.println("------------------------------------------");
//	    	    	System.out.println("backup when spinning round curCheck");
//	    	    	spinPosInterval--;
//	    	    	angularAcc= -info.getAngularVelocity();
//    			    
//    				return new DriverAction(-10,angularAcc);
//	    		}
//	    		 else if(distance(null,spin)<0.5) {
//		    		 System.out.println("------------------------------------------");
//		 	    	System.out.println("prob will reborn");
//		    		listPointCounter=1;
//		    	 }
//	    		
//	    	}
//	    	
//	    	spin= new Point((int)info.getX(),(int)info.getY());
//	    	
//	    }
//	    
//	    vOrientation = new Vector2f((float)(Math.cos(info.getOrientation())), (float) (Math.sin(info.getOrientation())));
//	    vCurPosition = new Vector2f(info.getX(), info.getY());
//	    Vector2f.sub(vCurCheckpoint,vCurPosition, destination);
//	    distToCheckpoint = (float)(Math.sqrt(Math.pow(vCurCheckpoint.x - info.getX(), 2) + Math.pow(vCurCheckpoint.y - info.getY(), 2)));
//	   
//	    align();
//	    avoidObstacles(brakeRadius);
//	    
//	    angularAcc = (desiredAV-info.getAngularVelocity()) / 1;
//	    if(slowdown!=0) {
//	    	 System.out.println("getvel:"+info.getVelocity());
//	    	System.out.println("slowodwn:"+slowdown);
//	    	slowdown =0;
//	    	 return new DriverAction(slowdown,angularAcc);
//	    }
//	    //getThrottle with brakeRadius of 80f
//	    return new DriverAction(getThrottle(),angularAcc);
//	    
//    }
//   
//
//    private float getThrottle() {
//	   	 float velocity = 0;
//	   	 float currentSpeed = info.getVelocity().length();
//	   	 //arrive
//	   	 if ( distToCheckpoint >= currentSpeed/brakeRadius) {
//	   		velocity=info.getMaxVelocity();
//	   	 }
//	     else{
//	         return  distToCheckpoint*info.getMaxVelocity()/ brakeRadius;
//	     }
//	   	 return velocity - currentSpeed;
//    }
//
//    public void align() {
//        float angleBetweenPosAndDest = Vector2f.angle(vOrientation, destination);
//        float tolerance = 0.000001f;
//        float dot = vOrientation.x * - destination.y + vOrientation.y *  destination.x;
//        if (dot > 0) angleBetweenPosAndDest = -angleBetweenPosAndDest;
//        if (Math.abs(angleBetweenPosAndDest) < Math.abs(info.getAngularVelocity())/2) {
//            desiredAV = (angleBetweenPosAndDest * info.getMaxAbsoluteAngularVelocity() / 2*Math.abs(info.getAngularVelocity())); //TODO: Tweak
//        } else desiredAV = (angleBetweenPosAndDest > tolerance) ? info.getMaxAbsoluteAngularVelocity() : -info.getMaxAbsoluteAngularVelocity();
//    }
//
//    public void avoidObstacles(float brakeRadius) {
////    	float extendLengthAt = brakeRadius;
//        float detectLength = info.getVelocity().length();
//        
//        //if far enuff, extend detector length
//        if ( distance(null,info.getCurrentCheckpoint()) >= brakeRadius) {
//            detectLength *= 3;
//        }
//        Vector2f orientationWithLength = (Vector2f)vOrientation.scale(detectLength);
//
//        //Single Ray (middle)
//        Vector2f.add(vCurPosition, orientationWithLength, midDetect);
//
//        //turn orientation vector
//        float fov = (float)Math.PI/8; 
//        float ox = orientationWithLength.x;
//        float oy = orientationWithLength.y;
//
//        //TODO: evtl vereinfachen (math.pi*2)
//        //Ray Left
//        Vector2f rayLeftOrientation = new Vector2f((float)(Math.cos(fov) * ox - Math.sin(fov) * oy), (float)(Math.sin(fov) * ox + Math.cos(fov) * oy));
//        Vector2f.add(vCurPosition, rayLeftOrientation, leftDetect);
//
//        //Ray Right
//        Vector2f rayRightOrientation = new Vector2f((float)(Math.cos(2*Math.PI-fov) * ox - Math.sin(2*Math.PI-fov) * oy), (float)(Math.sin(2*Math.PI-fov) * ox + Math.cos(2*Math.PI-fov) * oy));
//        Vector2f.add(vCurPosition, rayRightOrientation, rightDetect);
//
//        for (int i = 2; i < obstacles.length; i++) {
//            if (obstacles[i].contains(leftDetect.x, leftDetect.y)) {
//                desiredAV = -info.getMaxAbsoluteAngularVelocity();
//            }
//            else if (obstacles[i].contains(rightDetect.x, rightDetect.y)) {
//                desiredAV = info.getMaxAbsoluteAngularVelocity();
//            }
//        }
//    }
//  
//
//    String showArray(int[] array, int length) {
//   	 String result = "";
//   	 for (int i = 0; i < length; i++) {
//   		 result = result + array[i] + ", ";
//   	 }
//   	 return result;
//    }
//
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
//
//    @Override
//	public void doDebugStuff() {
//   	 glBegin(GL_LINES);
//   	 glColor3f(1, 0, 0);
//   	 glVertex2f(info.getX(), info.getY());
//   	 glVertex2d(info.getCurrentCheckpoint().getX(), info.getCurrentCheckpoint().getY());
//   	 glEnd();
//   	
//   	 //draw all the edges
////     for (Map.Entry<Point, List<Edge>> entry : g.adjVertices.entrySet()) {
////    	 for(Edge v : entry.getValue()) {
////	    	 glBegin(GL_LINES);
////	         glColor3f(1,1,1);
////			 glVertex2f(entry.getKey().x,entry.getKey().y);
////			 glVertex2f(v.point.x,v.point.y);
////			 glEnd();
////    	 }
////     }
//   	 //draw shortest weg
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
//     //draw detectors
//     glBegin(GL_LINES);
//     glColor3f(0,1,0);
//     glVertex2f(info.getX(), info.getY());
//     glVertex2d(midDetect.x, midDetect.y);
//     glVertex2f(info.getX(), info.getY());
//     glVertex2d(leftDetect.x, leftDetect.y);
//     glVertex2f(info.getX(), info.getY());
//     glVertex2d(rightDetect.x, rightDetect.y);
//     glEnd();
//     
//	}
//
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
//    	for(int i = 0 ; i <obs.length;i++) {
////    		allObsPoints[i] = new Vector2f[obs[i].npoints];
//    		for(int j = 0; j<obs[i].npoints; j++) {
//    			//if is reflex vertex(not concave) add to node
//    			if(isReflexecke(obs[i],j)) {
//    				if(!isBorder(obs[i],j)) {
//    				//then check outer border
//    				
//	    				g.addPoint(pointPlusBuffer(obs[i],j, bufferFromObs));//10 is best
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
//				int prev = j>0? j-1:ob.npoints-1;
//				int next = j<ob.npoints-1? j+1:0;
//				Point p0 = new Point(ob.xpoints[j],ob.ypoints[j]);
//				Point p1 = new Point(ob.xpoints[prev],ob.ypoints[prev]);
//				Point p2 = new Point(ob.xpoints[next],ob.ypoints[next]);
//				double lengthPrev = Math.sqrt((p0.x-p1.x)* (p0.x-p1.x) + (p0.y-p1.y)*(p0.y-p1.y));
//				double lengthNext = Math.sqrt((p0.x-p2.x)* (p0.x-p2.x) + (p0.y-p2.y)*(p0.y-p2.y));
//
//				
//				//find 1/lengthOfLine point of each side
//				Vector2f v01 = new Vector2f((float) (p0.x + (p1.x-p0.x)/lengthPrev),(float)(p0.y+(p1.y-p0.y)/lengthPrev));
//				Vector2f v02 = new Vector2f((float) (p0.x + (p2.x-p0.x)/lengthNext),(float)(p0.y+(p2.y-p0.y)/lengthNext));
//				//then get mid point
//				Vector2f mid = new Vector2f((v01.x+v02.x)/2, (v01.y+v02.y)/2);
//				//then connect this point with p0
//				
//				float m = (p0.y-mid.y)/(p0.x-mid.x);
//				
//				int xBuffer = buffer;
//				if(mid.x>p0.x) {
//					xBuffer*=-1;
//				}
//				int yBuffer = Math.round(xBuffer*m);
//				
//				return new Point((int)p0.x+xBuffer,(int)(p0.y+yBuffer));
//	}
//
//	private boolean isBorder(Polygon ob, int j) {
//		// TODO Auto-generated method stub
//		return ob.xpoints[j]==0 || ob.xpoints[j]==1000 || ob.ypoints[j]==0 || ob.ypoints[j]==1000;
//	}
//
//	//** still not working well, get ang but when is ang over 
//	private boolean isReflexecke(Polygon ob, int j) {
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
//
//    static boolean onSegment(Point p, Point q, Point r) 
//    { 
//        if (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) && 
//            q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y)) 
//        return true; 
//      
//        return false; 
//    } 
//}
//
//
