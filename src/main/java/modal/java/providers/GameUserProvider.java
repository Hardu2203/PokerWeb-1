package modal.java.providers;

import com.google.inject.Singleton;
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
}
