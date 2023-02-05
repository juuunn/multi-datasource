package com.test.test.service;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class testService {

    private final JdbcTemplate sourceJdbcTemplate;
    private final JdbcTemplate targetJdbcTemplate;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    public testService(JdbcTemplate sourceJdbcTemplate, JdbcTemplate targetJdbcTemplate) {
        this.sourceJdbcTemplate = sourceJdbcTemplate;
        this.targetJdbcTemplate = targetJdbcTemplate;
    }

    @StepScope
    @Bean
    public JdbcCursorItemReader<DataObject> dataObjectReader() {
        JdbcCursorItemReader<DataObject> reader = new JdbcCursorItemReader<>();
        log.info("READ!!");
        reader.setDataSource(sourceJdbcTemplate.getDataSource());
        reader.setSql("SELECT * FROM test2");
        reader.setRowMapper(new BeanPropertyRowMapper<>(DataObject.class));
        log.info("reader!! : {}", reader);
        return reader;
    }

    @StepScope
    @Bean
    public ItemWriter<DataObject> dataObjectWriter() {
        log.info("ItemWritetr!!!!");
        return new ItemWriter<DataObject>() {

            @Override
            public void write(List<? extends DataObject> dataObjects) throws Exception {
                log.info("WRITE!! : {}", dataObjects);
                targetJdbcTemplate.batchUpdate("INSERT INTO test3 (code, vin, ymd, col2) VALUES (?, ?, ?, ?)",
                        new BatchPreparedStatementSetter() {
                            @Override
                            public void setValues(PreparedStatement ps, int i) throws SQLException {
                                DataObject dataObject = dataObjects.get(i);
                                ps.setString(1, dataObject.getCode());
                                ps.setString(2, dataObject.getVin());
                                ps.setString(3, dataObject.getYmd());
                                ps.setString(4, dataObject.getCol2());
                                // set values for other columns as well
                            }

                            @Override
                            public int getBatchSize() {
                                log.info("SIZE: {}", dataObjects.size());
                                return dataObjects.size();
                            }
                        });
            }
        };
    }

    @Bean
    public Step dataMigrationStep() {
        return stepBuilderFactory.get("dataMigrationStep")
                .<DataObject, DataObject>chunk(100)
                .reader(dataObjectReader())
                .writer(dataObjectWriter())
                .build();
    }

    @Bean
    public Job dataMigrationJob() {
        return jobBuilderFactory.get("dataMigrationJob")
                .start(dataMigrationStep())
                .build();
    }
}