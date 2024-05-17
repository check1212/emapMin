/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.emapMin.web;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springmodules.validation.commons.DefaultBeanValidator;

import com.emapMin.WeatherVO;
import com.emapMin.service.LibJson;
import com.emapMin.service.mapService;
import com.ibm.icu.text.SimpleDateFormat;

import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * @Class Name : EgovSampleController.java
 * @Description : EgovSample Controller Class
 * @Modification Information
 * @
 * @  ������      ������              ��������
 * @ ---------   ---------   -------------------------------
 * @ 2009.03.16           ���ʻ���
 *
 * @author ���������ӿ�ũ ����ȯ�� ������
 * @since 2009. 03.16
 * @version 1.0
 * @see
 *
 *  Copyright (C) by MOPAS All right reserved.
 */

@Controller
public class WebController {	

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;

	/** Validator */
	@Resource(name = "beanValidator")
	protected DefaultBeanValidator beanValidator;
	
	@Resource(name = "mapService")
    private mapService mapService;

	LibJson json = new LibJson(); //json ����
	
	LocalDate now = LocalDate.now();
	 
    // ���� ����
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    // ���� ����
    String formatedNow = now.format(formatter);
	
	//�ʱ�ȭ��
	@RequestMapping(value = "/index.do")
	public String selectSampleList(ModelMap model) throws Exception {
	
		//RouteVO vo = mapService.test();
		//System.out.println("vo.getRouteid : "+vo.getRouteid());	
		
		return "emapMin/map/map";
	}
	
	//�ʱ�ȭ��
	@RequestMapping(value = "/test.do")
	public String selectTest(ModelMap model) throws Exception {
	
		//RouteVO vo = mapService.test();
		//System.out.println("vo.getRouteid : "+vo.getRouteid());	
		
		return "emapMin/map/maptest";
	}

	@RequestMapping("weatherDate.do")
	public void weatherDate(HttpServletRequest req, HttpServletResponse res) throws Exception {
		WeatherVO vo = new WeatherVO();

		List<WeatherVO> slist = mapService.weatherDate(vo);		
		if(slist.size() > 0) {
			json.Json(res, slist);
		}
	}
	
	@RequestMapping("weatherTime.do")
	public void weatherTime(HttpServletRequest req, HttpServletResponse res) throws Exception {
		WeatherVO vo = new WeatherVO();
		String dateToTable = (String)req.getParameter("date"); // 20231203
		vo.setDate(dateToTable);

		dateToTable = "tb_" + dateToTable;
		vo.setTableName(dateToTable);

		List<WeatherVO> slist = mapService.weatherTime(vo);		
		if(slist.size() > 0) {
			json.Json(res, slist);
		}
	}
	
	// ���� ���
	@RequestMapping("getWeatherLast1.do")
	public void getWeatherLast1(HttpServletRequest req, HttpServletResponse res) throws Exception {
		WeatherVO vo = new WeatherVO();
		String dateToTable = (String)req.getParameter("date"); // 20231203
		dateToTable = "tb_" + dateToTable;
		vo.setTableName(dateToTable);

		List<WeatherVO> slist = mapService.getWeatherLast1(vo);		
		if(slist.size() > 0) {			
			json.Json(res, slist);
		}
	}
	
	// ���� ���
	@RequestMapping("getWeatherLast2.do")
	public void getWeatherLast2(HttpServletRequest req, HttpServletResponse res) throws Exception {
		WeatherVO vo = new WeatherVO();
		String dateToTable = (String)req.getParameter("date"); // 20231203
		dateToTable = "tb_" + dateToTable;
		vo.setTableName(dateToTable);

		List<WeatherVO> slist = mapService.getWeatherLast2(vo);

	    // �߰����� ������ ����
	    slist.sort(Comparator.comparing(WeatherVO::getLat)
	            .thenComparing(WeatherVO::getLon));

		if(slist.size() > 0) {			
			json.Json(res, slist);
		}
	}
	
	// ���� ���
	@RequestMapping("getWeatherLast2Test.do")
	public void getWeatherLast2Test(HttpServletRequest req, HttpServletResponse res) throws Exception {
		WeatherVO vo = new WeatherVO();
		String dateToTable = (String)req.getParameter("date"); // 20231203
		String time = dateToTable;

		LocalDate time_ = LocalDate.parse(time, DateTimeFormatter.BASIC_ISO_DATE);
        String time__ = time_.format(DateTimeFormatter.ISO_LOCAL_DATE);
		
		dateToTable = "tb_" + dateToTable;
		vo.setTableName(dateToTable);
		
		time = time__ + " " + (String)req.getParameter("time") + ":00:00";
		vo.setTime(time);

		List<WeatherVO> slist = mapService.getWeatherLast2Test(vo);
	    
	    // �߰����� ������ ����
	    slist.sort(Comparator.comparing(WeatherVO::getLat)
	            .thenComparing(WeatherVO::getLon));

		if(slist.size() > 0) {			
			json.Json(res, slist);
		}
	}

	// ��� ����
	@RequestMapping("getWeather.do")
	public void getWeather(HttpServletRequest req, HttpServletResponse res) throws Exception {
		WeatherVO vo = new WeatherVO();
		//vo.setDate((String)req.getParameter("date"));
		String dateToTable = (String)req.getParameter("date"); // 20231203
		vo.setDate(dateToTable);

		dateToTable = "tb_" + dateToTable;
		vo.setTableName(dateToTable);
		//vo.setDate(formattedDate);

		String time = (String)req.getParameter("time");
		/*if (time.length() == 1) // 1�ڸ����� ��� �տ� 0�� �߰�
			time = "0" + time;*/
		vo.setTime(time);

		String degreeStr = (String)req.getParameter("degree");
		float degree;
		if (degreeStr == null)
			degree = 1;
		else
			degree = Float.parseFloat(degreeStr);
		vo.setDegree(degree);

		/*String lat1Str = (String)req.getParameter("lat1");
		float lat1;
		if (lat1Str == null)
			lat1 = -85;
		else
			lat1 = Float.parseFloat(lat1Str);
		lat1 -= degree;
		vo.setLat1(lat1);
		String lat2Str = (String)req.getParameter("lat2");
		float lat2;
		if (lat2Str == null)
			lat2 = 360;
		else
			lat2 = Float.parseFloat(lat2Str);
		lat2 += degree;
		vo.setLat2(lat2);*/
		
		String lat1Str = (String)req.getParameter("lat1");
		float lat1;
		if (lat1Str == null)
			lat1 = -90;
		else
			lat1 = Float.parseFloat(lat1Str);
		lat1 -= degree;
		vo.setLat1(lat1);
		
		String lat2Str = (String)req.getParameter("lat2");
		float lat2;
		if (lat2Str == null)
			lat2 = 90;
		else
			lat2 = Float.parseFloat(lat2Str);
		lat2 += degree;
		vo.setLat2(lat2);

		
		
		
		
		
		
		
		String lon1Str = (String)req.getParameter("lon1");
		float lon1;
		if (lon1Str == null)
			lon1 = 0;
		else {
			lon1 = Float.parseFloat(lon1Str);
			lon1 -= degree;
		}

		String lon2Str = (String)req.getParameter("lon2");
		float lon2;
		if (lon2Str == null)
			lon2 = 360;
		else {
			lon2 = Float.parseFloat(lon2Str);
			lon2 += degree;
		}

		float lon1Real = lon1; // �� : -790
		float lon2Real = lon2; // �� : -790
		float lon1Convert = 0;
		float lon2Convert = 0;
		float lon3 = -1;
		float lon4 = -1;
		//float lonRound = (Math.abs((float) Math.floor(lon1 / 360))) + 1;
		int lonRound = (Math.abs((int) (lon1 / 360))) + 1;
		//float lonRound = 1;
		if (lon1 < 0) {
			lon1Convert = 360 + (lon1 % 360); // �� : 290
			lon2Convert = 360 + (lon2 % 360); // �� : 290
			if (lon2 < 0) {
				lon1 = lon1Convert;
				lon2 = lon2Convert;
			} else {
				lon1 = 0;
				lon3 = lon1Convert;
				lon4 = 360;
			}
		} else if (lon2 > 360) {
			lon1Convert = lon1 % 360; // �� : 290
			lon2Convert = lon2 % 360; // �� : 290
			if (lon1 <= 360) {
				lon2 = 360 - degree;
				lon3 = 0; // ���ʹ� �ݴ��
				lon4 = lon2Convert;
			}
		}
		
		vo.setLon1(lon1);
		vo.setLon2(lon2);
		vo.setLon3(lon3);
		vo.setLon4(lon4);

		List<WeatherVO> slist = mapService.getWeather(vo);

	    // �߰����� ������ ����
	    slist.sort(Comparator.comparing(WeatherVO::getLat)
	            .thenComparing(WeatherVO::getLon));

	    if (lon1Real < -(degree)) { // 0�� �ƴ� -0.5�� ������ ���� ������ �� ���� �������� 0.5���� �� �������� �������Ƿ�
	    //if (lon1Real1 < -(degree) && lon2Real1 > 0 && lon2Real1 < 360) { // 0�� �ƴ� -0.5�� ������ ���� ������ �� ���� �������� 0.5���� �� �������� �������Ƿ�
		//else if (lon1Real1 < -(degree) && lon2Real1 < -(degree)) { // 0�� �ƴ� -0.5�� ������ ���� ������ �� ���� �������� 0.5���� �� �������� �������Ƿ�
			List<WeatherVO> updatedList1 = new ArrayList<>(slist.size());
	
			boolean lonContinue = false; // 339.5�� �ִ��� ���η� 340 ������ ���Ӽ� üũ
			// ��) 340���� 359.5������ �������� -20 ~ -0.5�� ��ȯ
		    for (WeatherVO weatherVO : slist) {
		        float lon = weatherVO.getLon();
		    	if (lon >= (lon1Convert - degree) && lon < lon1Convert)
		    		lonContinue = true;
		        if (lon >= lon1Convert && lon < 360) {
		            // ���ο� WeatherVO ��ü�� ���� �� ����
		            WeatherVO updatedWeatherVO = new WeatherVO();
		            updatedWeatherVO.setLon(lon - (360 * lonRound));
		            // �ٸ� ���� �״�� ����
		            updatedWeatherVO.setLat(weatherVO.getLat());
		            updatedWeatherVO.setAir_temp(weatherVO.getAir_temp());
		            updatedWeatherVO.setWater_temp(weatherVO.getWater_temp());
		            updatedWeatherVO.setU_current(weatherVO.getU_current());
		            updatedWeatherVO.setV_current(weatherVO.getV_current());
		            updatedWeatherVO.setUgrd10m(weatherVO.getUgrd10m());
		            updatedWeatherVO.setVgrd10m(weatherVO.getVgrd10m());
		            updatedWeatherVO.setFsdir(weatherVO.getFsdir());
		            updatedWeatherVO.setFshgt(weatherVO.getFshgt());
		            updatedWeatherVO.setFwaveu(weatherVO.getFwaveu());
		            updatedWeatherVO.setFwavev(weatherVO.getFwavev());
		            // ���ο� ��ü�� ����Ʈ�� �߰�
		            updatedList1.add(updatedWeatherVO);
		        }
		    }
	
			// ���� ����Ʈ�� �߰�
			slist.addAll(updatedList1);
		
			// ����Ʈ ������
			slist.sort(Comparator.comparing(WeatherVO::getLat)
			        .thenComparing(WeatherVO::getLon));
			
			// ���Ӽ� ���� ��� 340 �̻��� ����
			if (!lonContinue) {
				float lon1Final = lon1Convert;
				List<WeatherVO> filteredList = slist.stream()
				        .filter(weather -> weather.getLon() < lon1Final)
				        .collect(Collectors.toList());
	
				slist.clear(); // ���� ����Ʈ�� ���ϴ�.
				slist.addAll(filteredList); // ���͸��� ����Ʈ�� �ٽ� �߰��մϴ�.
			}
		}

	    else if (lon2Real > 360 + degree) {
			List<WeatherVO> updatedList2 = new ArrayList<>(slist.size());
	
			boolean lonContinue = false; // 339.5�� �ִ��� ���η� 340 ������ ���Ӽ� üũ
			// ��) 340���� 359.5������ �������� -20 ~ -0.5�� ��ȯ
		    for (WeatherVO weatherVO : slist) {
		        float lon = weatherVO.getLon();
		    	if (lon >= (lon1Convert - degree) && lon < lon1Convert)
		    		lonContinue = true;
		        if (lon <= lon2Convert && lon >= 0) {
		        //if ((lon >= lon1Convert && lon < 360) || (lon <= lon2Convert && lon >= 0)) {
		            // ���ο� WeatherVO ��ü�� ���� �� ����
		            WeatherVO updatedWeatherVO = new WeatherVO();
		            updatedWeatherVO.setLon(lon + (360 * lonRound));
		            // �ٸ� ���� �״�� ����
		            updatedWeatherVO.setLat(weatherVO.getLat());
		            updatedWeatherVO.setAir_temp(weatherVO.getAir_temp());
		            updatedWeatherVO.setWater_temp(weatherVO.getWater_temp());
		            updatedWeatherVO.setU_current(weatherVO.getU_current());
		            updatedWeatherVO.setV_current(weatherVO.getV_current());
		            updatedWeatherVO.setUgrd10m(weatherVO.getUgrd10m());
		            updatedWeatherVO.setVgrd10m(weatherVO.getVgrd10m());
		            updatedWeatherVO.setFsdir(weatherVO.getFsdir());
		            updatedWeatherVO.setFshgt(weatherVO.getFshgt());
		            updatedWeatherVO.setFwaveu(weatherVO.getFwaveu());
		            updatedWeatherVO.setFwavev(weatherVO.getFwavev());
		            // ���ο� ��ü�� ����Ʈ�� �߰�
		            updatedList2.add(updatedWeatherVO);
		        }
		    }
	
			// ���� ����Ʈ�� �߰�
			slist.addAll(updatedList2);
		
			// ����Ʈ ������
			slist.sort(Comparator.comparing(WeatherVO::getLat)
			        .thenComparing(WeatherVO::getLon));
			
			// ���Ӽ� ���� ��� 340 �̻��� ����
			if (!lonContinue) {
				float lon1Final = lon1Convert;
				float lon2Final = lon2Convert;
				List<WeatherVO> filteredList = slist.stream()
				        .filter(weather -> weather.getLon() >= lon1Final)
				        .collect(Collectors.toList());
	
				slist.clear(); // ���� ����Ʈ�� ���ϴ�.
				slist.addAll(filteredList); // ���͸��� ����Ʈ�� �ٽ� �߰��մϴ�.
			}
		}
		    
		if(slist.size() > 0) {
			json.Json(res, slist);
		}
		
		
		
		
		
		
		
		
		
		
		
		
		/*String lon1Str = (String)req.getParameter("lon1");
		float lon1;
		if (lon1Str == null)
			lon1 = 0;
		else
			lon1 = Float.parseFloat(lon1Str);
		lon1 -= degree;
		vo.setLon1(lon1);
		
		String lon2Str = (String)req.getParameter("lon2");
		float lon2;
		if (lon2Str == null || Float.parseFloat(lon2Str) >= 340) // �浵 �ִ�ġ 340 �̻��̸� 360���� ���� (�浵 -20 ~ 0 �� ó�� ����)
			lon2 = 360;
		else {
			lon2 = Float.parseFloat(lon2Str);
			lon2 += degree;
		}
		vo.setLon2(lon2);
		
		float lon3;
		float lon4;
		if (lon1 < 0 && lon2 < 340) {
			lon3 = 340;
			lon4 = 360; // �������� �� �κ��� < ó���Ǿ 359.5�� ��
		} else {
			lon3 = -1;
			lon4 = -1;
		}
		vo.setLon3(lon3);
		vo.setLon4(lon4);

		List<WeatherVO> slist = mapService.getWeather(vo);

	    // �߰����� ������ ����
	    slist.sort(Comparator.comparing(WeatherVO::getLat)
	            .thenComparing(WeatherVO::getLon));

	    
	    //String uvTruePre = (String)req.getParameter("uv");
	    //int uvTrue = Integer.parseInt(uvTruePre);
	    //if (uvTrue == 1 && lon1 < -0.5 && (lon2 == 360 || lon4 == 360)) {
	    if (lon1 < -0.5 && (lon2 == 360 || lon4 == 360)) {
			// �浵 -20 ~ -0.5 �߰�
			List<WeatherVO> updatedList = new ArrayList<>(slist.size());
	
			boolean under340 = false; // 339.5�� �ִ��� ���η� 340 ������ ���Ӽ� üũ
			// 340���� 359.5������ �������� -20 ~ -0.5�� ��ȯ
		    for (WeatherVO weatherVO : slist) {
		        float lon = weatherVO.getLon();
		    	if (lon == 339.5)
		    		under340 = true;
		        if (lon >= 340 && lon < 360) {
		            // ���ο� WeatherVO ��ü�� ���� �� ����
		            WeatherVO updatedWeatherVO = new WeatherVO();
		            updatedWeatherVO.setLon(lon - 360);
		            // �ٸ� ���� �״�� ����
		            updatedWeatherVO.setLat(weatherVO.getLat());
		            updatedWeatherVO.setAir_temp(weatherVO.getAir_temp());
		            updatedWeatherVO.setWater_temp(weatherVO.getWater_temp());
		            updatedWeatherVO.setU_current(weatherVO.getU_current());
		            updatedWeatherVO.setV_current(weatherVO.getV_current());
		            updatedWeatherVO.setUgrd10m(weatherVO.getUgrd10m());
		            updatedWeatherVO.setVgrd10m(weatherVO.getVgrd10m());
		            updatedWeatherVO.setFsdir(weatherVO.getFsdir());
		            updatedWeatherVO.setFshgt(weatherVO.getFshgt());
		            updatedWeatherVO.setFwaveu(weatherVO.getFwaveu());
		            updatedWeatherVO.setFwavev(weatherVO.getFwavev());
		            // ���ο� ��ü�� ����Ʈ�� �߰�
		            updatedList.add(updatedWeatherVO);
		        }
		    }
	
			// ���� ����Ʈ�� �߰�
			slist.addAll(updatedList);
		
			// ����Ʈ ������
			slist.sort(Comparator.comparing(WeatherVO::getLat)
			        .thenComparing(WeatherVO::getLon));
			
			// ���Ӽ� ���� ��� 340 �̻��� ����
			//if (under340) {
				List<WeatherVO> filteredList = slist.stream()
				        .filter(weather -> weather.getLon() < 340)
				        .collect(Collectors.toList());
	
				slist.clear(); // ���� ����Ʈ�� ���ϴ�.
				slist.addAll(filteredList); // ���͸��� ����Ʈ�� �ٽ� �߰��մϴ�.
			//}

		}
		    
		if(slist.size() > 0) {
			json.Json(res, slist);
		}*/
	}

	// ��� ���� (�˾�)
	@RequestMapping("getWeatherPopup.do")
	public void getWeatherPopup(HttpServletRequest req, HttpServletResponse res) throws Exception {
		WeatherVO vo = new WeatherVO();
		String dateToTable = (String)req.getParameter("date"); // 20231203
		vo.setDate(dateToTable);

		dateToTable = "tb_" + dateToTable;
		vo.setTableName(dateToTable);

		String lat = (String)req.getParameter("lat");
		float floatLat = Float.parseFloat(lat);
		vo.setLat(floatLat);
		String lon = (String)req.getParameter("lon");
		float floatLon = Float.parseFloat(lon);
		vo.setLon(floatLon);

		List<WeatherVO> slist = mapService.getWeatherPopup(vo);		
		if(slist.size() > 0) {
			json.Json(res, slist);
		}
	}
}
