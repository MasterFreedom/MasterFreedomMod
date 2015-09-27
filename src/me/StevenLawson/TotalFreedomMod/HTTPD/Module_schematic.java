/*     */ package me.StevenLawson.TotalFreedomMod.HTTPD;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Admin;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*     */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.lang3.StringEscapeUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ public class Module_schematic
/*     */   extends TFM_HTTPD_Module
/*     */ {
/*  23 */   private static final File SCHEMATIC_FOLDER = new File("./plugins/WorldEdit/schematics/");
/*     */   private static final String REQUEST_FORM_FILE_ELEMENT_NAME = "schematicFile";
/*  25 */   private static final Pattern SCHEMATIC_FILENAME_LC = Pattern.compile("^[a-z0-9_'!,\\-]{1,30}\\.schematic$");
/*  26 */   private static final String[] SCHEMATIC_FILTER = { "schematic" };
/*     */   private static final String UPLOAD_FORM = "<form method=\"post\" name=\"schematicForm\" id=\"schematicForm\" action=\"/schematic/upload/\" enctype=\"multipart/form-data\">\n<p>Select a schematic file to upload. Filenames must be alphanumeric, between 1 and 30 characters long (inclusive), and have a .schematic extension.</p>\n<input type=\"file\" id=\"schematicFile\" name=\"schematicFile\" />\n<br />\n<button type=\"submit\">Submit</button>\n</form>";
/*     */   
/*     */   public Module_schematic(NanoHTTPD.HTTPSession session)
/*     */   {
/*  39 */     super(session);
/*     */   }
/*     */   
/*     */   public NanoHTTPD.Response getResponse()
/*     */   {
/*     */     try
/*     */     {
/*  47 */       return new TFM_HTTPD_PageBuilder(body(), title(), null, null).getResponse();
/*     */     }
/*     */     catch (ResponseOverrideException ex)
/*     */     {
/*  51 */       return ex.getResponse();
/*     */     }
/*     */   }
/*     */   
/*     */   public String title()
/*     */   {
/*  57 */     return "TotalFreedomMod :: Schematic Manager";
/*     */   }
/*     */   
/*     */   public String body()
/*     */     throws Module_schematic.ResponseOverrideException
/*     */   {
/*  62 */     if (!SCHEMATIC_FOLDER.exists()) {
/*  64 */       return HTMLGenerationTools.paragraph("Can't find the WorldEdit schematic folder.");
/*     */     }
/*  67 */     StringBuilder out = new StringBuilder();
/*     */     
/*  69 */     String[] args = StringUtils.split(uri, "/");
/*  70 */     ModuleMode mode = ModuleMode.getMode(getArg(args, 1));
/*  72 */     switch (mode)
/*     */     {
/*     */     case LIST: 
/*  76 */       Collection<File> schematics = FileUtils.listFiles(SCHEMATIC_FOLDER, SCHEMATIC_FILTER, false);
/*     */       
/*  78 */       List<String> schematicsFormatted = new ArrayList();
/*  79 */       for (File schematic : schematics)
/*     */       {
/*  81 */         String filename = StringEscapeUtils.escapeHtml4(schematic.getName());
/*  83 */         if (SCHEMATIC_FILENAME_LC.matcher(filename.trim().toLowerCase()).find()) {
/*  85 */           schematicsFormatted.add("<li><a href=\"/schematic/download?schematicName=" + filename + "\">" + filename + "</a></li>");
/*     */         } else {
/*  89 */           schematicsFormatted.add("<li>" + filename + " - (Illegal filename, can't download)</li>");
/*     */         }
/*     */       }
/*  93 */       Collections.sort(schematicsFormatted, new Comparator()
/*     */       {
/*     */         public int compare(String a, String b)
/*     */         {
/*  98 */           return a.toLowerCase().compareTo(b.toLowerCase());
/*     */         }
/* 101 */       });
/* 102 */       out.append(HTMLGenerationTools.heading("Schematics:", 1)).append("<ul>").append(StringUtils.join(schematicsFormatted, "\r\n")).append("</ul>");
/*     */       
/* 108 */       break;
/*     */     case DOWNLOAD: 
/*     */       try
/*     */       {
/* 114 */         throw new ResponseOverrideException(downloadSchematic((String)params.get("schematicName")));
/*     */       }
/*     */       catch (SchematicTransferException ex)
/*     */       {
/* 118 */         out.append(HTMLGenerationTools.paragraph("Error downloading schematic: " + ex.getMessage()));
/*     */       }
/*     */     case UPLOAD: 
/* 124 */       String remoteAddress = socket.getInetAddress().getHostAddress();
/* 125 */       if (!isAuthorized(remoteAddress)) {
/* 127 */         out.append(HTMLGenerationTools.paragraph("Schematic upload access denied: Your IP, " + remoteAddress + ", is not registered to a superadmin on this server."));
/* 131 */       } else if (method == NanoHTTPD.Method.POST) {
/*     */         try
/*     */         {
/* 135 */           uploadSchematic();
/* 136 */           out.append(HTMLGenerationTools.paragraph("Schematic uploaded successfully."));
/*     */         }
/*     */         catch (SchematicTransferException ex)
/*     */         {
/* 140 */           out.append(HTMLGenerationTools.paragraph("Error uploading schematic: " + ex.getMessage()));
/*     */         }
/*     */       } else {
/* 145 */         out.append("<form method=\"post\" name=\"schematicForm\" id=\"schematicForm\" action=\"/schematic/upload/\" enctype=\"multipart/form-data\">\n<p>Select a schematic file to upload. Filenames must be alphanumeric, between 1 and 30 characters long (inclusive), and have a .schematic extension.</p>\n<input type=\"file\" id=\"schematicFile\" name=\"schematicFile\" />\n<br />\n<button type=\"submit\">Submit</button>\n</form>");
/*     */       }
/* 148 */       break;
/*     */     default: 
/* 152 */       out.append(HTMLGenerationTools.paragraph("Invalid request mode."));
/*     */     }
/* 157 */     return out.toString();
/*     */   }
/*     */   
/*     */   private boolean uploadSchematic()
/*     */     throws Module_schematic.SchematicTransferException
/*     */   {
/* 162 */     Map<String, String> files = getFiles();
/*     */     
/* 164 */     String tempFileName = (String)files.get("schematicFile");
/* 165 */     if (tempFileName == null) {
/* 167 */       throw new SchematicTransferException("No file transmitted to server.");
/*     */     }
/* 170 */     File tempFile = new File(tempFileName);
/* 171 */     if (!tempFile.exists()) {
/* 173 */       throw new SchematicTransferException();
/*     */     }
/* 176 */     String origFileName = (String)params.get("schematicFile");
/* 177 */     if ((origFileName == null) || ((origFileName = origFileName.trim()).isEmpty())) {
/* 179 */       throw new SchematicTransferException("Can't resolve original file name.");
/*     */     }
/* 182 */     if (tempFile.length() > 65536L) {
/* 184 */       throw new SchematicTransferException("Schematic is too big (64kb max).");
/*     */     }
/* 187 */     if (!SCHEMATIC_FILENAME_LC.matcher(origFileName.toLowerCase()).find()) {
/* 189 */       throw new SchematicTransferException("File name must be alphanumeric, between 1 and 30 characters long (inclusive), and have a \".schematic\" extension.");
/*     */     }
/* 192 */     File targetFile = new File(SCHEMATIC_FOLDER.getPath(), origFileName);
/* 193 */     if (targetFile.exists()) {
/* 195 */       throw new SchematicTransferException("Schematic already exists on the server.");
/*     */     }
/*     */     try
/*     */     {
/* 200 */       FileUtils.copyFile(tempFile, targetFile);
/*     */     }
/*     */     catch (IOException ex)
/*     */     {
/* 204 */       TFM_Log.severe(ex);
/* 205 */       throw new SchematicTransferException();
/*     */     }
/* 208 */     return true;
/*     */   }
/*     */   
/*     */   private NanoHTTPD.Response downloadSchematic(String schematicName)
/*     */     throws Module_schematic.SchematicTransferException
/*     */   {
/* 213 */     if ((schematicName == null) || (!SCHEMATIC_FILENAME_LC.matcher((schematicName = schematicName.trim()).toLowerCase()).find())) {
/* 215 */       throw new SchematicTransferException("Invalid schematic name requested: " + schematicName);
/*     */     }
/* 218 */     File targetFile = new File(SCHEMATIC_FOLDER.getPath(), schematicName);
/* 219 */     if (!targetFile.exists()) {
/* 221 */       throw new SchematicTransferException("Schematic not found: " + schematicName);
/*     */     }
/* 224 */     NanoHTTPD.Response response = TFM_HTTPD_Manager.serveFileBasic(targetFile);
/*     */     
/* 226 */     response.addHeader("Content-Disposition", "attachment; filename=" + targetFile.getName() + ";");
/*     */     
/* 228 */     return response;
/*     */   }
/*     */   
/*     */   private boolean isAuthorized(String remoteAddress)
/*     */   {
/* 233 */     TFM_Admin entry = TFM_AdminList.getEntryByIp(remoteAddress);
/* 234 */     return (entry != null) && (entry.isActivated());
/*     */   }
/*     */   
/*     */   private static class SchematicTransferException
/*     */     extends Exception
/*     */   {
/*     */     public SchematicTransferException() {}
/*     */     
/*     */     public SchematicTransferException(String string)
/*     */     {
/* 245 */       super();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ResponseOverrideException
/*     */     extends Exception
/*     */   {
/*     */     private final NanoHTTPD.Response response;
/*     */     
/*     */     public ResponseOverrideException(NanoHTTPD.Response response)
/*     */     {
/* 255 */       this.response = response;
/*     */     }
/*     */     
/*     */     public NanoHTTPD.Response getResponse()
/*     */     {
/* 260 */       return response;
/*     */     }
/*     */   }
/*     */   
/*     */   private static String getArg(String[] args, int index)
/*     */   {
/* 266 */     String out = args.length == index + 1 ? args[index] : null;
/* 267 */     return out.trim().isEmpty() ? null : out == null ? null : out.trim();
/*     */   }
/*     */   
/*     */   private static enum ModuleMode
/*     */   {
/* 272 */     LIST("list"),  UPLOAD("upload"),  DOWNLOAD("download"),  INVALID(null);
/*     */     
/*     */     private final String modeName;
/*     */     
/*     */     private ModuleMode(String modeName)
/*     */     {
/* 281 */       this.modeName = modeName;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 287 */       return modeName;
/*     */     }
/*     */     
/*     */     public static ModuleMode getMode(String needle)
/*     */     {
/* 292 */       for (ModuleMode mode : )
/*     */       {
/* 294 */         String haystack = mode.toString();
/* 295 */         if ((haystack != null) && (haystack.equalsIgnoreCase(needle))) {
/* 297 */           return mode;
/*     */         }
/*     */       }
/* 300 */       return INVALID;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\HTTPD\Module_schematic.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */