package com.noob.databurialpoint;

import com.fooww.soft.android.DataModel.Entity.FunctionId;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by xiaoqi on 2018/3/15
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Statistics {
	FunctionId functionId() default FunctionId.DEFAULT;
}
