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

    @ManyToOne
    @JoinColumn(name = "winner_name")
    private User winner_name;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date date_time;

    public void setDate_time(Date date_time) {
        this.date_time = date_time;
    }

    public void setWinner(User winner) {
        this.winner_name = winner;
    }

    public User getWinner() {
        return winner_name;
    }

    public Date getDate_time() {
        return date_time;
    }

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
