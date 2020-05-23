package au.com.nab.users;

import au.com.nab.users.domain.LogEntry;
import au.com.nab.users.domain.LogSeverityEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class AuditTraceRepository implements HttpTraceRepository {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RestTemplate restTemplate;

    @Value("${audit.log.service.url}")
    String logServiceUrl;

    AtomicReference<HttpTrace> lastTrace = new AtomicReference<>();

    public List<HttpTrace> findAll() {
        return Collections.singletonList(lastTrace.get());
    }

    public void add(HttpTrace trace) {
        lastTrace.set(trace);

        LogEntry log = new LogEntry();
        log.setService("user");
        log.setTimestamp(trace.getTimestamp().toString());
        log.setAction(objectToJSON(trace.getRequest()));
        log.setResult(objectToJSON(trace.getResponse()));

        LogSeverityEnum severity = trace.getResponse().getStatus() < 300 ? LogSeverityEnum.INFO : LogSeverityEnum.ERROR;
        log.setSeverity(severity);

        HttpEntity<LogEntry> request = new HttpEntity<>(log);
        restTemplate.postForLocation(logServiceUrl, request);
    }

    private String objectToJSON(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }
}
