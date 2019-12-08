package org.egov.msg.util;



import static org.egov.msg.util.Constants.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.msg.config.Configuration;
import org.egov.msg.models.RequestInfoWrapper;
import org.egov.msg.models.SMSRequest;
import org.egov.msg.producer.Producer;
import org.egov.msg.repository.ServiceRequestRepository;
import org.egov.tracer.model.CustomException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NotificationUtil {

    private ServiceRequestRepository serviceRequestRepository;

    private Configuration config;

    private Producer producer;

    @Autowired
    public NotificationUtil(Configuration config, ServiceRequestRepository serviceRequestRepository) {
        this.config = config;
        this.serviceRequestRepository = serviceRequestRepository;

    }

    /**
     * Fetches messages from localization service
     * @param tenantId tenantId
     * @param requestInfo The requestInfo of the request
     * @return Localization messages for the module
     */
    public String getLocalizationMessages(String tenantId, RequestInfo requestInfo){
        LinkedHashMap responseMap = (LinkedHashMap)serviceRequestRepository.fetchResult(getUri(tenantId,requestInfo),requestInfo);
        String jsonString = new JSONObject(responseMap).toString();
        return jsonString;
    }

    public StringBuilder getUri(String tenantId,RequestInfo requestInfo){

        if(config.getLocalizationStateLevel())
            tenantId = tenantId.split("\\.")[0];

        String locale = NOTIFICATION_LOCALE;
        String messageId = requestInfo.getMsgId();
        if(!StringUtils.isEmpty(messageId) && messageId.split("|").length>=2)
            locale = messageId.split("\\|")[1];

        StringBuilder uri = new StringBuilder();
        uri.append(config.getLocalizationHost()).append(config.getLocalizationContextPath())
                .append(config.getLocalizationSearchEndpoint()).append("?")
                .append("locale=").append(locale)
                .append("&tenantId=").append(tenantId)
                .append("&module=").append(Constants.MODULE);

        return uri;
    }

    /**
     * Creates customized message based on tradelicense
     * @param value The module for which message is to be sent
     * @param localizationMessage The messages from localization
     * @param placeholderConfig Contains JSON PATH for placeholder
     * @param notificationCode Based on the code message template is fetched from localizationMessage
     * @return customized message based on module
     */
    public String getCustomizedMsg( JSONObject value, String localizationMessage, Map<String, String> placeholderConfig, String notificationCode){
        String message,messageTemplate;
        messageTemplate = getMessageTemplate(notificationCode,localizationMessage);
        List<String> placeholdersToReplace = getPlaceholder(messageTemplate);
        message = getCustomMsg(value,placeholderConfig,messageTemplate,placeholdersToReplace);
        return message;
    }

    private String getMessageTemplate(String notificationCode, String localizationMessage){
        String path = MESSAGETEMPLATE_PATH;
        path.replace("{}",notificationCode);
        String message=null;
        try {
            Object messageObj = JsonPath.parse(localizationMessage).read(path);
            message = ((ArrayList<String>)messageObj).get(0);
        }
        catch (Exception e){
            log.warn("Fetching from localization failed",e);
        }
        return message;


    }

    private List<String> getPlaceholder(String messageTemplate){
        List<String> placeholders = new LinkedList<>();
        Pattern pattern = Pattern.compile(PLACEHOLDER_REGEX);
        Matcher match = pattern.matcher(messageTemplate);

        while(match.find())
            placeholders.add ((String) match.group().subSequence(1, match.group().length()-1));


        return placeholders;
    }



    /**
     * Creates customized message
     * @param value The module for which message is to be sent
     * @param placeholderConfig Contains JSON PATH for placeholder
     * @param message Message from localization
     * @param placeholdersToReplace contains placeholder to replace with its value, in message
     * @return customized message for initiate
     */
    private String getCustomMsg(JSONObject value,Map<String, String> placeholderConfig,String message, List<String> placeholdersToReplace){
        for(int i=0 ; i<placeholdersToReplace.size();i++){
            String placeholder = placeholdersToReplace.get(i);
            String path = placeholderConfig.get(placeholder);

            if((JsonPath.parse(value.toString()).read(path)).toString() != null){
                message = message.replace(placeholder,(JsonPath.parse(value.toString()).read(path)).toString());
            }

        }
        return message;
    }

    /**
     * Creates the uri for getBill by adding query params from the license
     * @param value The TradeLicense for which getBill has to be called
     * @return The uri for the getBill
     */
    private StringBuilder getBillUri(JSONObject value){
        StringBuilder builder = new StringBuilder(config.getCalculatorHost());
        builder.append(config.getGetBillEndpoint());
        builder.append("?tenantId=");
        builder.append(value.getString("tenantId"));
        builder.append("&consumerCode=");
        builder.append(value.getString("applicationNumber"));
        builder.append("&businessService=");
        builder.append(TRADE_LICENSE_MODULE_CODE);
        return builder;
    }

    /**
     * Creates sms request for the each owners
     * @param message The message for the specific module
     * @param mobileNumberToOwnerName Map of mobileNumber to OwnerName
     * @return List of SMSRequest
     */
    public List<SMSRequest> createSMSRequest(String message, Map<String,String> mobileNumberToOwnerName){
        List<SMSRequest> smsRequest = new LinkedList<>();
        for(Map.Entry<String,String> entryset : mobileNumberToOwnerName.entrySet()) {
            String customizedMsg = message.replace("<1>",entryset.getValue());
            smsRequest.add(new SMSRequest(entryset.getKey(),customizedMsg));
        }
        return smsRequest;
    }

    /**
     * Send the SMSRequest on the SMSNotification kafka topic
     * @param smsRequestList The list of SMSRequest to be sent
     */
    public void sendSMS(List<SMSRequest> smsRequestList){
        if (config.getSMSEnabled()) {
            if (CollectionUtils.isEmpty(smsRequestList))
                log.info("Messages from localization couldn't be fetched!");
            for(SMSRequest smsRequest: smsRequestList) {
                producer.push(config.getSmsNotifTopic(), smsRequest);
                log.info("MobileNumber: "+smsRequest.getMobileNumber()+" Messages: "+smsRequest.getMessage());
            }
        }
    }

    /*
     * Fetches the amount to be paid from getBill API
     * @param requestInformation The RequestInfo of the request
     * @param value The TradeLicense object for which
     * @return
     */
    private BigDecimal getAmountToBePaid(JSONObject requestInformation,JSONObject value){

        ObjectMapper mapper = new ObjectMapper();
        RequestInfo requestInfo;
        requestInfo = mapper.convertValue(requestInformation, RequestInfo.class);

        LinkedHashMap responseMap = (LinkedHashMap)serviceRequestRepository.fetchResult(getBillUri(value),new RequestInfoWrapper(requestInfo));
        String jsonString = new JSONObject(responseMap).toString();

        BigDecimal amountToBePaid = null;
        try {
            Object obj = JsonPath.parse(jsonString).read(BILL_AMOUNT_JSONPATH);
            amountToBePaid = new BigDecimal(obj.toString());
        }
        catch (Exception e){
            throw new CustomException("PARSING ERROR","Failed to parse the response using jsonPath: "+BILL_AMOUNT_JSONPATH);
        }
        return amountToBePaid;
    }


}
