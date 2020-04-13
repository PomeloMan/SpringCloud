package io.pomelo.user.center.core.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.pomelo.commons.view.IPage;
import io.pomelo.user.center.core.persistence.entity.Authority;
import io.pomelo.user.center.core.service.interfaces.IAuthorityService;
import io.pomelo.user.center.core.view.IAuthority;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/auth")
@Api(value = "/auth", tags = "AuthorityController")
public class AuthorityController {

	@Autowired
	IAuthorityService authService;

	@PostMapping("page")
	@ApiOperation(value = "权限分页查询")
	public ResponseEntity<Page<IAuthority>> page(@RequestBody IPage<IAuthority> pageView) {
		return new ResponseEntity<Page<IAuthority>>(
				authService.query(pageView, pageView.getPageable(Direction.ASC, "sequence")), HttpStatus.OK);
	}

	@PostMapping("list")
	@ApiOperation(value = "权限查询")
	public ResponseEntity<Collection<IAuthority>> list(
			@ApiParam(required = false) @RequestBody(required = false) IAuthority view) {
		return new ResponseEntity<Collection<IAuthority>>(authService.query(view), HttpStatus.OK);
	}

	@PostMapping()
	@ApiOperation(value = "新建权限", notes = "头部需要带token信息")
	public ResponseEntity<Authority> save(@ApiParam(required = true) @RequestBody IAuthority view) {
		return new ResponseEntity<Authority>(authService.saveOne(view), HttpStatus.OK);
	}

	@DeleteMapping()
	@ApiOperation(value = "删除权限", notes = "头部需要带token信息")
	public ResponseEntity<Authority> delete(@ApiParam(required = true) @RequestBody List<String> ids) {
		authService.delete(ids);
		return new ResponseEntity<Authority>(HttpStatus.OK);
	}

	@PutMapping()
	@ApiOperation(value = "修改权限", notes = "头部需要带token信息")
	public ResponseEntity<Authority> update(@ApiParam(required = true) @RequestBody IAuthority view) {
		return new ResponseEntity<Authority>(authService.saveOne(view), HttpStatus.OK);
	}

}
