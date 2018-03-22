import java.util.Random;

/**
 * two additional belts tries to transmit processed bicycles from the inspector to consumer2.
 */

public class AdditionalBelts extends BicycleHandlingThread {

    // the belt from which the consumer takes the bicycles
    protected Inspector inspector;
    protected Bicycle[] segment2; //simulate two segments of the belt.
    protected Bicycle target = null; // get the processed bicycle from the inspector.
    protected boolean received = false; // mark whether consumer2 can be involved.
    
    // the length of this belt
    protected int beltLength = 2;

    // to help format output trace
    final private static String indentation = "                  ";

    /**
     * 
     * @param belt
     * @param inspector
     */
    public AdditionalBelts(Inspector inspector) {
        super();
        this.inspector = inspector;
        segment2 = new Bicycle[beltLength];
        for (int i = 0; i < segment2.length; i++) {
            segment2[i] = null;
        }
    }

    /**
     * Loop indefinitely trying to get bicycles from the quality control belt
     */
    public void run() {
        while (!isInterrupted()) {
        	if(this.inspector.pass){
        	try {
                passing();

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
    
public synchronized void passing() 
        throws InterruptedException{

	target = this.inspector.target;
	segment2[0] = target;
	
    // move the elements along, making position 0 null
    for (int i = segment2.length-1; i > 0; i--) {
        if (this.segment2[i-1] != null) {
            System.out.println(
            		indentation +
            		this.segment2[i-1] +
                    " [ s" + (i+5) + " -> s" + (i+1+5) +" ]");
        }
        segment2[i] = segment2[i-1];
    }
    segment2[0] = null;
    received = true; // allow consumer2 to process the target bicycle.
    this.inspector.restarting(); //notify inspector can work now
    wait();
}

/**
 * restart this thread, and initialize.
 */
	public synchronized void restarting() {
        target = null;
        received = false;
		notifyAll();
}

    
}
