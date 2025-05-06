/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package infrastructure;

import infrastructure.repository.RepositoryException;
import java.util.concurrent.Callable;
import javax.persistence.OptimisticLockException;

/**
 *
 * @author zymci
 */
public class TransactionRetryHelper {
    private static final int MAX_RETRIES = 3;
    
    public static <T> T executeWithRetry(Callable<T> operation) {
        int retryCount = 0;
        while (retryCount < MAX_RETRIES) {
            try {
                return operation.call();
            } catch (OptimisticLockException e) {
                retryCount++;
                if (retryCount >= MAX_RETRIES) {
                    throw new RepositoryException("Operation failed after " + MAX_RETRIES + " attempts", e);
                }
                // Logika backoff
                try {
                    Thread.sleep(100 * retryCount);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RepositoryException("Thread interrupted during retry", ie);
                }
            } catch (Exception e) {
                throw new RepositoryException("Operation failed", e);
            }
        }
        throw new RepositoryException("Operation failed after " + MAX_RETRIES + " attempts");
    }
}
