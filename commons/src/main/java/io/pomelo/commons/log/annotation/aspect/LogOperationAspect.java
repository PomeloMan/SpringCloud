package io.pomelo.commons.log.annotation.aspect;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHeaders;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import io.pomelo.commons.log.annotation.LogOperation;
import io.pomelo.commons.log.persistence.entity.Log;
import io.pomelo.commons.log.service.interfaces.ILogService;
import io.pomelo.commons.util.HttpContextUtils;
import io.pomelo.commons.util.http.IpUtils;

@Aspect
@Component
public class LogOperationAspect {

	private final static org.apache.commons.logging.Log logger = LogFactory.getLog(LogOperationAspect.class);

	@Value("${secure.header}")
	public String secureHeader = "Authorization";
	@Value("${secure.prefix}")
	public String securePrefix = "Bearer ";

	@Autowired
	private Gson gson;
	@Autowired
	private ILogService logService;

	@Pointcut("@annotation(io.pomelo.commons.log.annotation.LogOperation)")
	public void logPointCut() {
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		int status = 0;
		try {
			// 执行方法
			Object result = point.proceed();
			status = 1;
			return result;
		} catch (Exception e) {
			status = 0;
			throw e;
		} finally {
			// 执行时长(毫秒)
			long time = System.currentTimeMillis() - beginTime;
			// 保存日志
			saveLog(point, time, status);
		}
	}

	private void saveLog(ProceedingJoinPoint joinPoint, long time, Integer status) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		Log log = new Log();
		LogOperation annotation = method.getAnnotation(LogOperation.class);
		if (annotation != null) {
			// 注解上的描述
			log.setOperation(annotation.value());
		}

		log.setStatus(status);
		log.setRequestTime((int) time);

		// 请求相关信息
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		String header = request.getHeader(secureHeader);
		String token = StringUtils.substringAfter(header, securePrefix);
		log.setCreator(token);
		log.setIp(IpUtils.getIpAddr(request));
		log.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
		log.setRequestUri(request.getRequestURI());
		log.setRequestMethod(request.getMethod());

		// 请求参数
		Object[] args = joinPoint.getArgs();
		try {
			String params = gson.toJson(args[0]);
			log.setRequestParams(params);
		} catch (Exception e) {
			logger.error(e);
		}

		logService.save(log);
	}
}
