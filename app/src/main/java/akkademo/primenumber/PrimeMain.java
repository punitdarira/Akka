package akkademo.primenumber;

import akka.actor.typed.ActorSystem;

public class PrimeMain {

	public static void main(String[] args) {
		 ActorSystem<ManagerBehavior.Command> actorSystem = ActorSystem.create(ManagerBehavior.create(), "ManagerActor");
		 for(int i = 0 ; i < 20 ; i++) {
			 actorSystem.tell(new ManagerBehavior.InstructionCommand("start"));
		 }
	}
}
