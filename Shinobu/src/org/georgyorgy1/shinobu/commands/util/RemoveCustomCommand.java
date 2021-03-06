package org.georgyorgy1.shinobu.commands.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.core.Permission;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class RemoveCustomCommand extends Command
{
    public RemoveCustomCommand()
    {
        this.name = "dcr";
        this.help = "Remove a custom command. For owners and admins only. Usage is !dcr <command id>";
    }

    @Override
    protected void execute(CommandEvent ce)
    {
        if (ce.isOwner() || ce.getMember().hasPermission(Permission.MANAGE_SERVER))
        {
            Logger logger = LoggerFactory.getLogger("org.georgyorgy1.shinobu.commands.util.AddCustomCommand");
            String[] args = ce.getArgs().split("\\s+");
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
            String sql = "DELETE FROM custom_commands WHERE rowid = ?";
            
            try
            {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, args[0]);
                preparedStatement.execute();
            }
            
            catch (SQLException exception)
            {
                logger.error(exception.toString(), exception);
            }
            
            try
            {
                connection.close();
            }
            
            catch (SQLException exception)
            {
                logger.error(exception.toString(), exception);
            }
            
            ce.reply("Command removed!");
        }
    }
}
