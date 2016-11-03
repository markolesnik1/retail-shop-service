package com.retail.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public abstract class DomainBase implements Serializable {
    private static ObjectWriter writer =
            (new ObjectMapper()).setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .writer().withDefaultPrettyPrinter();

    private static Logger log = LoggerFactory.getLogger(DomainBase.class);

    @Override
    public String toString() {
        try {
            return "\n" + writer.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error(e.toString());
        }

        return super.toString();
    }
}
