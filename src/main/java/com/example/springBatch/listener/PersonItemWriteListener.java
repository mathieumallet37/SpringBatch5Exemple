package com.example.springBatch.listener;


import com.example.springBatch.model.Personnel;
import com.example.springBatch.utils.LoggerConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;

@Slf4j
public class PersonItemWriteListener extends LoggerConstants implements ItemWriteListener<Personnel> {


    @Override
    public void beforeWrite(Chunk<? extends Personnel> items) {
        log.info(LoggerConstants.WRITING_STARTED_PERSONS_LIST + "{}", items);
    }

    @Override
    public void afterWrite(Chunk<? extends Personnel> items) {
        log.info(LoggerConstants.WRITING_COMPLETED_PERSONS_LIST + "{}", items);
        ;
    }

    @Override
    public void onWriteError(Exception e, Chunk<? extends Personnel> items) {
        log.error(LoggerConstants.ERROR_IN_WRITING_THE_PERSON_RECORD +"{}", items);
        log.error("Error in reading the person records " + e);
    }
}
