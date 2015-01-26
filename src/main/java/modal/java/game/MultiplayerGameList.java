package modal.java.game;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import modal.java.users.User;

import java.util.Date;
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

    @Inject
    LinkedList<LinkedList<User>> joinedUsers;

    @Inject
    LinkedList<LinkedList<Date>> joinedDates;

    public LinkedList<LinkedList<Date>> getJoinedDates() {
        return joinedDates;
    }

    public LinkedList<LinkedList<User>> getJoinedUsers() {
        return joinedUsers;
    }

    public void setJoinedDates(LinkedList<LinkedList<Date>> joinedDates) {
        this.joinedDates = joinedDates;
    }

    public void setJoinedUsers(LinkedList<LinkedList<User>> joinedUsers) {
        this.joinedUsers = joinedUsers;
    }

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
