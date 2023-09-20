package com.sewon.officehealth.algorithm;

public class ECG_RESULT {
  //cycle
  public int iCycle;
  //RRI
  public double NNInterval;
  //SDNN
  public double SDNN;
  //RMSSD
  public double RMSSD;
  //RRI 평균
  public double IntAVG;
  //SDNN 평균
  public double SDNNAVG;
  //RMSSD 평균
  public double RMSSDAVG;
  //Normalize RRI
  public double NormNNint;
  //Normalize SDNN
  public double NormSDNN;
  //Normalize RMSSD
  public double NormRMSSD;
  //NormNNint 자연로그
  public double LnNint;
  //NormSDNN 자연로그
  public double LnNSDNN;
  //NormRMSSD 자연로그
  public double LnNRMSSD;
  //NormNNint 제곱
  public double sqNint;
  //NormSDNN 제곱
  public double sqSDNN;
  //NormRMSSD 제곱
  public double sqRMSSD;
  //sleep1(정상) vs sleep5 판정값
  public double sleep5_predict;
  //sleep1(정상) vs sleep5 상태
  public String sleep5_State;
  //sleep1(정상) vs stress 판정값
  public double stress_predict;
  //sleep1(정상) vs stress 상태
  public String stress_State;
}
