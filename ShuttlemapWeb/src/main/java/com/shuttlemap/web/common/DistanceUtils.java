package com.shuttlemap.web.common;

public class DistanceUtils {
	
	private String distanceLatitude = "111205.67";
	private String distanceLongitude = "85295.23";
	

    /**
     * 이것은 아주 단순한 계산법으로 대략적으로 계산한다.
     * 그리고 원을 기반으로 하는게 아니라 사각형 형식의 range만 구한다.
     * Distance(lat) = 111205.67 *(lat2-lat1) (미터)
     * Distance(long) = 85295.23 * (long2-long1) (미터)
     * distance는 미터단위다.
     */
//	@Override
//	public CoordinatesRange getRange(Coordinates referencePoint,
//			BigDecimal distance) {
//		//distance.setScale(16);
//		double latTelta = distance.divide(new BigDecimal(distanceLatitude),32,BigDecimal.ROUND_HALF_UP).doubleValue();
//		double minLatitude = referencePoint.getLatitude().doubleValue() - latTelta;
//		double maxLatitude = referencePoint.getLatitude().doubleValue() + latTelta;
//		
//		double longTelta = distance.divide(new BigDecimal(distanceLongitude),32,BigDecimal.ROUND_HALF_UP).doubleValue();
//		double minLongitude = referencePoint.getLongitude().doubleValue() - longTelta;
//		double maxLongitude = referencePoint.getLongitude().doubleValue() + longTelta;
//		
//		
//		return new CoordinatesRange(new BigDecimal(minLatitude),new BigDecimal(maxLatitude),
//				new BigDecimal(minLongitude), new BigDecimal(maxLongitude));
//	}

	 
	
	/**
	 * Great Circle Distance Calculation 방식으로 계산
	 * Distance = 69.1 * (180/원주율)*arccos[sin(LAT1)*sin(LAT2)+cos(LAT1)*cos(LAT2)*cos(LONG2-LONG1)]
	 * (69.1은 마일기준)
	 * meter를 리턴한다.
	 */
	public static double getDistance(double lat1, double lon1,double lat2, double lon2) {
		
		double theta = 0;
		if(lon1 > lon2)
			theta = lon1 - lon2;
		else
			theta = lon2 - lon1;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + 
						Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1609.344;
		
		return dist;
	}	
    
	private static double deg2rad(double deg){
		return (deg * Math.PI / 180.0);
	}
    
	private static double rad2deg(double rad){
		return (rad / Math.PI * 180.0);
	}
}
