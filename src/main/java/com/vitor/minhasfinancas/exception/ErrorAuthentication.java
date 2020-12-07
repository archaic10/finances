package com.vitor.minhasfinancas.exception;

public class ErrorAuthentication extends RuntimeException {
	
	public ErrorAuthentication(String msg) {
		super(msg);
	}
}
