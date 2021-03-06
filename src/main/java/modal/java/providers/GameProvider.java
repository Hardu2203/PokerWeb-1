package modal.java.providers;

import com.google.inject.Singleton;
import ninja.jpa.UnitOfWork;

import javax.persistence.Query;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import modal.java.game.Game;
/**
 * Created by Eduan on 2015-01-20.
 */
@Singleton
public class GameProvider extends DatabaseProvider<Game> {
    @UnitOfWork
    public List<Game> findAllGames()
    {
        Query query = getEntityManager().createQuery("SELECT g FROM Game g");
        return query.getResultList();
    }
    @UnitOfWork
    public Optional findGameByID(Long id)
    {
        Query query = getEntityManager().createQuery("SELECT g FROM Game g WHERE g.id = :id").setParameter("id",id);
        return getSingleResult(query);
    }
    @UnitOfWork
    public List<Game> findAllUnfinishedGames()
    {
        Query query = getEntityManager().createQuery("SELECT g FROM Game g WHERE g.played = :false").setParameter("false",false);
        List<Game> games = query.getResultList();
        return games;
    }
}