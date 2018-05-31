package org.yipuran.http;

import java.io.Serializable;

/**
 * HTTP応答データ.
 * <pre>
 * HttpUtil が返す応答データを格納するオブジェクトです。
 * HTTP応答コードが識別できない (有効な HTTP でない) 場合、 →getResponseCode()の結果が、-1 の時は、
 * getResponseMessage()「HTTP応答コードメッセージ」の結果は NULL です。
 * HTTP応答コードが、２００番台でない場合、HTTP応答コードメッセージを参照すべきです。
 * HTTP応答コード 503 は、Exception 捕捉でしか取得できないことに注意！
 * </pre>
 */
public final class HttpUtilResponse implements Serializable{
   private static final long serialVersionUID = 1L;
   /** HTTP応答コード */
   public int responseCode;

   /** HTTP応答コードメッセージ */
   public String responseMessage;

   /** HTTP Conetent-Type */
   public String contentType;

   /** Content-Language */
   public String contentLanguage;

   /** Content-Date */
   public String contentDate;

   /** Content-Language */
   public String contentLength;

   /** コンテンツメッセージ */
   public String message;

   /** デフォルトコンストラクタ */
   public HttpUtilResponse(){}

   /**
    * HTTP応答コード取得
    * @return HTTP応答コード
    */
   public int getResponseCode() {
      return this.responseCode;
   }
   /**
    * HTTP応答コードset
    * @param responseCode HTTP応答コード
    */
   public void setResponseCode(int responseCode) {
      this.responseCode = responseCode;
   }
   /**
    * HTTP応答コードメッセージ取得
    * @return HTTP応答コードメッセージ
    */
   public String getResponseMessage() {
      return this.responseMessage;
   }
   /**
    * HTTP応答コードメッセージset
    * @param responseMessage HTTP応答コードメッセージ
    */
   public void setResponseMessage(String responseMessage) {
      this.responseMessage = responseMessage;
   }
   /**
    * コンテンツメッセージ取得
    * @return コンテンツメッセージ
    */
   public String getMessage() {
      return this.message;
   }
   /**
    * コンテンツメッセージset
    * @param message コンテンツメッセージ
    */
   public void setMessage(String message) {
      this.message = message;
   }
   /**
    * HTTP Content-Type 参照
    * @return
    */
   public String getContentType(){
      return this.contentType;
   }
   /**
    * HTTP Content-Type set
    * @param contentType
    */
   public void setContentType(String contentType){
      this.contentType = contentType;
   }

   public String getContentLanguage(){
      return this.contentLanguage;
   }

   public void setContentLanguage(String contentLanguage){
      this.contentLanguage = contentLanguage;
   }

   public String getContentDate(){
      return this.contentDate;
   }

   public void setContentDate(String contentDate){
      this.contentDate = contentDate;
   }

   public String getContentLength(){
      return this.contentLength;
   }

   public void setContentLength(String contentLength){
      this.contentLength = contentLength;
   }
}
