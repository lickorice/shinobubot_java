package org.georgyorgy1.shinobu.commands.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.core.Permission;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class AddCustomCommand extends Command
{
    public AddCustomCommand()
    {
        this.name = "acr";
        this.help = "Add a custom command. For owners and admins only. Usage is !acr <command name> | <response>";
    }

    @Override
    protected void execute(CommandEvent ce)
    {
        if (ce.isOwner() || ce.getMember().hasPermission(Permission.MANAGE_SERVER))
        {
            Logger logger = LoggerFactory.getLogger("org.georgyorgy1.shinobu.commands.util.AddCustomCommand");
            String[] args = ce.getArgs().split("\\s+");
            List<String> temp = new ArrayList<>();
            

            // This checks for subsequent | characters and warns the user
            for (int g = 1; g <= args.length; g++)
            {
                // Checks if there are any adjacent | characters
                if (args[g-1] == args[g] && args[g].equals("|"))
                {
                    ce.reply("Please avoid placing two vertical bars (this character: |) **next to each other.**);
                    // Breaks the function
                    return 0;
                }
            }

            int i = 0;
            while (!args[i].equals("|"))
            {
                temp.add(args[i]);
                i++;
            }
            
            String commandName = String.join(" ", temp);
            String response = "";

            for (int j = Arrays.asList(args).indexOf("|") + 1; j < args.length; j++)
            {
                response = response + args[j] + " ";
            }
            
            String url = "jdbc:sqlite:files/shinobu.db";
            Connection connection = null;
            
            try
            {
                connection = DriverManager.getConnection(url);
            }
            
            catch (SQLException exception)
            {
                logger.error(exception.toString(), exception);
            }
            
            PreparedStatement preparedStatement = null;
            String sql = "INSERT INTO custom_commands VALUES (?, ?, ?)";
            
            try
            {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, ce.getGuild().getId());
                preparedStatement.setString(2, commandName);
                preparedStatement.setString(3, response);
                preparedStatement.executeUpdate();
            }
            
            catch (SQLException exception)
            {
                logger.error(exception.toString(), exception);
                // Reply to show that command was invalid
                ce.reply("Failed to add custom reaction! Please do !help for more information.");
            }
            
            try
            {
                connection.close();
            }
            
            catch (SQLException exception)
            {
                logger.error(exception.toString(), exception);
            }
            
            ce.reply("Command added!");
        }
    }
}
