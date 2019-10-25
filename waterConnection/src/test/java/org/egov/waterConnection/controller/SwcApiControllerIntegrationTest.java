package org.egov.waterConnection.controller;

import java.math.BigDecimal;
import org.egov.waterConnection.model.ErrorRes;
import org.egov.waterConnection.model.RequestInfo;
import org.egov.waterConnection.model.ResponseInfo;
import org.egov.waterConnection.model.SewerageConnectionRequest;
import org.egov.waterConnection.model.SewerageConnectionResponse;

import java.util.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SwcApiControllerIntegrationTest {

    @Autowired
    private SwcApi api;

    @Test
    public void swcCancelPostTest() throws Exception {
        RequestInfo body = new RequestInfo();
        String tenantId = "tenantId_example";
        String connectionNo = "connectionNo_example";
        ResponseEntity<ResponseInfo> responseEntity = api.swcCancelPost(body, tenantId, connectionNo);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
    }

    @Test
    public void swcCreatePostTest() throws Exception {
        SewerageConnectionRequest body = new SewerageConnectionRequest();
        ResponseEntity<SewerageConnectionResponse> responseEntity = api.swcCreatePost(body);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
    }

    @Test
    public void swcDeletePostTest() throws Exception {
        RequestInfo body = new RequestInfo();
        String tenantId = "tenantId_example";
        String connectionNo = "connectionNo_example";
        ResponseEntity<ResponseInfo> responseEntity = api.swcDeletePost(body, tenantId, connectionNo);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
    }

    @Test
    public void swcSearchPostTest() throws Exception {
        RequestInfo body = new RequestInfo();
        String tenantId = "tenantId_example";
        List<String> ids = Arrays.asList("ids_example");
        List<String> connectionNo = Arrays.asList("connectionNo_example");
        List<String> oldConnectionNo = Arrays.asList("oldConnectionNo_example");
        Long mobileNumber = 789L;
        BigDecimal fromDate = new BigDecimal();
        BigDecimal toDate = new BigDecimal();
        ResponseEntity<SewerageConnectionResponse> responseEntity = api.swcSearchPost(body, tenantId, ids, connectionNo, oldConnectionNo, mobileNumber, fromDate, toDate);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
    }

    @Test
    public void swcUpdatePostTest() throws Exception {
        SewerageConnectionRequest body = new SewerageConnectionRequest();
        ResponseEntity<SewerageConnectionResponse> responseEntity = api.swcUpdatePost(body);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
    }

}
