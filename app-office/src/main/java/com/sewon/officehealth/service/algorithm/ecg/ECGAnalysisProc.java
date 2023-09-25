package com.sewon.officehealth.service.algorithm.ecg;

import java.util.ArrayList;
import java.util.List;


public class ECGAnalysisProc {

  public static List<ECGResult> ECG_AnalysisData(List<Double> lstData) {
    //분석결과를 담기위한 List
    List<ECGResult> listECGResult = new ArrayList<>();

    listECGResult.clear();

    try {
      double sum = 0;
      double rri = 0;
      double cumulIntAVG = 0;
      double cumulSDNNAVG = 0;
      double cumulRMSSDAVG = 0;
      double iCnt = 0;

      List<Double> listRRI = new ArrayList<>();

      for (int y = 0; y < lstData.size(); y++) {
        //RRI를 Sum한다
        sum += lstData.get(y);

        listRRI.add(lstData.get(y));

        //9개 그룹마다 RRI 평균, SDNN, RMSSD를 구한다.
        if ((y + 1) % 9 == 0) {
          iCnt++;
          ECGResult node = new ECGResult();
          node.iCycle = y;
          node.NNInterval = sum / 9;
          node.SDNN = CalcStdDev(listRRI, node.NNInterval);
          node.RMSSD = CalcRMSSD(listRRI);

          //실시간 평균내기, 9개까지만
          cumulIntAVG = CumulativeAVG(cumulIntAVG, node.NNInterval, iCnt);
          cumulSDNNAVG = CumulativeAVG(cumulSDNNAVG, node.SDNN, iCnt);
          cumulRMSSDAVG = CumulativeAVG(cumulRMSSDAVG, node.RMSSD, iCnt);

          node.IntAVG = cumulIntAVG;
          node.SDNNAVG = cumulSDNNAVG;
          node.RMSSDAVG = cumulRMSSDAVG;

          listRRI.clear();
          sum = 0;
          rri = 0;

          if (iCnt % 9 == 0) {
            cumulIntAVG = 0;
            cumulSDNNAVG = 0;
            cumulRMSSDAVG = 0;
            iCnt = 0;
          }

          node.NormNNint = node.NNInterval / node.IntAVG;
          node.NormSDNN = node.SDNN / node.SDNNAVG;
          node.NormRMSSD = node.RMSSD / node.RMSSDAVG;
          node.LnNint = Math.log(node.NormNNint);
          node.LnNSDNN = Math.log(node.NormSDNN);
          node.LnNRMSSD = Math.log(node.NormRMSSD);
          node.sqNint = Math.pow(node.NormNNint, 2);
          node.sqSDNN = Math.pow(node.NormSDNN, 2);
          node.sqRMSSD = Math.pow(node.NormRMSSD, 2);

          //현재 분석된 데이터로 아래의 모델식 계산을 하여 sleep5에 대한 State를 판정한다.
          // P = exp(Intercept Estimate상수 + sqNint의 Estimate상수 * 분석된 sqNint)
          // 결과 = P / (1 + P)

          node.sleep5_predict = Math.exp(-3.129 + 3.106 * node.sqNint) / (1 + Math.exp(-3.129 + 3.106 * node.sqNint));

          if (node.sleep5_predict > 0.5)
            node.sleep5_State = "sleep5";
          else
            node.sleep5_State = "sleep1";

          //현재 분석된 데이터로 아래의 모델식 계산을 하여 stress에 대한 State를 판정한다.
          // P = exp(Intercept Estimate상수 + NormRMSSD의 Estimate상수 * 분석된 NormRMSSD)
          // 결과 = P / (1 + P)

          node.stress_predict = Math.exp(1.94 - 2.145 * node.NormRMSSD) / (1 + Math.exp(1.94 - 2.145 * node.NormRMSSD));

          if (node.stress_predict > 0.5)
            node.stress_State = "stress";
          else
            node.stress_State = "sleep1";

          //ECG_PQRST 최종 결과 클래스를 리스트에 담는다.
          listECGResult.add(node);
        }
      }


      /////////////////////////////////////////////////////////////
      //
      //분석결과 내역을 파일로 생성한다.
      //
      /////////////////////////////////////////////////////////////
    /*
			String strFile = "C:\\Users\\knw79\\Downloads\\Sub20_Analysis_Data.csv";

			File fi = new File(strFile);

	        //해당 파일이 이미 존재하면 삭제 후 생성한다.
	        if(fi.exists() == true)
	        {
	        	fi.delete();
	        }

			BufferedWriter bufWriter = null;

			bufWriter = new BufferedWriter(new FileWriter(strFile, true));
			//bufWriter_test.write("\uFEFF");  //한글깨지는 것 방지

			String strHeader = "NNInterval,SDNN,RMSSD,IntAVG,SDNNAVG,RMSSDAVG,NormNNint,NormSDNN,NormRMSSD,LnNint,LnNSDNN,LnRMSSD,sqNint,sqSDNN,sqRMSSD,sleep5_predict, sleep5_State, stress_predict, stress_State\n";

			//처음 헤더를 기록한다.
			bufWriter.write(strHeader);

			//결과파일 생성
			for(int iter = 0;iter < listECGResult.size(); iter++)
			{
				String strLine = listECGResult.get(iter).NNInterval + ","
			                   + listECGResult.get(iter).SDNN + ","
			                   + listECGResult.get(iter).RMSSD + ","
			                   + listECGResult.get(iter).IntAVG + ","
			                   + listECGResult.get(iter).SDNNAVG + ","
			                   + listECGResult.get(iter).RMSSDAVG + ","
			                   + listECGResult.get(iter).NormNNint + ","
			                   + listECGResult.get(iter).NormSDNN + ","
			                   + listECGResult.get(iter).NormRMSSD + ","
			                   + listECGResult.get(iter).LnNint + ","
			                   + listECGResult.get(iter).LnNSDNN + ","
			                   + listECGResult.get(iter).LnNRMSSD + ","
			                   + listECGResult.get(iter).sqNint + ","
			                   + listECGResult.get(iter).sqSDNN + ","
			                   + listECGResult.get(iter).sqRMSSD + ","
			                   + listECGResult.get(iter).sleep5_predict + ","
			                   + listECGResult.get(iter).sleep5_State + ","
			                   + listECGResult.get(iter).stress_predict + ","
			                   + listECGResult.get(iter).stress_State + "\n";

				bufWriter.write(strLine);
			}

			bufWriter.close();
    */
    } catch (Exception e) {
      e.printStackTrace();
    }

    //최종 결과를 담은 리스트를 리턴한다.
    return listECGResult;
  }

  public static double CalcRRIAvg(double[] rriData) {
    double sum = 0;
    for (double rriDatum : rriData) {
      sum += rriDatum;
    }
    return sum / rriData.length;
  }

  public static double CalcStdDev(List<Double> listRRI, double meanValue) {
    if (listRRI.size() < 2) return Double.NaN;

    double sum = 0.0;
    double sd = 0.0;
    double diff;

    for (int i = 0; i < listRRI.size(); i++) {
      diff = listRRI.get(i) - meanValue;
      sum += diff * diff;
    }
    sd = Math.sqrt(sum / listRRI.size());

    return sd;
  }

  public static double CalcRMSSD(List<Double> listRRI) {
    if (listRRI.size() < 2) return Double.NaN;

    double sum = 0.0;
    double sd = 0.0;
    double diff;

    for (int i = 0; i < listRRI.size() - 1; i++) {
      diff = listRRI.get(i) - listRRI.get(i + 1);
      sum += diff * diff;
    }
    sd = Math.sqrt(sum / (listRRI.size() - 1));

    return sd;
  }

  public static double CumulativeAVG(double prevAvg, double newNumber, double listLength) {
    double oldWeight = (listLength - 1) / listLength;
    double newWeight = 1 / listLength;
    return (prevAvg * oldWeight) + (newNumber * newWeight);
  }
}
