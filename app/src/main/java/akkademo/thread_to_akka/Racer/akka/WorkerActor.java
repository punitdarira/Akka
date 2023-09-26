package akkademo.thread_to_akka.Racer.akka;

import java.util.Random;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akkademo.thread_to_akka.Racer.akka.ManagerActor.ManagerMessage;

public class WorkerActor extends AbstractBehavior<WorkerActor.Message>{

	static class Message{
		int id, raceLength;
		ActorRef<ManagerMessage> caller;
		public Message(int id, int raceLength, ActorRef<ManagerMessage> actorRef) {
			this.id = id;
			this.raceLength = raceLength;
			this.caller = actorRef;
		}
		public ActorRef<ManagerMessage> getCaller(){
			return caller;
		}
	}
	//
	private int id, raceLength;
	private Random random = new Random();
	private double currentSpeed = 0;
	private double currentPosition = 0;
	private int averageSpeedAdjustmentFactor = random.nextInt(30) - 10;
	private final double defaultAverageSpeed = 48.2;
	//
	private WorkerActor(ActorContext<WorkerActor.Message> context) {
		super(context);
	}

	public static Behavior<WorkerActor.Message> create() {
		return Behaviors.setup(WorkerActor::new);
		
	}
	@Override
	public Receive<WorkerActor.Message> createReceive() {
		return newReceiveBuilder().onMessage(WorkerActor.Message.class, message->{
			id = message.id;
			raceLength = message.raceLength;
			
			while (currentPosition < raceLength) {
				
				determineNextSpeed();
				currentPosition += getDistanceMovedPerSecond();
				if (currentPosition > raceLength )
					currentPosition  = raceLength;
				message.getCaller().tell(new ManagerActor.CurrentPositionMessage(id, (int)currentPosition));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			message.getCaller().tell(new ManagerActor.ResultMessage(id, System.currentTimeMillis()));
			
			return this;
		}).build();
	}
	private double getMaxSpeed() {
		return defaultAverageSpeed * (1+((double)averageSpeedAdjustmentFactor / 100));
	}
		
	private double getDistanceMovedPerSecond() {
		return currentSpeed * 1000 / 3600;
	}
	
	private void determineNextSpeed() {
		if (currentPosition < (raceLength / 4)) {
			currentSpeed = currentSpeed  + (((getMaxSpeed() - currentSpeed) / 10) * random.nextDouble());
		}
		else {
			currentSpeed = currentSpeed * (0.5 + random.nextDouble());
		}
	
		if (currentSpeed > getMaxSpeed()) 
			currentSpeed = getMaxSpeed();
		
		if (currentSpeed < 5)
			currentSpeed = 5;
		
		if (currentPosition > (raceLength / 2) && currentSpeed < getMaxSpeed() / 2) {
			currentSpeed = getMaxSpeed() / 2;
		}
	}

}
