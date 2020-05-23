package au.com.nab.users.domain;

import lombok.Data;

import javax.persistence.*;

@Data
public class LogEntry {
    @Id
    @GeneratedValue
    private Integer id;

    @Column
//    @Temporal(TemporalType.TIMESTAMP)
    private String timestamp;

    @Column
    @Enumerated
    private LogSeverityEnum severity;

    @Column
    private String service;

    @Column(length = 2000)
    private String action;

    @Column(length = 2000)
    private String result;
}
