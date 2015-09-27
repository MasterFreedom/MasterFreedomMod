/*     */ package me.StevenLawson.TotalFreedomMod.Commands;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ @CommandPermissions(level=AdminLevel.SENIOR, source=SourceType.ONLY_CONSOLE)
/*     */ @CommandParameters(description="For developers only - debug things via reflection.", usage="/<command>")
/*     */ public class Command_debug
/*     */   extends TFM_Command
/*     */ {
/*     */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*     */   {
/*  17 */     if (args.length < 3) {
/*  19 */       return false;
/*     */     }
/*     */     try
/*     */     {
/*  24 */       String className = args[0];
/*  25 */       String fieldName = args[1];
/*  26 */       String newValue = StringUtils.join(ArrayUtils.subarray(args, 2, args.length), " ");
/*  28 */       if (className.equalsIgnoreCase("_")) {
/*  30 */         className = "me.StevenLawson.TotalFreedomMod.TotalFreedomMod";
/*     */       }
/*  33 */       setStaticValue(className, fieldName, newValue);
/*     */       
/*  35 */       sender.sendMessage("Debug: OK");
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*  39 */       sender.sendMessage(ex.getMessage());
/*     */     }
/*  42 */     return true;
/*     */   }
/*     */   
/*     */   public static void setStaticValue(String className, String fieldName, String newValueString)
/*     */     throws Exception
/*     */   {
/*  47 */     Class<?> forName = Class.forName(className);
/*  48 */     if (forName != null)
/*     */     {
/*  50 */       Field field = forName.getDeclaredField(fieldName);
/*  51 */       if (field != null)
/*     */       {
/*  55 */         Class<?> type = field.getType();
/*  56 */         if (type.isPrimitive())
/*     */         {
/*     */           Object newValue;
/*  58 */           if (type.getName().equals("int"))
/*     */           {
/*  60 */             newValue = Integer.valueOf(Integer.parseInt(newValueString));
/*     */           }
/*     */           else
/*     */           {
/*     */             Object newValue;
/*  62 */             if (type.getName().equals("double"))
/*     */             {
/*  64 */               newValue = Double.valueOf(Double.parseDouble(newValueString));
/*     */             }
/*     */             else
/*     */             {
/*     */               Object newValue;
/*  66 */               if (type.getName().equals("boolean")) {
/*  68 */                 newValue = Boolean.valueOf(Boolean.parseBoolean(newValueString));
/*     */               } else {
/*  72 */                 throw new Exception("Unknown primitive field type.");
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/*     */           Object newValue;
/*  77 */           if (type.isAssignableFrom(Integer.class))
/*     */           {
/*  79 */             newValue = new Integer(newValueString);
/*     */           }
/*     */           else
/*     */           {
/*     */             Object newValue;
/*  81 */             if (type.isAssignableFrom(Double.class))
/*     */             {
/*  83 */               newValue = new Double(newValueString);
/*     */             }
/*     */             else
/*     */             {
/*     */               Object newValue;
/*  85 */               if (type.isAssignableFrom(Boolean.class))
/*     */               {
/*  87 */                 newValue = Boolean.valueOf(newValueString);
/*     */               }
/*     */               else
/*     */               {
/*     */                 Object newValue;
/*  89 */                 if (type.isAssignableFrom(String.class)) {
/*  91 */                   newValue = newValueString;
/*     */                 } else {
/*  95 */                   throw new Exception("Unknown complex field type.");
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         Object newValue;
/*  99 */         field.setAccessible(true);
/*     */         
/* 101 */         Object oldValue = field.get(Class.forName(className));
/* 102 */         if (oldValue != null) {
/* 104 */           field.set(oldValue, newValue);
/*     */         }
/* 107 */         field.setAccessible(false);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_debug.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */