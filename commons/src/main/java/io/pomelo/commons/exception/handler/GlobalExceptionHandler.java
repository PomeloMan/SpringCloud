package io.pomelo.commons.exception.handler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.google.gson.Gson;

import io.pomelo.commons.exception.BusinessException;
import io.pomelo.commons.log.persistence.entity.LogError;
import io.pomelo.commons.log.service.interfaces.ILogErrorService;
import io.pomelo.commons.util.HttpContextUtils;
import io.pomelo.commons.util.http.IpUtils;

/**
 * @ClassName GlobalExceptionHandler.java
 * @Description {@Link BasicErrorController}
 * @author PomeloMan
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Log logger = LogFactory.getLog(GlobalExceptionHandler.class);

	@Autowired
	private Gson gson;
	@Autowired
	private ILogErrorService logErrorService;

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<?> handleRenException(BusinessException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public Object defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		saveErrorLog(e);
		return new ResponseEntity<Exception>(e, HttpStatus.EXPECTATION_FAILED);
	}

	private void saveErrorLog(Exception e) {
		logger.error(e.getMessage(), e);
		LogError log = new LogError();
		// 请求相关信息
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		log.setIp(IpUtils.getIpAddr(request));
		log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
		log.setRequestUri(request.getRequestURI());
		log.setRequestMethod(request.getMethod());
		Map<String, String> params = HttpContextUtils.getParameterMap(request);
		if (MapUtils.isNotEmpty(params)) {
			log.setRequestParams(gson.toJson(params));
		}
		// 异常信息
		log.setErrorInfo(ExceptionUtils.getStackTrace(e));
		// 保存
		logErrorService.save(log);
	}
}