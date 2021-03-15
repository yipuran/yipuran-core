package org.yipuran.util;

import java.io.Serializable;

/**
 * Generic enum interface.
 */
public interface EnumBase<E extends Enum<E>> extends Serializable {
	/**
	 * コード値を返却.
	 * @return コード値
	 */
	public Object getValue();

}
