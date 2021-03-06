package org.georgyorgy1.shinobu.commands.moderation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.core.exceptions.HierarchyException;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class RemoveRoleCommand extends Command
{
    public RemoveRoleCommand()
    {
        this.name = "removerole";
        this.help = "Removes a role to a user.";
    }

    @Override
    protected void execute(CommandEvent ce)
    {
        if (ce.getMember().hasPermission(Permission.MANAGE_ROLES))
        {
            String[] args = ce.getArgs().split("\\s+");
            String user = args[0].replace("<", "").replace("@", "").replace("!", "").replace(">", "");
            List<String> combinedStrings = new ArrayList<String>();
            Logger logger = LoggerFactory.getLogger(RemoveRoleCommand.class.getName());

            for (int i = 1; i < args.length; i++)
            {
                combinedStrings.add(args[i]);
            }
            
            String roleName = String.join(" ", combinedStrings);

            try
            {
                ce.getEvent().getGuild().getController().removeSingleRoleFromMember(ce.getGuild().getMemberById(user), ce.getEvent().getGuild().getRolesByName(roleName, true).get(0)).queue();
                ce.reply("The role named " + roleName + " was removed from <@!" + user + ">");
            }
            
            catch (HierarchyException ex)
            {
                logger.error(ex.toString());
                ce.reply("I cannot remove that role due to an issue with the role hierarchy.");
            }
            
            catch (InsufficientPermissionException ex)
            {
                logger.error(ex.toString());
                ce.reply("I do not have the permission to remove the user's role.");
            }
            
            catch (IndexOutOfBoundsException ex)
            {
                logger.error(ex.toString());
                ce.reply("The role you are trying to remove does not exist!");
            }
        }
        
        else
        {
            ce.reply("You do not have the Manage Roles permission!");
        }
    }
}
