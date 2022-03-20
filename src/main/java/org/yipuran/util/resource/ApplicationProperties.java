package org.yipuran.util.resource;

import java.io.File;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import org.yipuran.file.FilenameFilterImpl;

/**
 * アプリケーション属性、全ての properties の読込み.
 * <pre>
 * CLASSPATH で参照可能な位置にある全ての properties を読込み、
 * 再読込みを回避する Singleton として提供する。
 * CLASSPATH が示すディレクトリを探索して見つかる properties が複数あり、キーが重複する場合
 * java.io.File#list(FilenameFilter) での探索、^.*\.properties$ での探索結果の
 * 後方で見つかった重複キーが採用されることに注意すべきである。
 * 読込んだ属性の時間による再読込み（Resource のキャッシュ破棄、再読込）はここでは実装していません。
 * java.util.ResourceBundle.Control を別途オーバーライドして作成してください。
 *
 * propertiesファイルの value に日本語文字、２バイト文字を含む場合の対処として、
 * java.util.ResourceBundle.Control#newBundle をオーバーライドした ResourceBundleControl を
 * 使用している。このクラスは、エンコードをSystem.getProperty("file.encoding")で取得したものを
 * 使用している
 * </pre>
 * @since 1.1.6
 */
public final class ApplicationProperties{
   private static ApplicationProperties inst;
   private Properties prop;

   private ApplicationProperties(){
      this.prop = new Properties();
      String[] clspaths = System.getProperty("java.class.path").split(java.io.File.pathSeparator);
      for(int i=0;i < clspaths.length;i++){
         File f = new File(clspaths[i]);
         if (f.isDirectory()){
            String[] fnames = f.list(new FilenameFilterImpl("^.*\\.properties$"));
            for(int k=0;k < fnames.length;k++){
               ResourceBundle rs = ResourceBundle.getBundle(fnames[k].replaceAll("\\.properties$","")
                                                           ,new ResourceBundleControl(System.getProperty("file.encoding")));
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
   public final static synchronized ApplicationProperties getInstance(){
      if (inst==null){
         inst = new ApplicationProperties();
      }
      return inst;
   }
   /**
    * 追加または再読込み.
    * @param baseName propertiesファイルのbaseName
    */
   public void load(String baseName){
      ResourceBundle rs = ResourceBundle.getBundle(baseName,new ResourceBundleControl("MS932"));
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
      ResourceBundle rs = ResourceBundle.getBundle(baseName,new ResourceBundleControl("MS932"));
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
