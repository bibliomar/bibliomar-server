package bibliomar.bibliomarserver.services.impl;

import bibliomar.bibliomarserver.models.metadata.Metadata;
import bibliomar.bibliomarserver.models.statistics.Statistics;
import bibliomar.bibliomarserver.repositories.StatisticsRepository;
import bibliomar.bibliomarserver.services.MetadataService;
import bibliomar.bibliomarserver.services.StatisticsService;
import bibliomar.bibliomarserver.utils.constants.Topics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class StatisticServiceImpl implements StatisticsService {

    private final StatisticsRepository statisticsRepository;
    private final MetadataService metadataService;

    @Autowired
    public StatisticServiceImpl(StatisticsRepository statisticsRepository, MetadataService metadataService) {
        this.statisticsRepository = statisticsRepository;
        this.metadataService = metadataService;
    }

    @Async
    @Override
    public CompletableFuture<Metadata> getMetadata(String MD5, Topics topic) throws ExecutionException, InterruptedException {
        Metadata metadata;
        if (topic == Topics.fiction) {
            metadata = metadataService.getFictionMetadata(MD5).get();
        } else {
            metadata = metadataService.getScitechMetadata(MD5).get();
        }

        if (metadata == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Metadata not found");
        }

        return CompletableFuture.completedFuture(metadata);
    }

    @Async
    @Override
    public CompletableFuture<Void> incrementViews(String MD5, Topics topic) throws ExecutionException, InterruptedException {
        Metadata metadata = this.getMetadata(MD5, topic).get();
        Statistics statistics = statisticsRepository.findByMD5AndTopic(metadata.getMD5(), metadata.getTopic());
        if (statistics == null) {
            Statistics newStatistics = Statistics.build(metadata);
            newStatistics.incrementViews();
            statisticsRepository.save(newStatistics);
        } else {
            statistics.incrementViews();
            statisticsRepository.save(statistics);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async
    @Override
    public CompletableFuture<Void> incrementDownloads(String MD5, Topics topic) throws ExecutionException, InterruptedException {
        Statistics statistics = statisticsRepository.findByMD5AndTopic(MD5, topic);
        if (statistics == null) {
            Metadata metadata = this.getMetadata(MD5, topic).get();
            Statistics newStatistics = Statistics.build(metadata);
            newStatistics.incrementDownloads();
            statisticsRepository.save(newStatistics);
        } else {
            statistics.incrementDownloads();
            statisticsRepository.save(statistics);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async
    @Override
    public CompletableFuture<Statistics> getStatistics(String MD5, Topics topic) throws ExecutionException, InterruptedException {
        Statistics statistics = statisticsRepository.findByMD5AndTopic(MD5, topic);
        if (statistics == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No statistics entry for the given metadata.");
        }
        return CompletableFuture.completedFuture(statistics);
    }

    @Async
    @Override
    public CompletableFuture<List<Statistics>> getTopByViewsAndDownloads(int limit) {
        Pageable pageRequest = PageRequest.of(0, limit);
        Slice<Statistics> statisticsSlice;
        statisticsSlice = statisticsRepository.findAllByOrderByNumOfViewsDescNumOfDownloadsDesc(pageRequest);

        List<Statistics> statisticsList = statisticsSlice.getContent();
        return CompletableFuture.completedFuture(statisticsList);
    }

    @Async
    @Override
    public CompletableFuture<List<Statistics>> getTopByViews(int limit) {
        Pageable pageRequest = PageRequest.of(0, limit);
        Slice<Statistics> statisticsSlice;
        statisticsSlice = statisticsRepository.findAllByOrderByNumOfViewsDesc(pageRequest);

        List<Statistics> statisticsList = statisticsSlice.getContent();
        return CompletableFuture.completedFuture(statisticsList);
    }

    @Async
    @Override
    public CompletableFuture<List<Statistics>> getTopByDownloads(int limit) {
        Pageable pageRequest = PageRequest.of(0, limit);
        Slice<Statistics> statisticsSlice;

        statisticsSlice = statisticsRepository.findAllByOrderByNumOfDownloadsDesc(pageRequest);

        List<Statistics> statisticsList = statisticsSlice.getContent();
        return CompletableFuture.completedFuture(statisticsList);
    }
    
}
