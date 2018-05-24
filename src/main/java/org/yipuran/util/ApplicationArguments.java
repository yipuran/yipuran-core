package org.yipuran.util;

/**
 * main()クラス引数→スレッドローカル変数.
 * <pre>
 * public static void main(String[] args)メソッドで受け取った String[] argsを
 * スレッド変数として持ち回る。
 *     public static void main(String[] args){
 *        ApplicationArguments.set(args);       // この後、有効になる
 * 子スレッドにも自動的に引き継がれます。
 * </pre>
 * @since 1.1.6
 */
public class ApplicationArguments{
   private static ThreadLocal<String[]> args;
   static int argleng;
   private ApplicationArguments(){}

   /**
    * main()引数のセット.
    *
    * <br>  public static void main(String[] args){ 直後に行う必要があります。
    * @param _args String[]main()引数
    */
   public final static void set(String[] _args){
      if (args==null){
         argleng = _args.length;
         args = new InheritableThreadLocal<String[]>(){
            @Override
            protected synchronized String[] initialValue(){
               return new String[argleng];
            }
         };
      }
      args.set(_args);
   }
   /**
    * main()引数の参照
    * @return main()引数String[]
    */
   public final static String[] get(){
      if (args==null){
         args = new InheritableThreadLocal<String[]>(){
            @Override
            protected synchronized String[] initialValue(){
               return new String[0];
            }
         };
      }
      return args.get();
   }
}
