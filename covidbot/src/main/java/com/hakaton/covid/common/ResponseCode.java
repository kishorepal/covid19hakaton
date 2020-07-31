package com.hakaton.covid.common;

public enum ResponseCode {

    //TODO complete the code
    SUCCESS(200),
    FAILED(505);
	
	public Integer value;
	
	private ResponseCode(int value) {
		this.value = value;
	}
	
	public Integer getValue() {
		return this.value;
	}


}
