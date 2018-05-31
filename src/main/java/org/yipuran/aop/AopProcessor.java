package org.yipuran.aop;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * ＡＯＰ処理インターフェース.
 * ＡＯＰ処理対象のクラスが実装するインターフェース
 */
public interface AopProcessor{
	/**
	 * 前処理.
	 * @param manotations メソッド付与されたアノテーションリスト
	 * @param panotations メソッドのパラメータに付与されたアノテーションリストのリスト
	 * @param paramValues メソッドパラメータ
	 * @return 前処理の実行が正常なら true, false はメソッドを実行しない。
	 */
	public boolean preMethod(List<Annotation> manotations, List<List<Annotation>> panotations, Object[] paramValues);

	/**
	 * 後処理.
	 * @param value メソッドの戻り値
	 */
	public void finish(Object value);
}
