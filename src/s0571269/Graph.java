package s0571269;

import java.awt.Point;
import java.util.*; 

public class Graph {
	public static boolean debug = false;
	public int numOfPoints = 0;
	public Point source=null;
	public Point destination=null;
    public Map<Point, List<Edge>> adjVertices;
    Graph(Point s, Point d){
    	adjVertices = new HashMap<Point, List<Edge>>();
    	source = new Point(s);
    	destination = new Point(d);
    }
    Graph(){
    	adjVertices = new HashMap<Point, List<Edge>>();
//    	source = new Point(s);
//    	destination = new Point(d);
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
    boolean removeSrc() {
    	System.out.println("====================in remove src====================");
    	System.out.println("n of points before:"+getNumOfPoints());
    	//get neighbors of source
    	List<Edge> srcNeighbor = adjVertices.get(source);
    	 adjVertices.remove(source);
        System.out.println("n of points after:"+getNumOfPoints());
        //remove edges with source
        	
        
        for (Edge neighborEdge : srcNeighbor) {
        	if(neighborEdge !=null && neighborEdge.point!=null && adjVertices.containsKey(neighborEdge.point)) {
        	
        	
	        	List<Edge> edgesOfNeigh = adjVertices.get(neighborEdge.point);
	        	//find the edge that contains src
	        	 System.out.println("n of edges of Neigh "+ neighborEdge.point+":"+ edgesOfNeigh.size());
	        	 int counter = 0;
	          	for (Edge e : edgesOfNeigh) {
	          	 if(e.containsPoint(source)) {
	          		 break;
	          	 }
	          	 counter++;
	          	}
	          	if(counter< edgesOfNeigh.size())
	          		edgesOfNeigh.remove(counter);
	        	 System.out.println("n of edges of Neigh "+ neighborEdge.point+" after remove :"+ edgesOfNeigh.size());
        	}
        	
        }
        	
        		    
        System.out.println("====================donone removing source====================");
        
        source = null;
        return true;
        
    }
    boolean removeDes() {
    	System.out.println("====================in remove des====================");
    	System.out.println("n of points before:"+getNumOfPoints());
    	List<Edge> srcNeighbor = adjVertices.get(destination);
	 adjVertices.remove(destination);
     System.out.println("n of points after:"+getNumOfPoints());
     
     
     for (Edge neighborEdge : srcNeighbor) {
     	List<Edge> edgesOfNeigh = adjVertices.get(neighborEdge.point);
     	//find the edge that contains src
     	 System.out.println("n of edges of Neigh "+ neighborEdge.point+":"+ edgesOfNeigh.size());
     	 //can't remove all the edges, just one
     	int counter = 0;
     	for (Edge e : edgesOfNeigh) {
     	 if(e.containsPoint(destination)) {
     		 break;
     	 }
     	 counter++;
     	}
     	if(counter< edgesOfNeigh.size())
     		edgesOfNeigh.remove(counter);
     	 System.out.println("n of edges of Neigh "+ neighborEdge.point+" after remove :"+ edgesOfNeigh.size());
     	
     }
     
     		 System.out.println("====================donone removing des====================");
     
     destination = null;
     return true;
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
    void removeEdge(Point p1, Point p2) {
        Point v1 = new Point(p1);
        Point v2 = new Point(p2);
        List<Edge> eV1 = adjVertices.get(v1);
        List<Edge> eV2 = adjVertices.get(v2);
        if (eV1 != null)
            eV1.remove(v2);
        if (eV2 != null)
            eV2.remove(v1);
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
    
}

