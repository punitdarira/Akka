package akkademo.thread_to_akka.Racer.akka.instructors_solution;

import akka.actor.typed.ActorSystem;

public class Main {
    public static void main(String[] args) {
        ActorSystem<RaceController.Command> raceController = ActorSystem.create(RaceController.create(), "RaceSimulation");
        raceController.tell(new RaceController.StartCommand());
    }
}
