package fr.robie.craftengineconverter.database.migrations;

import fr.maxlego08.sarah.database.Migration;
import fr.robie.craftengineconverter.api.BlockHistory;

public class WorldBlockConverterHistorical extends Migration {
    @Override
    public void up() {
//        this.create("world_block_converter_historical", table -> {
//            table.autoIncrementBigInt("id");
//            table.string("world_name", 255);
//            table.integer("chunk_x");
//            table.integer("chunk_z");
//            table.integer("block_x");
//            table.integer("block_y");
//            table.integer("block_z");
//            table.string("original_block", 255);
//            table.string("converted_block", 255);
//            table.bool("reverted").defaultValue(false);
//        });
        this.create("world_block_converter_historical", BlockHistory.class);
    }

}
