package com.zensar.uac.web.crawler.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by kiran.govind on 07/20/2016.
 * Purpose of the class: This is converter to convert LocalDateTime in java 8 to SQL TimeStamp
 * Sponsor: www.zensar.com
 * License: This code is released under GPL. You are free to make changes to the code as long as you provide
 * attribution to the Sponsor.
 */
@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(LocalDateTime entityValue) {
        return (entityValue == null ? null : Timestamp.valueOf(entityValue));
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Timestamp databaseValue) {
        return (databaseValue == null ? null : databaseValue.toLocalDateTime());
    }
}
