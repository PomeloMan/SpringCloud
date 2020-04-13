package io.pomelo.commons.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtil {

	private final static int Default_Page = 0;
	private final static int Default_Size = 10;

	public static Pageable getDefaultPageRequest() {
		return PageRequest.of(Default_Page, Default_Size);
	}

	public static Pageable getPageRequest(int page, int size) {
		return getPageRequest(page, size, null);
	}

	public static Pageable getPageRequest(int page, int size, Sort sort) {
		if (page < 0)
			page = Default_Page;
		if (size < 1)
			size = Default_Size;
		return PageRequest.of(page, size, sort);
	}
}
