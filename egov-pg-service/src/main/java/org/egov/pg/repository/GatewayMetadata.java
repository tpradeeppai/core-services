package org.egov.pg.repository;

import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.egov.pg.config.AppProperties;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Slf4j
@Repository
public class GatewayMetadata {

    public static final String MDMS_PAYMENT_GATEWAY_MODULE = "PaymentGateway";
    public static final String MDMS_GATEWAY_MASTER = "gateways";
    public static final String MDMS_RESPONSE = "MdmsRes";
    public static final String GATEWAY_DETAILS = "serviceOverride";
    public static final String GATEWAY_NAME = "code";
    public static final String GATEWAY_DEFAULT = "default";
    public static final String GATEWAY_ENABLED = "enabled";
    private AppProperties appProperties;
    private RestTemplate restTemplate;

    @Autowired
    public GatewayMetadata(AppProperties appProperties, RestTemplate restTemplate) {
        this.appProperties = appProperties;
        this.restTemplate = restTemplate;
    }

    private MdmsCriteriaReq getMDMSRequest(RequestInfo requestInfo, String tenantId) {
        List<MasterDetail> paymentGatewayDetails = new ArrayList<>();


        paymentGatewayDetails.add(MasterDetail.builder().name(MDMS_GATEWAY_MASTER).build());

        ModuleDetail gatewayModuledetls = ModuleDetail.builder().masterDetails(paymentGatewayDetails)
                .moduleName(MDMS_PAYMENT_GATEWAY_MODULE).build();


        List<ModuleDetail> moduleDetails = new LinkedList<>();
        moduleDetails.add(gatewayModuledetls);

        MdmsCriteria mdmsCriteria = MdmsCriteria.builder().moduleDetails(moduleDetails).tenantId(tenantId)
                .build();

        MdmsCriteriaReq mdmsCriteriaReq = MdmsCriteriaReq.builder().mdmsCriteria(mdmsCriteria)
                .requestInfo(requestInfo).build();
        return mdmsCriteriaReq;
    }

    public HashMap mDMSCall(RequestInfo requestInfo, String tenantId) {

        MdmsCriteriaReq mdmsCriteriaReq = getMDMSRequest(requestInfo, tenantId);
        HashMap result = restTemplate.postForObject(getMDMSURL(), mdmsCriteriaReq, HashMap.class);
        return result;
    }

    //returns metData for gateway, tenant,module
    public Map metaData(RequestInfo requestInfo, String gateway, String tenantId, String module) throws Exception {
        Map gatewayData = mDMSCall(requestInfo, tenantId);
        List gatewayDetails = (List) ((HashMap) ((HashMap) gatewayData.get(MDMS_RESPONSE))
                .get(MDMS_PAYMENT_GATEWAY_MODULE)).get(MDMS_GATEWAY_MASTER);
        Map result = new HashMap();
        try {
            for (int i = 0; i < gatewayDetails.size(); i++) {
                //if default gateway is needed
                if (gateway.equals(GATEWAY_DEFAULT) && ((HashMap) gatewayDetails.get(i)).get(GATEWAY_DEFAULT).equals(true)
                        && ((HashMap) gatewayDetails.get(i)).get(GATEWAY_ENABLED).equals(true)) {
                    if (!((HashMap) ((HashMap) gatewayDetails.get(i)).get(GATEWAY_DETAILS)).isEmpty()) {
                        result.put(gateway, ((HashMap) ((HashMap) gatewayDetails.get(i)).get(GATEWAY_DETAILS)).get("*"));

                        if (((HashMap) ((HashMap) gatewayDetails.get(i)).get(GATEWAY_DETAILS)).containsKey(module)) {
                            result.putAll(((HashMap) ((HashMap) ((HashMap) gatewayDetails.get(i)).get(GATEWAY_DETAILS)).get(module)));
                        }
                    } else {
                        result.put(gateway, null);
                    }


                } else if (((HashMap) gatewayDetails.get(i)).get(GATEWAY_NAME).equals(gateway) && ((HashMap) gatewayDetails.get(i)).get(GATEWAY_ENABLED).equals(true)) {
                    if (!((HashMap) ((HashMap) gatewayDetails.get(i)).get(GATEWAY_DETAILS)).isEmpty()) {
                        result.put(gateway, ((HashMap) ((HashMap) gatewayDetails.get(i)).get(GATEWAY_DETAILS)).get("*"));

                        if (((HashMap) ((HashMap) gatewayDetails.get(i)).get(GATEWAY_DETAILS)).containsKey(module)) {
                            ((HashMap) result.get(gateway)).putAll(((HashMap) ((HashMap) ((HashMap) gatewayDetails.get(i)).get(GATEWAY_DETAILS)).get(module)));
                        }
                    }


                }

            }

        } catch (Exception e) {
            throw new Exception("METADATA_FETCH_ERROR", e);
        }
        if (result.size() > 1 && gateway.equals(GATEWAY_DEFAULT)) {
            log.error("Expected to find one default gateway for tenant " +
                    "{}, instead found {}", tenantId, result.size());
            throw new CustomException("GATEWAY_CONFIG_ERROR", "More than one default enabled");
        } else if (result.size() > 1) {
            log.error("Expected to find one gateway for tenant " +
                    "{}, instead found {}", tenantId, result.size());
            throw new CustomException("GATEWAY_CONFIG_ERROR", "More than one gateway definition present");
        } else if (result == null) {
            log.error("No gateway is enabled");
            throw new CustomException("GATEWAY_CONFIG_ERROR", "No enabled gateway exists");
        } else {
            log.info("metaData", result);
            return result;
        }

    }


    //Gives list of all enabled gateways
    public LinkedList listOfGateways(RequestInfo requestInfo, String tenantId) throws Exception {

        HashMap gatewayData = mDMSCall(requestInfo, tenantId);
        List paymentGateways = (List) ((HashMap) ((HashMap) gatewayData.get(MDMS_RESPONSE))
                .get(MDMS_PAYMENT_GATEWAY_MODULE)).get(MDMS_GATEWAY_MASTER);

        LinkedList enabledGateways = new LinkedList();

        try {
            for (int i = 0; i < paymentGateways.size(); i++) {
                ((HashMap) paymentGateways.get(i)).remove(GATEWAY_DETAILS);
            }
            for (int i = 0; i < paymentGateways.size(); i++) {
                if ((((HashMap) paymentGateways.get(i)).get(GATEWAY_ENABLED)).equals(true)) {

                    enabledGateways.add(((HashMap) paymentGateways.get(i)));
                }
            }
        } catch (Exception e) {
            throw new Exception("GATEWAY_SEARCH_ERROR", e);
        }
        if (enabledGateways == null) {
            log.error("NO enabled gateways exist");
            throw new CustomException("GATEWAY_CONFIG_ERROR", "No enabled gateway exists");
        } else {
            return enabledGateways;
        }
    }

    private String getMDMSURL() {
        String uri = UriComponentsBuilder
                .fromHttpUrl(appProperties.getMdmsHost())
                .path(appProperties.getMdmsPath())
                .build()
                .toUriString();

        return uri;
    }

}
