package org.egov.filestore.web.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FilenameUtils;
import org.egov.filestore.domain.model.FileInfo;
import org.egov.filestore.domain.service.StorageService;
import org.egov.filestore.web.contract.File;
import org.egov.filestore.web.contract.FileStoreResponse;
import org.egov.filestore.web.contract.GetFilesByTagResponse;
import org.egov.filestore.web.contract.ResponseFactory;
import org.egov.filestore.web.contract.StorageResponse;
import org.egov.tracer.model.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/v1/files")
public class StorageController {
	
	private static List<String> DEFAULT_FORMATS = Arrays.asList("jpg","jpeg","png","pdf");
	
	@Value("#{${allowed.file.formats}}")
	private List<String> allowedFileFormats;
	
	@PostConstruct
	public void setDefaultFIleFormats() {
		
		if(CollectionUtils.isEmpty(allowedFileFormats)) {
			allowedFileFormats = DEFAULT_FORMATS;
		}
	}

	private StorageService storageService;
	private ResponseFactory responseFactory;

	public StorageController(StorageService storageService, ResponseFactory responseFactory) {
		this.storageService = storageService;
		this.responseFactory = responseFactory;
	}

	@GetMapping("/id")
	@ResponseBody
	public ResponseEntity<Resource> getFile(@RequestParam(value = "tenantId") String tenantId,
			@RequestParam("fileStoreId") String fileStoreId) {
		org.egov.filestore.domain.model.Resource resource =null;
		try {
			resource = storageService.retrieve(fileStoreId, tenantId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFileName() + "\"")
				.header(HttpHeaders.CONTENT_TYPE, resource.getContentType()).body(resource.getResource());
	}

	@GetMapping("/metadata")
	@ResponseBody
	public ResponseEntity<org.egov.filestore.domain.model.Resource> getMetaData(
			@RequestParam(value = "tenantId") String tenantId, @RequestParam("fileStoreId") String fileStoreId) {
		org.egov.filestore.domain.model.Resource resource =null;
		try {
		    resource = storageService.retrieve(fileStoreId, tenantId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		resource.setResource(null);
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}

	@GetMapping(value = "/tag", produces = APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public GetFilesByTagResponse getUrlListByTag(@RequestParam(value = "tenantId") String tenantId,
			@RequestParam("tag") String tag) {
		final List<FileInfo> fileInfoList = storageService.retrieveByTag(tag, tenantId);
		return responseFactory.getFilesByTagResponse(fileInfoList);
	}

	@PostMapping(produces = APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public StorageResponse storeFiles(@RequestParam("file") List<MultipartFile> files,
			@RequestParam(value = "tenantId") String tenantId,
			@RequestParam(value = "module", required = true) String module,
			@RequestParam(value = "tag", required = false) String tag) {
		
		List<String> formats = allowedFileFormats;
		
		for(MultipartFile file : files) {
	
			if (!formats.contains(FilenameUtils.getExtension(file.getOriginalFilename()))) {

				throw new CustomException("EG_FILESTORE_INVALID_INPUT",
						"Invalid input provided for the file, please upload any of the allowed formats : " + formats);
			}
		}

		final List<String> fileStoreIds = storageService.save(files, module, tag, tenantId);
		return getStorageResponse(fileStoreIds, tenantId);
	}

	private StorageResponse getStorageResponse(List<String> fileStorageIds, String tenantId) {
		List<File> files = new ArrayList<>();
		for (String fileStorageId : fileStorageIds) {
			File f = new File(fileStorageId, tenantId);
			files.add(f);
		}
		return new StorageResponse(files);
	}
	
	@GetMapping("/url")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getUrls(@RequestParam(value = "tenantId") String tenantId,
			@RequestParam("fileStoreIds") List<String> fileStoreIds) {
		
		Map<String, Object> responseMap = new HashMap<>();
		if (fileStoreIds.isEmpty())
			return new ResponseEntity<>(new HashMap<>(), HttpStatus.OK);
			Map<String, String> maps= storageService.getUrls(tenantId, fileStoreIds);
			
		List<FileStoreResponse> responses = new ArrayList<>();
		for (Entry<String, String> entry : maps.entrySet()) {

			responses.add(FileStoreResponse.builder().id(entry.getKey()).url(entry.getValue()).build());
		}
		responseMap.putAll(maps);
		responseMap.put("fileStoreIds", responses);
		
		return new ResponseEntity<>(responseMap, HttpStatus.OK);
	}
	
}
