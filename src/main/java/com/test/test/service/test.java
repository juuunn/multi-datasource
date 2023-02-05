// package com.test.test.service;

// import javax.sql.DataSource;

// import org.springframework.batch.core.Job;
// import org.springframework.batch.core.Step;
// import
// org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
// import
// org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
// import
// org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
// import org.springframework.batch.core.launch.support.RunIdIncrementer;
// import
// org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
// import org.springframework.batch.item.database.JdbcBatchItemWriter;
// import org.springframework.batch.item.database.JdbcCursorItemReader;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// @Configuration
// @EnableBatchProcessing
// public class test {

// @Autowired
// private JobBuilderFactory jobBuilderFactory;

// @Autowired
// private StepBuilderFactory stepBuilderFactory;

// @Autowired
// private DataSource sourceDataSource;

// @Autowired
// private DataSource targetDataSource;

// @Bean
// public JdbcCursorItemReader<Person> reader() {
// JdbcCursorItemReader<Person> reader = new JdbcCursorItemReader<>();
// reader.setDataSource(sourceDataSource);
// reader.setSql("SELECT id, name, age FROM person");
// reader.setRowMapper(new PersonRowMapper());
// return reader;
// }

// @Bean
// public PersonItemProcessor processor() {
// return new PersonItemProcessor();
// }

// @Bean
// public JdbcBatchItemWriter<Person> writer() {
// JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<>();
// writer.setDataSource(targetDataSource);
// writer.setSql("INSERT INTO person (id, name, age) VALUES (:id, :name,
// :age)");
// writer.setItemSqlParameterSourceProvider(new
// BeanPropertyItemSqlParameterSourceProvider<>());
// return writer;
// }

// @Bean
// public Job migrateJob(JobCompletionNotificationListener listener) {
// return jobBuilderFactory.get("migrateJob")
// .incrementer(new RunIdIncrementer())
// .listener(listener)
// .flow(step1())
// .end()
// .build();
// }

// @Bean
// public Step step1() {
// return stepBuilderFactory.get("step1")
// .<Person, Person>chunk(1000)
// .reader(reader())
// .processor(processor())
// .writer(writer())
// .build();
// }
// }
