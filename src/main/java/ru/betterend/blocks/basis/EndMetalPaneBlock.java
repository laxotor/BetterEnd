package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.Collections;
import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;

public class EndMetalPaneBlock extends IronBarsBlock implements BlockPatterned, IRenderTypeable {
	public EndMetalPaneBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).strength(5.0F, 6.0F).noOcclusion());
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		return Patterns.createJson(data, blockId.getPath(), blockId.getPath());
	}
	
	@Override
	public String getModelPattern(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		if (block.contains("item")) {
			return Patterns.createJson(Patterns.ITEM_BLOCK, blockId.getPath());
		}
		if (block.contains("post")) {
			return Patterns.createJson(Patterns.BLOCK_BARS_POST, blockId.getPath(), blockId.getPath());
		}
		else {
			return Patterns.createJson(Patterns.BLOCK_BARS_SIDE, blockId.getPath(), blockId.getPath());
		}
	}
	
	@Environment(EnvType.CLIENT)
	public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
		if (direction.getAxis().isVertical() && stateFrom.getBlock().is(this) && !stateFrom.equals(state)) {
			return false;
		}
		return super.skipRendering(state, stateFrom, direction);
	}
	
	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_BARS;
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
}
