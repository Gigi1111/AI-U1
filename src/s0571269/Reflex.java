//package s0571269;
//
//import java.awt.Point;
//import java.awt.Polygon;
//
//import org.lwjgl.util.vector.Vector2f;
//
//public class Reflex {
////p1 is center
//	public static void main(String arg[]) 
//	 { 
//		Point p0 = new Point(0,0);
//		Point p1 = new Point(5,0);
//		Point p2 = new Point(5,5);
//		Reflex r = new Reflex();
//		r.isReflex(p0,p1,p2);
//		//get two vectors from j to j-1 and j to j+1, get the smaller angle if middle to smaller angle is contained or intersect the obstacle, it's reflexive
//		
//	}
//	private void isReflex(Point p0, Point p1, Point p2) {
//		// TODO Auto-generated method stub
//		float a = (p1.x-p0.x)*(p1.x-p0.x) + (p1.y-p0.y)*(p1.y-p0.y);
//		float b = (p1.x-p2.x)*(p1.x-p2.x) + (p1.y-p2.y)*(p1.y-p2.y);
//		float c = (p2.x-p0.x)*(p2.x-p0.x) + (p2.y-p0.y)*(p2.y-p0.y);
//		float result = (float) (Math.acos( (a+b-c) / Math.sqrt(4*a*b) ) * 180/Math.PI);
//		//only returns 0-180
//		if(debug)
//			System.out.println("result: "+result);
//		 float mx = (float)(p2.x+p0.x)/2;
//		 float my = (float)(p2.y+p0.y)/2;
//		if(debug)
//			 System.out.println("mid: "+mx+","+my);
//		//get slope between mid and p1
//		float m = (my-p1.y) / (mx-p1.x);
//		System.out.println("m: "+m);
//		//get center p1 to go slightly inward towards mid along the slope
//		int i =0;
//		if(mx < p1.x) {
//			i=-1;
//		}
//		float nx = p1.x+i;
//		float ny =(p1.y+(m *i));
//		if(debug)
//		System.out.println("new: "+nx+","+ny);
//				
//	}
//	
//}
