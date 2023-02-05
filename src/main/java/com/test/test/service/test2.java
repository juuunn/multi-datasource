// package com.test.test.service;

// import java.sql.PreparedStatement;
// import java.sql.SQLException;
// import java.util.List;

// import javax.batch.api.chunk.ItemWriter;

// import org.springframework.batch.core.Job;
// import org.springframework.batch.core.Step;
// import
// org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
// import
// org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
// import org.springframework.batch.core.configuration.annotation.StepScope;
// import org.springframework.batch.item.database.JdbcCursorItemReader;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.jdbc.core.BeanPropertyRowMapper;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.stereotype.Service;

// @Service
// public class test2 {

// private final JdbcTemplate sourceJdbcTemplate;
// private final JdbcTemplate targetJdbcTemplate;

// @Autowired
// private JobBuilderFactory jobBuilderFactory;

// @Autowired
// private StepBuilderFactory stepBuilderFactory;

// @Autowired
// public test2(JdbcTemplate sourceJdbcTemplate, JdbcTemplate
// targetJdbcTemplate) {
// this.sourceJdbcTemplate = sourceJdbcTemplate;
// this.targetJdbcTemplate = targetJdbcTemplate;
// }

// @StepScope
// @Bean
// public JdbcCursorItemReader<DataObject> dataObjectReader() {
// JdbcCursorItemReader<DataObject> reader = new JdbcCursorItemReader<>();
// reader.setDataSource(sourceJdbcTemplate.getDataSource());
// reader.setSql("SELECT * FROM source_table");
// reader.setRowMapper(new BeanPropertyRowMapper<>(DataObject.class));
// return reader;
// }

// @StepScope
// @Bean
// public ItemWriter<DataObject> dataObjectWriter() {
// return new ItemWriter<DataObject>() {
// @Override
// public void write(List<? extends DataObject> dataObjects) throws Exception {
// targetJdbcTemplate.batchUpdate("INSERT INTO target_table (column1, column2,
// ...) VALUES (?, ?, ...)",
// new BatchPreparedStatementSetter() {
// @Override
// public void setValues(PreparedStatement ps, int i) throws SQLException {
// DataObject dataObject = dataObjects.get(i);
// ps.setString(1, dataObject.getColumn1());
// ps.setString(2, dataObject.getColumn2());
// // set values for other columns as well
// }

// @Override
// public int getBatchSize() {
// return dataObjects.size();
// }
// });
// }
// };
// }

// @Bean
// public Step dataMigrationStep() {
// return stepBuilderFactory.get("dataMigrationStep")
// .<DataObject, DataObject> chunk(1000)
// .reader(dataObjectReader())
// .writer(dataObjectWriter())
// .build();
// }

// @Bean
// public Job dataMigrationJob() {
// return jobBuilderFactory.get("dataMigrationJob")
// .start(dataMigrationStep())
// .build();
// }
// }
