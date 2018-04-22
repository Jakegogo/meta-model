package com.concur.meta.client.common;

import java.io.Serializable;

import com.concur.meta.client.domain.ToString;
import org.apache.commons.lang.StringUtils;


/**
 * 通用返回结果
 * @param <T>
 *
 * @author yongfu.cyf
 * @create 2017-07-28 下午3:18
 */
public class ResultDO<T> extends ToString implements Serializable {

	private static final long serialVersionUID = 1738821497566027418L;

	/**
	 * 是否执行成功
	 */
	private boolean success = false;

	/**
	 * 如果返回错误或失败信息
	 */
	private String resultCode;

	/**
	 * 错误提示信息
	 */
	private String errorMsg;

	/**
	 * 业务结果(集)返回
	 */
	private T result;

    /**
     * 异常信息
     */
	private RuntimeException exception;

	public ResultDO() {
		super();
	}

	public ResultDO(boolean success) {
		super();
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public boolean isFailured() {
		return !success;
	}

	public ResultDO<T> setSuccess(boolean success) {
		this.success = success;
		return this;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T value) {
		this.result = value;
	}

	public String getErrorMsg() {
		return errorMsg;
	}


	/**
	 * 包含models里的信息
	 * 
	 * @return
	 */
	public String getFullErrorMsg() {
		StringBuffer sb = new StringBuffer();
		sb.append(success).append("\t");
		if (StringUtils.isNotBlank(errorMsg)) {
			sb.append(errorMsg);
		}
		return sb.toString();
	}

	/**
	 * 不包含models里的信息
	 * 
	 * @param errorMsg
	 */
	public void setErrorMsg(String errorMsg) {
		this.success = false;
		this.errorMsg = errorMsg;
	}

	public ResultDO<T> addErrorMsg(String errorMsg) {
		if (this.errorMsg == null) {
			this.errorMsg = "";
		}
		boolean appendSep = StringUtils.isNotBlank(this.errorMsg);
		if(null != errorMsg && !"null".equals(errorMsg)) {
			this.errorMsg += (appendSep ? (";" + errorMsg) : errorMsg);
		}
		return this;
	}

	public void setErrorResult(String code, String msg) {
		this.success = false;
		this.resultCode = code;
		this.errorMsg = msg;
	}

	public void setErrorResult(IResultCode code, String msg) {
		this.setErrorResult(code.getCode() + ":" + code.getMessage(), msg);
	}

    public void setErrorResult(IResultCode code, String msg, RuntimeException e) {
        this.setErrorResult(code.getCode() + ":" + code.getMessage(), msg);
        this.setException(e);
    }

	public void setResultCode(IResultCode code) {
		this.resultCode = code.getCode();
		this.errorMsg = code.getMessage();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResultDO<T> mergeResult(ResultDO result) {
		this.success = result.success;
		this.resultCode = result.resultCode;
		this.addErrorMsg(result.errorMsg);
		return this;
	}

	public ResultDO<T> mergeResultWidthResult(ResultDO<T> result) {
		this.setResult(result.getResult());
		this.success = result.success;
		this.resultCode = result.resultCode;
		this.addErrorMsg(result.errorMsg);
		return this;
	}

    public RuntimeException getException() {
        return exception;
    }

    public void setException(RuntimeException exception) {
        this.exception = exception;
    }
}
