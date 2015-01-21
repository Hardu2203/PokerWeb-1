package modal.java.providers;

import com.google.inject.Singleton;
import ninja.jpa.UnitOfWork;
import javax.persistence.Query;
import java.util.Optional;

/**
 * Created by Eduan on 2015-01-16.
 */
@Singleton
public class UserProvider<User> extends DatabaseProvider {
    @UnitOfWork
    public Optional<User> findUserByName(String name)
    {
        Query query = getEntityManager().createQuery("SELECT u FROM User u WHERE u.name = :name").setParameter("name",name);
        return getSingleResult(query);
    }
}
