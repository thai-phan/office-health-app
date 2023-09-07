package com.sewon.officehealth.algorithm;

public class ECG_RESULT {
	public int iCycle;   //cycle
    public double NNInterval;  //RRI 
    public double SDNN;   //SDNN
	public double RMSSD;   //RMSSD
	public double IntAVG;   //RRI 평균
	public double SDNNAVG;   //SDNN 평균
	public double RMSSDAVG;   //RMSSD 평균
	public double NormNNint;  //Normalize RRI
	public double NormSDNN;  //Normalize SDNN
	public double NormRMSSD;  //Normalize RMSSD
	public double LnNint;  //NormNNint 자연로그
	public double LnNSDNN;  //NormSDNN 자연로그
	public double LnNRMSSD;  //NormRMSSD 자연로그
	public double sqNint;  //NormNNint 제곱
	public double sqSDNN;  //NormSDNN 제곱
	public double sqRMSSD;  //NormRMSSD 제곱
	public double sleep5_predict;  //sleep1(정상) vs sleep5 판정값
	public String sleep5_State;  //sleep1(정상) vs sleep5 상태
	public double stress_predict;  //sleep1(정상) vs stress 판정값
	public String stress_State;  //sleep1(정상) vs stress 상태
}
