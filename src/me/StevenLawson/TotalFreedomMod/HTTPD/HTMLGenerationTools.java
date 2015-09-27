/*    */ package me.StevenLawson.TotalFreedomMod.HTTPD;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.Set;
/*    */ import org.apache.commons.lang3.StringEscapeUtils;
/*    */ 
/*    */ public class HTMLGenerationTools
/*    */ {
/*    */   private HTMLGenerationTools()
/*    */   {
/* 12 */     throw new AssertionError();
/*    */   }
/*    */   
/*    */   public static String paragraph(String data)
/*    */   {
/* 17 */     return "<p>" + StringEscapeUtils.escapeHtml4(data) + "</p>\r\n";
/*    */   }
/*    */   
/*    */   public static String heading(String data, int level)
/*    */   {
/* 22 */     return "<h" + level + ">" + StringEscapeUtils.escapeHtml4(data) + "</h" + level + ">\r\n";
/*    */   }
/*    */   
/*    */   public static <K, V> String list(Map<K, V> map)
/*    */   {
/* 27 */     StringBuilder output = new StringBuilder();
/*    */     
/* 29 */     output.append("<ul>\r\n");
/*    */     
/* 31 */     Iterator<Map.Entry<K, V>> it = map.entrySet().iterator();
/* 32 */     while (it.hasNext())
/*    */     {
/* 34 */       Map.Entry<K, V> entry = (Map.Entry)it.next();
/* 35 */       output.append("<li>").append(StringEscapeUtils.escapeHtml4(entry.getKey().toString() + " = " + entry.getValue().toString())).append("</li>\r\n");
/*    */     }
/* 38 */     output.append("</ul>\r\n");
/*    */     
/* 40 */     return output.toString();
/*    */   }
/*    */   
/*    */   public static <T> String list(Collection<T> list)
/*    */   {
/* 45 */     StringBuilder output = new StringBuilder();
/*    */     
/* 47 */     output.append("<ul>\r\n");
/* 49 */     for (T entry : list) {
/* 51 */       output.append("<li>").append(StringEscapeUtils.escapeHtml4(entry.toString())).append("</li>\r\n");
/*    */     }
/* 54 */     output.append("</ul>\r\n");
/*    */     
/* 56 */     return output.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\HTTPD\HTMLGenerationTools.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */