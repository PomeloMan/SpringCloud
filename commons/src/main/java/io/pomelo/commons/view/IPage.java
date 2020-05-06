package io.pomelo.commons.view;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import io.pomelo.commons.util.PageableUtil;
import io.swagger.annotations.ApiModelProperty;

public class IPage<O> {

	int page;
	int size;

	String order;
	String dir;

	O searchable;

	public O getSearchable() {
		return searchable;
	}

	public void setSearchable(O searchable) {
		this.searchable = searchable;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	@ApiModelProperty(hidden = true)
	public Pageable getPageable() {
		return getPageable(getDir(), getOrder());
	}

	@ApiModelProperty(hidden = true)
	public Pageable getPageable(String dir, String order) {
		Sort sort = Sort.unsorted();
		if (StringUtils.isNoneEmpty(dir) && StringUtils.isNoneEmpty(order)) {
			sort = new Sort(Direction.valueOf(dir), order);
		}
		return PageableUtil.getPageRequest(getPage(), getSize(), sort);
	}

	@ApiModelProperty(hidden = true)
	public Pageable getPageable(Direction dir, String order) {
		Sort sort = Sort.unsorted();
		if (dir != null && StringUtils.isNoneEmpty(order)) {
			sort = new Sort(dir, order);
		}
		return PageableUtil.getPageRequest(getPage(), getSize(), sort);
	}

}
