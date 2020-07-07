package com.bgsoftware.superiorskyblock.commands;

import com.bgsoftware.superiorskyblock.Locale;
import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.utils.StringUtils;
import com.bgsoftware.superiorskyblock.wrappers.player.SSuperiorPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CmdBalance implements ISuperiorCommand {

    @Override
    public List<String> getAliases() {
        return Arrays.asList("balance", "bal", "money");
    }

    @Override
    public String getPermission() {
        return "superior.island.balance";
    }

    @Override
    public String getUsage(java.util.Locale locale) {
        return "balance [" + Locale.COMMAND_ARGUMENT_PLAYER_NAME.getMessage(locale) + "]";
    }

    @Override
    public String getDescription(java.util.Locale locale) {
        return Locale.COMMAND_DESCRIPTION_BALANCE.getMessage(locale);
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public int getMaxArgs() {
        return 2;
    }

    @Override
    public boolean canBeExecutedByConsole() {
        return false;
    }

    @Override
    public void execute(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        SuperiorPlayer targetPlayer;
        Island island;

        if(args.length == 1){
            if(!(sender instanceof Player)){
                Locale.sendMessage(sender, "&cYou must specify a player's name.", true);
                return;
            }

            targetPlayer = SSuperiorPlayer.of(sender);

            Island locationIsland = plugin.getGrid().getIslandAt(targetPlayer.getLocation());

            island = locationIsland == null || locationIsland.isSpawn() ? targetPlayer.getIsland() : locationIsland;
        }
        else{
            targetPlayer = SSuperiorPlayer.of(args[1]);
            island = targetPlayer == null ? plugin.getGrid().getIsland(args[1]) : targetPlayer.getIsland();
        }

        if(island == null){
            if(args.length == 1 || args[1].equalsIgnoreCase(sender.getName()))
                Locale.INVALID_ISLAND.send(sender);
            else if(targetPlayer == null)
                Locale.INVALID_ISLAND_OTHER_NAME.send(sender, StringUtils.stripColors(args[1]));
            else
                Locale.INVALID_ISLAND_OTHER.send(sender, targetPlayer.getName());
            return;
        }

        if(targetPlayer != null && island == targetPlayer.getIsland())
            Locale.ISLAND_BANK_SHOW.send(sender, island.getMoneyInBank());
        else if(targetPlayer == null)
            Locale.ISLAND_BANK_SHOW_OTHER_NAME.send(sender, island.getName(), island.getMoneyInBank());
        else
            Locale.ISLAND_BANK_SHOW_OTHER.send(sender, targetPlayer.getName(), island.getMoneyInBank());
    }

    @Override
    public List<String> tabComplete(SuperiorSkyblockPlugin plugin, CommandSender sender, String[] args) {
        SuperiorPlayer superiorPlayer = sender instanceof Player ? SSuperiorPlayer.of(sender) : null;
        Island island = superiorPlayer == null ? null : superiorPlayer.getIsland();
        List<String> list = new ArrayList<>();

        if(args.length == 2){
            for(Player player : Bukkit.getOnlinePlayers()){
                SuperiorPlayer onlinePlayer = SSuperiorPlayer.of(player);
                Island playerIsland = onlinePlayer.getIsland();
                if (playerIsland != null && (superiorPlayer == null || island == null ||
                        !island.getOwner().getUniqueId().equals(player.getUniqueId()))) {
                    if (player.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                        list.add(player.getName());
                    if(!playerIsland.getName().isEmpty() && playerIsland.getName().toLowerCase().startsWith(args[1].toLowerCase()))
                        list.add(playerIsland.getName());
                }
            }
        }

        return list;
    }
}