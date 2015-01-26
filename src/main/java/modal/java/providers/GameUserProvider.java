package modal.java.providers;

import com.google.inject.Singleton;
import modal.java.game.Game;
import modal.java.users.User;
import ninja.jpa.UnitOfWork;

import javax.persistence.Query;
import java.util.List;

/**
 * Created by Eduan on 2015-01-21.
 */
@Singleton
public class GameUserProvider<GameUser> extends DatabaseProvider{
    @UnitOfWork
    public List<GameUser> findAllGameUsers()
    {
        Query query = getEntityManager().createQuery("SELECT g FROM GameUser g");
        return query.getResultList();
    }

    @UnitOfWork
    public List<GameUser> findByUser(User user)
    {
        Query query = getEntityManager().createQuery("SELECT g FROM GameUser g WHERE g.user_name = :username").setParameter("username",user.getName());
        return query.getResultList();
    }

    @UnitOfWork
    public List<GameUser> findByGame(Game game)
    {
        Query query = getEntityManager().createQuery("SELECT g FROM GameUser g WHERE g.game_id = :game_id").setParameter("game_id",game.getId());
        return query.getResultList();
    }
}
