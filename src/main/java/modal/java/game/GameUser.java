package modal.java.game;

import modal.java.users.User;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Eduan on 2015-01-20.
 */
@Table
@Entity
public class GameUser implements Serializable{
    @Id
    @ManyToOne
    User user;

    @Id
    @ManyToOne
    Game game;

    @Column
    private String hand;

    public void setGame(Game game) { this.game = game;  }

    public void setHand(String hand) {
        this.hand = hand;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Game getGame() { return game; }

    public String getHand() {
        return hand;
    }

    public User getUser() {
        return user;
    }
}
