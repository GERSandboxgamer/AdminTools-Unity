package de.sbg.unity.admintools.Objects;

import de.sbg.unity.admintools.AdminTools;
import de.sbg.unity.admintools.atConsole;
import de.sbg.unity.admintools.database.stDatabase;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.risingworld.api.objects.Player;
import net.risingworld.api.utils.Quaternion;
import net.risingworld.api.utils.Vector3f;


public class Teleporters {
    
    private final List<Teleporter> TeleporterList;
    public final stDatabase Database;
    
    public Teleporters(AdminTools plugin, atConsole Console) throws SQLException{
        this.TeleporterList = new ArrayList<>();
        this.Database = new stDatabase(plugin, Console, this);
    }
    
    public Teleporter getTeleporterByName(String name) {
        for (Teleporter tel : TeleporterList) {
            if (tel.getName().equals(name)) {
                return tel;
            }
        }
        return null;
    }

    public Teleporter getTeleporterByID(int id) {
        for (Teleporter tel : TeleporterList) {
            if (tel.getID() == id) {
                return tel;
            }
        }
        return null;
    }

    public List<Teleporter> getTeleporterList() {
        return TeleporterList;
    }

    public boolean hasTeleporter(String name) {
        for (Teleporter tel : getTeleporterList()) {
            if (tel.getName().matches(name)) {
                return true;
            }
        }
        return false;
    }
    
    public void editTeleporter(String name, Vector3f pos, Quaternion rot) throws SQLException {
        Teleporter tel = getTeleporterByName(name);
        tel.setPosition(pos);
        tel.setRotation(rot);
        Database.editTeleporter(name, pos, rot);
    }

    public Teleporter addNewTeleporter(String name, Player player) throws SQLException {
        return addNewTeleporter(name, player.getPosition(), player.getRotation());
    }

    public Teleporter addNewTeleporter(String name, Vector3f pos, Quaternion rot) throws SQLException {
        int id = Database.setNewTeleporter(name, pos, rot);
        Teleporter tel = new Teleporter(id, name, pos, rot);
        getTeleporterList().add(tel);
        return tel;
    }

    public boolean removeTeleporter(String name) throws SQLException {
        if (hasTeleporter(name)) {
            Database.removeTeleporter(name);
            getTeleporterList().remove(getTeleporterByName(name));
            
            return true;
        }
        return false;
    }
    
}
