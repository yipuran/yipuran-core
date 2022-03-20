package org.yipuran.util.resource;

import java.io.File;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import org.yipuran.file.FilenameFilterImpl;
/**
 * アプリケーション属性、全ての properties の読込み(UTF-8 版).
 * <pre>
 * ApplicationProperties の 文字エンコード UTF-8 バージョンです。
 * MS932 で読む必要がある場合は、ApplicationProperties を使用してください
 *
 * CLASSPATH で参照可能な位置にある全ての properties を読込み、
 * 再読込みを回避する Singleton として提供する。
 * CLASSPATH が示すディレクトリを探索して見つかる properties が複数あり、キーが重複する場合
 * java.io.File#list(FilenameFilter) での探索、^.*\.properties$ での探索結果の
 * 後方で見つかった重複キーが採用されることに注意すべきである。
 * 読込んだ属性の時間による再読込み（Resource のキャッシュ破棄、再読込）はここでは実装していません。
 * java.util.ResourceBundle.Control を別途オーバーライドして作成してください。
 *
 * </pre>
 * @since 1.1.6
 */
public final class ApplicationPropertiesUTF8{
   private static ApplicationPropertiesUTF8 inst;
   private Properties prop;

   private ApplicationPropertiesUTF8(){
      this.prop = new Properties();
      String[] clspaths = System.getProperty("java.class.path").split(java.io.File.pathSeparator);
      for(int i=0;i < clspaths.length;i++){
         File f = new File(clspaths[i]);
         if (f.isDirectory()){
            String[] fnames = f.list(new FilenameFilterImpl("^.*\\.properties$"));
            for(int k=0;k < fnames.length;k++){
               ResourceBundle rs = ResourceBundle.getBundle(fnames[k].replaceAll("\\.properties$","")
                                                            ,new ResourceBundleControl("UTF-8"));
               for(Enumeration<String> en=rs.getKeys();en.hasMoreElements();){
                  String key = en.nextElement();
                  this.prop.setProperty(key,rs.getString(key));
               }
            }
         }
      }
   }

   /**
    * Singleton インスタンス取得
    * @return ApplicationPropertiesインスタンス
    */
   public final static synchronized ApplicationPropertiesUTF8 getInstance(){
      if (inst==null){
         inst = new ApplicationPropertiesUTF8();
      }
      return inst;
   }
   /**
    * 追加または再読込み.
    * @param baseName propertiesファイルのbaseName
    */
   public void load(String baseName){
      ResourceBundle rs = ResourceBundle.getBundle(baseName,new ResourceBundleControl("UTF-8"));
      for(Enumeration<String> en=rs.getKeys();en.hasMoreElements();){
         String key = en.nextElement();
         this.prop.setProperty(key,rs.getString(key));
      }
   }
   /**
    * 属性を全て破棄して指定のbaseNameで読込み
    * @param baseName
    */
   public void clearAndLoad(String baseName){
      this.prop.clear();
      ResourceBundle rs = ResourceBundle.getBundle(baseName,new ResourceBundleControl("UTF-8"));
      for(Enumeration<String> en=rs.getKeys();en.hasMoreElements();){
         String key = en.nextElement();
         this.prop.setProperty(key,rs.getString(key));
      }
   }

   /**
    * 属性値の取得.
    * @param key 属性Key
    * @return 属性値
    */
   public String getProperty(String key){
      return this.prop.getProperty(key);
   }

   /**
    * 属性値（int型）の取得.
    * @param key 属性Key
    * @return 属性値 （int型）
    */
   public int getInt(String key){
      return Integer.parseInt(this.prop.getProperty(key));
   }

   /**
    * 属性Key のSet取得
    * @return 属性Key のSet
    */
   public Set<Object> keys(){
      return this.prop.keySet();
   }
}
