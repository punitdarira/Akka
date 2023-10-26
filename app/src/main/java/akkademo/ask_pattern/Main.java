package akkademo.ask_pattern;

import akka.actor.typed.ActorSystem;


public class Main {
    public static void main(String[] args) {
        akka.actor.typed.ActorSystem<String> actorSystem =
                ActorSystem.create(ManagerActor.create(), "ManagerActor");
        actorSystem.tell("start");
    }
}
