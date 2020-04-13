package io.pomelo.commons.view;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import io.pomelo.commons.util.PageableUtil;

public class IPage<O> {

	int page;
	int size;

	String order;
	String dir;

	O object;

	public O getObject() {
		return object;
	}

	public void setObject(O object) {
		this.object = object;
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

	public Pageable getPageable() {
		return getPageable(getDir(), getOrder());
	}

	public Pageable getPageable(String dir, String order) {
		Sort sort = Sort.unsorted();
		if (StringUtils.isNoneEmpty(dir) && StringUtils.isNoneEmpty(order)) {
			sort = new Sort(Direction.valueOf(dir), order);
		}
		return PageableUtil.getPageRequest(getPage(), getSize(), sort);
	}

	public Pageable getPageable(Direction dir, String order) {
		Sort sort = Sort.unsorted();
		if (dir != null && StringUtils.isNoneEmpty(order)) {
			sort = new Sort(dir, order);
		}
		return PageableUtil.getPageRequest(getPage(), getSize(), sort);
	}

}
