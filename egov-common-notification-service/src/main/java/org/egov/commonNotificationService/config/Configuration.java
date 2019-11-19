package org.egov.commonNotificationService.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.egov.tracer.config.TracerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Import({TracerConfiguration.class})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
@EnableKafka
public class Configuration {

    @Value("${app.timezone}")
    private String timeZone;

    @PostConstruct
    public void initialize() {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }

    @Bean
    @Autowired
    public MappingJackson2HttpMessageConverter jacksonConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }


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

    public String getMdmsHost() {
        return mdmsHost;
    }

    public void setMdmsHost(String mdmsHost) {
        this.mdmsHost = mdmsHost;
    }

    public String getMdmsEndPoint() {
        return mdmsEndPoint;
    }

    public void setMdmsEndPoint(String mdmsEndPoint) {
        this.mdmsEndPoint = mdmsEndPoint;
    }


    public String getSmsNotifTopic() {
        return smsNotifTopic;
    }

    public void setSmsNotifTopic(String smsNotifTopic) {
        this.smsNotifTopic = smsNotifTopic;
    }

    public Boolean getSMSEnabled() {
        return isSMSEnabled;
    }

    public void setSMSEnabled(Boolean SMSEnabled) {
        isSMSEnabled = SMSEnabled;
    }

    public String getCalculatorHost() {
        return calculatorHost;
    }

    public void setCalculatorHost(String calculatorHost) {
        this.calculatorHost = calculatorHost;
    }

    public String getCalculateEndpoint() {
        return calculateEndpoint;
    }

    public void setCalculateEndpoint(String calculateEndpoint) {
        this.calculateEndpoint = calculateEndpoint;
    }

    public String getGetBillEndpoint() {
        return getBillEndpoint;
    }

    public void setGetBillEndpoint(String getBillEndpoint) {
        this.getBillEndpoint = getBillEndpoint;
    }

    public String getSaveLicenseTopic() {
        return saveLicenseTopic;
    }

    public void setSaveLicenseTopic(String saveLicenseTopic) {
        this.saveLicenseTopic = saveLicenseTopic;
    }

    public String getUpdateLicenseTopic() {
        return updateLicenseTopic;
    }

    public void setUpdateLicenseTopic(String updateLicenseTopic) {
        this.updateLicenseTopic = updateLicenseTopic;
    }

    public String getLocalizationHost() {
        return localizationHost;
    }

    public void setLocalizationHost(String localizationHost) {
        this.localizationHost = localizationHost;
    }

    public String getLocalizationContextPath() {
        return localizationContextPath;
    }

    public void setLocalizationContextPath(String localizationContextPath) {
        this.localizationContextPath = localizationContextPath;
    }

    public String getLocalizationSearchEndpoint() {
        return localizationSearchEndpoint;
    }

    public void setLocalizationSearchEndpoint(String localizationSearchEndpoint) {
        this.localizationSearchEndpoint = localizationSearchEndpoint;
    }

    public Boolean getLocalizationStateLevel() {
        return isLocalizationStateLevel;
    }

    public void setLocalizationStateLevel(Boolean localizationStateLevel) {
        isLocalizationStateLevel = localizationStateLevel;
    }

    public String getSavePropertyTopic() {
        return savePropertyTopic;
    }

    public void setSavePropertyTopic(String savePropertyTopic) {
        this.savePropertyTopic = savePropertyTopic;
    }

    public String getUpdatePropertyTopic() {
        return updatePropertyTopic;
    }

    public void setUpdatePropertyTopic(String updatePropertyTopic) {
        this.updatePropertyTopic = updatePropertyTopic;
    }




}
