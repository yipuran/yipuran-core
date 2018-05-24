package org.yipuran.aop;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * AopProcessor
 */
public interface AopProcessor{
	public boolean preMethod(List<Annotation> manotations,List<List<Annotation>> panotations,Object[] paramValues);
	public void finish(Object value);
}
