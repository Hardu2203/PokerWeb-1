package modal.java.providers;

import com.google.inject.Singleton;
import ninja.jpa.UnitOfWork;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

/**
 * Created by Eduan on 2015-01-20.
 */
@Singleton
public class GameProvider<Game> extends DatabaseProvider {
    @UnitOfWork
    public List<Game> findAllGames()
    {
        Query query = getEntityManager().createQuery("SELECT g FROM Game g");
        return query.getResultList();
    }
}