package bibliomar.bibliomarserver.services;

import bibliomar.bibliomarserver.models.metadata.Metadata;
import bibliomar.bibliomarserver.models.statistics.Statistics;
import bibliomar.bibliomarserver.utils.contants.Topics;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface StatisticsService {

    public CompletableFuture<Metadata> getMetadata(String MD5, Topics topic) throws ExecutionException, InterruptedException;
    public CompletableFuture<Void> incrementViews(String MD5, Topics topic) throws ExecutionException, InterruptedException;
    public CompletableFuture<Void> incrementDownloads(String MD5, Topics topic) throws ExecutionException, InterruptedException;
    public CompletableFuture<Statistics> getStatistics(String MD5, Topics topic) throws ExecutionException, InterruptedException;
    public CompletableFuture<List<Statistics>> getTopByViewsAndDownloads(int limit);
    public CompletableFuture<List<Statistics>> getTopByViews(int limit);
    public CompletableFuture<List<Statistics>> getTopByDownloads(int limit);
}
