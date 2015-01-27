package modal.java.game;

import modal.java.cards.Hand;
import modal.java.users.User;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
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

    @ManyToOne
    @JoinColumn(name = "host_name")
    private User host_name;

    @OneToMany(fetch=FetchType.EAGER, mappedBy = "game")
    private List<GameUser> gameUsers;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date date_time;

    @Column
    private boolean played = false;

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public boolean getPlayed()
    {
        return played;
    }

    public User getHost_name() {
        return host_name;
    }

    public void setHost_name(User host_name) {
        this.host_name = host_name;
    }

    public void setGameUsers(List<GameUser> gameUsers) {
        this.gameUsers = gameUsers;
    }

    public List<GameUser> getGameUsers() {
        return gameUsers;
    }

    public void setDate_time(Date date_time) {
        this.date_time = date_time;
    }

    public void setWinner(User winner) {
        this.winner_name = winner;
    }

    public User getWinner_name() { return winner_name;  }

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
