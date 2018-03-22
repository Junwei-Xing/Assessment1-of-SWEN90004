import java.util.Random;

/**
 * this class simulates the robot arm that can get tagged bike from belts to a inspector and return back 
 * the processed bike from the inspector to belts.
 * @author Junwei Xing
 *
 */

public class Robot extends BicycleHandlingThread{
	protected Belt belt;
	protected Sensor sensor;
	protected Inspector inspector;
	
	protected Bicycle tagged = null; // it's the bicycle for sending to the inspector.
	protected Bicycle returnTagged = null; // it's the bicycle for backing to the belts.
	
	protected int sensorPosition = 2; // the position of belts should be processed.
	
	protected boolean sending = false; // whether the operation of sending should be invoked.
	protected boolean backing = false; // whether the operation of backing should be invoked.
	
	/**
	 * initialization
	 * @param belt
	 * @param sensor
	 */
	public Robot (Belt belt, Sensor sensor) {
        super();
        this.belt = belt;
        this.sensor = sensor;
    }
	
	/**
	 * A iteration is used to process the transmission between a inspector and belts.
 	 * A if judgment decides which operation (i.e. sending or backing) should be executed now.
 	 * sending and backing cannot happen at same time.
	 * 
	 */
	 public void run() {
	        while (!isInterrupted()) {
	        	
	        	if(this.sensor.findTagged){
	        	
	            try {
	            	
                     getTagged();        
                     Random random = new Random();
                     int sleepTime = random.nextInt(Params.ROBOT_MOVE_TIME);
                     sleep(sleepTime);
                     
	            }  catch (InterruptedException e) {
	                this.interrupt();
	            }
	        	}
	        	
	        	if(backing){
		        	
		            try {
		            	
			        	 returnBike();        
		                 Random random2 = new Random();
		                 int sleepTime2 = random2.nextInt(Params.ROBOT_MOVE_TIME);
		                 sleep(sleepTime2);
	                     
		            }  catch (InterruptedException e) {
		                this.interrupt();
		            }
		        	}

               
	        }

	        System.out.println("Robort terminated");
	    }
	 
	 /**
	  * the sending operation
	  * @throws InterruptedException
	  */
	 public synchronized void getTagged() throws InterruptedException {    	
	        
	        tagged = belt.segment[sensorPosition];
	        belt.segment[sensorPosition] = null;
	        
	        System.out.println("the tagged " + tagged + " is moving to inspector...");
	        sending = true;
	        this.belt.restarting(); // notify the belts can restart.
	        this.sensor.restarting(); // notify the sensor can restart.
	        
	        wait();
	    }
	 
	 /**
	  * wake this thread up, and executing the backing operation.
	  * @throws InterruptedException
	  */
	 public synchronized void restartingFront() throws InterruptedException{
		 tagged = null;
		 sending = false;
		 notifyAll();
	 }
	  
	 /**
	  * the backing operating
	  * @throws InterruptedException
	  */
	 
	    public synchronized void returnBike() throws InterruptedException {   
	    	
	        returnTagged.setChecked();
	        
	        // a clash happens, failed.
	        if(belt.segment[sensorPosition] != null && belt.segment[sensorPosition].tagged && !belt.segment[sensorPosition].getChecked()  ){
	        	System.out.println("-----------  a tagged bike is waiting at position 3. A Clash happens -----------");
	        	Thread.currentThread().interrupt();
	        }
	        
	        // put back, successful.
	        if (belt.segment[sensorPosition] == null){
	        	System.out.println("the processed " + returnTagged + " is moving to belt...");
	        	belt.segment[sensorPosition] = returnTagged;  
		        returnTagged = null;
		        backing = false;        
		       
	        }
	        
	        // notify the inspector object to work.
	        this.inspector.restarting();

	        
	        
	        
	    }
	    
	    /**
	     * notify this thread to restart after finishing the backing operation
	     * @throws InterruptedException
	     */
	    
	    public synchronized void restartingBack() throws InterruptedException { 
	    	 notifyAll();
	    }
	 
	    /**
	     * get the object of inspector for notifying after finishing the backing oepration.
	     * @param inspector
	     */
	    public synchronized void getInspector (Inspector inspector){
	    	this.inspector = inspector;
	    }
	    
	 
}
