package com.yks.bigdata.web.system;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;

import com.yks.bigdata.web.vo.MessageVo;

/*@Controller
public class ExceptionController implements HandlerExceptionResolver{
	
	private static Logger log = Logger.getLogger(ExceptionController.class);
	*//**
	 * SimpleMappingExceptionResolver
	 * 可以通过配置这个自定义异常 跳转
	 *//*
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object arg2,Exception ex) {
		log.error("异常：", ex);
		return new ModelAndView("error/500", "message", ex.getLocalizedMessage());
	} 
}*/
@ControllerAdvice
public class ExceptionController{

	private static Logger log = Logger.getLogger(ExceptionController.class);

	@ExceptionHandler(Exception.class)
	public String exception(Exception ex, HttpServletRequest request){
		log.error("Exception异常：", ex);
		request.setAttribute("message", ex.getLocalizedMessage());
		if(isAjaz(request)){
			return new MessageVo(HttpStatus.SC_BAD_REQUEST,ex.getLocalizedMessage()).toString();
		}else{
			return "error/500";
		}
	}

	@ExceptionHandler(MultipartException.class)
	@ResponseBody
	public MessageVo multipartException(MultipartException ex, HttpServletRequest request){
		log.error("MultipartException异常：", ex);
		return new MessageVo(HttpStatus.SC_BAD_REQUEST, "文件上传异常");
	}

	private boolean isAjaz(HttpServletRequest request){
		String requestType = request.getHeader("X-Requested-With");
		if(StringUtils.isNotEmpty(requestType)){
			return true;
		}else{
			return false;
		}
	}
}
