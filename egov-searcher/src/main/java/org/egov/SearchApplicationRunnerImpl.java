package org.egov;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
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

import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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

    public static ConcurrentHashMap<String, SearchDefinition> searchDefinitionMap = new ConcurrentHashMap<>();


    public static final Logger logger = LoggerFactory.getLogger(SearchApplicationRunnerImpl.class);

    @Override
    public void run(final ApplicationArguments arg0) throws Exception {
        try {
            log.info("Reading yaml files......");
            readFiles();
        } catch (Exception e) {
            log.error("Exception while loading yaml files: ", e);
        }
    }

    //file types to be resolved have to be passed as comma separated types.
    public List<String> resolveAllConfigFolders(List<String> listOfFiles, String fileTypesToResolve) {
        List<String> fileList = new ArrayList<String>();
        List<String> fileTypes = Arrays.asList(fileTypesToResolve.split("[,]"));

		for (String listOfFile : listOfFiles) {
			String[] fileName = listOfFile.split("[.]");
			if (fileTypes.contains(fileName[fileName.length - 1])) {
				fileList.add(listOfFile);
			} else {
				fileList.addAll(getFilesInFolder(listOfFile, fileTypes));
			}

		}
        return fileList;
    }

    public List<String> getFilesInFolder(String baseFolderPath,List<String> fileTypes) {
        File folder = new File(baseFolderPath);
        File[] listOfFiles = folder.listFiles();
        List<String> configFolderList = new ArrayList<String>();

        for (int i = 0; i < listOfFiles.length; i++) {
            log.info("File " + listOfFiles[i].getName());
            File file = listOfFiles[i];
            String name = file.getName();
            String[] fileName = name.split("[.]");
            if (fileTypes.contains(fileName[fileName.length - 1])) {
                log.debug(" Resolving folder....:- " + name);
                configFolderList.add(file.getAbsolutePath());
            }

        }
        return configFolderList;
    }


    public SearchApplicationRunnerImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    // 2 file types yaml and yml have to be resolved
    public void readFiles() {
        ConcurrentHashMap<String, SearchDefinition> map = new ConcurrentHashMap<>();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        SearchDefinitions searchDefinitions = null;
        try {
            List<String> fileUrls = Arrays.asList(yamllist.split(","));
            if (0 == fileUrls.size()) {
                fileUrls.add(yamllist);
            }
            String fileTypes = "yaml,yml";
            List<String> ymlUrlS = resolveAllConfigFolders(fileUrls, fileTypes);
            log.info(" These are all the files " + ymlUrlS);

            for (String yamlLocation : ymlUrlS) {
                if (yamlLocation.startsWith("https://") || yamlLocation.startsWith("http://")) {
                    logger.info("Reading....: " + yamlLocation);
                    URL yamlFile = new URL(yamlLocation);
                    try {
                        searchDefinitions = mapper.readValue(new InputStreamReader(yamlFile.openStream()), SearchDefinitions.class);
                    } catch (Exception e) {
                        logger.error("Exception while fetching search definitions for: " + yamlLocation + " = ", e);
                        continue;
                    }
                    logger.info("Parsed to object: " + searchDefinitions.toString());
                    map.put(searchDefinitions.getSearchDefinition().getModuleName(),
                            searchDefinitions.getSearchDefinition());

                } else if (yamlLocation.startsWith("file://")) {
                    logger.info("Reading....: " + yamlLocation);
                    Resource resource = resourceLoader.getResource(yamlLocation);
                    File file = resource.getFile();
                    try {
                        searchDefinitions = mapper.readValue(file, SearchDefinitions.class);
                    } catch (Exception e) {
                        logger.error("Exception while fetching search definitions for: " + yamlLocation + " = ", e);
                        continue;
                    }
                    logger.info("Parsed to object: " + searchDefinitions.toString());
                    map.put(searchDefinitions.getSearchDefinition().getModuleName(),
                            searchDefinitions.getSearchDefinition());
                }
            }
        } catch (Exception e) {
            logger.error("Exception while loading yaml files: ", e);
        }
        searchDefinitionMap = map;
    }


    public ConcurrentHashMap<String, SearchDefinition> getSearchDefinitionMap() {
        return searchDefinitionMap;
    }
}
