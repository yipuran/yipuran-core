package org.yipuran.provider;

import java.io.IOException;
import java.util.Properties;

import com.google.inject.throwingproviders.CheckedProvider;
/**
 * プロパティ Provider インターフェース.
 */
public interface IPropertiesProvider extends CheckedProvider<Properties>{
	@Override
	public Properties get() throws IOException;

}
