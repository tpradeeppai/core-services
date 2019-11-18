package org.egov.commonNotificationService.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.egov.common.contract.request.RequestInfo;
import org.egov.commonNotificationService.config.Configuration;
import org.egov.commonNotificationService.models.SMSRequest;
import org.egov.commonNotificationService.repository.ServiceRequestRepository;
import org.egov.commonNotificationService.util.NotificationUtil;
import org.egov.mdms.model.MasterDetail;
import org.egov.mdms.model.MdmsCriteria;
import org.egov.mdms.model.MdmsCriteriaReq;
import org.egov.mdms.model.ModuleDetail;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Pattern;

import static org.egov.commonNotificationService.util.Constants.*;

@Service
@Slf4j
public class NotificationService {

    @Autowired
    private Configuration config;

    private ServiceRequestRepository serviceRequestRepository;

    private NotificationUtil util;

    private RestTemplate rest;


   /* @Autowired
    public NotificationService(TLConfiguration config, ServiceRequestRepository serviceRequestRepository, NotificationUtil util, RestTemplate rest) {
        this.config = config;
        this.serviceRequestRepository = serviceRequestRepository;
        this.util = util;
        this.rest = rest;
    }*/

    public void process(JSONObject record, String topic){
        List<SMSRequest> smsRequests = new LinkedList<>();
        enrichSMSRequest(record,smsRequests, topic);
        util.sendSMS(smsRequests);

    }

    /**
     * Enriches the smsRequest with the customized messages
     * @param record The record from kafka topic
     * @param smsRequests List of SMSRequets
     * @param topic kafka topic
     */
    private void enrichSMSRequest(JSONObject record, List<SMSRequest> smsRequests, String topic){

        //Get RequestInfo
        JSONObject requestInformation = JsonPath.parse(record.toString()).read(REQUESTINFO_PATH);
        ObjectMapper mapper = new ObjectMapper();
        RequestInfo requestInfo;
        requestInfo = mapper.convertValue(requestInformation, RequestInfo.class);

        //Get Tenantid
        String tenantId = null;
        Object ids = JsonPath.parse(record.toString()).read(TENANTID_PATH);
        ArrayList<String> tenantIds = ((ArrayList<String>)ids);
        for(int i=0;i<tenantIds.size();i++){
            if(Pattern.matches(TENANT_REGEX,tenantIds.get(i))){
                tenantId = tenantIds.get(i);
            }
        }

        // MDMS call to fetch MDMS data
        Map<String,Object> mdmsobject = (Map<String, Object>) mdmsCall(requestInfo,tenantId);
        List<Map<String,Object>> mdmsList = (List<Map<String, Object>>) mdmsobject.get(MDMSLIST);
        Map<String,Object> mdmsData = null;

        // Extracting required MDMS data for required topic
        for(Map<String,Object> mdmsValue : mdmsList){
            List<String> topics = new ArrayList<>((List<String>) mdmsValue.get(TOPICLIST));
            if(topics.contains(topic)){
                mdmsData = mdmsValue;
            }
        }

        //Fetching Localization Messages
        String localizationMessages = util.getLocalizationMessages(tenantId,requestInfo);

        JSONArray values = JsonPath.parse(record.toString()).read(mdmsData.get(BASEPATH_KEY).toString());


        for(int i=0;i< values.length();i++){

            // Fetching code for message template
            String notificationCode = getLocalizationCodes(requestInfo,values.getJSONObject(i),mdmsData,topic);
            Map<String, String> placeholderConfig = (Map<String, String>) mdmsData.get(PLACEHOLDER_KEY);

            // Creates customized message
            String message = util.getCustomizedMsg(values.getJSONObject(i),localizationMessages,placeholderConfig, notificationCode);
            if(message==null)
                continue;

            Map<String,String> mobileNumberToOwner = new HashMap<>();
            mobileNumberToOwner = getMobileNumbers(values.getJSONObject(i),placeholderConfig);
            smsRequests.addAll(util.createSMSRequest(message,mobileNumberToOwner));

        }

    }


    /**
     * Get all the unique mobileNumbers of the owners of the property
     * @param value The module whose unique mobileNumber are to be returned
     * @return Unique mobileNumber of the given module
     */
    private Map<String,String> getMobileNumbers(JSONObject value, Map<String, String> placeholderConfig){
        Map<String,String> mobileNumberToOwner = new HashMap<>();
        List<JSONObject> baseDetails = new LinkedList<>();
        baseDetails = JsonPath.parse(value.toString()).read(placeholderConfig.get(BASEDETAILS_KEY));

        for(int i=0;i<baseDetails.size();i++){
            JSONArray owners =JsonPath.parse(baseDetails.get(i).toString()).read(placeholderConfig.get(OWNERS_KEY));
            for(int j=0;j<owners.length();j++){
                String ownername = JsonPath.parse(owners.get(j).toString()).read(placeholderConfig.get(OWNERNAME_KEY));
                String mobileNumber = JsonPath.parse(owners.get(j).toString()).read(placeholderConfig.get(MOBILENUMBER_KEY));
                mobileNumberToOwner.put(mobileNumber,ownername);
            }
        }


        return mobileNumberToOwner;
    }


    public Object mdmsCall( RequestInfo requestInfo, String tenantId){

        MdmsCriteriaReq mdmsCriteriaReq = getMDMSRequest(requestInfo,tenantId);
        Object result = serviceRequestRepository.fetchResult(getMdmsSearchUrl(), mdmsCriteriaReq);
        return result;
    }

    private MdmsCriteriaReq getMDMSRequest(RequestInfo requestInfo,String tenantId){
        List<MasterDetail> masterDetails = new ArrayList<>();

        masterDetails.add(MasterDetail.builder().name(COMMON_NOTIFICATION_FILE).build());

        ModuleDetail moduleDetail = ModuleDetail.builder()
                .moduleName(NOTIFICATION_MODULE).masterDetails(masterDetails).build();
        List<ModuleDetail> moduleDetails = new ArrayList<>();
        moduleDetails.add(moduleDetail);
        MdmsCriteria mdmsCriteria = MdmsCriteria.builder().tenantId(tenantId).moduleDetails(moduleDetails).build();
        return MdmsCriteriaReq.builder().requestInfo(requestInfo).mdmsCriteria(mdmsCriteria).build();
    }

    /**
     * Returns the url for mdms search endpoint
     *
     * @return url for mdms search endpoint
     */
    public StringBuilder getMdmsSearchUrl() {
        return new StringBuilder().append(config.getMdmsHost()).append(config.getMdmsEndPoint());
    }


    public String getLocalizationCodes(RequestInfo requestInfo,JSONObject jsonObject,Map<String,Object> mdmsData,String currentTopic){

        String codesToBeFetched = null;
        String userType = requestInfo.getUserInfo().getType();
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(jsonObject);
        //Fetch localization
        List<Map<String,Object>> localizationConfig = (List<Map<String, Object>>) mdmsData.get(LOCALIZATION_KEY);

        for(Map<String,Object> localization : localizationConfig){
            String code = (String)localization.get(CODE_KEY);
            String trigger = (String)localization.get(TRIGGER_KEY);

            if(trigger != null){
                Object triggetOutPut = JsonPath.read(jsonArray,trigger);
                if(triggetOutPut!=null)
                    codesToBeFetched = code;
            }

            else{
                if((localization.get(USERTYPE_KEY)).toString()!= null) {
                    if (currentTopic.equals((localization.get(TOPIC_KEY)).toString()) && userType.equals( (localization.get(USERTYPE_KEY)).toString())) {
                        codesToBeFetched = code;
                    }
                }
                else{
                    if (currentTopic.equals((localization.get(TOPIC_KEY)).toString())) {
                        codesToBeFetched = code;
                    }
                }
            }
        }


        return codesToBeFetched;
    }








}
