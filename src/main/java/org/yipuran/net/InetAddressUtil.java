package org.yipuran.net;

import java.net.InetAddress;

/**
 * ＨＯＳＴ名取得
 * @since 1.0.0
 */
public final class InetAddressUtil {
	private static String hostName;
	static {
		try{
		InetAddress inet = InetAddress.getLocalHost();
		hostName = inet.getHostName();
		} catch (Exception e) {
			hostName = "unknown host";
		}
	}
	private InetAddressUtil(){}
	
	/**
	 * ＨＯＳＴ名を戻す
	 * @return ＨＯＳＴ名
	 */
	public static String getHostName () {
		return hostName;
	}
}
