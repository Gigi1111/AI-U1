package s0571269;

import java.awt.Point;
import java.util.*; 

public class Graph {

	private ArrayList <Point> nodes;
	private HashMap<Point, ArrayList<Point>> neighbourNodes;
  
    Graph(ArrayList<Point> nodes, HashMap<Point, ArrayList<Point>> neighbourNodes ){
    	this.nodes =  nodes;
    	this.neighbourNodes = neighbourNodes;
      }
    
    Graph(){
		this.nodes = new ArrayList<>();
		this.neighbourNodes = new HashMap<>();
    }
    
    void addNode(Point point) {
    	nodes.add(point);
    }
    
    void addNeigbourNode(Point currentNode, Point neighbourNode) {
    	ArrayList<Point> neighboursOfCurrentNode = neighbourNodes.get(currentNode);
    	if(neighboursOfCurrentNode == null) {
    		neighboursOfCurrentNode = new ArrayList<>();
    		neighbourNodes.put(currentNode, neighboursOfCurrentNode);
    	}
    	neighboursOfCurrentNode.add(neighbourNode);
    }
    
    public ArrayList<Point> getNeighbours(Point point){
    	ArrayList<Point> result = neighbourNodes.get(point);
    	if (result == null) {
    		return new ArrayList<Point>();
    	} else {
    		return result;
    	}
    }
    
    
	public ArrayList<Point> getAllNodes() {
		ArrayList<Point> nodesCopy = new ArrayList<>(nodes);
		return nodesCopy;
	}
    
    
       
}

