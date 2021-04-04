import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import models.Purchase;
import worker.DbWriterRunnable;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Basic {

    private static final int numThreads = 8;

    public static void main(String[] args) throws IOException, TimeoutException {
        for (int i=0; i<numThreads; i++){
            DbWriterRunnable runnable = new DbWriterRunnable();
            Thread t = new Thread (runnable);
            t.start();
        }
    }

}
