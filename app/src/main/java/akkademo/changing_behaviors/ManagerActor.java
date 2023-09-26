package akkademo.changing_behaviors;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class ManagerActor extends AbstractBehavior<String> {

	private ManagerActor(ActorContext<String> context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public static Behavior<String> create() {
		return Behaviors.setup(context -> new ManagerActor(context));
	}

	@Override
	public Receive<String> createReceive() {
		return newReceiveBuilder().onMessageEquals("first", () -> {
			System.out.println("FirstMessage");
			return secondReceiver();
		}).build();
	}

	public Receive<String> secondReceiver() {
		return newReceiveBuilder().onMessageEquals("first", () -> {
			System.out.println("2ndMessage");
			return this;
		}).build();
	}
}
