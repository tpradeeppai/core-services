package org.egov.pg.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.HashMap;
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


}
