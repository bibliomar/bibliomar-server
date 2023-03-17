package bibliomar.bibliomarserver.controllers;

import bibliomar.bibliomarserver.models.statistics.Statistics;
import bibliomar.bibliomarserver.services.StatisticsService;
import bibliomar.bibliomarserver.utils.MD5;
import bibliomar.bibliomarserver.utils.constants.Topics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/top")
    public List<Statistics> getTop(@RequestParam(name = "limit", defaultValue = "10") int limit) throws ExecutionException, InterruptedException {
        List<Statistics> top = statisticsService.getTopByViewsAndDownloads(limit).get();
        return top;
    }

    @GetMapping("/top/views")
    public List<Statistics> getTopByViews(@RequestParam(name = "limit", defaultValue = "10") int limit) throws ExecutionException, InterruptedException {
        List<Statistics> top = statisticsService.getTopByViews(limit).get();
        return top;
    }

    @GetMapping("/top/downloads")
    public List<Statistics> getTopByDownloads(@RequestParam(name = "limit", defaultValue = "10") int limit) throws ExecutionException, InterruptedException {
        List<Statistics> top = statisticsService.getTopByDownloads(limit).get();
        return top;
    }

    @GetMapping("/{topic}/{MD5}")
    public Statistics getStatistics(MD5 MD5, @PathVariable Topics topic) throws ExecutionException, InterruptedException {
        Statistics statistics = statisticsService.getStatistics(MD5.getMD5(), topic).get();
        return statistics;
    }

    @PostMapping("/{topic}/{MD5}/views")
    public void incrementViews(MD5 MD5, @PathVariable Topics topic) throws ExecutionException, InterruptedException {
        statisticsService.incrementViews(MD5.getMD5(), topic).get();
    }

    @PostMapping("/{topic}/{MD5}/downloads")
    public void incrementDownloads(MD5 MD5, @PathVariable Topics topic) throws ExecutionException, InterruptedException {
        statisticsService.incrementDownloads(MD5.getMD5(), topic).get();
    }


}
