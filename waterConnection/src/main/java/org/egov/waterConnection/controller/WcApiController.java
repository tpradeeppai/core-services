package org.egov.waterConnection.controller;

import java.math.BigDecimal;
import org.egov.waterConnection.model.ErrorRes;
import org.egov.waterConnection.model.RequestInfo;
import org.egov.waterConnection.model.ResponseInfo;
import org.egov.waterConnection.model.WaterConnectionRequest;
import org.egov.waterConnection.model.WaterConnectionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-24T11:22:24.450+05:30[Asia/Kolkata]")
@Controller
public class WcApiController implements WcApi {

    private static final Logger log = LoggerFactory.getLogger(WcApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public WcApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<ResponseInfo> wcCancelPost(@ApiParam(value = "Request header for the property delete Request." ,required=true )  @Valid @RequestBody RequestInfo body,@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,@NotNull @Size(min=4,max=128) @ApiParam(value = "The properrtyId to be deactivated", required = true) @Valid @RequestParam(value = "propertyId", required = true) String propertyId) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<ResponseInfo>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<WaterConnectionResponse> wcCreatePost(@ApiParam(value = "Details for the new property + RequestHeader meta data." ,required=true )  @Valid @RequestBody WaterConnectionRequest body) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<WaterConnectionResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<ResponseInfo> wcDeletePost(@ApiParam(value = "Request header for the connection delete Request." ,required=true )  @Valid @RequestBody RequestInfo body,@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,@NotNull @Size(min=2,max=64) @ApiParam(value = "The connection no to be deactivated", required = true) @Valid @RequestParam(value = "connectionNo", required = true) String connectionNo) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<ResponseInfo>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<WaterConnectionResponse> wcSearchPost(@ApiParam(value = "RequestHeader meta data." ,required=true )  @Valid @RequestBody RequestInfo body,@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,@ApiParam(value = "List of system generated ids of water connection.") @Valid @RequestParam(value = "ids", required = false) List<String> ids,@ApiParam(value = "List of water connection numbers to search..") @Valid @RequestParam(value = "connectionNo", required = false) List<String> connectionNo,@ApiParam(value = "List of old water connection numbers to search..") @Valid @RequestParam(value = "oldConnectionNo", required = false) List<String> oldConnectionNo,@ApiParam(value = "MobileNumber of owner whose water connection is to be searched.") @Valid @RequestParam(value = "mobileNumber", required = false) Long mobileNumber,@ApiParam(value = "Fetches properties with created time after fromDate.") @Valid @RequestParam(value = "fromDate", required = false) BigDecimal fromDate,@ApiParam(value = "Fetches properties with created time till toDate.") @Valid @RequestParam(value = "toDate", required = false) BigDecimal toDate) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<WaterConnectionResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<WaterConnectionResponse> wcUpdatePost(@ApiParam(value = "Request of water connection details." ,required=true )  @Valid @RequestBody WaterConnectionRequest body) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<WaterConnectionResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

}
