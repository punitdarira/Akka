package akkademo.thread_to_akka.Racer.akka.instructors_solution.with_changing_behaviors;

import akka.actor.typed.ActorSystem;

public class Main {
    public static void main(String[] args) {
        ActorSystem<RaceController.Command> raceController = ActorSystem.create(RaceController.create(), "RaceSimulation");
        raceController.tell(new RaceController.StartCommand());
    }
}
