package io.pomelo.file.server.core.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;

import io.pomelo.commons.log.annotation.LogOperation;
import io.pomelo.commons.util.base.FileUtil;
import io.pomelo.file.server.core.persistence.entity.File;
import io.pomelo.file.server.core.service.interfaces.IFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/file")
@Api(value = "/file", tags = "FileController")
public class FileController {

	@Value("${file.path}")
	String filePath;

	@Autowired
	IFileService fileService;

	@GetMapping("/user")
	public ResponseEntity<Principal> user(Principal principal) {
		if (OAuth2Authentication.class.isInstance(principal)) {
			OAuth2Authentication auth = (OAuth2Authentication) principal;
			return new ResponseEntity<Principal>(auth.getUserAuthentication(), HttpStatus.OK);
		}
		return new ResponseEntity<Principal>(principal, HttpStatus.OK);
	}

	@GetMapping(value = "/{service}/{module}/{id}")
	@ApiOperation(value = "info")
	public ResponseEntity<File> info(@PathVariable(value = "service") String service,
			@PathVariable(value = "module") String module, @PathVariable(value = "id") String id,
			HttpServletResponse response) throws IOException {
		return new ResponseEntity<File>(fileService.findOne(id), HttpStatus.OK);
	}

	@GetMapping(value = "/bytes/{service}/{module}/{id}")
	@ApiOperation(value = "bytes")
	public void bytes(@PathVariable(value = "service") String service, @PathVariable(value = "module") String module,
			@PathVariable(value = "id") String id, HttpServletResponse response) throws IOException {
		File file = fileService.findOne(id);
		if (file != null) {
			response.setContentType(file.getType());
			try (FileInputStream fis = new FileInputStream(new java.io.File(file.getPath()));
					OutputStream os = response.getOutputStream();) {
				IOUtils.copy(fis, os);
			}
		}
	}

	@PostMapping("/{service}/{module}/upload")
	@LogOperation("文件上传")
	@ApiOperation(value = "upload")
	public ResponseEntity<List<File>> upload(MultipartFile[] file, HttpServletRequest req,
			@PathVariable(value = "service") String service, @PathVariable(value = "module") String module)
			throws IOException {
		List<File> savedFiles = Lists.newArrayList();
		for (MultipartFile multipartFile : file) {
			String id = UUID.randomUUID().toString();
			java.io.File destFile = FileUtil.getFile(
					StringUtils.isNotEmpty(filePath) ? filePath : FileUtil.getCurrentProjectDirection(), service,
					module, DateFormatUtils.format(Calendar.getInstance(), "yyyyMMdd"),
					id + "." + StringUtils.substringAfterLast(multipartFile.getOriginalFilename(), "."));
			File sysFile = new File(multipartFile.getOriginalFilename(), multipartFile.getContentType(),
					destFile.getPath(), service + "/" + module);
			sysFile.setId(id);
			savedFiles.add(fileService.saveOne(sysFile));
			multipartFile.transferTo(destFile);
		}
		return new ResponseEntity<List<File>>(savedFiles, HttpStatus.OK);
	}

}
