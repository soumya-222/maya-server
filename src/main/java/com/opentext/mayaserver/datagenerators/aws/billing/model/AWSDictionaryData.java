package com.opentext.mayaserver.datagenerators.aws.billing.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AWSDictionaryData {
    private static final Logger logger = LoggerFactory.getLogger(AWSDictionaryData.class);

    private AWSDictionary awsDictionary;
    private String schema;

    public AWSDictionaryData(AWSDictionary awsDictionary, String schema) {
        this.awsDictionary = awsDictionary;
        this.schema = schema;
    }

    public AWSDictionary getAwsBillDictionary() {
        return awsDictionary;
    }

    public String getSchema() {
        return schema;
    }

    public static AWSDictionaryData[] schemaToDataDictionaryArray(String[] schema) {
        AWSDictionaryData[] result = new AWSDictionaryData[schema.length];
        for (int i = 0; i < schema.length; i++) {
            AWSDictionary awsDictionary = null;
            try {
                awsDictionary = AWSDictionary.get(schema[i]);
            } catch (IllegalArgumentException e) {
                logger.trace("ignoring unknown column in aws report csv '{}'", schema[i]);
            }
            result[i] = new AWSDictionaryData(awsDictionary, schema[i]);
        }
        return result; // likely sparse, in order as determined by passed-in schema
    }
}
