package fr.robie.craftengineconverter.command;

import com.moulberry.axiom.paperapi.AxiomCustomBlocksAPI;
import com.moulberry.axiom.paperapi.block.AxiomCustomBlockBuilder;
import fr.robie.craftengineconverter.CraftEngineConverter;
import fr.robie.craftengineconverter.common.permission.Permission;
import fr.robie.craftengineconverter.utils.command.CommandType;
import fr.robie.craftengineconverter.utils.command.VCommand;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

public class CraftEngineConverterCommand extends VCommand {
    public CraftEngineConverterCommand(CraftEngineConverter craftEngineConverter) {
        super(craftEngineConverter);
        this.setPermission(Permission.COMMAND_USE);
        this.addSubCommand(new CraftEngineConverterCommandReload(craftEngineConverter));
        this.addSubCommand(new CraftEngineConverterCommandConvert(craftEngineConverter));
        this.addSubCommand(new CraftEngineConverterCommandClearFilesCache(craftEngineConverter));
    }

    @Override
    protected CommandType perform(CraftEngineConverter plugin) {
        syntaxMessage();
        AxiomCustomBlocksAPI api = AxiomCustomBlocksAPI.getAPI();
        BlockData blockData = plugin.getServer().createBlockData(Material.DIAMOND_BLOCK);
        AxiomCustomBlockBuilder helloWorld = api.createSingle(Key.key("test:test"), "hello world", blockData);
        helloWorld.sendServerPickBlockIfPossible(true);
        helloWorld.pickBlockItemStack(new ItemStack(Material.DIAMOND_BLOCK));
        try {
            api.register(plugin, helloWorld);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return CommandType.SUCCESS;
    }
}
