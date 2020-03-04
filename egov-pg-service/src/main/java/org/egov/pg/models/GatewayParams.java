package org.egov.pg.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class GatewayParams {

    //Metdata for payment gateway
    @JsonIgnore
    private Map metaData;

    public Object get(String key) throws Exception {
        if (metaData.containsKey(key)) {
            return metaData.get(key);
        }
        //TODO
        throw new Exception ("TODO: Update this");
    }
}
