package modal.java.game;

import modal.java.cards.Hand;
import modal.java.users.User;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Eduan on 2015-01-20.
 */
@Entity
@Table
public class Game {
    @Column
    private String game_name;
    @GeneratedValue()
    @Id
    private Long id;

    public Long getId() {
        return id;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }
}
