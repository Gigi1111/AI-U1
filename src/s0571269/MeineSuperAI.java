package s0571269;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Point;

import lenz.htw.ai4g.ai.AI;
import lenz.htw.ai4g.ai.DriverAction;
import lenz.htw.ai4g.ai.Info;
import lenz.htw.ai4g.track.Track;

public class MeineSuperAI extends AI {

	float old_slope, new_slope;
	Point checkpoint;
	float x ,y;
	boolean there = false;
	
    public MeineSuperAI(Info info) {
        super(info);
        enlistForTournament(571269,234);
        
        // hier irgendwas
    }
    private float getAtan2(float cx, float cy, float x, float y) {
    	return (float)Math.atan2(cy-y, cx-x);
    }

	private float getSlope(float cx, float cy, float x, float y) {
		return (cy-y)/(cx-x);
	}

	@Override
    public String getName() {
        return "Fan";
    }

    @Override
    public DriverAction update(boolean wasResetAfterCollision) {
        Track track = info.getTrack();
//        System.out.println(track.getObstacles()); // Hindernisse - nächste Übung

        float drehbeschleunigungVonAlign=turnToCheckpoint();
        
       float throttle = getThrottle();
        
        //throttle, steering
        //throttle: natural acceleration, slow at begginning and end, there's a plateu in between
        return new DriverAction(throttle, drehbeschleunigungVonAlign);//-3.14(pi) - +3.14(pi)
    }


    private float getThrottle() {
    	info.getMaxAbsoluteAcceleration();
    	info.getMaxVelocity();
    	info.getVelocity();

    	
    	
        
//    	Velocity matching
//    	• Accept the speed of a target
//    	• Acceleration = (target speed - starting speed) / desired time
//    	(Desired time = how long should it take until the target speed is reached)
//    	• Clip against maximum / minimum acceleration

    	float desiredSpeed = distance();
    	float currentSpeed = info.getVelocity().length();
    	float desiredTime = 0.01f;
    	float dist = distance();
    	float finishRadius = 15f;
    	float brakingRadius = 35f;
    	if(dist < finishRadius) {
    	//do nothing	
    	}else if (dist  < brakingRadius) {
    		System.out.println("***in radius**");
//    		info.getMaxVelocity()/bradingRadius;
//    		desiredSpeed = dist;
    		desiredSpeed = info.getMaxVelocity()/brakingRadius;
    	}else {
    		desiredSpeed = info.getMaxVelocity();
    	}
    	
//    	arrive
//    	• Braking when approaching the target
//    	• Distance (start, finish) <finish radius
//    	 Already arrived - done!
//    	• Distance (start, finish) <braking radius
//    	 Desired speed = (finish - start)
//    	∙ maximum speed / braking radius
//    	 Otherwise: Desired speed = max. Speed
//    	• Acceleration = (desired speed - current speed) / desired time
//    	• Clip against maximum acceleration
    	
    	float throttle = Math.min((desiredSpeed - currentSpeed)/desiredTime, info.getMaxAbsoluteAcceleration());
    	System.out.println("*****");
    	System.out.println("dist:"+dist);
    	System.out.println("currentSpeed:"+currentSpeed);
    	System.out.println("desiredSpeed:"+desiredSpeed);
    	System.out.println("throttle:"+throttle);
    	System.out.println("*****");
		return throttle;
	}
	private float distance() {
		Point goal = info.getCurrentCheckpoint();
		float dist = (float)Math.sqrt(Math.pow(goal.x-info.getX(),2)+Math.pow(goal.y-info.getY(), 2));
		return dist;
	}
	private float turnToCheckpoint() {
    	 float drehbeschleunigungVonAlign = 0;
	 Point checkpoint = info.getCurrentCheckpoint();
     float currentDirection = info.getOrientation();
     float tolerance = 0.01f;//0.01f
     float brakingAngle = 2.5f;
     float desiredTime = 0.1f;
     float goal_ori = getAtan2(checkpoint.x,checkpoint.y,info.getX(),info.getY());
     float angleBwOrs = goal_ori - currentDirection;
     float desiredSpeed = 0;
     System.out.println("angleBwOrs:"+angleBwOrs);
     System.out.println("goal_ori:"+ goal_ori);
     System.out.println("ori:"+currentDirection);
     
     if(Math.abs(  angleBwOrs ) < tolerance) {//do nothing 
     	drehbeschleunigungVonAlign = 0;
     }else {
     	if (Math.abs( angleBwOrs ) < brakingAngle) {
     		desiredSpeed = angleBwOrs;
     	}else {
	        	desiredSpeed = info.getMaxAbsoluteAngularVelocity()/brakingAngle;//max
	        }
	   drehbeschleunigungVonAlign  = (desiredSpeed-info.getAngularVelocity()) / desiredTime;
 	}
     
     //max accelaration clipping
     if(Math.abs(drehbeschleunigungVonAlign)> info.getMaxAbsoluteAngularAcceleration()) {
    	 if(drehbeschleunigungVonAlign<0) {
    		 drehbeschleunigungVonAlign = -info.getMaxAbsoluteAngularAcceleration();
    	 }else {
    		 drehbeschleunigungVonAlign = info.getMaxAbsoluteAngularAcceleration();
    	 }
     }
     System.out.println("dreh:"+ drehbeschleunigungVonAlign);
     System.out.println("angVelocity:"+info.getAngularVelocity());
    
     System.out.println("----------");
		return drehbeschleunigungVonAlign;
	}

	@Override
    public String getTextureResourceName() {
        return "car.png";
    }

    @Override
    public void doDebugStuff() {
        glBegin(GL_LINES);
        glColor3f(1, 0, 0);
        glVertex2f(info.getX(), info.getY());
        glVertex2d(info.getCurrentCheckpoint().getX(), info.getCurrentCheckpoint().getY());
        glEnd();
    }
}