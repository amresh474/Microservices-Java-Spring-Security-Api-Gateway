package com.lcwa.user.service.exceptions;

import java.net.InetAddress;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomException extends Exception {

	private static final long serialVersionUID = 3443427464756748L;

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomException.class);

	private final String errorCode;
	private final long traceNumber;
	private final String machineInfo;
	private final ArrayList<?> errorCodes;

	public CustomException() {
		this.errorCode = null;
		this.traceNumber = 0;
		this.machineInfo = null;
		this.errorCodes = null;
	}

	public CustomException(String argErroCode, String argMessage) {
		super(argMessage);
		this.errorCode = argErroCode;
		this.traceNumber = extractTraceNumber(null);
		this.machineInfo = extractMachineInfo();
		this.errorCodes = null;
	}

	public CustomException(ArrayList<?> argErroCodes, String argMessage) {
		super(argMessage);
		this.errorCode = null;
		this.traceNumber = extractTraceNumber(null);
		this.machineInfo = extractMachineInfo();
		this.errorCodes = argErroCodes;
	}

	public CustomException(String argErroCode, Throwable throwable) {
		super(throwable);
		this.errorCode = argErroCode;
		this.traceNumber = extractTraceNumber(throwable);
		this.machineInfo = extractMachineInfo();
		this.errorCodes = null;
	}

	public CustomException(String argErroCode, String argMessage, Throwable throwable) {
		super(argMessage, throwable);
		this.errorCode = argErroCode;
		this.traceNumber = extractTraceNumber(throwable);
		this.machineInfo = extractMachineInfo();
		this.errorCodes = null;
	}

	public CustomException(Throwable throwable) {
		super(throwable);
		this.errorCode = getErrorCode(throwable);
		this.traceNumber = extractTraceNumber(throwable);
		this.machineInfo = extractMachineInfo();
		this.errorCodes = getErrorCodes(throwable);
	}

	private String extractMachineInfo() {
		String info = null;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			// Get IP Address
			byte[] ipAddr = addr.getAddress();
			// java.nio.charset.charset
			String ipAddrStr = new String(ipAddr, "UTF8");
			// Get hostname
			String hostname = addr.getHostName();
			info = ipAddrStr.concat(";").concat(hostname);
		} catch (Exception e) {
			LOGGER.info("Error while extracting machine Info ", e);
		}
		return info;
	}

	private long extractTraceNumber(Throwable thr) {
		return (thr != null && thr.getCause() instanceof CustomException)
				? ((CustomException) (thr.getCause())).traceNumber
				: CustomException.generateTraceNumber();
	}

	public static long generateTraceNumber() {
		return Long.valueOf(new SecureRandom().nextInt(999999));
	}

	public ArrayList<?> getErrorCodes() {
		return errorCodes;
	}

	public ArrayList<?> getErrorCodes(Throwable throwable) {
		if (throwable != null) {
			return (throwable instanceof CustomException) ? ((CustomException) throwable).getErrorCodes()
					: new ArrayList<>(0);
		}
		return null;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorCode(Throwable throwable) {
		return (throwable instanceof CustomException) ? ((CustomException) throwable).getErrorCode() : null;
	}

}
