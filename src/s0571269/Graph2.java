package s0571269;

import java.awt.Point;
import java.util.*; 

public class Graph2 {
	public static boolean debug = false;
	public int numOfPoints = 0;
	public Point source=null;
	public Point destination=null;
    public Map<Point, List<Edge>> adjVertices;
    Graph2(Point s, Point d){
    	adjVertices = new HashMap<Point, List<Edge>>();
    	source = new Point(s);
    	destination = new Point(d);
    }
    void updateSource(Point s) {
    	source = new Point(s);
    }
    void updateDes(Point d) {
    	destination = new Point(d);
    }
    void addPoint(Point p) {
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
        Point v = new Point(p);
        adjVertices.values().stream().forEach(e -> e.remove(v));
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

