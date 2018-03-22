import java.util.Random;

/**
 * A inspector continually tries to secondly check whether the tagged bicycle is really defective.
 * @author Junwei Xing
 *
 */

public class Inspector extends BicycleHandlingThread{
	protected Robot robot;
	protected Bicycle target = null; // get the tagged bicycle.
	protected boolean pass = false; //allow addition belts to pass the processed bicycle.
	
	/**
	 * 
	 * @param robot connects with this inspector.
	 */
	public Inspector (Robot robot) {
        super();
        this.robot = robot;
    }
	
	/**
	 * a loop waits the robot passes a tagged bicycle to it.
	 */
	
	 public void run() {
	        while (!isInterrupted()) {
	        	if(this.robot.sending){
	            try {
                     inspecting();        // execute the concrete function of a inspector.
                  // let some time pass
                     Random random = new Random();
                     int sleepTime = random.nextInt(Params.INSPECT_TIME);
                     sleep(sleepTime);
	            }  catch (InterruptedException e) {
	                this.interrupt();
	            }
	        	}
	        }

	        System.out.println("Inspector terminated"); // let some time pass
	    }
	 
	 
	 /**
	  * a logic judgment checks whether a tagged bike is defective, and post the result out.
	  * @throws InterruptedException
	  */
	 
	    public synchronized void inspecting() throws InterruptedException {
	    	
	    	target = this.robot.tagged;
	    	
	    	if(!target.isDefective()){
	    		target.setNotTagged();
	    		System.out.println(target + "passed the second check at inspector!");
	    	}else{
	    		System.out.println(target + "DIDNOT pass the second check at inspector!");
	    	}
	    	this.robot.backing = true; // mark the backing process of the robot object can be working.
	    	this.robot.restartingFront(); // notify the robot to work
	    	pass = true; //allow additional belts to transmit the processed bicycle.
	      	
	    	wait();
	    }

	    /**
	     * notify this inspect can work again.
	     * @throws InterruptedException
	     */
	    public synchronized void restarting() throws InterruptedException {
	        target = null;
	        pass = false; //initialize the value.
	    	notifyAll();
	    }
	    

}

