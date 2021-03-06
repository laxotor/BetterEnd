package ru.betterend.blocks;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.material.Fluids;
import ru.betterend.blocks.BlockProperties.TripleShape;
import ru.betterend.blocks.basis.UnderwaterPlantWithAgeBlock;
import ru.betterend.registry.EndBlocks;
import ru.betterend.util.BlocksHelper;

public class EndLilySeedBlock extends UnderwaterPlantWithAgeBlock {
	@Override
	public void grow(WorldGenLevel world, Random random, BlockPos pos) {
		if (canGrow(world, pos)) {
			BlocksHelper.setWithoutUpdate(world, pos, EndBlocks.END_LILY.defaultBlockState().setValue(EndLilyBlock.SHAPE, TripleShape.BOTTOM));
			BlockPos up = pos.above();
			while (world.getFluidState(up).isSource()) {
				BlocksHelper.setWithoutUpdate(world, up, EndBlocks.END_LILY.defaultBlockState().setValue(EndLilyBlock.SHAPE, TripleShape.MIDDLE));
				up = up.above();
			}
			BlocksHelper.setWithoutUpdate(world, up, EndBlocks.END_LILY.defaultBlockState().setValue(EndLilyBlock.SHAPE, TripleShape.TOP));
		}
	}
	
	private boolean canGrow(WorldGenLevel world, BlockPos pos) {
		BlockPos up = pos.above();
		while (world.getBlockState(up).getFluidState().getType().equals(Fluids.WATER.getSource())) {
			up = up.above();
		}
		return world.isEmptyBlock(up);
	}
}
