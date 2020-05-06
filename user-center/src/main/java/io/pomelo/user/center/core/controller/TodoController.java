package io.pomelo.user.center.core.controller;

import java.util.Collection;
import java.util.List;

import javax.validation.groups.Default;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.pomelo.commons.log.annotation.LogOperation;
import io.pomelo.commons.validation.Update;
import io.pomelo.commons.view.IPage;
import io.pomelo.user.center.core.persistence.entity.Todo;
import io.pomelo.user.center.core.persistence.entity.TodoGroup;
import io.pomelo.user.center.core.persistence.entity.TodoStep;
import io.pomelo.user.center.core.persistence.entity.User;
import io.pomelo.user.center.core.service.interfaces.ITodoGroupService;
import io.pomelo.user.center.core.service.interfaces.ITodoService;
import io.pomelo.user.center.core.service.interfaces.ITodoStepService;
import io.pomelo.user.center.core.view.ITodo;
import io.pomelo.user.center.core.view.ITodoGroup;
import io.pomelo.user.center.core.view.ITodoStep;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 处理待办组,待办事项,待办步骤逻辑模块
 * 
 * @author FengChao
 */
@RestController
@RequestMapping("/todo")
@Api(value = "todo", tags = "TodoController")
public class TodoController {

	@Autowired
	ITodoGroupService todoGroupService;
	@Autowired
	ITodoService todoService;
	@Autowired
	ITodoStepService todoStepService;

	/********* 待办组 *********/
	@PostMapping("group/page")
	@ApiOperation(value = "待办组分页")
	public ResponseEntity<Page<ITodoGroup>> groupPage(@RequestBody IPage<ITodoGroup> pageView) {
		return new ResponseEntity<Page<ITodoGroup>>(
				todoGroupService.query(pageView, pageView.getPageable(Direction.ASC, "sequence")), HttpStatus.OK);
	}

	@GetMapping("group/{id}")
	@ApiOperation(value = "待办组详情")
	public ResponseEntity<ITodoGroup> groupInfo(@PathVariable Long id) {
		return new ResponseEntity<ITodoGroup>(todoGroupService.findOne(id), HttpStatus.OK);
	}

	@PostMapping("group")
	@LogOperation("待办组新建")
	@ApiOperation(value = "待办组新建", notes = "头部需要带token信息")
	public ResponseEntity<TodoGroup> saveGroup(@ApiParam(required = true) @RequestBody ITodoGroup view) {
		return new ResponseEntity<TodoGroup>(todoGroupService.saveOne(view), HttpStatus.OK);
	}

	@DeleteMapping("group")
	@LogOperation("待办组删除")
	@ApiOperation(value = "待办组删除", notes = "头部需要带token信息")
	public ResponseEntity<User> deleteGroup(@ApiParam(required = true) @RequestBody List<Long> ids) {
		todoGroupService.delete(ids);
		return new ResponseEntity<User>(HttpStatus.OK);
	}

	@PutMapping("group")
	@LogOperation("待办组修改")
	@ApiOperation(value = "待办组修改", notes = "头部需要带token信息")
	public ResponseEntity<TodoGroup> updateGroup(@ApiParam(required = true) @RequestBody ITodoGroup view) {
		return new ResponseEntity<TodoGroup>(todoGroupService.saveOne(view), HttpStatus.OK);
	}

	/********* 待办 *********/
	@PostMapping("page")
	@ApiOperation(value = "待办项分页")
	public ResponseEntity<Page<ITodo>> page(@RequestBody IPage<ITodo> pageView) {
		return new ResponseEntity<Page<ITodo>>(
				todoService.query(pageView, pageView.getPageable(Direction.ASC, "sequence")), HttpStatus.OK);
	}

	@PostMapping("list")
	@ApiOperation(value = "待办项列表")
	public ResponseEntity<Collection<ITodo>> list(@RequestBody ITodo view) {
		return new ResponseEntity<Collection<ITodo>>(todoService.query(view), HttpStatus.OK);
	}

	@PostMapping()
	@LogOperation("待办项新建")
	@ApiOperation(value = "待办项新建", notes = "头部需要带token信息")
	public ResponseEntity<Todo> save(@ApiParam(required = true) @RequestBody @Validated({ Default.class }) ITodo view) {
		return new ResponseEntity<Todo>(todoService.saveOne(view), HttpStatus.OK);
	}

	@DeleteMapping()
	@LogOperation("待办项删除")
	@ApiOperation(value = "待办项删除", notes = "头部需要带token信息")
	public ResponseEntity<Todo> delete(@ApiParam(required = true) @RequestBody List<Long> ids) {
		todoService.delete(ids);
		return new ResponseEntity<Todo>(HttpStatus.OK);
	}

	@PutMapping()
	@LogOperation("待办项修改")
	@ApiOperation(value = "待办项修改", notes = "头部需要带token信息")
	public ResponseEntity<Todo> update(
			@ApiParam(required = true) @RequestBody @Validated({ Update.class }) ITodo view) {
		return new ResponseEntity<Todo>(todoService.saveOne(view), HttpStatus.OK);
	}

	/********* 待办步骤 *********/
	@PostMapping("step/list")
	@ApiOperation(value = "待办项步骤列表")
	public ResponseEntity<Collection<ITodoStep>> stepList(@RequestBody ITodoStep view) {
		return new ResponseEntity<Collection<ITodoStep>>(todoStepService.query(view), HttpStatus.OK);
	}

	@PostMapping("step")
	@LogOperation("待办项步骤新建")
	@ApiOperation(value = "待办项步骤新建", notes = "头部需要带token信息")
	public ResponseEntity<TodoStep> saveStep(
			@ApiParam(required = true) @RequestBody @Validated({ Default.class }) ITodoStep view) {
		return new ResponseEntity<TodoStep>(todoStepService.saveOne(view), HttpStatus.OK);
	}

	@DeleteMapping("step")
	@LogOperation("待办项步骤删除")
	@ApiOperation(value = "待办项步骤删除", notes = "头部需要带token信息")
	public ResponseEntity<TodoStep> deleteStep(@ApiParam(required = true) @RequestBody List<Long> ids) {
		todoStepService.delete(ids);
		return new ResponseEntity<TodoStep>(HttpStatus.OK);
	}

	@PutMapping("step")
	@LogOperation("待办项步骤修改")
	@ApiOperation(value = "待办项步骤修改", notes = "头部需要带token信息")
	public ResponseEntity<TodoStep> updateStep(
			@ApiParam(required = true) @RequestBody @Validated({ Update.class }) ITodoStep view) {
		return new ResponseEntity<TodoStep>(todoStepService.saveOne(view), HttpStatus.OK);
	}

}
