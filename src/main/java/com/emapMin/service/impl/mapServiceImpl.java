package com.emapMin.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.emapMin.WeatherVO;
import com.emapMin.service.mapService;
import com.emapMin.RouteDetailVO;
import com.emapMin.RouteVO;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * @Class Name : EgovCmmUseServiceImpl.java
 * @Description : 공통코드등 전체 업무에서 공용해서 사용해야 하는 서비스를 정의하기위한 서비스 구현 클래스
 * @Modification Information
 *
 *    수정일       수정자         수정내용
 *    -------        -------     -------------------
 *    2009. 3. 11.     이삼섭
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 3. 11.
 * @version
 * @see
 *
 */
@Service("mapService")
public class mapServiceImpl extends EgovAbstractServiceImpl implements mapService {

	@Resource(name = "mapDAO")
	private mapDAO mapDAO;

	/**
	 * 공통코드를 조회한다.
	 *
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<WeatherVO> weatherDate(WeatherVO vo) throws Exception {
		return mapDAO.weatherDate(vo);
	}

	@Override
	public List<WeatherVO> weatherTime(WeatherVO vo) throws Exception {
		return mapDAO.weatherTime(vo);
	}

	@Override
	public List<WeatherVO> getWeatherLast1(WeatherVO vo) throws Exception {
		return mapDAO.getWeatherLast1(vo);
	}

	@Override
	public List<WeatherVO> getWeatherLast2(WeatherVO vo) throws Exception {
		return mapDAO.getWeatherLast2(vo);
	}

	@Override
	public List<WeatherVO> getWeatherLast2Test(WeatherVO vo) throws Exception {
		return mapDAO.getWeatherLast2Test(vo);
	}

	@Override
	public List<WeatherVO> getWeather(WeatherVO vo) throws Exception {
		return mapDAO.getWeather(vo);
	}

	@Override
	public List<WeatherVO> getWeatherPopup(WeatherVO vo) throws Exception {
		return mapDAO.getWeatherPopup(vo);
	}
	
	@Override
	public List<RouteVO> getRouteList(RouteVO vo) throws Exception {
		return mapDAO.getRouteList(vo);
	}

	@Override
	public void RouteInsert(RouteVO vo) throws Exception {
		mapDAO.RouteInsert(vo);
	}

	@Override
	public int RouteUpdate(RouteVO vo) throws Exception {
		return mapDAO.RouteUpdate(vo);
	}

	@Override
	public int RouteDelete(RouteDetailVO vo) throws Exception {
		return mapDAO.RouteDelete(vo);
	}
	
	@Override
	public List<RouteDetailVO> getRouteDetailList(RouteVO vo) throws Exception {
		return mapDAO.getRouteDetailList(vo);
	}

	@Override
	public void RouteDetailInsert(RouteDetailVO vo) throws Exception {
		mapDAO.RouteDetailInsert(vo);
	}

	@Override
	public int RouteDetailDelete(RouteDetailVO vo) throws Exception {
		return mapDAO.RouteDetailDelete(vo);
	}
}
