package s0571269;

import java.awt.Point;
import java.awt.Polygon;
import java.util.*;

import org.lwjgl.util.vector.Vector2f;

import lenz.htw.ai4g.track.Track; 

public class Graph_DPQ {
	public static boolean debug = false;
	public int numOfPoints = 0;
	public Point source=null;
	public Point destination=null;
    public Map<Point, List<Edge>> adjVertices= new HashMap<Point, List<Edge>>();
    List < Point > shortList = null;
    DPQ dpq;
    Polygon[] obstacles;
    int buffer = 10;//buffer to nodes 10-13?
    Graph_DPQ(Point s, Point d){
//    	adjVertices = new HashMap<Point, List<Edge>>();
//    	source = new Point(s);
//    	destination = new Point(d);
    }
    Graph_DPQ(){
    	adjVertices = new HashMap<Point, List<Edge>>();
    }
    List<Point> getAllNodes(){
    	 List<Point> keyList = new ArrayList<Point>(adjVertices.keySet());
    	return keyList;
    }
	List<Edge> getNeighbours(Point p) {
		return adjVertices.get(p);
	}
    void addPoint(Point p) {
    	if(getEdgeList(p)==null)
    		adjVertices.put(new Point(p), new ArrayList<Edge>());
    }
    void addSrc(Point p) {
    	source = p;
    	if(getEdgeList(p)==null)
    		adjVertices.put(new Point(p), new ArrayList<Edge>());
    }
    void setSrcToPrevDes() {
    	if(destination!=null) {
    		source = destination;
    	}

    }
    void addDes(Point p) {
    	destination = p;
    	if(getEdgeList(p)==null)
    		adjVertices.put(new Point(p), new ArrayList<Edge>());
    }
    Point getPoint(int i) {
    	int j =0;
    	 for (Map.Entry<Point, List<Edge>> entry :adjVertices.entrySet()) {
    		 if(j==i)
    			 return entry.getKey();
    		 j++;
         }
    	 return null;
    }
    int getNumOfPoints() {
    	int i =0;
    	for (Map.Entry<Point, List<Edge>> entry :adjVertices.entrySet()) {
    		i++;
    	}
    	numOfPoints = i;
		return numOfPoints;
    }

    void removePoint(Point p) {
        adjVertices.values().stream().forEach(e -> e.remove(p));
        adjVertices.remove(new Point(p));
        
    }
    void addEdge(Point p1, Point p2) {
        Edge e1 = new Edge(new Point(p1),dist(p1,p2));
        Edge e2 = new Edge(new Point(p2),dist(p1,p2));
        List<Edge> l1 = getEdgeList(p1);
        List<Edge> l2 = getEdgeList(p2);
        if(l1==null) {
        	l1 = new ArrayList<Edge>();
        }
        if(l2==null) {
       	 	l2 = new ArrayList<Edge>();
       }
        l1.add(e2);
        l2.add(e1);
    }
    List<Edge> getEdgeList(Point p) {
    	for (Map.Entry<Point, List<Edge>> entry :adjVertices.entrySet()) {
   		 if(p.equals(entry.getKey()))
   			 return entry.getValue();
        }
    	return null;
    }

    List<Edge> getAdjVertices(Point p) {
        return adjVertices.get(new Point(p));
    }
    float dist(Point p0, Point p1) {
    	
    	return (float) Math.sqrt(Math.pow((p0.x-p1.x), 2)+Math.pow((p0.y-p1.y), 2));
    } 
    
    void printGraph() {
    	int i=0;
    	for (Map.Entry<Point, List<Edge>> entry :adjVertices.entrySet()) {
    		System.out.print("("+i+")"+"("+entry.getKey().x+","+entry.getKey().y+")"+":");
    		printEdgeList(entry.getKey());
    		i++;
    		System.out.println();
    	}
    }
    void printEdgeList(Point p) {
    	int j =0;
    	for (Edge entry :getEdgeList(p)) {
    		System.out.print("("+entry.point.x+","+entry.point.y+")"+"[dist:"+entry.cost+"]");
    		j++;
    	}
    	System.out.println();
    }
    
    
  //***************************Graph stuff **************************//

    List<Point>  createGraph(Track track,Point currentPosition,Point currentCheckPoint, int pointBuffer) {
    	adjVertices = new HashMap<Point, List<Edge>>();
    	obstacles = track.getObstacles();
        System.out.println("in Git init");
        System.out.println("beforeupdatesource");
        buffer = pointBuffer;
        updateSource(currentPosition);
        updateDes(currentCheckPoint);
        setupObsNodes(obstacles);//buffer
        setupObsEdges();
        System.out.println("src:" + source);
        System.out.println("des:" + destination);

        System.out.println("get shortest ");
       
       
        System.out.println("after sh ");
        return shortList;
    }

    List<Point> updateGraphSrcAndDes(Point prevCheckpoint,Point currentCheckPoint) {
        System.out.println("**************in updateGraphSrcAndDes********************");
//        getShortest = true;
        System.out.println("numOfPoints before:" + getNumOfPoints());
        updateSource(prevCheckpoint);
        updateDes(currentCheckPoint);
        
        System.out.println("numOfPoints after:" + getNumOfPoints());
        System.out.println("src:" + source);
        System.out.println("des:" + destination);

       
        System.out.println("after sh ");
        return shortList;
    }


   void updateSource(Point startPoint) {
     
      if(startPoint!=null) {//new startpoint
	     	startPoint = new Point(startPoint);

	      System.out.println("- - numOfPoints after:" + getNumOfPoints());
	      System.out.println("new startpoint");
	      addSrc(startPoint);
	      System.out.println(startPoint + "," + source);
	
	      //then add the edges with new source
	      int i = 0;
	      while (getPoint(i) != null) {
	          //add edge to all nodes that are not startpoint
	          if (!source.equals(getPoint(i))) {
	              Point q1 = new Point(getPoint(i).x, getPoint(i).y);
	              if (!intersectWithObs(source, q1)) {
	            	  //must also check it doesnt interset with 
	                  addEdge(source, q1);
	              }
	          }
	          i++;
	      }
      }
      else {//already has a prevcheckpoint
    	  setSrcToPrevDes();
      }
  }

    void updateDes(Point des) {

        System.out.println("new destination");
        addDes(des);
        System.out.println(des + "," + destination);

        //then add the edges with new source
        int i = 0;
        while (getPoint(i) != null) {
            //add edge to all nodes that are not startpoint
            if (!des.equals(getPoint(i))) {
                Point q1 = new Point(getPoint(i).x, getPoint(i).y);
                if (!intersectWithObs(des, q1)) {
                    addEdge(des, q1);
                }
            }
            i++;
        }
    }

   void setupObsNodes(Polygon[] obs) {
        //TODO add in current location and checkpoint
        //TODO isReflex not working yet
        //not include  four corners 
        for (int i = 0; i < obs.length; i++) {
            //    		allObsPoints[i] = new Vector2f[obs[i].npoints];
            for (int j = 0; j < obs[i].npoints; j++) {
                //if is reflex vertex(not concave) add to node
                if (isReflexecke(obs[i], j)) {
                    if (!isBorder(obs[i], j)) {
                        //then check outer border

                        addPoint(pointPlusBuffer(obs[i], j, buffer)); //10 is best
                    }
                }
            }
        }
    }


    void setupObsEdges() {
        //not connect to four corners  //now
        //polygon intersection
        //create array list of  vertex
        int i = 0;
        while (getPoint(i) != null) {
            int j = i + 1;
            Point p1 = new Point(getPoint(i).x, getPoint(i).y);
            while (getPoint(j) != null) {
                Point q1 = new Point(getPoint(j).x, getPoint(j).y);
                if (!intersectWithObs(p1, q1)) {
                    addEdge(p1, q1);
                }
                j++;
            }
            i++;
        }

    }

     boolean intersectWithObs(Point p1, Point q1) {
        // TODO innerpoints still connected(the obstacle points to itself)
        //first check if they belong to same obstacle
        //to types of check  (1)contain(to check if it's two points from same obs) (2)intersect (to see if there's other obs in between)

        for (int i = 0; i < obstacles.length; i++) {

            for (int j = 0; j < obstacles[i].npoints; j++) {
                int N = obstacles[i].npoints;

                Point p2 = new Point(obstacles[i].xpoints[j], obstacles[i].ypoints[j]);
                Point q2 = new Point(obstacles[i].xpoints[(j + 1) % N], obstacles[i].ypoints[(j + 1) % N]);

                //first check they are not next to each other
                if (!p1.equals(p2) && !p1.equals(q2) && !q1.equals(p2) && !q1.equals(q2)) {
                    //only checking line, not checking content
                    if (doIntersect(p1, q1, p2, q2)) {
                        return true;
                    }
                    // checking if mid point contain
                    if (obstacles[i].contains(new Point((p1.x + q1.x) / 2, (p1.y + q1.y) / 2)))
                        return true;
                }
            }
        }
        return false;
    }

   Point pointPlusBuffer(Polygon ob, int j, int buffer) {
        // TODO Auto-generated method stub

        int prev = j > 0 ? j - 1 : ob.npoints - 1;
        int next = j < ob.npoints - 1 ? j + 1 : 0;
        Point p0 = new Point(ob.xpoints[j], ob.ypoints[j]);
        Point p1 = new Point(ob.xpoints[prev], ob.ypoints[prev]);
        Point p2 = new Point(ob.xpoints[next], ob.ypoints[next]);
        double lengthPrev = Math.sqrt((p0.x - p1.x) * (p0.x - p1.x) + (p0.y - p1.y) * (p0.y - p1.y));
        double lengthNext = Math.sqrt((p0.x - p2.x) * (p0.x - p2.x) + (p0.y - p2.y) * (p0.y - p2.y));


        //find 1/lengthOfLine point of each side
        Vector2f v01 = new Vector2f((float)(p0.x + (p1.x - p0.x) / lengthPrev), (float)(p0.y + (p1.y - p0.y) / lengthPrev));
        Vector2f v02 = new Vector2f((float)(p0.x + (p2.x - p0.x) / lengthNext), (float)(p0.y + (p2.y - p0.y) / lengthNext));
        //then get mid point
        Vector2f mid = new Vector2f((v01.x + v02.x) / 2, (v01.y + v02.y) / 2);
        //then connect this point with p0

        int xBuffer=0;
        int yBuffer=0;
        if(p0.x== mid.x){
        	yBuffer = buffer;
        	 if(mid.y>p0.y) {
 	        	yBuffer*=-1;
 	        }
        	
        }
        else {
        	float m = (p0.y - mid.y) / (p0.x - mid.x);

	        xBuffer = buffer;
	        if (mid.x > p0.x) {
	            xBuffer *= -1;
	        }
	       
	        yBuffer = Math.round(xBuffer * m);
	    }
        return new Point((int) p0.x + xBuffer, (int)(p0.y + yBuffer));
    }

    boolean isBorder(Polygon ob, int j) {
        // TODO Auto-generated method stub
        return ob.xpoints[j] == 0 || ob.xpoints[j] == 1000 || ob.ypoints[j] == 0 || ob.ypoints[j] == 1000;
    }

    //** still not working well, get ang but when is ang over 
  boolean isReflexecke(Polygon ob, int j) {

        //first get angle bw three points with p1 as center
        int prev = j > 0 ? j - 1 : ob.npoints - 1;
        int next = j < ob.npoints - 1 ? j + 1 : 0;
        Point p0 = new Point(ob.xpoints[j], ob.ypoints[j]);
        Point p1 = new Point(ob.xpoints[prev], ob.ypoints[prev]);
        Point p2 = new Point(ob.xpoints[next], ob.ypoints[next]);
        Vector2f M = new Vector2f(p1.x - p0.x, p1.y - p0.y);
        Vector2f C = new Vector2f(p2.x - p0.x, p2.y - p0.y);
        double dot = Vector2f.dot(M, C);
        double det = ((C.getX() * M.getY()) - (C.getY() * M.getX()));
        double angle = Math.toDegrees(Math.atan2(det, dot));

        if (angle > 0) {
            return true;
        }
        return false;
    }

    // The main function that returns true if line segment 'p1q1' 
    // and 'p2q2' intersect. 
  boolean doIntersect(Point p1, Point q1, Point p2, Point q2) {
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
	
	 boolean onSegment(Point p, Point q, Point r) {
	        if (q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
	            q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y))
	            return true;
	
	        return false;
	    }
	int orientation(Point p, Point q, Point r) {
	        // See https://www.geeksforgeeks.org/orientation-3-ordered-points/ 
	        // for details of below formula. 
	        int val = (q.y - p.y) * (r.x - q.x) -
	            (q.x - p.x) * (r.y - q.y);
	
	        if (val == 0) return 0; // colinear 
	
	        return (val > 0) ? 1 : 2; // clock or counterclock wise 
	    }

}

