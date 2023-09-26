package akkademo.thread_to_akka.Racer.akka;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akkademo.thread_to_akka.Racer.akka.ManagerActor.ManagerMessage;

public class DisplayerActor extends AbstractBehavior<DisplayerActor.DisplayerActorMessage> {

	static int displayLength = 160;
	private Map<Integer, Integer> currentPositions = new HashMap<>();
	private long start;

	public interface DisplayerActorMessage extends Serializable {

	}

	public static class CurrentPositionMessage implements DisplayerActorMessage {
		private static final long serialVersionUID = 1L;
		int id, currentPosition;

		public CurrentPositionMessage(int id, int currentPosition) {
			this.id = id;
			this.currentPosition = currentPosition;
		}
	}

	public static class StartMessage implements DisplayerActorMessage {

		private static final long serialVersionUID = 1L;

		public StartMessage() {

		}

	}

	private DisplayerActor(ActorContext<DisplayerActor.DisplayerActorMessage> context) {
		super(context);
	}

	public static Behavior<DisplayerActor.DisplayerActorMessage> create() {
		return Behaviors.setup(DisplayerActor::new);
	}

	@Override
	public Receive<DisplayerActor.DisplayerActorMessage> createReceive() {
		return newReceiveBuilder().onMessage(StartMessage.class, message -> {
			start = System.currentTimeMillis();
			ActorSystem<ManagerMessage> actorSystem = ActorSystem.create(ManagerActor.create(), "ManagerActor");
			actorSystem.tell(new ManagerActor.InitiateCommand(getContext().getSelf()));
			return this;
		}).onMessage(CurrentPositionMessage.class, currentPosition -> {
			try {
				
				currentPositions.put(currentPosition.id, currentPosition.currentPosition);
				for (int i = 0; i < 50; ++i)
					System.out.println();
				System.out.println(
						"Race has been running for " + ((System.currentTimeMillis() - start) / 1000) + " seconds.");
				System.out.println("    " + new String(new char[displayLength]).replace('\0', '='));
				for (int i = 0; i < 10; i++) {
					if(currentPositions.get(i)!= null) {
						System.out.println(i + " : "
								+ new String(new char[currentPositions.get(i) * displayLength / 100]).replace('\0', '*'));
					}
					
				}
				
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			

			return this;
		}).build();
	}

}
