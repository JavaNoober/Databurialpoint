package com.noob.databurialpoint;

import com.fooww.soft.android.Application.AppLike;
import com.fooww.soft.android.DataAccessLayer.DBManager;
import com.fooww.soft.android.DataModel.Entity.FeatureEventTracksEntity;
import com.fooww.soft.android.DataModel.Entity.FeatureId;
import com.fooww.soft.android.DataModel.Entity.FunctionId;
import com.fooww.soft.android.Utility.DateUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by xiaoqi on 2018/3/13
 */

@Aspect
public class StatisticsInstrumentation {

	public static final String TAG = "Statistics";

	@Around("execution(@com.noob.databurialpoint.Statistics * *(..)) && @annotation(statistics)")
	public void aroundJoinPoint(ProceedingJoinPoint joinPoint, Statistics statistics) throws Throwable {
		calculate(statistics);
		joinPoint.proceed();//执行原方法
	}

	private void calculate(Statistics statistics){
		if(statistics != null){

		}
	}
}
