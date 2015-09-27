/*     */ package me.StevenLawson.TotalFreedomMod.HTTPD;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ public class Module_dump
/*     */   extends TFM_HTTPD_Module
/*     */ {
/*  15 */   private File echoFile = null;
/*     */   private final String body;
/*     */   
/*     */   public Module_dump(NanoHTTPD.HTTPSession session)
/*     */   {
/*  20 */     super(session);
/*     */     
/*  23 */     body = body();
/*     */   }
/*     */   
/*     */   public NanoHTTPD.Response getResponse()
/*     */   {
/*  29 */     String echo = (String)params.get("echo");
/*  30 */     boolean doEcho = (echo != null) && (((echo = echo.toLowerCase().trim()).equalsIgnoreCase("true")) || (echo.equalsIgnoreCase("1")));
/*  32 */     if ((doEcho) && (echoFile != null) && (echoFile.exists())) {
/*  34 */       return TFM_HTTPD_Manager.serveFileBasic(echoFile);
/*     */     }
/*  38 */     return super.getResponse();
/*     */   }
/*     */   
/*     */   public String getBody()
/*     */   {
/*  45 */     return body;
/*     */   }
/*     */   
/*     */   private String body()
/*     */   {
/*  50 */     StringBuilder responseBody = new StringBuilder();
/*     */     
/*  52 */     String remoteAddress = socket.getInetAddress().getHostAddress();
/*     */     
/*  54 */     String[] args = StringUtils.split(uri, "/");
/*     */     
/*  56 */     Map<String, String> files = getFiles();
/*     */     
/*  58 */     responseBody.append(HTMLGenerationTools.paragraph("URI: " + uri)).append(HTMLGenerationTools.paragraph("args (Length: " + args.length + "): " + StringUtils.join(args, ","))).append(HTMLGenerationTools.paragraph("Method: " + method.toString())).append(HTMLGenerationTools.paragraph("Remote Address: " + remoteAddress)).append(HTMLGenerationTools.paragraph("Headers:")).append(HTMLGenerationTools.list(headers)).append(HTMLGenerationTools.paragraph("Params:")).append(HTMLGenerationTools.list(params)).append(HTMLGenerationTools.paragraph("Files:")).append(HTMLGenerationTools.list(files));
/*     */     
/*  70 */     Iterator<Map.Entry<String, String>> it = files.entrySet().iterator();
/*  71 */     while (it.hasNext())
/*     */     {
/*  73 */       Map.Entry<String, String> entry = (Map.Entry)it.next();
/*  74 */       String formName = (String)entry.getKey();
/*  75 */       String tempFileName = (String)entry.getValue();
/*  76 */       String origFileName = (String)params.get(formName);
/*     */       
/*  78 */       File tempFile = new File(tempFileName);
/*  79 */       if (tempFile.exists())
/*     */       {
/*  81 */         echoFile = tempFile;
/*  83 */         if (!origFileName.contains("../"))
/*     */         {
/*  88 */           String targetFileName = "./public_html/uploads/" + origFileName;
/*     */           
/*  90 */           File targetFile = new File(targetFileName);
/*     */           try
/*     */           {
/*  94 */             FileUtils.copyFile(tempFile, targetFile);
/*     */           }
/*     */           catch (IOException ex)
/*     */           {
/*  98 */             TFM_Log.severe(ex);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 103 */     return responseBody.toString();
/*     */   }
/*     */   
/*     */   public String getTitle()
/*     */   {
/* 109 */     return "TotalFreedomMod :: Request Debug Dumper";
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\HTTPD\Module_dump.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */