package akkademo.search_utility.akka;

import java.io.File;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class DirectorySearcherActor
		extends AbstractBehavior<akkademo.search_utility.akka.DirectorySearcherActor.SearchCommandI> {
	int subCalls = 0;
	int completedSubCalls = 0;

	int noOfFilesFoundByActor = 0;
	int filesFoundByChildActors = 0;

	boolean childCreated = false;
	ActorRef<SearchCommandI> actorRefOfCaller;

	interface SearchCommandI {

	}

	static class SearchCommand implements SearchCommandI {
		String message;
		ActorRef<SearchCommandI> actorRefOfCaller;

		public SearchCommand(String message, ActorRef<SearchCommandI> actorRefOfCaller) {
			this.message = message;
			this.actorRefOfCaller = actorRefOfCaller;
		}

		public String getMessage() {
			return message;
		}

		public ActorRef<SearchCommandI> getActorRefOfCaller() {
			return actorRefOfCaller;
		}
	}

	static class CompleteCommand implements SearchCommandI {
		int numberOfFiles;

		public CompleteCommand(int numberOfFiles) {
			super();
			this.numberOfFiles = numberOfFiles;
		}

		public int getNumberOfFiles() {
			return numberOfFiles;
		}


	}

	private DirectorySearcherActor(ActorContext<SearchCommandI> context) {
		super(context);

	}

	@Override
	public Receive<SearchCommandI> createReceive() {
		return newReceiveBuilder().onMessage(SearchCommand.class, msg -> {

			try {
				actorRefOfCaller = msg.getActorRefOfCaller();
				String path = msg.getMessage();
				File file = new File(path);
				if (file.list() != null) {
					for (String childPath : file.list()) {
						File childFile = new File(path + childPath);
						if (childFile.isDirectory() && !childFile.isHidden()) {
							ActorRef<SearchCommandI> childActor = getContext().spawn(DirectorySearcherActor.create(),
									actorNameGenerator(path + childPath + "\\"));
							childActor.tell(new SearchCommand(path + childPath + "\\", getContext().getSelf()));
							childCreated = true;
							subCalls++;
						} else {
							System.out.println("FileName is " + childFile.getPath());
							noOfFilesFoundByActor++;
						}
					}
				}
				if (!childCreated) {
					msg.getActorRefOfCaller().tell(new CompleteCommand(noOfFilesFoundByActor));
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return this;

		}).onMessage(CompleteCommand.class, msg -> {
			completedSubCalls++;
			filesFoundByChildActors += msg.getNumberOfFiles();
			if (subCalls == completedSubCalls) {
				if (actorRefOfCaller != null)
					actorRefOfCaller.tell(new CompleteCommand(noOfFilesFoundByActor + filesFoundByChildActors));
				return Behaviors.stopped();
			}
			return this;
		}).build();
	}

	public static Behavior<SearchCommandI> create() {
		return Behaviors.setup(DirectorySearcherActor::new);
	}

	private String actorNameGenerator(String input) {
		return input.replace("\\", "-").replace(" ", "_").replace("(", "open").replace(")", "close")
				.replace("{", "open").replace("}", "close").replace("#", "hash").replace("%", "per");
	}
}
