package org.yipuran.aop;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
/**
 * AopModule.
 * インターセプタをbind 定義する インターセプタの前処理、後処理として
 * AopProcessor を指定する
 */
public class AopModule extends AbstractModule{
	private Class<? extends Annotation> methodAnnotationType;
	private AopProcessor aopProcessor;
	/**
	 * コンストラクタ
	 * @param methodAnnotationType 対象メソッドに付けられたアノテーション
	 * @param aopProcessor インターセプタの前処理、後処理を指定
	 */
	public AopModule(Class<? extends Annotation> methodAnnotationType,AopProcessor aopProcessor){
		this.methodAnnotationType = methodAnnotationType;
		this.aopProcessor = aopProcessor;
	}
	/* (非 Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	@Override
	protected void configure(){
		binder().bindInterceptor(Matchers.any(), Matchers.annotatedWith(this.methodAnnotationType), new _Interceptor(this.aopProcessor));
	}
	protected class _Interceptor implements MethodInterceptor{
		private AopProcessor process;
		protected _Interceptor(AopProcessor process){
			this.process = process;
		}
		/* (非 Javadoc)
		 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
		 */
		@Override
		public Object invoke(MethodInvocation m) throws Throwable{
			Object rtn = null;
			List<Annotation> mlist = new ArrayList<Annotation>();
			for(Annotation a : m.getMethod().getAnnotations()){
				mlist.add(a);
			}
			List<List<Annotation>> list = new ArrayList<List<Annotation>>();
			Annotation[][] ans = m.getMethod().getParameterAnnotations();
			for(int i=0;i < ans.length;i++){
				List<Annotation> al = new ArrayList<Annotation>();
				for(int n=0;n < ans[i].length;n++){
					al.add(ans[i][n]);
				}
				list.add(al);
			}
			if (this.process.preMethod(mlist,list,m.getArguments())){
				rtn = m.proceed();
			}
			this.process.finish(rtn);
			return rtn;
		}
	}
}
