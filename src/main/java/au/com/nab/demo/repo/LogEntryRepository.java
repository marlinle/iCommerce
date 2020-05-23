package au.com.nab.demo.repo;

import au.com.nab.demo.domain.LogEntry;
import org.springframework.data.repository.CrudRepository;

public interface LogEntryRepository extends CrudRepository<LogEntry, Integer> {
}
