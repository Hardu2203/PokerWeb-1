package modal.java.providers;

import com.google.inject.Singleton;
import ninja.jpa.UnitOfWork;

import javax.persistence.Query;
import java.util.Optional;

/**
 * Created by Eduan on 2015-01-20.
 */
@Singleton
public class GameProvider<Game> extends DatabaseProvider {
    @UnitOfWork
    public Optional<Game> findAllGames()
    {
        Query query = getEntityManager().createQuery("SELECT * FROM Game");
        return getSingleResult(query);
    }
}