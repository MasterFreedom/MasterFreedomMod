/*     */ package me.StevenLawson.TotalFreedomMod.HTTPD;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ public class Module_file
/*     */   extends TFM_HTTPD_Module
/*     */ {
/*  24 */   private final File rootDir = new File(TFM_ConfigEntry.HTTPD_PUBLIC_FOLDER.getString());
/*  25 */   public static final Map<String, String> MIME_TYPES = new HashMap();
/*     */   
/*     */   static
/*     */   {
/*  29 */     MIME_TYPES.put("css", "text/css");
/*  30 */     MIME_TYPES.put("htm", "text/html");
/*  31 */     MIME_TYPES.put("html", "text/html");
/*  32 */     MIME_TYPES.put("xml", "text/xml");
/*  33 */     MIME_TYPES.put("java", "text/x-java-source, text/java");
/*  34 */     MIME_TYPES.put("txt", "text/plain");
/*  35 */     MIME_TYPES.put("asc", "text/plain");
/*  36 */     MIME_TYPES.put("yml", "text/yaml");
/*  37 */     MIME_TYPES.put("gif", "image/gif");
/*  38 */     MIME_TYPES.put("jpg", "image/jpeg");
/*  39 */     MIME_TYPES.put("jpeg", "image/jpeg");
/*  40 */     MIME_TYPES.put("png", "image/png");
/*  41 */     MIME_TYPES.put("mp3", "audio/mpeg");
/*  42 */     MIME_TYPES.put("m3u", "audio/mpeg-url");
/*  43 */     MIME_TYPES.put("mp4", "video/mp4");
/*  44 */     MIME_TYPES.put("ogv", "video/ogg");
/*  45 */     MIME_TYPES.put("flv", "video/x-flv");
/*  46 */     MIME_TYPES.put("mov", "video/quicktime");
/*  47 */     MIME_TYPES.put("swf", "application/x-shockwave-flash");
/*  48 */     MIME_TYPES.put("js", "application/javascript");
/*  49 */     MIME_TYPES.put("pdf", "application/pdf");
/*  50 */     MIME_TYPES.put("doc", "application/msword");
/*  51 */     MIME_TYPES.put("ogg", "application/x-ogg");
/*  52 */     MIME_TYPES.put("zip", "application/octet-stream");
/*  53 */     MIME_TYPES.put("exe", "application/octet-stream");
/*  54 */     MIME_TYPES.put("class", "application/octet-stream");
/*     */   }
/*     */   
/*     */   public Module_file(NanoHTTPD.HTTPSession session)
/*     */   {
/*  59 */     super(session);
/*     */   }
/*     */   
/*     */   private File getRootDir()
/*     */   {
/*  64 */     return rootDir;
/*     */   }
/*     */   
/*     */   private String encodeUri(String uri)
/*     */   {
/*  69 */     String newUri = "";
/*  70 */     StringTokenizer st = new StringTokenizer(uri, "/ ", true);
/*  71 */     while (st.hasMoreTokens())
/*     */     {
/*  73 */       String tok = st.nextToken();
/*  74 */       if (tok.equals("/")) {
/*  76 */         newUri = newUri + "/";
/*  78 */       } else if (tok.equals(" ")) {
/*  80 */         newUri = newUri + "%20";
/*     */       } else {
/*     */         try
/*     */         {
/*  86 */           newUri = newUri + URLEncoder.encode(tok, "UTF-8");
/*     */         }
/*     */         catch (UnsupportedEncodingException ignored) {}
/*     */       }
/*     */     }
/*  93 */     return newUri;
/*     */   }
/*     */   
/*     */   public NanoHTTPD.Response serveFile(String uri, Map<String, String> params, File homeDir)
/*     */   {
/*  98 */     NanoHTTPD.Response res = null;
/* 101 */     if (!homeDir.isDirectory()) {
/* 103 */       res = new NanoHTTPD.Response(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "INTERNAL ERRROR: serveFile(): given homeDir is not a directory.");
/*     */     }
/* 106 */     if (res == null)
/*     */     {
/* 109 */       uri = uri.trim().replace(File.separatorChar, '/');
/* 110 */       if (uri.indexOf('?') >= 0) {
/* 112 */         uri = uri.substring(0, uri.indexOf('?'));
/*     */       }
/* 116 */       if ((uri.startsWith("src/main")) || (uri.endsWith("src/main")) || (uri.contains("../"))) {
/* 118 */         res = new NanoHTTPD.Response(NanoHTTPD.Response.Status.FORBIDDEN, "text/plain", "FORBIDDEN: Won't serve ../ for security reasons.");
/*     */       }
/*     */     }
/* 122 */     File f = new File(homeDir, uri);
/* 123 */     if ((res == null) && (!f.exists())) {
/* 125 */       res = new NanoHTTPD.Response(NanoHTTPD.Response.Status.NOT_FOUND, "text/plain", "Error 404, file not found.");
/*     */     }
/* 129 */     if ((res == null) && (f.isDirectory()))
/*     */     {
/* 133 */       if (!uri.endsWith("/"))
/*     */       {
/* 135 */         uri = uri + "/";
/* 136 */         res = new NanoHTTPD.Response(NanoHTTPD.Response.Status.REDIRECT, "text/html", "<html><body>Redirected: <a href=\"" + uri + "\">" + uri + "</a></body></html>");
/*     */         
/* 138 */         res.addHeader("Location", uri);
/*     */       }
/* 141 */       if (res == null) {
/* 144 */         if (new File(f, "index.html").exists()) {
/* 146 */           f = new File(homeDir, uri + "/index.html");
/* 148 */         } else if (new File(f, "index.htm").exists()) {
/* 150 */           f = new File(homeDir, uri + "/index.htm");
/* 152 */         } else if (f.canRead()) {
/* 155 */           res = new NanoHTTPD.Response(listDirectory(uri, f));
/*     */         } else {
/* 159 */           res = new NanoHTTPD.Response(NanoHTTPD.Response.Status.FORBIDDEN, "text/plain", "FORBIDDEN: No directory listing.");
/*     */         }
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 166 */       if (res == null)
/*     */       {
/* 169 */         String mime = null;
/* 170 */         int dot = f.getCanonicalPath().lastIndexOf('.');
/* 171 */         if (dot >= 0) {
/* 173 */           mime = (String)MIME_TYPES.get(f.getCanonicalPath().substring(dot + 1).toLowerCase());
/*     */         }
/* 175 */         if (mime == null) {
/* 177 */           mime = TFM_HTTPD_Manager.MIME_DEFAULT_BINARY;
/*     */         }
/* 181 */         String etag = Integer.toHexString((f.getAbsolutePath() + f.lastModified() + "" + f.length()).hashCode());
/*     */         
/* 183 */         long fileLen = f.length();
/*     */         
/* 185 */         long startFrom = 0L;
/* 186 */         long endAt = -1L;
/* 187 */         String range = (String)params.get("range");
/* 188 */         if (range != null)
/*     */         {
/* 190 */           String[] rangeParams = StringUtils.split(range, "=");
/* 191 */           if (rangeParams.length >= 2) {
/* 193 */             if ("bytes".equalsIgnoreCase(rangeParams[0])) {
/*     */               try
/*     */               {
/* 197 */                 int minus = rangeParams[1].indexOf('-');
/* 198 */                 if (minus > 0)
/*     */                 {
/* 200 */                   startFrom = Long.parseLong(rangeParams[1].substring(0, minus));
/* 201 */                   endAt = Long.parseLong(rangeParams[1].substring(minus + 1));
/*     */                 }
/*     */               }
/*     */               catch (NumberFormatException ignored) {}
/* 208 */             } else if ("tail".equalsIgnoreCase(rangeParams[0])) {
/*     */               try
/*     */               {
/* 212 */                 long tailLen = Long.parseLong(rangeParams[1]);
/* 213 */                 if (tailLen < fileLen)
/*     */                 {
/* 215 */                   startFrom = fileLen - tailLen - 2L;
/* 216 */                   if (startFrom < 0L) {
/* 218 */                     startFrom = 0L;
/*     */                   }
/*     */                 }
/*     */               }
/*     */               catch (NumberFormatException ignored) {}
/*     */             }
/*     */           }
/*     */         }
/* 230 */         if ((range != null) && (startFrom >= 0L))
/*     */         {
/* 232 */           if (startFrom >= fileLen)
/*     */           {
/* 234 */             res = new NanoHTTPD.Response(NanoHTTPD.Response.Status.RANGE_NOT_SATISFIABLE, "text/plain", "");
/* 235 */             res.addHeader("Content-Range", "bytes 0-0/" + fileLen);
/* 236 */             res.addHeader("ETag", etag);
/*     */           }
/*     */           else
/*     */           {
/* 240 */             if (endAt < 0L) {
/* 242 */               endAt = fileLen - 1L;
/*     */             }
/* 244 */             long newLen = endAt - startFrom + 1L;
/* 245 */             if (newLen < 0L) {
/* 247 */               newLen = 0L;
/*     */             }
/* 250 */             final long dataLen = newLen;
/* 251 */             FileInputStream fis = new FileInputStream(f)
/*     */             {
/*     */               public int available()
/*     */                 throws IOException
/*     */               {
/* 256 */                 return (int)dataLen;
/*     */               }
/* 258 */             };
/* 259 */             fis.skip(startFrom);
/*     */             
/* 261 */             res = new NanoHTTPD.Response(NanoHTTPD.Response.Status.PARTIAL_CONTENT, mime, fis);
/* 262 */             res.addHeader("Content-Length", "" + dataLen);
/* 263 */             res.addHeader("Content-Range", "bytes " + startFrom + "-" + endAt + "/" + fileLen);
/* 264 */             res.addHeader("ETag", etag);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 269 */           res = new NanoHTTPD.Response(NanoHTTPD.Response.Status.OK, mime, new FileInputStream(f));
/* 270 */           res.addHeader("Content-Length", "" + fileLen);
/* 271 */           res.addHeader("ETag", etag);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/* 277 */       res = new NanoHTTPD.Response(NanoHTTPD.Response.Status.FORBIDDEN, "text/plain", "FORBIDDEN: Reading file failed.");
/*     */     }
/* 280 */     res.addHeader("Accept-Ranges", "bytes");
/* 281 */     return res;
/*     */   }
/*     */   
/*     */   private String listDirectory(String uri, File f)
/*     */   {
/* 286 */     String heading = "Directory " + uri;
/* 287 */     String msg = "<html><head><title>" + heading + "</title><style><!--\n" + "span.dirname { font-weight: bold; }\n" + "span.filesize { font-size: 75%; }\n" + "// -->\n" + "</style>" + "</head><body><h1>" + heading + "</h1>";
/*     */     
/* 294 */     String up = null;
/* 295 */     if (uri.length() > 1)
/*     */     {
/* 297 */       String u = uri.substring(0, uri.length() - 1);
/* 298 */       int slash = u.lastIndexOf('/');
/* 299 */       if ((slash >= 0) && (slash < u.length())) {
/* 301 */         up = uri.substring(0, slash + 1);
/*     */       }
/*     */     }
/* 305 */     List<String> _files = Arrays.asList(f.list(new FilenameFilter()
/*     */     {
/*     */       public boolean accept(File dir, String name)
/*     */       {
/* 310 */         return new File(dir, name).isFile();
/*     */       }
/* 312 */     }));
/* 313 */     Collections.sort(_files);
/* 314 */     List<String> directories = Arrays.asList(f.list(new FilenameFilter()
/*     */     {
/*     */       public boolean accept(File dir, String name)
/*     */       {
/* 319 */         return new File(dir, name).isDirectory();
/*     */       }
/* 321 */     }));
/* 322 */     Collections.sort(directories);
/* 323 */     if ((up != null) || (directories.size() + _files.size() > 0))
/*     */     {
/* 325 */       msg = msg + "<ul>";
/* 326 */       if ((up != null) || (directories.size() > 0))
/*     */       {
/* 328 */         msg = msg + "<section class=\"directories\">";
/* 329 */         if (up != null) {
/* 331 */           msg = msg + "<li><a rel=\"directory\" href=\"" + up + "\"><span class=\"dirname\">..</span></a></b></li>";
/*     */         }
/* 333 */         for (int i = 0; i < directories.size(); i++)
/*     */         {
/* 335 */           String dir = (String)directories.get(i) + "/";
/* 336 */           msg = msg + "<li><a rel=\"directory\" href=\"" + encodeUri(new StringBuilder().append(uri).append(dir).toString()) + "\"><span class=\"dirname\">" + dir + "</span></a></b></li>";
/*     */         }
/* 338 */         msg = msg + "</section>";
/*     */       }
/* 340 */       if (_files.size() > 0)
/*     */       {
/* 342 */         msg = msg + "<section class=\"files\">";
/* 343 */         for (int i = 0; i < _files.size(); i++)
/*     */         {
/* 345 */           String file = (String)_files.get(i);
/*     */           
/* 347 */           msg = msg + "<li><a href=\"" + encodeUri(new StringBuilder().append(uri).append(file).toString()) + "\"><span class=\"filename\">" + file + "</span></a>";
/* 348 */           File curFile = new File(f, file);
/* 349 */           long len = curFile.length();
/* 350 */           msg = msg + "&nbsp;<span class=\"filesize\">(";
/* 351 */           if (len < 1024L) {
/* 353 */             msg = msg + len + " bytes";
/* 355 */           } else if (len < 1048576L) {
/* 357 */             msg = msg + len / 1024L + "." + len % 1024L / 10L % 100L + " KB";
/*     */           } else {
/* 361 */             msg = msg + len / 1048576L + "." + len % 1048576L / 10L % 100L + " MB";
/*     */           }
/* 363 */           msg = msg + ")</span></li>";
/*     */         }
/* 365 */         msg = msg + "</section>";
/*     */       }
/* 367 */       msg = msg + "</ul>";
/*     */     }
/* 369 */     msg = msg + "</body></html>";
/* 370 */     return msg;
/*     */   }
/*     */   
/*     */   public NanoHTTPD.Response getResponse()
/*     */   {
/* 376 */     return serveFile(uri, params, getRootDir());
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\HTTPD\Module_file.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */