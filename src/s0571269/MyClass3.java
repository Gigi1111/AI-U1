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


    float angularAcc;
    Track track = info.getTrack();
    Polygon[] obstacles = track.getObstacles();
    Point pCurPosition = new Point((int) info.getX(), (int) info.getY());
    Vector2f vCurPosition;

    Vector2f vCurCheckpoint, vOrientation;
    Vector2f vDestination = new Vector2f(), midDetect = new Vector2f(), leftDetect = new Vector2f(), rightDetect = new Vector2f();
    float brakeRadius = 50f;
    float distToCheckpoint;
    float desiredAV;
    float slowdown = 0;

    //for controling if the update is finished
    boolean stillUpdating = false;
    boolean debug = false;

    //keeping track of positions  at certain points
    int prevPosInterval = 0; //5 frames
    int spinPosInterval = 0; //100 frames
    Point spin = null;
    Point prevCheckpoint;

    //graph and shortweg
    Graph_DPQ g = new Graph_DPQ();
    int bufferFromObs = 10;
    List < Point > shortList = null;
    boolean getShortest = true;
    DPQ dpq = null;
    
    Point spawnPoint = new Point();


    int listPointCounter = 1;

    Point prevPos = null;
    
    //get time elapse
    long startTime_prevPos, startTime_counter, startTime_spin;
    
//    System.out.println("Total time: "+(endTime-startTime)/Math.pow(10, 9));
//    startTime=endTime;
    // ... the code being measured ...    
//    long estimatedTime = System.nanoTime() - startTime;
//    long startTime = System.nanoTime();

   

    public MyClass3(lenz.htw.ai4g.ai.Info info) {
        super(info);
        enlistForTournament(549481, 571269);

        prevCheckpoint = new Point(info.getCurrentCheckpoint());
        startTime_prevPos = System.nanoTime();    
        startTime_counter = System.nanoTime();  
        startTime_spin = System.nanoTime();  
       
        listPointCounter = 1;
        //prevPos is for seeing if car is always approaching next node
        prevPos = new Point((int) info.getX(), (int) info.getY());
        spawnPoint = new Point((int) info.getX(), (int) info.getY());
    }

    @Override
    public String getName() {
        return "calless";//slow down when close to curnode
    }

    @Override
    public String getTextureResourceName() {
        return "/s0571269/car2.png";
    }

    int counter = 200;
    int interval = 200;
    @Override
    public DriverAction update(boolean wasResetAfterCollision) {
//    	 for(int i = 0; i < 1000000; i++) {
//    	      long test = System.nanoTime();
//    	    }

    	    long endTime_prevPos = System.nanoTime();
    	    long endTime_counter = System.nanoTime();
    	    long endTime_spin = System.nanoTime();
    	    double prevPosInterval = (endTime_prevPos-startTime_prevPos)/Math.pow(10, 9);
    	    double counterInterval = (endTime_counter-startTime_counter)/Math.pow(10, 9);
    	    double spinInterval = (endTime_spin-startTime_spin)/Math.pow(10, 9);
    	    
    	counter++;
    	distToCheckpoint = distance(pCurPosition, info.getCurrentCheckpoint());
        Point pCurCheck = info.getCurrentCheckpoint();
        pCurPosition = new Point((int) info.getX(), (int) info.getY());
       
        if(g.source == null) {
        	  g.createGraph(track,pCurPosition,pCurCheck, 13);
        	 dpq = new DPQ(g.getNumOfPoints(), g);
	           shortList = dpq.getShortestWay(g.source,g.destination);
        }
        
        prevPosInterval++;
//        if (prevPosInterval % 5 == 0) {//TODO time 1 sec?
        if(prevPosInterval>1) {//1 second
        	startTime_prevPos=endTime_prevPos;
            prevPos = new Point((int) info.getX(), (int) info.getY());
        }

        Point currentPosition = new Point((int) info.getX(), (int) info.getY());


        if (!info.getCurrentCheckpoint().equals(prevCheckpoint)) {
//	        	if(counter>interval) {
//	        		counter = 0;
        		//change spwn
        	spawnPoint = info.getCurrentCheckpoint();
		           checkPointChange();
//	        	}
        }
        
       if(shortList ==null) {
    	   if(counterInterval>2) {//1 second
    		   startTime_counter=endTime_counter;
//      		counter = 0;
       		recalShortestFromCurrentPos(currentPosition);
      		System.out.println("------------------------------------------");
           System.out.println("shortList == null");
      	}
       }
        Point curNode = null;
        if (shortList != null && listPointCounter < shortList.size())
            curNode = shortList.get(listPointCounter); //info.getCurrentCheckpoint();
        if (shortList != null) {
            if (listPointCounter < shortList.size())
                curNode = shortList.get(listPointCounter);
        }

        if (curNode != null && !curNode.equals(info.getCurrentCheckpoint()) && distance(null, curNode) < 20 
        		
        		&& shortList != null && listPointCounter + 1 < shortList.size()) {
            System.out.println("------------------------------------------");
            System.out.println("close enuff so move on to next");
            listPointCounter++;

        }
        
        if (hasExploded() ) {//&& counter%5==0) {//TODO time
        	if(!shortList.get(0).equals(prevCheckpoint)) {
        		 System.out.println("------------------------------------------");
                 System.out.println("exploded");
            	recalShortestFromCurrentPos(getCurrentLocation());
            }
			System.out.println("boom");
			listPointCounter = 1;			
		}

       

//if obstructed by ob and not super close to curCheck
        if (curPosAndCurCheckObsctrucedByObstacles(currentPosition,pCurCheck) && 
        		distance(currentPosition,pCurCheck)>150) {
       	 
        	 if(counterInterval>2) {//1 second
             	startTime_counter=endTime_counter;
        		recalShortestFromCurrentPos(currentPosition);
       		System.out.println("------------------------------------------");
            System.out.println("path obstructed by obs");
       	}
       }
      
        vCurCheckpoint = new Vector2f((float) info.getCurrentCheckpoint().getX(), (float) info.getCurrentCheckpoint().getY());
        if (shortList != null) {
        	//
        	if(curNode !=null) {
        		  distToCheckpoint = distance(pCurPosition, curNode);
        	}
            if (listPointCounter >= shortList.size()) listPointCounter = shortList.size() - 1;
            curNode = shortList.get(listPointCounter);
            vCurCheckpoint = new Vector2f((float) curNode.x, (float) curNode.y);
            if (distance(null, curNode) < 10 && listPointCounter < shortList.size() - 1 && !curNode.equals(info.getCurrentCheckpoint())) {
                listPointCounter++;
                vCurCheckpoint = new Vector2f(shortList.get(listPointCounter).x, shortList.get(listPointCounter).y);
                vDestination = vCurCheckpoint;
            }

        }

        //detect spinning around checkpoint and backup
        //can also use this if we stuck at a spot
        spinPosInterval++;
        if(spinInterval>15) {//2 second //TODO time
        	
            //if to curCheck is the same as spinning(150 frame ago), then slow it down
            //if almost in same location
            if (spin != null && Math.abs(distance(spin, pCurCheck) - distance(null, pCurCheck)) < 7) {

                System.out.println("------------------------------------------");
                System.out.println("in spin");
                //if already super close to check

                if (distance(null, pCurCheck) < 30 && Math.abs(info.getAngularVelocity()) > 0.2) {
                    System.out.println("------------------------------------------");
                    System.out.println("backup when spinning round curCheck");
                    spinPosInterval--;
                    	
                    angularAcc = -info.getAngularVelocity();

                    return new DriverAction(-10, angularAcc);
                } else if (distance(null, spin) < 0.5) {
                    System.out.println("------------------------------------------");
                    System.out.println("prob will reborn");
                    listPointCounter = 1;
                }
                startTime_spin=endTime_spin;
            }

            spin = new Point((int) info.getX(), (int) info.getY());

        }

        
        vOrientation = new Vector2f((float)(Math.cos(info.getOrientation())), (float)(Math.sin(info.getOrientation())));
        vCurPosition = new Vector2f(info.getX(), info.getY());
        distToCheckpoint = (float)(Math.sqrt(Math.pow(vCurCheckpoint.x - info.getX(), 2) + Math.pow(vCurCheckpoint.y - info.getY(), 2)));
        Vector2f.sub(vCurCheckpoint, vCurPosition, vDestination);
        
        align();
        avoidObstacles(brakeRadius);

        angularAcc = (desiredAV - info.getAngularVelocity());
        //slow down
        if (slowdown != 0) {
            System.out.println("getvel:" + info.getVelocity());
            System.out.println("slowodwn:" + slowdown);
            slowdown = 0;
            return new DriverAction(slowdown, angularAcc);
        }
        
        //getThrottle with brakeRadius of 80f
        return new DriverAction(getThrottle(), angularAcc);

    }

    private boolean hasExploded() {
		return getCurrentLocation().equals(spawnPoint);
	}
    private Point getCurrentLocation() {
	Point point = new Point();
	point.setLocation(info.getX(), info.getY());
	return point;
}
    private boolean curPosAndCurCheckObsctrucedByObstacles(Point curPos, Point curCheck) {
		// TODO Auto-generated method stub
    	
		return g.intersectWithObs(curPos, curCheck);
	}

	private void checkPointChange() {
		// TODO Auto-generated method stub
    	 System.out.println("------------------------------------------");
         System.out.println("in cur check ponit not equal to prev checkpoint");
         System.out.println("in a ");
        g.updateGraphSrcAndDes(prevCheckpoint,info.getCurrentCheckpoint() );
        
        dpq = new DPQ(g.getNumOfPoints(), g);
        shortList = dpq.getShortestWay(g.source,g.destination);
         System.out.println("after ");

         listPointCounter = 1;
         prevCheckpoint = new Point(info.getCurrentCheckpoint());
	}
	private void recalShortestFromCurrentPos(Point curPos) {
		// TODO Auto-generated method stub
    	 System.out.println("------------------------------------------");
         System.out.println("recal shortset from curent po");
         System.out.println("in a ");
        g.updateGraphSrcAndDes(curPos,info.getCurrentCheckpoint() );
        
        dpq = new DPQ(g.getNumOfPoints(), g);
        shortList = dpq.getShortestWay(g.source,g.destination);
         System.out.println("after ");

         listPointCounter = 1;
         prevCheckpoint = new Point(info.getCurrentCheckpoint());
	}

	private float getThrottle() {
        float velocity = 0;
        float currentSpeed = info.getVelocity().length();
        //arrive
        if (distToCheckpoint >= currentSpeed / brakeRadius) {
            velocity = info.getMaxVelocity();
        } else {
            return distToCheckpoint * info.getMaxVelocity() / brakeRadius;
        }
        return velocity - currentSpeed;
    }

    public void avoidObstacles(float brakeRadius) {
        //    	float extendLengthAt = brakeRadius;
        float detectLength = info.getVelocity().length();
        //if far enuff, extend detector length
        if (distance(null, info.getCurrentCheckpoint()) >= 4* brakeRadius) {
            detectLength *= 4;
        }
        
        //mid
        Vector2f midDetOrientation =  (Vector2f)vOrientation.scale(detectLength);//new Vector2f(vOrientation.x*detectLength,vOrientation.y*detectLength);
        Vector2f.add(vCurPosition, midDetOrientation, midDetect);//extend it out
        //turn orientation vector
        float scopeAng = (float) Math.PI / 8;//180/8deg
        //right
        Vector2f rightDetOrientation = new Vector2f((float)(Math.cos(2 * Math.PI - scopeAng) * midDetOrientation.x - Math.sin(2 * Math.PI - scopeAng) * midDetOrientation.y), (float)(Math.sin(2 * Math.PI - scopeAng) * midDetOrientation.x + Math.cos(2 * Math.PI - scopeAng) * midDetOrientation.y));
        Vector2f.add(vCurPosition, rightDetOrientation, rightDetect);
        //left 
        Vector2f leftDetOrientation = new Vector2f((float)(Math.cos(scopeAng) * midDetOrientation.x - Math.sin(scopeAng) * midDetOrientation.y), (float)(Math.sin(scopeAng) * midDetOrientation.x + Math.cos(scopeAng) * midDetOrientation.y));
        Vector2f.add(vCurPosition, leftDetOrientation, leftDetect);
 
        for (int i = 2; i < obstacles.length; i++) {
            if (obstacles[i].contains(leftDetect.x, leftDetect.y)) {
                desiredAV = -info.getMaxAbsoluteAngularVelocity();
            } else if (obstacles[i].contains(rightDetect.x, rightDetect.y)) {
                desiredAV = info.getMaxAbsoluteAngularVelocity();
            }
        }
    }


    public void align() {
        float angle = Vector2f.angle(vOrientation, vDestination);
        float cross = vOrientation.x * -vDestination.y + vOrientation.y * vDestination.x;
        if (cross > 0) angle *= -1;
        
        if (Math.abs(angle) < Math.abs(info.getAngularVelocity())/2) {
            desiredAV = (angle * info.getMaxAbsoluteAngularVelocity() / 2 * Math.abs(info.getAngularVelocity()));
        } else {
        	//neg or pos
        	desiredAV = (angle > 0.000001f) ? info.getMaxAbsoluteAngularVelocity() : -info.getMaxAbsoluteAngularVelocity();
        }
    }

    private float distance(Point p, Point g) {
        if (g == null) {
            return 0;
        }
        Point goal = g;
        if (p == null)
            p = new Point((int) info.getX(), (int) info.getY());
        Point p0 = p;
        float dist = (float) Math.sqrt(Math.pow(goal.x - p0.x, 2) + Math.pow(goal.y - p0.y, 2));
        return dist;
    }


    @Override
    public void doDebugStuff() {
        glBegin(GL_LINES);
        glColor3f(1, 0, 0);
        glVertex2f(info.getX(), info.getY());
        glVertex2d(info.getCurrentCheckpoint().getX(), info.getCurrentCheckpoint().getY());
        glEnd();

        //draw all the edges
        //     for (Map.Entry<Point, List<Edge>> entry : g.adjVertices.entrySet()) {
        //    	 for(Edge v : entry.getValue()) {
        //	    	 glBegin(GL_LINES);
        //	         glColor3f(1,1,1);
        //			 glVertex2f(entry.getKey().x,entry.getKey().y);
        //			 glVertex2f(v.point.x,v.point.y);
        //			 glEnd();
        //    	 }
        //     }
        //draw shortest weg
        if (shortList != null) {
            for (int i = 0; i < shortList.size() - 1; i++) {
                glBegin(GL_LINES);
                glColor3f(1, 0, 1);
                glVertex2f(shortList.get(i).x, shortList.get(i).y);
                glVertex2f(shortList.get(i + 1).x, shortList.get(i + 1).y);
                glEnd();
            }
        }

        //draw detectors
        glBegin(GL_LINES);
        glColor3f(0, 1, 0);
        glVertex2f(info.getX(), info.getY());
        glVertex2d(midDetect.x, midDetect.y);
        glVertex2f(info.getX(), info.getY());
        glVertex2d(leftDetect.x, leftDetect.y);
        glVertex2f(info.getX(), info.getY());
        glVertex2d(rightDetect.x, rightDetect.y);
        glEnd();

    }

    
}