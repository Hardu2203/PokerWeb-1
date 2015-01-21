package filters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.*;

/**
 * Created by Eduan on 2015-01-16.
 */

@Singleton
public class SecureFilter implements Filter {
    public static final String USERNAME = "username";

    @Inject private Router router;

    @Override
    public Result filter(FilterChain filterChain, Context context) {
        if(context.getSession() == null || context.getSession().get(USERNAME) == null)
        {
            context.getFlashScope().error("User must login first.");
            return Results.html().redirect("index");
        }
        else {
            return  filterChain.next(context);
        }
    }
}
