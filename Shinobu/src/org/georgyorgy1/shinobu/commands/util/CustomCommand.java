package org.georgyorgy1.shinobu.commands.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomCommand extends ListenerAdapter
{
    //Don't ask why. This is the only way I can implement custom commands.
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.getAuthor().isBot() == false)
        {
            Logger logger = LoggerFactory.getLogger("org.georgyorgy1.shinobu.commands.util.CustomCommand");
            Connection connection = null;
            String url = "jdbc:sqlite:files/shinobu.db";

            try
            {
                connection = DriverManager.getConnection(url);
            }

            catch (SQLException exception)
            {
                logger.error(exception.toString(), exception);
            }

            PreparedStatement preparedStatement = null;
            String sql = "SELECT response FROM custom_commands WHERE guild = ? AND command_name = ? ORDER BY RANDOM() LIMIT 1";
            Message message = event.getMessage();
            String response = "";

            try
            {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, event.getGuild().getId());
                preparedStatement.setString(2, message.getContentDisplay());

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next())
                {
                    response = resultSet.getString("response");
                }
            }

            catch (SQLException exception)
            {
                logger.error(exception.toString(), exception);
            }

            if (!response.equals(""))
            {
                MessageChannel channel = event.getChannel();
                channel.sendMessage(response).queue();

                try
                {
                    connection.close();
                }

                catch (SQLException exception)
                {
                    logger.error(exception.toString(), exception);
                }
            }

            else
            {
                try
                {
                    connection.close();
                }

                catch (SQLException exception)
                {
                    logger.error(exception.toString(), exception);
                }
            }
        }
    }
}
