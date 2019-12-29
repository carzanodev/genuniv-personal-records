package carzanodev.genuniv.microservices.precord.cache;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import carzanodev.genuniv.microservices.common.util.cache.CacheContext;
import carzanodev.genuniv.microservices.common.util.cache.DataCache;
import carzanodev.genuniv.microservices.common.util.cache.PeriodicCacheLoader;
import carzanodev.genuniv.microservices.precord.cache.model.CollegeDTO;
import carzanodev.genuniv.microservices.precord.cache.model.DegreeDTO;
import carzanodev.genuniv.microservices.precord.config.LoadingCacheProperties;

@Component
public class GeneralCacheContext implements CacheContext {

    public static Map<Integer, CollegeDTO> COLLEGE = new HashMap<>();
    public static Map<Integer, DegreeDTO> DEGREE = new HashMap<>();

    private CollegeApiCache collegeCache;
    private DegreeApiCache degreeCache;

    private PeriodicCacheLoader<CollegeApiCache> collegeLoader;
    private PeriodicCacheLoader<DegreeApiCache> degreeLoader;

    private int slowLoadInterval;
    private int fastLoadInterval;

    private static final int FORCE_LOAD_INTERVAL = 300;

    @Autowired
    public GeneralCacheContext(CollegeApiCache collegeCache, DegreeApiCache degreeCache,
                               LoadingCacheProperties loadingCacheProperties) {
        this.collegeCache = collegeCache;
        this.degreeCache = degreeCache;

        this.slowLoadInterval = loadingCacheProperties.getSlowInterval();
        this.fastLoadInterval = loadingCacheProperties.getFastInterval();

    }

    @PostConstruct
    private void init() {
        this.collegeCache.registerContext(this);
        this.degreeCache.registerContext(this);

        this.collegeLoader = new PeriodicCacheLoader<>(collegeCache, slowLoadInterval, FORCE_LOAD_INTERVAL);
        this.degreeLoader = new PeriodicCacheLoader<>(degreeCache, slowLoadInterval, FORCE_LOAD_INTERVAL);

        this.collegeLoader.start();
        this.degreeLoader.start();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadToContext(DataCache cacheInstance) {
        if (cacheInstance instanceof CollegeApiCache) {
            COLLEGE = cacheInstance.getCache();
        } else if (cacheInstance instanceof DegreeApiCache) {
            DEGREE = cacheInstance.getCache();
        }
    }

    @PreDestroy
    private void endSafe() {
        this.collegeLoader.stop(true);
        this.degreeLoader.stop(true);
    }

}
