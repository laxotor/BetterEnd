package ru.betterend.blocks.basis;

import java.io.Reader;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import ru.betterend.blocks.entities.EFurnaceBlockEntity;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;
import ru.betterend.patterns.BlockPatterned;
import ru.betterend.patterns.Patterns;

public class EndFurnaceBlock extends FurnaceBlock implements BlockPatterned, IRenderTypeable {
	public EndFurnaceBlock(Block source) {
		super(FabricBlockSettings.copyOf(source).luminance((state) -> {
			return state.getValue(LIT) ? 13 : 0;
		}));
	}

	@Override
	public BlockEntity newBlockEntity(BlockGetter world) {
		return new EFurnaceBlockEntity();
	}
	
	@Override
	protected void openContainer(Level world, BlockPos pos, Player player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof EFurnaceBlockEntity) {
			player.openMenu((MenuProvider) blockEntity);
			player.awardStat(Stats.INTERACT_WITH_FURNACE);
		}
	}
	
	@Override
	public String getStatesPattern(Reader data) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		return Patterns.createJson(data, blockId.getPath(), blockId.getPath());
	}
	
	@Override
	public String getModelPattern(String block) {
		ResourceLocation blockId = Registry.BLOCK.getKey(this);
		Map<String, String> map = Maps.newHashMap();
		map.put("%top%", blockId.getPath() + "_top");
		map.put("%side%", blockId.getPath() + "_side");
		if (block.contains("_on")) {
			map.put("%front%", blockId.getPath() + "_front_on");
			map.put("%glow%", blockId.getPath() + "_glow");
			return Patterns.createJson(Patterns.BLOCK_FURNACE_GLOW, map);
		}
		else {
			map.put("%front%", blockId.getPath() + "_front");
			return Patterns.createJson(Patterns.BLOCK_FURNACE, map);
		}
	}
	
	@Override
	public ResourceLocation statePatternId() {
		return Patterns.STATE_FURNACE;
	}

	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		List<ItemStack> drop = Lists.newArrayList(new ItemStack(this));
		BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		if (blockEntity instanceof EFurnaceBlockEntity) {
			EFurnaceBlockEntity entity = (EFurnaceBlockEntity) blockEntity;
			for (int i = 0; i < entity.getContainerSize(); i++) {
				drop.add(entity.getItem(i));
			}
		}
		return drop;
	}
}
