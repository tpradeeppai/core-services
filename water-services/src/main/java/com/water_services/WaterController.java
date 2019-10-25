package com.water_services;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@RestController
public class WaterController{
	
    private final WaterProducer producer;
    
    private final ResponseInfoFactory responseInfoFactory;
    
    private final WaterService waterService;
    
    @Autowired
    public WaterController(WaterProducer producer,ResponseInfoFactory responseInfoFactory,WaterService waterService) {
    this.producer = producer;
    this.responseInfoFactory = responseInfoFactory;
    this.waterService = waterService;
    
    }
    
    @PostMapping(value = "/createWaterConnection", produces = "application/json")
    public ResponseEntity<Void> addArticle(@RequestBody WaterModel article, UriComponentsBuilder builder) {
    boolean flag = waterService.addArticle(article);
    if (flag == false) {
    	return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }
    
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(builder.path("/createWaterConnection/{id}").buildAndExpand(article.getConnection_id()).toUri());
    return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
    
    
    @GetMapping("articles")
	public ResponseEntity<List<WaterModel>> getAllArticles() {
		List<WaterModel> list = waterService.getAllArticles();
		return new ResponseEntity<List<WaterModel>>(list, HttpStatus.OK);
	}
    
    @RequestMapping(value="/_search", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<WaterConnectionResponse> search(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
                                                       @Valid @ModelAttribute WaterConnectionSearchCriteria criteria){

        List<WaterConnection> waterConnection = waterService.search(criteria,requestInfoWrapper.getRequestInfo());

        WaterConnectionResponse response = WaterConnectionResponse.builder().waterConnection(waterConnection).responseInfo(
                responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    
    @PostMapping("/_create")
    public ResponseEntity<WaterConnectionResponse> create(@Valid @RequestBody WaterConnectionRequest waterConnectionRequest)
    {
         List<WaterConnection> waterConnection = waterService.create(waterConnectionRequest);
         
         WaterConnectionResponse response = WaterConnectionResponse.builder().waterConnection(waterConnection).responseInfo(
                responseInfoFactory.createResponseInfoFromRequestInfo(waterConnectionRequest.getRequestInfo(), true))
                .build();
            return new ResponseEntity<>(response,HttpStatus.OK);
    }
    
 
    

//   @PostMapping(value = "/publish")
//   public void sendMessageToKafkaTopic(@RequestParam("message") String message){
//   this.producer.sendMessage(message);
//     }
 
}
