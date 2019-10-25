package org.egov.waterConnection.controller;

import java.math.BigDecimal;
import org.egov.waterConnection.model.ErrorRes;
import org.egov.waterConnection.model.RequestInfo;
import org.egov.waterConnection.model.ResponseInfo;
import org.egov.waterConnection.model.WaterConnectionRequest;
import org.egov.waterConnection.model.WaterConnectionResponse;

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
public class WcApiControllerIntegrationTest {

    @Autowired
    private WcApi api;

    @Test
    public void wcCancelPostTest() throws Exception {
        RequestInfo body = new RequestInfo();
        String tenantId = "tenantId_example";
        String propertyId = "propertyId_example";
        ResponseEntity<ResponseInfo> responseEntity = api.wcCancelPost(body, tenantId, propertyId);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
    }

    @Test
    public void wcCreatePostTest() throws Exception {
        WaterConnectionRequest body = new WaterConnectionRequest();
        ResponseEntity<WaterConnectionResponse> responseEntity = api.wcCreatePost(body);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
    }

    @Test
    public void wcDeletePostTest() throws Exception {
        RequestInfo body = new RequestInfo();
        String tenantId = "tenantId_example";
        String connectionNo = "connectionNo_example";
        ResponseEntity<ResponseInfo> responseEntity = api.wcDeletePost(body, tenantId, connectionNo);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
    }

    @Test
    public void wcSearchPostTest() throws Exception {
        RequestInfo body = new RequestInfo();
        String tenantId = "tenantId_example";
        List<String> ids = Arrays.asList("ids_example");
        List<String> connectionNo = Arrays.asList("connectionNo_example");
        List<String> oldConnectionNo = Arrays.asList("oldConnectionNo_example");
        Long mobileNumber = 789L;
        BigDecimal fromDate = new BigDecimal();
        BigDecimal toDate = new BigDecimal();
        ResponseEntity<WaterConnectionResponse> responseEntity = api.wcSearchPost(body, tenantId, ids, connectionNo, oldConnectionNo, mobileNumber, fromDate, toDate);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
    }

    @Test
    public void wcUpdatePostTest() throws Exception {
        WaterConnectionRequest body = new WaterConnectionRequest();
        ResponseEntity<WaterConnectionResponse> responseEntity = api.wcUpdatePost(body);
        assertEquals(HttpStatus.NOT_IMPLEMENTED, responseEntity.getStatusCode());
    }

}
