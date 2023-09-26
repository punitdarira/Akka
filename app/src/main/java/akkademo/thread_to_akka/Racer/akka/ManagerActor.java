package akkademo.thread_to_akka.Racer.akka;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akkademo.thread_to_akka.Racer.akka.DisplayerActor.DisplayerActorMessage;

public class ManagerActor extends AbstractBehavior<ManagerActor.ManagerMessage>{

	
	private Map<Integer, Long> results = new HashMap<>();
	private long start;
	private ActorRef<DisplayerActor.DisplayerActorMessage> caller;
	
	interface ManagerMessage extends Serializable{
		
	}
	public static class CurrentPositionMessage implements ManagerMessage{
		private static final long serialVersionUID = 1L;
		int id, currentPosition;
		public CurrentPositionMessage(int id, int currentPosition) {
			this.id = id;
			this.currentPosition = currentPosition;
		}
	}
	
	
	public static class ResultMessage implements ManagerMessage{
		private static final long serialVersionUID = 1L;
		int id;
		long result;
		public ResultMessage(int id, long result) {
			this.id = id;
			this.result = result;
		}
	}
	
	
	public static class InitiateCommand implements ManagerMessage{
		private static final long serialVersionUID = 1L;
		ActorRef<DisplayerActorMessage> caller;
		public InitiateCommand(ActorRef<DisplayerActorMessage> caller) {
			this.caller = caller;
		}
	}
	private ManagerActor(ActorContext<ManagerMessage> context) {
		super(context);
	}
	
	public static Behavior<ManagerMessage> create() {
		return Behaviors.setup(ManagerActor::new);
	}

	@Override
	public Receive<ManagerActor.ManagerMessage> createReceive() {
		return newReceiveBuilder()
		.onMessage(CurrentPositionMessage.class, currPos->{
			
			caller.tell(new DisplayerActor.CurrentPositionMessage(currPos.id, currPos.currentPosition));
			return this;
		})
		.onMessage(ResultMessage.class, resultMsg->{
			results.put(resultMsg.id, resultMsg.result);
			
			if(results.size() == 10) {
				results.values().stream().sorted().forEach(it -> {
					for (Integer key : results.keySet()) {
						if (results.get(key) == it) {
							System.out.println("Racer " + key + " finished in " + ( (double)it - start ) / 1000 + " seconds.");
						}
					}
				});
			}
			return this;
		})
		.onMessage(InitiateCommand.class, initiateCmd->{
			caller = initiateCmd.caller;
			start = System.currentTimeMillis();
			for (int i = 0 ; i<10 ; i++) {
				ActorSystem<WorkerActor.Message> actor = ActorSystem.create(WorkerActor.create(), "Worker-"+(i+1));
				actor.tell(new WorkerActor.Message(i, 100, getContext().getSelf()));
			}
			return this;
		}).build();

	}

}
