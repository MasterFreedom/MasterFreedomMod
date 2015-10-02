package me.StevenLawson.TotalFreedomMod.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions(level = AdminLevel.ALL, source = SourceType.BOTH)
@CommandParameters(description = "reuben4545 and ASMaster are totally sexy!", usage = "/<command>")
public class Command_sexy extends TFM_Command
{
    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        sender_p.chat("Guys!");
        sender_p.chat("I found out something!");
        sender_p.chat("reuben4545 is very sexy and is the best chief-dev i ever saw!");
        sender_p.chat("Also ASMaster is very sexy and the best main-owner and main-founder i've ever saw!")
        sender_p.chat("Also they are epic! :D");
        playerMsg("You just said reuben4545 and ASMaster are sexy! :O", ChatColor.RED);
        playerMsg("Your Words were 100% true, reuben4545 is sexy, and he's our Chief-dev! :D", ChatColor.YELLOW");
        playerMsg("Your other Words were 100% true, ASMaster is sexy, and he's our Main-Owner and Main-Founder! :D", ChatColor.YELLOW);
        return true;  
    }
}
