package io.pomelo.commons.view;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class IPageImpl<O> extends PageImpl<O> {

	private static final long serialVersionUID = 1L;

	public IPageImpl(List<O> content) {
		super(content);
	}

	public IPageImpl(List<O> content, Pageable pageable, long total) {
		super(content, pageable, total);
	}

	@JsonIgnore
	@Override
	public Pageable getPageable() {
		return super.getPageable();
	}
}
