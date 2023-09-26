package akkademo.primenumber;

import java.math.BigInteger;
import java.util.Random;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;


public class WorkerBehavior extends AbstractBehavior<WorkerBehavior.Command>{

	private WorkerBehavior(ActorContext<WorkerBehavior.Command> context) {
		super(context);
	}
	
	public static Behavior<WorkerBehavior.Command> create() {
		return Behaviors.setup(WorkerBehavior::new);
	}
	@Override
	public Receive<WorkerBehavior.Command> createReceive() {
		// TODO Auto-generated method stub
		return newReceiveBuilder()
				.onMessage(WorkerBehavior.Command.class, cmd->{
					System.out.println(BigInteger.probablePrime(2000, new Random()));
					return this;
				}).build();
		
	}
	
	public static class Command{
		String cmd;
		ActorRef<ManagerBehavior.Command> sender;
		
		
		public Command(String cmd, ActorRef<ManagerBehavior.Command> actorRef) {
			super();
			this.cmd = cmd;
			this.sender = actorRef;
		}
		public String getCmd() {
			return cmd;
		}
		public void setCmd(String cmd) {
			this.cmd = cmd;
		}
		public ActorRef<ManagerBehavior.Command> getSender() {
			return sender;
		}
		public void setSender(ActorRef<ManagerBehavior.Command> sender) {
			this.sender = sender;
		}
		
		
	}

}
