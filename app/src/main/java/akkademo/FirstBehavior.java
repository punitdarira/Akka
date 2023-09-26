package akkademo;

import java.math.BigInteger;
import java.util.Random;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.japi.Creator;

public class FirstBehavior extends AbstractBehavior<String>{

	int i = 0;
	private FirstBehavior(ActorContext<String> context) {
		super(context);
	}
	
	public static Behavior<String> create() {
		return Behaviors.setup(context->new FirstBehavior(context));
	}

	@Override
	public Receive<String> createReceive() {
		return newReceiveBuilder()
				.onMessageEquals("DemoMsg", () -> {
					System.out.println("This is a demoMsg");
					return this;
				})
				.onMessageEquals("Path", () -> {
					System.out.println(getContext().getSelf().path());
					return this;
				})
				.onMessageEquals("ChildActorAndCalcPrime", () -> {
					ActorRef<String> childActorRef =  getContext().spawn(FirstBehavior.create(), "ChildActor"+i++);
					childActorRef.tell("CalculatePrime");
					return this;
				})
				.onMessageEquals("CalculatePrime", () -> {
					System.out.println(BigInteger.probablePrime(2000, new Random()));
					return this;
				})
				.onAnyMessage(msg->{
					System.out.println(msg);
					return this;
				}).build();
	}



}
