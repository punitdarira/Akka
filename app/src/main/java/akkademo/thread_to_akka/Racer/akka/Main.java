package akkademo.thread_to_akka.Racer.akka;

import akka.actor.typed.ActorSystem;
import akkademo.primenumber.WorkerBehavior;

public class Main {

	public static void main(String[] args) {
		ActorSystem<DisplayerActor.DisplayerActorMessage> actorSystem = ActorSystem.create(DisplayerActor.create(), "DisplayerActor");
		actorSystem.tell(new DisplayerActor.StartMessage());

	}

}
