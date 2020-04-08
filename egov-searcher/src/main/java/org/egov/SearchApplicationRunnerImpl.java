package org.egov;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import org.egov.search.model.SearchDefinition;
import org.egov.search.model.SearchDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@Component
@Order(1)
@Slf4j
public class SearchApplicationRunnerImpl implements ApplicationRunner {

	@Autowired
	public static ResourceLoader resourceLoader;
	        
    @Autowired
    private static Environment env;
    
    @Value("${search.yaml.path}")
    private String yamllist;

    @Value("${folder.iterator}")
	public boolean resolveConfigFolder;

    public static ConcurrentHashMap<String, SearchDefinition> searchDefinitionMap  = new ConcurrentHashMap<>();

	
	public static final Logger logger = LoggerFactory.getLogger(SearchApplicationRunnerImpl.class);
	
    @Override
    public void run(final ApplicationArguments arg0) throws Exception {
    	try {
				logger.info("Reading yaml files......");
			    readFiles();
			}catch(Exception e){
				logger.error("Exception while loading yaml files: ",e);
			}
    }

	
	public List<String> resolveAllConfigFolders(List<String> listOfFiles, Boolean resolveFolderCheck){
	List<String> ymlUrlList = new ArrayList<String>();
	if(resolveFolderCheck==false){
		return listOfFiles;
	}else{
		for(int i=0;i<listOfFiles.size();i++){
			String[] fileName = listOfFiles.get(i).split("[.]");
			if(fileName[fileName.length - 1].equals("yml") || fileName[fileName.length - 1].equals("yaml")){
				ymlUrlList.add(listOfFiles.get(i));
			}else{
				ymlUrlList.addAll(FolderIterator(listOfFiles.get(i)));
			}
		}
		return ymlUrlList;
	}

	}

	public List<String> FolderIterator(String baseFolderPath) {
		File folder = new File(baseFolderPath);
		File[] listOfFiles = folder.listFiles();
		List<String> configFolderList = new ArrayList<String>();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				log.info("File " + listOfFiles[i].getName());
				File file = listOfFiles[i];
				String name = file.getName();
				String[] fileName = name.split("[.]");
				if (fileName[fileName.length - 1].equals("yml") || fileName[fileName.length - 1].equals("yaml")) {
					log.debug("Reading yml file....:- " + name);
						configFolderList.add(file.getAbsolutePath());
				}
			} else if (listOfFiles[i].isDirectory()) {
				log.info("Directory " + listOfFiles[i].getName());
				FolderIterator(listOfFiles[i].getAbsolutePath());
			}
		}
		return configFolderList;
	}


	public SearchApplicationRunnerImpl(ResourceLoader resourceLoader) {
    	this.resourceLoader = resourceLoader;
    }
       
    public void readFiles(){
    	ConcurrentHashMap<String, SearchDefinition> map  = new ConcurrentHashMap<>();
    	ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		SearchDefinitions searchDefinitions = null;
		try{
				List<String> fileUrls = Arrays.asList(yamllist.split(","));
				if(0 == fileUrls.size()){
					fileUrls.add(yamllist);
				}
				List<String> ymlUrlS = resolveAllConfigFolders(fileUrls, resolveConfigFolder);
				log.info(" These are all the files " + ymlUrlS);

			for(String yamlLocation : ymlUrlS){
					if(yamlLocation.startsWith("https://") || yamlLocation.startsWith("http://")) {
						logger.info("Reading....: "+yamlLocation);
						URL yamlFile = new URL(yamlLocation);
						try{
							searchDefinitions = mapper.readValue(new InputStreamReader(yamlFile.openStream()), SearchDefinitions.class);
						} catch(Exception e) {
							logger.error("Exception while fetching search definitions for: "+yamlLocation+" = ",e);
							continue;
						}
						logger.info("Parsed to object: "+searchDefinitions.toString());
						map.put(searchDefinitions.getSearchDefinition().getModuleName(), 
								searchDefinitions.getSearchDefinition());
						
					} else if(yamlLocation.startsWith("file://")){
						logger.info("Reading....: "+yamlLocation);
							Resource resource = resourceLoader.getResource(yamlLocation);
							File file = resource.getFile();
							try{
								searchDefinitions = mapper.readValue(file, SearchDefinitions.class);
							 } catch(Exception e) {
									logger.error("Exception while fetching search definitions for: "+yamlLocation+" = ",e);
									continue;
							}
							logger.info("Parsed to object: "+searchDefinitions.toString());
							map.put(searchDefinitions.getSearchDefinition().getModuleName(), 
									searchDefinitions.getSearchDefinition());
					}
				}
			}catch(Exception e){
				logger.error("Exception while loading yaml files: ",e);
			}
		searchDefinitionMap = map;
    }
   

	public ConcurrentHashMap<String, SearchDefinition> getSearchDefinitionMap(){
		return searchDefinitionMap;
	}
}
