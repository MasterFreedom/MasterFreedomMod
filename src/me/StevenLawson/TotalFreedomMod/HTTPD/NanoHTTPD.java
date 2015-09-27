/*      */ package me.StevenLawson.TotalFreedomMod.HTTPD;
/*      */ 
/*      */ import java.io.BufferedReader;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.Closeable;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintWriter;
/*      */ import java.io.RandomAccessFile;
/*      */ import java.io.SequenceInputStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.ServerSocket;
/*      */ import java.net.Socket;
/*      */ import java.net.SocketException;
/*      */ import java.net.URLDecoder;
/*      */ import java.nio.Buffer;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.nio.channels.FileChannel.MapMode;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.TimeZone;
/*      */ import me.StevenLawson.TotalFreedomMod.TFM_Log;
/*      */ 
/*      */ public abstract class NanoHTTPD
/*      */ {
/*      */   public static final String MIME_PLAINTEXT = "text/plain";
/*      */   public static final String MIME_HTML = "text/html";
/*      */   public static final String MIME_JSON = "application/json";
/*      */   private static final String QUERY_STRING_PARAMETER = "NanoHttpd.QUERY_STRING";
/*      */   private final String hostname;
/*      */   private final int myPort;
/*      */   private ServerSocket myServerSocket;
/*      */   private Thread myThread;
/*      */   private AsyncRunner asyncRunner;
/*      */   private TempFileManagerFactory tempFileManagerFactory;
/*      */   
/*      */   public NanoHTTPD(int port)
/*      */   {
/*  119 */     this(null, port);
/*      */   }
/*      */   
/*      */   public NanoHTTPD(String hostname, int port)
/*      */   {
/*  127 */     this.hostname = hostname;
/*  128 */     myPort = port;
/*  129 */     setTempFileManagerFactory(new DefaultTempFileManagerFactory(null));
/*  130 */     setAsyncRunner(new DefaultAsyncRunner());
/*      */   }
/*      */   
/*      */   private static final void safeClose(ServerSocket serverSocket)
/*      */   {
/*  135 */     if (serverSocket != null) {
/*      */       try
/*      */       {
/*  139 */         serverSocket.close();
/*      */       }
/*      */       catch (IOException e) {}
/*      */     }
/*      */   }
/*      */   
/*      */   private static final void safeClose(Socket socket)
/*      */   {
/*  149 */     if (socket != null) {
/*      */       try
/*      */       {
/*  153 */         socket.close();
/*      */       }
/*      */       catch (IOException e) {}
/*      */     }
/*      */   }
/*      */   
/*      */   private static final void safeClose(Closeable closeable)
/*      */   {
/*  163 */     if (closeable != null) {
/*      */       try
/*      */       {
/*  167 */         closeable.close();
/*      */       }
/*      */       catch (IOException e) {}
/*      */     }
/*      */   }
/*      */   
/*      */   public void start()
/*      */     throws IOException
/*      */   {
/*  182 */     myServerSocket = new ServerSocket();
/*  183 */     myServerSocket.bind(hostname != null ? new InetSocketAddress(hostname, myPort) : new InetSocketAddress(myPort));
/*      */     
/*  185 */     myThread = new Thread(new Runnable()
/*      */     {
/*      */       public void run()
/*      */       {
/*      */         do
/*      */         {
/*      */           try
/*      */           {
/*  194 */             final Socket finalAccept = myServerSocket.accept();
/*  195 */             final InputStream inputStream = finalAccept.getInputStream();
/*  196 */             if (inputStream == null) {
/*  198 */               NanoHTTPD.safeClose(finalAccept);
/*      */             } else {
/*  202 */               asyncRunner.exec(new Runnable()
/*      */               {
/*      */                 public void run()
/*      */                 {
/*  207 */                   OutputStream outputStream = null;
/*      */                   try
/*      */                   {
/*  210 */                     outputStream = finalAccept.getOutputStream();
/*  211 */                     NanoHTTPD.TempFileManager tempFileManager = tempFileManagerFactory.create();
/*  212 */                     NanoHTTPD.HTTPSession session = new NanoHTTPD.HTTPSession(NanoHTTPD.this, tempFileManager, inputStream, outputStream, finalAccept);
/*  213 */                     while (!finalAccept.isClosed()) {
/*  215 */                       session.execute();
/*      */                     }
/*      */                   }
/*      */                   catch (Exception e)
/*      */                   {
/*  222 */                     if ((!(e instanceof SocketException)) || (!"NanoHttpd Shutdown".equals(e.getMessage()))) {
/*  224 */                       TFM_Log.severe(e);
/*      */                     }
/*      */                   }
/*      */                   finally
/*      */                   {
/*  229 */                     NanoHTTPD.safeClose(outputStream);
/*  230 */                     NanoHTTPD.safeClose(inputStream);
/*  231 */                     NanoHTTPD.safeClose(finalAccept);
/*      */                   }
/*      */                 }
/*      */               });
/*      */             }
/*      */           }
/*      */           catch (IOException e) {}
/*  241 */         } while (!myServerSocket.isClosed());
/*      */       }
/*  243 */     });
/*  244 */     myThread.setDaemon(true);
/*  245 */     myThread.setName("NanoHttpd Main Listener");
/*  246 */     myThread.start();
/*      */   }
/*      */   
/*      */   public void stop()
/*      */   {
/*      */     try
/*      */     {
/*  256 */       safeClose(myServerSocket);
/*  257 */       myThread.join();
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*  261 */       TFM_Log.severe(e);
/*      */     }
/*      */   }
/*      */   
/*      */   public final int getListeningPort()
/*      */   {
/*  267 */     return myServerSocket == null ? -1 : myServerSocket.getLocalPort();
/*      */   }
/*      */   
/*      */   public final boolean wasStarted()
/*      */   {
/*  272 */     return (myServerSocket != null) && (myThread != null);
/*      */   }
/*      */   
/*      */   public final boolean isAlive()
/*      */   {
/*  277 */     return (wasStarted()) && (!myServerSocket.isClosed()) && (myThread.isAlive());
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public Response serve(String uri, Method method, Map<String, String> headers, Map<String, String> parms, Map<String, String> files)
/*      */   {
/*  296 */     return new Response(NanoHTTPD.Response.Status.NOT_FOUND, "text/plain", "Not Found");
/*      */   }
/*      */   
/*      */   public Response serve(HTTPSession session)
/*      */   {
/*  310 */     Map<String, String> files = new HashMap();
/*  311 */     Method method = session.getMethod();
/*  312 */     if ((Method.PUT.equals(method)) || (Method.POST.equals(method))) {
/*      */       try
/*      */       {
/*  316 */         session.parseBody(files);
/*      */       }
/*      */       catch (IOException ioe)
/*      */       {
/*  320 */         return new Response(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
/*      */       }
/*      */       catch (ResponseException re)
/*      */       {
/*  324 */         return new Response(re.getStatus(), "text/plain", re.getMessage());
/*      */       }
/*      */     }
/*  328 */     return serve(session.getUri(), method, session.getHeaders(), session.getParms(), files);
/*      */   }
/*      */   
/*      */   protected String decodePercent(String str)
/*      */   {
/*  339 */     String decoded = null;
/*      */     try
/*      */     {
/*  342 */       decoded = URLDecoder.decode(str, "UTF8");
/*      */     }
/*      */     catch (UnsupportedEncodingException ignored) {}
/*  347 */     return decoded;
/*      */   }
/*      */   
/*      */   protected Map<String, List<String>> decodeParameters(Map<String, String> parms)
/*      */   {
/*  360 */     return decodeParameters((String)parms.get("NanoHttpd.QUERY_STRING"));
/*      */   }
/*      */   
/*      */   protected Map<String, List<String>> decodeParameters(String queryString)
/*      */   {
/*  373 */     Map<String, List<String>> parms = new HashMap();
/*  374 */     if (queryString != null)
/*      */     {
/*  376 */       StringTokenizer st = new StringTokenizer(queryString, "&");
/*  377 */       while (st.hasMoreTokens())
/*      */       {
/*  379 */         String e = st.nextToken();
/*  380 */         int sep = e.indexOf('=');
/*  381 */         String propertyName = sep >= 0 ? decodePercent(e.substring(0, sep)).trim() : decodePercent(e).trim();
/*  382 */         if (!parms.containsKey(propertyName)) {
/*  384 */           parms.put(propertyName, new ArrayList());
/*      */         }
/*  386 */         String propertyValue = sep >= 0 ? decodePercent(e.substring(sep + 1)) : null;
/*  387 */         if (propertyValue != null) {
/*  389 */           ((List)parms.get(propertyName)).add(propertyValue);
/*      */         }
/*      */       }
/*      */     }
/*  393 */     return parms;
/*      */   }
/*      */   
/*      */   public void setAsyncRunner(AsyncRunner asyncRunner)
/*      */   {
/*  408 */     this.asyncRunner = asyncRunner;
/*      */   }
/*      */   
/*      */   public void setTempFileManagerFactory(TempFileManagerFactory tempFileManagerFactory)
/*      */   {
/*  423 */     this.tempFileManagerFactory = tempFileManagerFactory;
/*      */   }
/*      */   
/*      */   public static enum Method
/*      */   {
/*  431 */     GET,  PUT,  POST,  DELETE,  HEAD;
/*      */     
/*      */     private Method() {}
/*      */     
/*      */     static Method lookup(String method)
/*      */     {
/*  435 */       for (Method m : ) {
/*  437 */         if (m.toString().equalsIgnoreCase(method)) {
/*  439 */           return m;
/*      */         }
/*      */       }
/*  442 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */   public static abstract interface AsyncRunner
/*      */   {
/*      */     public abstract void exec(Runnable paramRunnable);
/*      */   }
/*      */   
/*      */   public static abstract interface TempFileManagerFactory
/*      */   {
/*      */     public abstract NanoHTTPD.TempFileManager create();
/*      */   }
/*      */   
/*      */   public static abstract interface TempFileManager
/*      */   {
/*      */     public abstract NanoHTTPD.TempFile createTempFile()
/*      */       throws Exception;
/*      */     
/*      */     public abstract void clear();
/*      */   }
/*      */   
/*      */   public static abstract interface TempFile
/*      */   {
/*      */     public abstract OutputStream open()
/*      */       throws Exception;
/*      */     
/*      */     public abstract void delete()
/*      */       throws Exception;
/*      */     
/*      */     public abstract String getName();
/*      */   }
/*      */   
/*      */   public static class DefaultAsyncRunner
/*      */     implements NanoHTTPD.AsyncRunner
/*      */   {
/*      */     private long requestCount;
/*      */     
/*      */     public void exec(Runnable code)
/*      */     {
/*  505 */       requestCount += 1L;
/*  506 */       Thread t = new Thread(code);
/*  507 */       t.setDaemon(true);
/*  508 */       t.setName("NanoHttpd Request Processor (#" + requestCount + ")");
/*  509 */       t.start();
/*      */     }
/*      */   }
/*      */   
/*      */   public static class DefaultTempFileManager
/*      */     implements NanoHTTPD.TempFileManager
/*      */   {
/*      */     private final String tmpdir;
/*      */     private final List<NanoHTTPD.TempFile> tempFiles;
/*      */     
/*      */     public DefaultTempFileManager()
/*      */     {
/*  529 */       tmpdir = System.getProperty("java.io.tmpdir");
/*  530 */       tempFiles = new ArrayList();
/*      */     }
/*      */     
/*      */     public NanoHTTPD.TempFile createTempFile()
/*      */       throws Exception
/*      */     {
/*  536 */       NanoHTTPD.DefaultTempFile tempFile = new NanoHTTPD.DefaultTempFile(tmpdir);
/*  537 */       tempFiles.add(tempFile);
/*  538 */       return tempFile;
/*      */     }
/*      */     
/*      */     public void clear()
/*      */     {
/*  544 */       for (NanoHTTPD.TempFile file : tempFiles) {
/*      */         try
/*      */         {
/*  548 */           file.delete();
/*      */         }
/*      */         catch (Exception ignored) {}
/*      */       }
/*  554 */       tempFiles.clear();
/*      */     }
/*      */   }
/*      */   
/*      */   public static class DefaultTempFile
/*      */     implements NanoHTTPD.TempFile
/*      */   {
/*      */     private File file;
/*      */     private OutputStream fstream;
/*      */     
/*      */     public DefaultTempFile(String tempdir)
/*      */       throws IOException
/*      */     {
/*  571 */       file = File.createTempFile("NanoHTTPD-", "", new File(tempdir));
/*  572 */       fstream = new FileOutputStream(file);
/*      */     }
/*      */     
/*      */     public OutputStream open()
/*      */       throws Exception
/*      */     {
/*  578 */       return fstream;
/*      */     }
/*      */     
/*      */     public void delete()
/*      */       throws Exception
/*      */     {
/*  584 */       NanoHTTPD.safeClose(fstream);
/*  585 */       file.delete();
/*      */     }
/*      */     
/*      */     public String getName()
/*      */     {
/*  591 */       return file.getAbsolutePath();
/*      */     }
/*      */   }
/*      */   
/*      */   public static class Response
/*      */   {
/*      */     private Status status;
/*      */     private String mimeType;
/*      */     private InputStream data;
/*  615 */     private Map<String, String> header = new HashMap();
/*      */     private NanoHTTPD.Method requestMethod;
/*      */     private boolean chunkedTransfer;
/*      */     
/*      */     public Response(String msg)
/*      */     {
/*  630 */       this(Status.OK, "text/html", msg);
/*      */     }
/*      */     
/*      */     public Response(Status status, String mimeType, InputStream data)
/*      */     {
/*  638 */       this.status = status;
/*  639 */       this.mimeType = mimeType;
/*  640 */       this.data = data;
/*      */     }
/*      */     
/*      */     public Response(Status status, String mimeType, String txt)
/*      */     {
/*  648 */       this.status = status;
/*  649 */       this.mimeType = mimeType;
/*      */       try
/*      */       {
/*  652 */         data = (txt != null ? new ByteArrayInputStream(txt.getBytes("UTF-8")) : null);
/*      */       }
/*      */       catch (UnsupportedEncodingException uee)
/*      */       {
/*  656 */         TFM_Log.severe(uee);
/*      */       }
/*      */     }
/*      */     
/*      */     public void addHeader(String name, String value)
/*      */     {
/*  665 */       header.put(name, value);
/*      */     }
/*      */     
/*      */     private void send(OutputStream outputStream)
/*      */     {
/*  673 */       String mime = mimeType;
/*  674 */       SimpleDateFormat gmtFrmt = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
/*  675 */       gmtFrmt.setTimeZone(TimeZone.getTimeZone("GMT"));
/*      */       try
/*      */       {
/*  679 */         if (status == null) {
/*  681 */           throw new Error("sendResponse(): Status can't be null.");
/*      */         }
/*  683 */         PrintWriter pw = new PrintWriter(outputStream);
/*  684 */         pw.print("HTTP/1.1 " + status.getDescription() + " \r\n");
/*  686 */         if (mime != null) {
/*  688 */           pw.print("Content-Type: " + mime + "\r\n");
/*      */         }
/*  691 */         if ((header == null) || (header.get("Date") == null)) {
/*  693 */           pw.print("Date: " + gmtFrmt.format(new Date()) + "\r\n");
/*      */         }
/*  696 */         if (header != null) {
/*  698 */           for (String key : header.keySet())
/*      */           {
/*  700 */             String value = (String)header.get(key);
/*  701 */             pw.print(key + ": " + value + "\r\n");
/*      */           }
/*      */         }
/*  705 */         pw.print("Connection: keep-alive\r\n");
/*  707 */         if ((requestMethod != NanoHTTPD.Method.HEAD) && (chunkedTransfer)) {
/*  709 */           sendAsChunked(outputStream, pw);
/*      */         } else {
/*  713 */           sendAsFixedLength(outputStream, pw);
/*      */         }
/*  715 */         outputStream.flush();
/*  716 */         NanoHTTPD.safeClose(data);
/*      */       }
/*      */       catch (IOException ioe) {}
/*      */     }
/*      */     
/*      */     private void sendAsChunked(OutputStream outputStream, PrintWriter pw)
/*      */       throws IOException
/*      */     {
/*  726 */       pw.print("Transfer-Encoding: chunked\r\n");
/*  727 */       pw.print("\r\n");
/*  728 */       pw.flush();
/*  729 */       int BUFFER_SIZE = 16384;
/*  730 */       byte[] CRLF = "\r\n".getBytes();
/*  731 */       byte[] buff = new byte[BUFFER_SIZE];
/*      */       int read;
/*  733 */       while ((read = data.read(buff)) > 0)
/*      */       {
/*  735 */         outputStream.write(String.format("%x\r\n", new Object[] { Integer.valueOf(read) }).getBytes());
/*  736 */         outputStream.write(buff, 0, read);
/*  737 */         outputStream.write(CRLF);
/*      */       }
/*  739 */       outputStream.write(String.format("0\r\n\r\n", new Object[0]).getBytes());
/*      */     }
/*      */     
/*      */     private void sendAsFixedLength(OutputStream outputStream, PrintWriter pw)
/*      */       throws IOException
/*      */     {
/*  744 */       int pending = data != null ? data.available() : 0;
/*  745 */       pw.print("Content-Length: " + pending + "\r\n");
/*      */       
/*  747 */       pw.print("\r\n");
/*  748 */       pw.flush();
/*  750 */       if ((requestMethod != NanoHTTPD.Method.HEAD) && (data != null))
/*      */       {
/*  752 */         int BUFFER_SIZE = 16384;
/*  753 */         byte[] buff = new byte[BUFFER_SIZE];
/*  754 */         while (pending > 0)
/*      */         {
/*  756 */           int read = data.read(buff, 0, pending > BUFFER_SIZE ? BUFFER_SIZE : pending);
/*  757 */           if (read <= 0) {
/*      */             break;
/*      */           }
/*  761 */           outputStream.write(buff, 0, read);
/*      */           
/*  763 */           pending -= read;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public Status getStatus()
/*      */     {
/*  770 */       return status;
/*      */     }
/*      */     
/*      */     public void setStatus(Status status)
/*      */     {
/*  775 */       this.status = status;
/*      */     }
/*      */     
/*      */     public String getMimeType()
/*      */     {
/*  780 */       return mimeType;
/*      */     }
/*      */     
/*      */     public void setMimeType(String mimeType)
/*      */     {
/*  785 */       this.mimeType = mimeType;
/*      */     }
/*      */     
/*      */     public InputStream getData()
/*      */     {
/*  790 */       return data;
/*      */     }
/*      */     
/*      */     public void setData(InputStream data)
/*      */     {
/*  795 */       this.data = data;
/*      */     }
/*      */     
/*      */     public NanoHTTPD.Method getRequestMethod()
/*      */     {
/*  800 */       return requestMethod;
/*      */     }
/*      */     
/*      */     public void setRequestMethod(NanoHTTPD.Method requestMethod)
/*      */     {
/*  805 */       this.requestMethod = requestMethod;
/*      */     }
/*      */     
/*      */     public void setChunkedTransfer(boolean chunkedTransfer)
/*      */     {
/*  810 */       this.chunkedTransfer = chunkedTransfer;
/*      */     }
/*      */     
/*      */     public static enum Status
/*      */     {
/*  818 */       OK(200, "OK"),  CREATED(201, "Created"),  ACCEPTED(202, "Accepted"),  NO_CONTENT(204, "No Content"),  PARTIAL_CONTENT(206, "Partial Content"),  REDIRECT(301, "Moved Permanently"),  NOT_MODIFIED(304, "Not Modified"),  BAD_REQUEST(400, "Bad Request"),  UNAUTHORIZED(401, "Unauthorized"),  FORBIDDEN(403, "Forbidden"),  NOT_FOUND(404, "Not Found"),  RANGE_NOT_SATISFIABLE(416, "Requested Range Not Satisfiable"),  INTERNAL_ERROR(500, "Internal Server Error");
/*      */       
/*      */       private final int requestStatus;
/*      */       private final String description;
/*      */       
/*      */       private Status(int requestStatus, String description)
/*      */       {
/*  827 */         this.requestStatus = requestStatus;
/*  828 */         this.description = description;
/*      */       }
/*      */       
/*      */       public int getRequestStatus()
/*      */       {
/*  833 */         return requestStatus;
/*      */       }
/*      */       
/*      */       public String getDescription()
/*      */       {
/*  838 */         return "" + requestStatus + " " + description;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static final class ResponseException
/*      */     extends Exception
/*      */   {
/*      */     private final NanoHTTPD.Response.Status status;
/*      */     
/*      */     public ResponseException(NanoHTTPD.Response.Status status, String message)
/*      */     {
/*  849 */       super();
/*  850 */       this.status = status;
/*      */     }
/*      */     
/*      */     public ResponseException(NanoHTTPD.Response.Status status, String message, Exception e)
/*      */     {
/*  855 */       super(e);
/*  856 */       this.status = status;
/*      */     }
/*      */     
/*      */     public NanoHTTPD.Response.Status getStatus()
/*      */     {
/*  861 */       return status;
/*      */     }
/*      */   }
/*      */   
/*      */   private class DefaultTempFileManagerFactory
/*      */     implements NanoHTTPD.TempFileManagerFactory
/*      */   {
/*      */     private DefaultTempFileManagerFactory() {}
/*      */     
/*      */     public NanoHTTPD.TempFileManager create()
/*      */     {
/*  873 */       return new NanoHTTPD.DefaultTempFileManager();
/*      */     }
/*      */   }
/*      */   
/*      */   protected class HTTPSession
/*      */   {
/*      */     public static final int BUFSIZE = 8192;
/*      */     private final NanoHTTPD.TempFileManager tempFileManager;
/*      */     private final OutputStream outputStream;
/*      */     private final Socket socket;
/*      */     private InputStream inputStream;
/*      */     private int splitbyte;
/*      */     private int rlen;
/*      */     private String uri;
/*      */     private NanoHTTPD.Method method;
/*      */     private Map<String, String> parms;
/*      */     private Map<String, String> headers;
/*      */     private NanoHTTPD.CookieHandler cookies;
/*      */     
/*      */     public HTTPSession(NanoHTTPD.TempFileManager tempFileManager, InputStream inputStream, OutputStream outputStream, Socket socket)
/*      */     {
/*  897 */       this.tempFileManager = tempFileManager;
/*  898 */       this.inputStream = inputStream;
/*  899 */       this.outputStream = outputStream;
/*  900 */       this.socket = socket;
/*      */     }
/*      */     
/*      */     public void execute()
/*      */       throws IOException
/*      */     {
/*      */       try
/*      */       {
/*  911 */         byte[] buf = new byte[' '];
/*  912 */         splitbyte = 0;
/*  913 */         rlen = 0;
/*      */         
/*  915 */         int read = inputStream.read(buf, 0, 8192);
/*  916 */         if (read == -1) {
/*  919 */           throw new SocketException("NanoHttpd Shutdown");
/*      */         }
/*  921 */         while (read > 0)
/*      */         {
/*  923 */           rlen += read;
/*  924 */           splitbyte = findHeaderEnd(buf, rlen);
/*  925 */           if (splitbyte > 0) {
/*      */             break;
/*      */           }
/*  929 */           read = inputStream.read(buf, rlen, 8192 - rlen);
/*      */         }
/*  933 */         if (splitbyte < rlen)
/*      */         {
/*  935 */           ByteArrayInputStream splitInputStream = new ByteArrayInputStream(buf, splitbyte, rlen - splitbyte);
/*  936 */           SequenceInputStream sequenceInputStream = new SequenceInputStream(splitInputStream, inputStream);
/*  937 */           inputStream = sequenceInputStream;
/*      */         }
/*  940 */         parms = new HashMap();
/*  941 */         headers = new HashMap();
/*      */         
/*  944 */         BufferedReader hin = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(buf, 0, rlen)));
/*      */         
/*  947 */         Map<String, String> pre = new HashMap();
/*  948 */         decodeHeader(hin, pre, parms, headers);
/*      */         
/*  950 */         method = NanoHTTPD.Method.lookup((String)pre.get("method"));
/*  951 */         if (method == null) {
/*  953 */           throw new NanoHTTPD.ResponseException(NanoHTTPD.Response.Status.BAD_REQUEST, "BAD REQUEST: Syntax error.");
/*      */         }
/*  956 */         uri = ((String)pre.get("uri"));
/*      */         
/*  958 */         cookies = new NanoHTTPD.CookieHandler(NanoHTTPD.this, headers);
/*      */         
/*  961 */         NanoHTTPD.Response r = serve(this);
/*  962 */         if (r == null) {
/*  964 */           throw new NanoHTTPD.ResponseException(NanoHTTPD.Response.Status.INTERNAL_ERROR, "SERVER INTERNAL ERROR: Serve() returned a null response.");
/*      */         }
/*  968 */         cookies.unloadQueue(r);
/*  969 */         r.setRequestMethod(method);
/*  970 */         NanoHTTPD.Response.access$600(r, outputStream);
/*      */       }
/*      */       catch (SocketException e)
/*      */       {
/*  976 */         throw e;
/*      */       }
/*      */       catch (IOException ioe)
/*      */       {
/*  980 */         NanoHTTPD.Response r = new NanoHTTPD.Response(NanoHTTPD.Response.Status.INTERNAL_ERROR, "text/plain", "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
/*  981 */         NanoHTTPD.Response.access$600(r, outputStream);
/*  982 */         NanoHTTPD.safeClose(outputStream);
/*      */       }
/*      */       catch (NanoHTTPD.ResponseException re)
/*      */       {
/*  986 */         NanoHTTPD.Response r = new NanoHTTPD.Response(re.getStatus(), "text/plain", re.getMessage());
/*  987 */         NanoHTTPD.Response.access$600(r, outputStream);
/*  988 */         NanoHTTPD.safeClose(outputStream);
/*      */       }
/*      */       finally
/*      */       {
/*  992 */         tempFileManager.clear();
/*      */       }
/*      */     }
/*      */     
/*      */     protected void parseBody(Map<String, String> files)
/*      */       throws IOException, NanoHTTPD.ResponseException
/*      */     {
/*  998 */       RandomAccessFile randomAccessFile = null;
/*  999 */       BufferedReader in = null;
/*      */       try
/*      */       {
/* 1003 */         randomAccessFile = getTmpBucket();
/*      */         long size;
/*      */         long size;
/* 1006 */         if (headers.containsKey("content-length"))
/*      */         {
/* 1008 */           size = Integer.parseInt((String)headers.get("content-length"));
/*      */         }
/*      */         else
/*      */         {
/*      */           long size;
/* 1010 */           if (splitbyte < rlen) {
/* 1012 */             size = rlen - splitbyte;
/*      */           } else {
/* 1016 */             size = 0L;
/*      */           }
/*      */         }
/* 1020 */         byte[] buf = new byte['Ȁ'];
/* 1021 */         while ((rlen >= 0) && (size > 0L))
/*      */         {
/* 1023 */           rlen = inputStream.read(buf, 0, 512);
/* 1024 */           size -= rlen;
/* 1025 */           if (rlen > 0) {
/* 1027 */             randomAccessFile.write(buf, 0, rlen);
/*      */           }
/*      */         }
/* 1032 */         ByteBuffer fbuf = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0L, randomAccessFile.length());
/* 1033 */         randomAccessFile.seek(0L);
/*      */         
/* 1036 */         InputStream bin = new FileInputStream(randomAccessFile.getFD());
/* 1037 */         in = new BufferedReader(new InputStreamReader(bin));
/* 1041 */         if (NanoHTTPD.Method.POST.equals(method))
/*      */         {
/* 1043 */           String contentType = "";
/* 1044 */           String contentTypeHeader = (String)headers.get("content-type");
/*      */           
/* 1046 */           StringTokenizer st = null;
/* 1047 */           if (contentTypeHeader != null)
/*      */           {
/* 1049 */             st = new StringTokenizer(contentTypeHeader, ",; ");
/* 1050 */             if (st.hasMoreTokens()) {
/* 1052 */               contentType = st.nextToken();
/*      */             }
/*      */           }
/* 1056 */           if ("multipart/form-data".equalsIgnoreCase(contentType))
/*      */           {
/* 1059 */             if (!st.hasMoreTokens()) {
/* 1061 */               throw new NanoHTTPD.ResponseException(NanoHTTPD.Response.Status.BAD_REQUEST, "BAD REQUEST: Content type is multipart/form-data but boundary missing. Usage: GET /example/file.html");
/*      */             }
/* 1064 */             String boundaryStartString = "boundary=";
/* 1065 */             int boundaryContentStart = contentTypeHeader.indexOf(boundaryStartString) + boundaryStartString.length();
/* 1066 */             String boundary = contentTypeHeader.substring(boundaryContentStart, contentTypeHeader.length());
/* 1067 */             if ((boundary.startsWith("\"")) && (boundary.endsWith("\""))) {
/* 1069 */               boundary = boundary.substring(1, boundary.length() - 1);
/*      */             }
/* 1072 */             decodeMultipartData(boundary, fbuf, in, parms, files);
/*      */           }
/*      */           else
/*      */           {
/* 1077 */             String postLine = "";
/* 1078 */             char[] pbuf = new char['Ȁ'];
/* 1079 */             int read = in.read(pbuf);
/* 1080 */             while ((read >= 0) && (!postLine.endsWith("\r\n")))
/*      */             {
/* 1082 */               postLine = postLine + String.valueOf(pbuf, 0, read);
/* 1083 */               read = in.read(pbuf);
/*      */             }
/* 1085 */             postLine = postLine.trim();
/* 1086 */             decodeParms(postLine, parms);
/*      */           }
/*      */         }
/* 1089 */         else if (NanoHTTPD.Method.PUT.equals(method))
/*      */         {
/* 1091 */           files.put("content", saveTmpFile(fbuf, 0, fbuf.limit()));
/*      */         }
/*      */       }
/*      */       finally
/*      */       {
/* 1096 */         NanoHTTPD.safeClose(randomAccessFile);
/* 1097 */         NanoHTTPD.safeClose(in);
/*      */       }
/*      */     }
/*      */     
/*      */     private void decodeHeader(BufferedReader in, Map<String, String> pre, Map<String, String> parms, Map<String, String> headers)
/*      */       throws NanoHTTPD.ResponseException
/*      */     {
/*      */       try
/*      */       {
/* 1110 */         String inLine = in.readLine();
/* 1111 */         if (inLine == null) {
/* 1113 */           return;
/*      */         }
/* 1116 */         StringTokenizer st = new StringTokenizer(inLine);
/* 1117 */         if (!st.hasMoreTokens()) {
/* 1119 */           throw new NanoHTTPD.ResponseException(NanoHTTPD.Response.Status.BAD_REQUEST, "BAD REQUEST: Syntax error. Usage: GET /example/file.html");
/*      */         }
/* 1122 */         pre.put("method", st.nextToken());
/* 1124 */         if (!st.hasMoreTokens()) {
/* 1126 */           throw new NanoHTTPD.ResponseException(NanoHTTPD.Response.Status.BAD_REQUEST, "BAD REQUEST: Missing URI. Usage: GET /example/file.html");
/*      */         }
/* 1129 */         String uri = st.nextToken();
/*      */         
/* 1132 */         int qmi = uri.indexOf('?');
/* 1133 */         if (qmi >= 0)
/*      */         {
/* 1135 */           decodeParms(uri.substring(qmi + 1), parms);
/* 1136 */           uri = decodePercent(uri.substring(0, qmi));
/*      */         }
/*      */         else
/*      */         {
/* 1140 */           uri = decodePercent(uri);
/*      */         }
/* 1147 */         if (st.hasMoreTokens())
/*      */         {
/* 1149 */           String line = in.readLine();
/* 1150 */           while ((line != null) && (line.trim().length() > 0))
/*      */           {
/* 1152 */             int p = line.indexOf(':');
/* 1153 */             if (p >= 0) {
/* 1155 */               headers.put(line.substring(0, p).trim().toLowerCase(), line.substring(p + 1).trim());
/*      */             }
/* 1157 */             line = in.readLine();
/*      */           }
/*      */         }
/* 1161 */         pre.put("uri", uri);
/*      */       }
/*      */       catch (IOException ioe)
/*      */       {
/* 1165 */         throw new NanoHTTPD.ResponseException(NanoHTTPD.Response.Status.INTERNAL_ERROR, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage(), ioe);
/*      */       }
/*      */     }
/*      */     
/*      */     private void decodeMultipartData(String boundary, ByteBuffer fbuf, BufferedReader in, Map<String, String> parms, Map<String, String> files)
/*      */       throws NanoHTTPD.ResponseException
/*      */     {
/*      */       try
/*      */       {
/* 1177 */         int[] bpositions = getBoundaryPositions(fbuf, boundary.getBytes());
/* 1178 */         int boundarycount = 1;
/* 1179 */         String mpline = in.readLine();
/* 1180 */         while (mpline != null)
/*      */         {
/* 1182 */           if (!mpline.contains(boundary)) {
/* 1184 */             throw new NanoHTTPD.ResponseException(NanoHTTPD.Response.Status.BAD_REQUEST, "BAD REQUEST: Content type is multipart/form-data but next chunk does not start with boundary. Usage: GET /example/file.html");
/*      */           }
/* 1186 */           boundarycount++;
/* 1187 */           Map<String, String> item = new HashMap();
/* 1188 */           mpline = in.readLine();
/* 1189 */           while ((mpline != null) && (mpline.trim().length() > 0))
/*      */           {
/* 1191 */             int p = mpline.indexOf(':');
/* 1192 */             if (p != -1) {
/* 1194 */               item.put(mpline.substring(0, p).trim().toLowerCase(), mpline.substring(p + 1).trim());
/*      */             }
/* 1196 */             mpline = in.readLine();
/*      */           }
/* 1198 */           if (mpline != null)
/*      */           {
/* 1200 */             String contentDisposition = (String)item.get("content-disposition");
/* 1201 */             if (contentDisposition == null) {
/* 1203 */               throw new NanoHTTPD.ResponseException(NanoHTTPD.Response.Status.BAD_REQUEST, "BAD REQUEST: Content type is multipart/form-data but no content-disposition info found. Usage: GET /example/file.html");
/*      */             }
/* 1205 */             StringTokenizer st = new StringTokenizer(contentDisposition, "; ");
/* 1206 */             Map<String, String> disposition = new HashMap();
/* 1207 */             while (st.hasMoreTokens())
/*      */             {
/* 1209 */               String token = st.nextToken();
/* 1210 */               int p = token.indexOf('=');
/* 1211 */               if (p != -1) {
/* 1213 */                 disposition.put(token.substring(0, p).trim().toLowerCase(), token.substring(p + 1).trim());
/*      */               }
/*      */             }
/* 1216 */             String pname = (String)disposition.get("name");
/* 1217 */             pname = pname.substring(1, pname.length() - 1);
/*      */             
/* 1219 */             String value = "";
/* 1220 */             if (item.get("content-type") == null) {
/* 1222 */               while ((mpline != null) && (!mpline.contains(boundary)))
/*      */               {
/* 1224 */                 mpline = in.readLine();
/* 1225 */                 if (mpline != null)
/*      */                 {
/* 1227 */                   int d = mpline.indexOf(boundary);
/* 1228 */                   if (d == -1) {
/* 1230 */                     value = value + mpline;
/*      */                   } else {
/* 1234 */                     value = value + mpline.substring(0, d - 2);
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/* 1241 */             if (boundarycount > bpositions.length) {
/* 1243 */               throw new NanoHTTPD.ResponseException(NanoHTTPD.Response.Status.INTERNAL_ERROR, "Error processing request");
/*      */             }
/* 1245 */             int offset = stripMultipartHeaders(fbuf, bpositions[(boundarycount - 2)]);
/* 1246 */             String path = saveTmpFile(fbuf, offset, bpositions[(boundarycount - 1)] - offset - 4);
/* 1247 */             files.put(pname, path);
/* 1248 */             value = (String)disposition.get("filename");
/* 1249 */             value = value.substring(1, value.length() - 1);
/*      */             do
/*      */             {
/* 1252 */               mpline = in.readLine();
/* 1254 */             } while ((mpline != null) && (!mpline.contains(boundary)));
/* 1256 */             parms.put(pname, value);
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (IOException ioe)
/*      */       {
/* 1262 */         throw new NanoHTTPD.ResponseException(NanoHTTPD.Response.Status.INTERNAL_ERROR, "SERVER INTERNAL ERROR: IOException: " + ioe.getMessage(), ioe);
/*      */       }
/*      */     }
/*      */     
/*      */     private int findHeaderEnd(byte[] buf, int rlen)
/*      */     {
/* 1271 */       int splitbyte = 0;
/* 1272 */       while (splitbyte + 3 < rlen)
/*      */       {
/* 1274 */         if ((buf[splitbyte] == 13) && (buf[(splitbyte + 1)] == 10) && (buf[(splitbyte + 2)] == 13) && (buf[(splitbyte + 3)] == 10)) {
/* 1276 */           return splitbyte + 4;
/*      */         }
/* 1278 */         splitbyte++;
/*      */       }
/* 1280 */       return 0;
/*      */     }
/*      */     
/*      */     private int[] getBoundaryPositions(ByteBuffer b, byte[] boundary)
/*      */     {
/* 1288 */       int matchcount = 0;
/* 1289 */       int matchbyte = -1;
/* 1290 */       List<Integer> matchbytes = new ArrayList();
/* 1291 */       for (int i = 0; i < b.limit(); i++) {
/* 1293 */         if (b.get(i) == boundary[matchcount])
/*      */         {
/* 1295 */           if (matchcount == 0) {
/* 1297 */             matchbyte = i;
/*      */           }
/* 1299 */           matchcount++;
/* 1300 */           if (matchcount == boundary.length)
/*      */           {
/* 1302 */             matchbytes.add(Integer.valueOf(matchbyte));
/* 1303 */             matchcount = 0;
/* 1304 */             matchbyte = -1;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 1309 */           i -= matchcount;
/* 1310 */           matchcount = 0;
/* 1311 */           matchbyte = -1;
/*      */         }
/*      */       }
/* 1314 */       int[] ret = new int[matchbytes.size()];
/* 1315 */       for (int i = 0; i < ret.length; i++) {
/* 1317 */         ret[i] = ((Integer)matchbytes.get(i)).intValue();
/*      */       }
/* 1319 */       return ret;
/*      */     }
/*      */     
/*      */     private String saveTmpFile(ByteBuffer b, int offset, int len)
/*      */     {
/* 1327 */       String path = "";
/* 1328 */       if (len > 0)
/*      */       {
/* 1330 */         FileOutputStream fileOutputStream = null;
/*      */         try
/*      */         {
/* 1333 */           NanoHTTPD.TempFile tempFile = tempFileManager.createTempFile();
/* 1334 */           ByteBuffer src = b.duplicate();
/* 1335 */           fileOutputStream = new FileOutputStream(tempFile.getName());
/* 1336 */           FileChannel dest = fileOutputStream.getChannel();
/* 1337 */           src.position(offset).limit(offset + len);
/* 1338 */           dest.write(src.slice());
/* 1339 */           path = tempFile.getName();
/*      */         }
/*      */         catch (Exception e)
/*      */         {
/* 1343 */           TFM_Log.severe(e);
/*      */         }
/*      */         finally
/*      */         {
/* 1347 */           NanoHTTPD.safeClose(fileOutputStream);
/*      */         }
/*      */       }
/* 1350 */       return path;
/*      */     }
/*      */     
/*      */     private RandomAccessFile getTmpBucket()
/*      */     {
/*      */       try
/*      */       {
/* 1357 */         NanoHTTPD.TempFile tempFile = tempFileManager.createTempFile();
/* 1358 */         return new RandomAccessFile(tempFile.getName(), "rw");
/*      */       }
/*      */       catch (Exception e)
/*      */       {
/* 1362 */         TFM_Log.severe(e);
/*      */       }
/* 1364 */       return null;
/*      */     }
/*      */     
/*      */     private int stripMultipartHeaders(ByteBuffer b, int offset)
/*      */     {
/* 1373 */       for (int i = offset; i < b.limit(); i++) {
/* 1375 */         if ((b.get(i) == 13) && (b.get(++i) == 10) && (b.get(++i) == 13) && (b.get(++i) == 10)) {
/*      */           break;
/*      */         }
/*      */       }
/* 1380 */       return i + 1;
/*      */     }
/*      */     
/*      */     private void decodeParms(String parms, Map<String, String> p)
/*      */     {
/* 1389 */       if (parms == null)
/*      */       {
/* 1391 */         p.put("NanoHttpd.QUERY_STRING", "");
/* 1392 */         return;
/*      */       }
/* 1395 */       p.put("NanoHttpd.QUERY_STRING", parms);
/* 1396 */       StringTokenizer st = new StringTokenizer(parms, "&");
/* 1397 */       while (st.hasMoreTokens())
/*      */       {
/* 1399 */         String e = st.nextToken();
/* 1400 */         int sep = e.indexOf('=');
/* 1401 */         if (sep >= 0) {
/* 1403 */           p.put(decodePercent(e.substring(0, sep)).trim(), decodePercent(e.substring(sep + 1)));
/*      */         } else {
/* 1408 */           p.put(decodePercent(e).trim(), "");
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public final Map<String, String> getParms()
/*      */     {
/* 1415 */       return parms;
/*      */     }
/*      */     
/*      */     public final Map<String, String> getHeaders()
/*      */     {
/* 1420 */       return headers;
/*      */     }
/*      */     
/*      */     public final String getUri()
/*      */     {
/* 1425 */       return uri;
/*      */     }
/*      */     
/*      */     public final NanoHTTPD.Method getMethod()
/*      */     {
/* 1430 */       return method;
/*      */     }
/*      */     
/*      */     public final InputStream getInputStream()
/*      */     {
/* 1435 */       return inputStream;
/*      */     }
/*      */     
/*      */     public NanoHTTPD.CookieHandler getCookies()
/*      */     {
/* 1440 */       return cookies;
/*      */     }
/*      */     
/*      */     public Socket getSocket()
/*      */     {
/* 1445 */       return socket;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class Cookie
/*      */   {
/*      */     private String n;
/*      */     private String v;
/*      */     private String e;
/*      */     
/*      */     public Cookie(String name, String value, String expires)
/*      */     {
/* 1455 */       n = name;
/* 1456 */       v = value;
/* 1457 */       e = expires;
/*      */     }
/*      */     
/*      */     public Cookie(String name, String value)
/*      */     {
/* 1462 */       this(name, value, 30);
/*      */     }
/*      */     
/*      */     public Cookie(String name, String value, int numDays)
/*      */     {
/* 1467 */       n = name;
/* 1468 */       v = value;
/* 1469 */       e = getHTTPTime(numDays);
/*      */     }
/*      */     
/*      */     public String getHTTPHeader()
/*      */     {
/* 1474 */       String fmt = "%s=%s; expires=%s";
/* 1475 */       return String.format(fmt, new Object[] { n, v, e });
/*      */     }
/*      */     
/*      */     public static String getHTTPTime(int days)
/*      */     {
/* 1480 */       Calendar calendar = Calendar.getInstance();
/* 1481 */       SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
/* 1482 */       dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
/* 1483 */       calendar.add(5, days);
/* 1484 */       return dateFormat.format(calendar.getTime());
/*      */     }
/*      */   }
/*      */   
/*      */   public class CookieHandler
/*      */     implements Iterable<String>
/*      */   {
/* 1497 */     private HashMap<String, String> cookies = new HashMap();
/* 1498 */     private ArrayList<NanoHTTPD.Cookie> queue = new ArrayList();
/*      */     
/*      */     public CookieHandler()
/*      */     {
/* 1502 */       String raw = (String)httpHeaders.get("cookie");
/* 1503 */       if (raw != null)
/*      */       {
/* 1505 */         String[] tokens = raw.split(";");
/* 1506 */         for (String token : tokens)
/*      */         {
/* 1508 */           String[] data = token.trim().split("=");
/* 1509 */           if (data.length == 2) {
/* 1511 */             cookies.put(data[0], data[1]);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public Iterator<String> iterator()
/*      */     {
/* 1520 */       return cookies.keySet().iterator();
/*      */     }
/*      */     
/*      */     public String read(String name)
/*      */     {
/* 1531 */       return (String)cookies.get(name);
/*      */     }
/*      */     
/*      */     public void set(String name, String value, int expires)
/*      */     {
/* 1543 */       queue.add(new NanoHTTPD.Cookie(name, value, NanoHTTPD.Cookie.getHTTPTime(expires)));
/*      */     }
/*      */     
/*      */     public void set(NanoHTTPD.Cookie cookie)
/*      */     {
/* 1548 */       queue.add(cookie);
/*      */     }
/*      */     
/*      */     public void delete(String name)
/*      */     {
/* 1558 */       set(name, "-delete-", -30);
/*      */     }
/*      */     
/*      */     public void unloadQueue(NanoHTTPD.Response response)
/*      */     {
/* 1568 */       for (NanoHTTPD.Cookie cookie : queue) {
/* 1570 */         response.addHeader("Set-Cookie", cookie.getHTTPHeader());
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\HTTPD\NanoHTTPD.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */