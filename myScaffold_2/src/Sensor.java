
/**
 * A sensor is noticed when a tagged bike arrives, and will inform robot arm to get the bike
 * @author Junwei Xing
 * 
 */

public class Sensor extends BicycleHandlingThread{

	protected Belt belt;
	protected int sensorPosition = 2; // the position is 3.
	protected boolean findTagged = false; // if a tagged bicycle arrived at belts, turn to true.
	
	/**
	 * 
	 * @param belt connects with belts.
	 */
	public Sensor (Belt belt) {
        super();
        this.belt = belt;
    }
	
	/**
	 * a loop is waiting to detect whether there is a tagged bicycle at position 3 as a sensor.
	 */
	
	 public void run() {
	        while (!isInterrupted()) {
	        	
	        	if(this.belt.peek(sensorPosition)!=null && this.belt.peek(sensorPosition).isTagged() && !this.belt.peek(sensorPosition).getChecked())
	            try {	 
	            	catching();
	            	
	            }  catch (InterruptedException e) {
	                this.interrupt();
	            }
	        }

	        System.out.println("Sensor terminated");
	    }
	 

	
	/**
	 * no
	 * @throws InterruptedException
	 */
    public synchronized void catching() throws InterruptedException {
    	findTagged = true; // let the robot arm to know whether can get a tagged bike now.
    	this.belt.stop = true; // stop the belt.
    	wait();
    }
   
   public synchronized void restarting() {
	   findTagged = false;
	   notifyAll();
   }
    
}
