package bibliomar.bibliomarserver.service.statistics;

import bibliomar.bibliomarserver.model.metadata.Metadata;
import bibliomar.bibliomarserver.model.statistics.Statistics;
import bibliomar.bibliomarserver.repository.statistics.StatisticsRepository;
import bibliomar.bibliomarserver.service.metadata.MetadataService;
import bibliomar.bibliomarserver.utils.contants.Topics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;

    private final MetadataService metadataService;

    @Autowired
    public StatisticsService(StatisticsRepository statisticsRepository, MetadataService metadataService) {
        this.statisticsRepository = statisticsRepository;
        this.metadataService = metadataService;
    }

    @Async
    public CompletableFuture<Metadata> getMetadata(String MD5, Topics topic) throws ExecutionException, InterruptedException {
        Metadata metadata;
        if (topic == Topics.fiction) {
            metadata = metadataService.getFictionMetadata(MD5).get();
        } else {
            metadata = metadataService.getScitechMetadata(MD5).get();
        }
        return CompletableFuture.completedFuture(metadata);
    }

    @Async
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
    public CompletableFuture<List<Statistics>> getTopByViews(int limit) {
        Pageable pageRequest = PageRequest.of(0, limit);
        Slice<Statistics> statisticsSlice = statisticsRepository.findAllByOrderByNumOfViewsDesc(pageRequest);
        List<Statistics> statisticsList = statisticsSlice.getContent();
        return CompletableFuture.completedFuture(statisticsList);
    }

    @Async
    public CompletableFuture<List<Statistics>> getTopByViews(Topics topic, int limit) {
        Pageable pageRequest = PageRequest.of(0, limit);
        Slice<Statistics> statisticsSlice = statisticsRepository.findAllByTopicOrderByNumOfViewsDesc(topic, pageRequest);
        List<Statistics> statisticsList = statisticsSlice.getContent();
        return CompletableFuture.completedFuture(statisticsList);
    }
}
