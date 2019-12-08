package org.egov.msg.config;

import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;

@Getter
@org.springframework.context.annotation.Configuration
public class Configuration {

    @Value("${app.timezone}")
    private String timeZone;
    
    //Localization
    @Value("${egov.localization.host}")
    private String localizationHost;

    @Value("${egov.localization.context.path}")
    private String localizationContextPath;

    @Value("${egov.localization.search.endpoint}")
    private String localizationSearchEndpoint;

    @Value("${egov.localization.statelevel}")
    private Boolean isLocalizationStateLevel;

    //PERSISTER
    @Value("${persister.save.property.topic}")
    private String savePropertyTopic;

    @Value("${persister.update.property.topic}")
    private String updatePropertyTopic;

    @Value("${persister.save.tradelicense.topic}")
    private String saveLicenseTopic;

    @Value("${persister.update.tradelicense.topic}")
    private String updateLicenseTopic;


    // tradelicense Calculator
    @Value("${egov.tl.calculator.host}")
    private String calculatorHost;

    @Value("${egov.tl.calculator.calculate.endpoint}")
    private String calculateEndpoint;

    @Value("${egov.tl.calculator.getBill.endpoint}")
    private String getBillEndpoint;

    //SMS
    @Value("${kafka.topics.notification.sms}")
    private String smsNotifTopic;

    @Value("${notification.sms.enabled}")
    private Boolean isSMSEnabled;

    //MDMS
    @Value("${egov.mdms.host}")
    private String mdmsHost;

    @Value("${egov.mdms.search.endpoint}")
    private String mdmsEndPoint;

    public Boolean getSMSEnabled() {
        return isSMSEnabled;
    }
    public Boolean getLocalizationStateLevel() {
        return isLocalizationStateLevel;
    }



}
