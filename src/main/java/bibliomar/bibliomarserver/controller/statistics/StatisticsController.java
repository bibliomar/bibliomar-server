package bibliomar.bibliomarserver.controller.statistics;

import bibliomar.bibliomarserver.model.statistics.Statistics;
import bibliomar.bibliomarserver.service.statistics.StatisticsService;
import bibliomar.bibliomarserver.utils.MD5;
import bibliomar.bibliomarserver.utils.contants.Topics;
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
        List<Statistics> top = statisticsService.getTopByViews(limit).get();
        return top;
    }


    @PostMapping("/{topic}/{MD5}/views")
    public void incrementViews(MD5 MD5, @PathVariable Topics topic) throws ExecutionException, InterruptedException {
        statisticsService.incrementViews(MD5.getMD5(), topic).get();
    }


}
