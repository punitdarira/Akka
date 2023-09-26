package akkademo.changing_behaviors;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;

public class Main{
	public static void main(String[] args) {
		ActorRef<String> actorRef = ActorSystem.create(ManagerActor.create(), "FirstBehavior");
		actorRef.tell("first");
		actorRef.tell("first");
	}

}