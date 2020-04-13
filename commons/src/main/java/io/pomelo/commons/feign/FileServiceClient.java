package io.pomelo.commons.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import io.pomelo.commons.config.FeignConfiguration;
import io.pomelo.commons.view.IFile;

@FeignClient(name = "file-server", configuration = FeignConfiguration.class)
public interface FileServiceClient {

	@PostMapping("/file/list")
	ResponseEntity<List<IFile>> list();

	@GetMapping("/file/{service}/{module}/{id}")
	ResponseEntity<IFile> info(@PathVariable("service") String service, @PathVariable("module") String module,
			@PathVariable("id") String id);
}
