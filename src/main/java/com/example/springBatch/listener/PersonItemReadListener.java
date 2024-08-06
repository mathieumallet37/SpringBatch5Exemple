package com.example.springBatch.listener;

import com.example.springBatch.model.Personnel;
import com.example.springBatch.utils.LoggerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

public class PersonItemReadListener extends LoggerConstants implements ItemReadListener<Personnel> {

    public static final Logger logger = LoggerFactory.getLogger(PersonItemReadListener.class);

    @Override
    public void beforeRead() {
        logger.info(LoggerConstants.READING_NEW_PERSON_RECORD);
    }

    @Override
    public void afterRead(Personnel input) {
        logger.info(LoggerConstants.NEW_PERSON_RECORD_READ + "{}", input);
    }

    @Override
    public void onReadError(Exception e) {
        logger.error(LoggerConstants.ERROR_IN_READING_THE_PERSON_RECORD + "{}", e);
    }
}
