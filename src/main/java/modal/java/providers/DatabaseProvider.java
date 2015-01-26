package modal.java.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

/**
 * Created by Eduan on 2015-01-20.
 */
@Singleton
public class DatabaseProvider<T> {

    @Inject
    private Provider<EntityManager> entityManagerProvider;

    @Transactional
    public void persist(T entity)
    {
        entityManagerProvider.get().persist(entity);
    }

    @Transactional
    public void Merge(T entity)
    {
        entityManagerProvider.get().merge(entity);
    }
    protected EntityManager getEntityManager()
    {
        return entityManagerProvider.get();
    }

    protected Optional<T> getSingleResult(Query query)
    {
        List<T> results= query.getResultList();
        if (results.isEmpty())
            return  Optional.empty();
        if (results.size()==1)
        {
            return Optional.ofNullable(results.get(0));
        }
        throw new NonUniqueResultException();
    }


}
