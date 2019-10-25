package org.egov.waterConnection.controller;

import java.math.BigDecimal;
import org.egov.waterConnection.model.ErrorRes;
import org.egov.waterConnection.model.RequestInfo;
import org.egov.waterConnection.model.ResponseInfo;
import org.egov.waterConnection.model.SewerageConnectionRequest;
import org.egov.waterConnection.model.SewerageConnectionResponse;
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
public class SwcApiController implements SwcApi {

    private static final Logger log = LoggerFactory.getLogger(SwcApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public SwcApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<ResponseInfo> swcCancelPost(@ApiParam(value = "Request header for the property delete Request." ,required=true )  @Valid @RequestBody RequestInfo body,@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,@NotNull @Size(min=4,max=128) @ApiParam(value = "The properrtyId to be deactivated", required = true) @Valid @RequestParam(value = "connectionNo", required = true) String connectionNo) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<ResponseInfo>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<SewerageConnectionResponse> swcCreatePost(@ApiParam(value = "Details for the new Sewerage Connection + RequestHeader meta data." ,required=true )  @Valid @RequestBody SewerageConnectionRequest body) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<SewerageConnectionResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<ResponseInfo> swcDeletePost(@ApiParam(value = "Request header for the connection delete Request." ,required=true )  @Valid @RequestBody RequestInfo body,@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,@NotNull @Size(min=2,max=64) @ApiParam(value = "The connection no to be deactivated", required = true) @Valid @RequestParam(value = "connectionNo", required = true) String connectionNo) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<ResponseInfo>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<SewerageConnectionResponse> swcSearchPost(@ApiParam(value = "RequestHeader meta data." ,required=true )  @Valid @RequestBody RequestInfo body,@NotNull @ApiParam(value = "Unique id for a tenant.", required = true) @Valid @RequestParam(value = "tenantId", required = true) String tenantId,@ApiParam(value = "List of system generated ids of Sewerage connection.") @Valid @RequestParam(value = "ids", required = false) List<String> ids,@ApiParam(value = "List of Sewerage connection numbers to search..") @Valid @RequestParam(value = "connectionNo", required = false) List<String> connectionNo,@ApiParam(value = "List of old Sewerage connection numbers to search..") @Valid @RequestParam(value = "oldConnectionNo", required = false) List<String> oldConnectionNo,@ApiParam(value = "MobileNumber of owner whose Sewerage connection is to be searched.") @Valid @RequestParam(value = "mobileNumber", required = false) Long mobileNumber,@ApiParam(value = "Fetches Sewerage Connection with created time after fromDate.") @Valid @RequestParam(value = "fromDate", required = false) BigDecimal fromDate,@ApiParam(value = "Fetches Sewerage Connection with created time till toDate.") @Valid @RequestParam(value = "toDate", required = false) BigDecimal toDate) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<SewerageConnectionResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<SewerageConnectionResponse> swcUpdatePost(@ApiParam(value = "Request of Sewerage connection details." ,required=true )  @Valid @RequestBody SewerageConnectionRequest body) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<SewerageConnectionResponse>(HttpStatus.NOT_IMPLEMENTED);
    }

}
