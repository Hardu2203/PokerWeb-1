package modal.java.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import modal.java.users.User;

import java.util.LinkedList;

/**
 * Created by Eduan on 2015-01-23.
 */
@Singleton
public class MultiplayerGameList {

    @Inject
    LinkedList<Game> games;

    @Inject
    LinkedList<User> hosts;

    public void setHosts(LinkedList<User> hosts) {
        this.hosts = hosts;
    }

    public LinkedList<User> getHosts() {
        return hosts;
    }

    public LinkedList<Game> getGames() {
        return games;
    }

    public void setGames(LinkedList<Game> games) {
        this.games = games;
    }
}
