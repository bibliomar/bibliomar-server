package bibliomar.bibliomarserver.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Method;

public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    /*
    This method is used to handle async calls to void methods.
    However, you should avoid using async void methods, if possible, since their exceptios are handled silently.
    Async void methods ignore ResponseStatusExceptions, for example.
    Use CompletableFuture<Void> and return a CompletableFuture.completedFuture(null) instead.
     */
    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
        throwable.printStackTrace();
    }
}
