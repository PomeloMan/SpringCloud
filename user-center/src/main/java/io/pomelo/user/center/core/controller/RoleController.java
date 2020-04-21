package io.pomelo.user.center.core.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.pomelo.commons.log.annotation.LogOperation;
import io.pomelo.commons.view.IPage;
import io.pomelo.user.center.core.persistence.entity.Role;
import io.pomelo.user.center.core.service.interfaces.IRoleService;
import io.pomelo.user.center.core.view.IRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/role")
@Api(value = "/role", tags = "RoleController")
public class RoleController {

	@Autowired
	IRoleService roleService;

	@PostMapping("page")
	@ApiOperation(value = "角色分页查询")
	public ResponseEntity<Page<IRole>> page(@RequestBody IPage<IRole> pageView) {
		return new ResponseEntity<Page<IRole>>(
				roleService.query(pageView, pageView.getPageable(Direction.ASC, "createdDate")), HttpStatus.OK);
	}

	@PostMapping("list")
	@ApiOperation(value = "角色查询")
	public ResponseEntity<Collection<IRole>> list(
			@ApiParam(required = false) @RequestBody(required = false) IRole view) {
		return new ResponseEntity<Collection<IRole>>(roleService.query(view), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "查询单个角色", notes = "头部需要带token信息")
	public ResponseEntity<IRole> save(@PathVariable(value = "id") String id) {
		return new ResponseEntity<IRole>(roleService.findOne(id), HttpStatus.OK);
	}

	@PostMapping()
	@LogOperation("角色新建")
	@ApiOperation(value = "角色新建", notes = "头部需要带token信息")
	public ResponseEntity<Role> save(@ApiParam(required = true) @RequestBody IRole view) {
		return new ResponseEntity<Role>(roleService.saveOne(view), HttpStatus.OK);
	}

	@DeleteMapping()
	@LogOperation("角色删除")
	@ApiOperation(value = "角色删除", notes = "头部需要带token信息")
	public ResponseEntity<Role> delete(@ApiParam(required = true) @RequestBody List<String> ids) {
		roleService.delete(ids);
		return new ResponseEntity<Role>(HttpStatus.OK);
	}

	@PutMapping()
	@LogOperation("角色修改")
	@ApiOperation(value = "角色修改", notes = "头部需要带token信息")
	public ResponseEntity<Role> update(@ApiParam(required = true) @RequestBody IRole view) {
		return new ResponseEntity<Role>(roleService.saveOne(view), HttpStatus.OK);
	}
}
