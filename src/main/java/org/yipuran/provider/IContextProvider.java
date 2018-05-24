package org.yipuran.provider;

import javax.naming.Context;

import com.google.inject.throwingproviders.CheckedProvider;
/**
 * Context Provider インターフェース.
 */
public interface IContextProvider extends CheckedProvider<Context>{
	@Override
	public Context get() throws Exception ;
}
