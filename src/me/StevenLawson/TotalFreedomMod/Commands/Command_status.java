/*    */ package me.StevenLawson.TotalFreedomMod.Commands;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.bukkit.ChatColor;
/*    */ import org.bukkit.Server;
/*    */ import org.bukkit.World;
/*    */ import org.bukkit.command.Command;
/*    */ import org.bukkit.command.CommandSender;
/*    */ import org.bukkit.entity.Player;
/*    */ 
/*    */ @CommandPermissions(level=AdminLevel.ALL, source=SourceType.BOTH)
/*    */ @CommandParameters(description="Show misc. server info.", usage="/<command>")
/*    */ public class Command_status
/*    */   extends TFM_Command
/*    */ {
/* 15 */   public static final Map<String, String> SERVICE_MAP = new HashMap();
/*    */   
/*    */   static
/*    */   {
/* 19 */     SERVICE_MAP.put("minecraft.net", "Minecraft.net");
/* 20 */     SERVICE_MAP.put("login.minecraft.net", "Minecraft Logins");
/* 21 */     SERVICE_MAP.put("session.minecraft.net", "Minecraft Multiplayer Sessions");
/* 22 */     SERVICE_MAP.put("account.mojang.com", "Mojang Accounts Website");
/* 23 */     SERVICE_MAP.put("auth.mojang.com", "Mojang Accounts Login");
/* 24 */     SERVICE_MAP.put("skins.minecraft.net", "Minecraft Skins");
/*    */   }
/*    */   
/*    */   public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
/*    */   {
/* 30 */     playerMsg("For information about TotalFreedomMod, try /tfm", ChatColor.GREEN);
/*    */     
/* 32 */     playerMsg("Server is currently running with 'online-mode=" + (server.getOnlineMode() ? "true" : "false") + "'.", ChatColor.YELLOW);
/* 33 */     playerMsg("Loaded worlds:", ChatColor.BLUE);
/* 34 */     int i = 0;
/* 35 */     for (World world : server.getWorlds()) {
/* 37 */       playerMsg(String.format("World %d: %s - %d players.", new Object[] { Integer.valueOf(i++), world.getName(), Integer.valueOf(world.getPlayers().size()) }), ChatColor.BLUE);
/*    */     }
/* 40 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\SilentBloodGaming\Desktop\MasterFreedomMod.jar!\me\StevenLawson\TotalFreedomMod\Commands\Command_status.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */