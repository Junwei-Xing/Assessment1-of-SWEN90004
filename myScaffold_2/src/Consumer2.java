import java.util.Random;

/**
 * A consumer continually tries to take bicycles from the end of additional belt.
 */

public class Consumer2 extends BicycleHandlingThread {

    // the belt from which the consumer takes the bicycles
    protected AdditionalBelts belts;
    // to help format output trace
    final private static String indentation = "                  ";
    protected Bicycle target = null;
    

    /**
     * Create a new Consumer that consumes from the additional belt
     */
    public Consumer2(AdditionalBelts belts) {
        super();
        this.belts = belts;
    }

    /**
     * Loop indefinitely trying to get bicycles from the additional belt
     */
    public void run() {
        while (!isInterrupted()) {
        	if(this.belts.received){
            try {
                getEndBelt2();
                // let some time pass ...
                Random random = new Random();
                int sleepTime = Params.CONSUMER_MIN_SLEEP + 
                		random.nextInt(Params.CONSUMER_MAX_SLEEP - 
                				Params.CONSUMER_MIN_SLEEP);
                sleep(sleepTime);
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
        }
        System.out.println("Consumer terminated");
    }
    
    /**
     * processing the achieved bicycle, and post the result.
     * @throws InterruptedException
     */
    public synchronized void getEndBelt2() throws InterruptedException {
    	
    		target = this.belts.segment2[this.belts.segment2.length-1];
    		this.belts.segment2[this.belts.segment2.length-1] = null;
    	
        	System.out.print(indentation + indentation);
            System.out.print(target + " departed");
            if(target.isTagged()){
            	System.out.println("------ recycling");
            }else{
            	System.out.println("------ packing");
            }
            
            belts.restarting();
            target = null;
            
        }
}
