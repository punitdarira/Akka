package akkademo.primenumber;

import java.io.Serializable;
import java.math.BigInteger;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class ManagerBehavior extends AbstractBehavior<ManagerBehavior.Command>{

	ActorSystem<WorkerBehavior.Command> actorSystem = ActorSystem.create(WorkerBehavior.create(), "WorkerActor");
	public ManagerBehavior(ActorContext<Command> context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public static Behavior<ManagerBehavior.Command> create() {
		return Behaviors.setup(ManagerBehavior::new);
	}
	@Override
	public Receive<ManagerBehavior.Command> createReceive() {
		
		return newReceiveBuilder()
				.onMessage(InstructionCommand.class, cmd->{
					try{
						//ActorSystem<WorkerBehavior.Command> actorSystem =ActorSystem.create(WorkerBehavior.create(),"WorkerActor"+(int)randDouble(1, 1000));

						actorSystem.tell(new WorkerBehavior.Command("generate", getContext().getSelf()));
					}
					catch (Exception ex){
						ex.printStackTrace();
					}

					return this;
				})
				.onMessage(WorkerResponseCommand.class, cmd->{
					System.out.println(cmd.getMsg());
					return this;
				}).build();
		
	}
	
	interface Command extends Serializable{
	}
	public static class InstructionCommand implements Command{
		private static final long serialVersionUID = 1L;
		String msg;
		public InstructionCommand(String msg) {
			this.msg = msg;
		}

		public String getMsg() {
			return msg;
		}
		
	}
	
	public static class WorkerResponseCommand implements Command{
		private static final long serialVersionUID = 1L;
		BigInteger msg;
		
		public WorkerResponseCommand(BigInteger msg) {
			this.msg = msg;
		}
		
		public BigInteger getMsg() {
			return msg;
		}
		
	}
	public double randDouble(double bound1, double bound2) {
		//make sure bound2> bound1
		double min = Math.min(bound1, bound2);
		double max = Math.max(bound1, bound2);
		//math.random gives random number from 0 to 1
		return min + (Math.random() * (max - min));
	}

}
