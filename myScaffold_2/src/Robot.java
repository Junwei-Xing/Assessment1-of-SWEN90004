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
	
	protected Bicycle tagged = null; // it's the bicycle for sending to the inspector.
	
	protected int sensorPosition = 2;// the position of belts should be processed.
	
	protected boolean sending = false;// whether the operation of sending should be invoked.
	protected boolean backing = false;// whether the operation of backing should be invoked.
	
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
	        this.belt.restarting();
	        this.sensor.restarting();
	        
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
	  * this function simulates the process of robot arm turns back, but without taking a bike than solution 1.
	  * @throws InterruptedException
	  */
	    public synchronized void returnBike() throws InterruptedException {   	            
	           
	        System.out.println("the robot is moving back...");
	        backing = false;        
	            	        
	    }
	 
}
