package ru.practicum.ewm.stats;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.stats.client.StatsClient;

@Service
@Getter
@Setter
public class StatsClientProvider {
    private final StatsClient client;

    @Autowired
    public StatsClientProvider(@Value("${stats-server.url}") String url) {
        client = new StatsClient(url);
    }

    public final StatsClient getClient() {
        return client;
    }
}
