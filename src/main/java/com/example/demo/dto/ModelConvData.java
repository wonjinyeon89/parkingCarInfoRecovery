package com.example.demo.dto;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public enum ModelConvData {
	/**
	 * 정산요청
	 */
	adjustmentRequest,
	/**
	 * 입차
	 */
	inVehicle,
	/**
	 * 입차요청
	 */
	inVehicleRequest,
	/**
	 * 수동 파트너 입차
	 */
	manualPartnerInVehicle,
	/**
	 * 수동 파트너 출차
	 */
	manualPartnerOutVehicle,
	/**
	 * 출차
	 */
	outVehicle,
	/**
	 * 주차
	 */
	parking,
	/**
	 * 파트너 입차
	 */
	partnerInVehicle,
	/**
	 * 파트너 출라
	 */
	partnerOutVehicle,
	/**
	 * 
	 */
	publicParkingSite;
	
	public static ModelConvData findBy(String arg) {
		for(ModelConvData modelConvData : values()) {
			if(StringUtils.equals(modelConvData.name(), arg)) {
				return modelConvData;
			}
		}
		return null;
	}
}