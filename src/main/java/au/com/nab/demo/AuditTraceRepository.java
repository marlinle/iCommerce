package au.com.nab.demo;

import au.com.nab.demo.domain.LogEntry;
import au.com.nab.demo.domain.LogSeverityEnum;
import au.com.nab.demo.repo.LogEntryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class AuditTraceRepository implements HttpTraceRepository {

    @Autowired
    LogEntryRepository logRepo;

    @Autowired
    ObjectMapper objectMapper;

    AtomicReference<HttpTrace> lastTrace = new AtomicReference<>();

    public List<HttpTrace> findAll() {
        return Collections.singletonList(lastTrace.get());
    }

    public void add(HttpTrace trace) {
        lastTrace.set(trace);

        LogEntry log = new LogEntry();
        log.setService("product");
        log.setTimestamp(trace.getTimestamp().toString());
        log.setAction(objectToJSON(trace.getRequest()));
        log.setResult(objectToJSON(trace.getResponse()));

        LogSeverityEnum severity = trace.getResponse().getStatus() < 300 ? LogSeverityEnum.INFO : LogSeverityEnum.ERROR;
        log.setSeverity(severity);

        logRepo.save(log);
    }

    private String objectToJSON(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }
}
