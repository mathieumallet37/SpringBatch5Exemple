package com.example.springBatch.config;

import com.example.springBatch.listener.JobCompletionNotificationListener;
import com.example.springBatch.listener.PersonItemReadListener;
import com.example.springBatch.listener.PersonItemWriteListener;
import com.example.springBatch.model.Personnel;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class JobConfiguration {

    private static final String INSERT_QUERY = """
      insert into personne (prenom, nom, age, is_active, date_creation)
      values (:prenom,:nom,:age,:active,:date)""";

    private final JobRepository jobRepository;

    public JobConfiguration(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Value("classpath:csv/data.csv")
    private Resource inputFeed;

    @Bean(name="insertIntoDbFromCsvJob")
    public Job insertIntoDbFromCsvJob(Step step1) {

        var name = "Persons Import Job";
        var builder = new JobBuilder(name, jobRepository);
        return builder.start(step1)
                .listener(new JobCompletionNotificationListener())
                .build();
    }

    @Bean
    public Step step1(ItemReader<Personnel> reader,
                      ItemWriter<Personnel> writer,
                      PlatformTransactionManager txManager) {
        var name = "INSERTION CSV DANS DB Step";
        var builder = new StepBuilder(name, jobRepository);
        return builder
                .<Personnel, Personnel>chunk(5, txManager)
                .reader(reader)
                .listener(new PersonItemReadListener())
                .writer(writer)
                .listener(new PersonItemWriteListener())
                .build();
    }

    @Bean
    public FlatFileItemReader<Personnel> csvFileReader(
            LineMapper<Personnel> lineMapper) {
        var itemReader = new FlatFileItemReader<Personnel>();
        itemReader.setLineMapper(lineMapper);
        itemReader.setResource(inputFeed);
        return itemReader;
    }

    @Bean
    public DefaultLineMapper<Personnel> lineMapper(LineTokenizer tokenizer,
                                                   FieldSetMapper<Personnel> mapper) {
        var lineMapper = new DefaultLineMapper<Personnel>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(mapper);
        return lineMapper;
    }

    @Bean
    public BeanWrapperFieldSetMapper<Personnel> fieldSetMapper() {
        var fieldSetMapper = new BeanWrapperFieldSetMapper<Personnel>();
        fieldSetMapper.setTargetType(Personnel.class);

        return fieldSetMapper;
    }




    @Bean
    public DelimitedLineTokenizer tokenizer() {
        var tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames("prenom", "nom", "age", "active", "date");
        return tokenizer;
    }

    @Bean
    public JdbcBatchItemWriter<Personnel> jdbcItemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Personnel>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql(INSERT_QUERY)
                .dataSource(dataSource)
                .build();
    }

}
