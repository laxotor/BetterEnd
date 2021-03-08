package ru.betterend.blocks;

import java.util.EnumMap;

import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import ru.betterend.blocks.basis.AttachedBlock;
import ru.betterend.client.render.ERenderLayer;
import ru.betterend.interfaces.IRenderTypeable;

public class SmaragdantCrystalShardBlock extends AttachedBlock implements IRenderTypeable, Waterloggable, FluidFillable {
	private static final EnumMap<Direction, VoxelShape> BOUNDING_SHAPES = Maps.newEnumMap(Direction.class);
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	
	public SmaragdantCrystalShardBlock() {
		super(FabricBlockSettings.of(Material.STONE)
				.materialColor(MaterialColor.GREEN)
				.breakByTool(FabricToolTags.PICKAXES)
				.sounds(BlockSoundGroup.GLASS)
				.luminance(15)
				.requiresTool()
				.noCollision());
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
		super.appendProperties(stateManager);
		stateManager.add(WATERLOGGED);
	}
	
	@Override
	public ERenderLayer getRenderLayer() {
		return ERenderLayer.CUTOUT;
	}
	
	@Override
	public boolean canFillWithFluid(BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
		return !state.get(WATERLOGGED);
	}

	@Override
	public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
		return !state.get(WATERLOGGED);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState state = super.getPlacementState(ctx);
		if (state != null) {
			WorldView worldView = ctx.getWorld();
			BlockPos blockPos = ctx.getBlockPos();
			boolean water = worldView.getFluidState(blockPos).getFluid() == Fluids.WATER;
			return state.with(WATERLOGGED, water);
		}
		return null;
	}
	
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : Fluids.EMPTY.getDefaultState();
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos) {
		return BOUNDING_SHAPES.get(state.get(FACING));
	}
	
	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		Direction direction = (Direction) state.get(FACING);
		BlockPos blockPos = pos.offset(direction.getOpposite());
		return world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, direction);
	}
	
	static {
		BOUNDING_SHAPES.put(Direction.UP, VoxelShapes.cuboid(0.125, 0.0, 0.125, 0.875F, 0.875F, 0.875F));
		BOUNDING_SHAPES.put(Direction.DOWN, VoxelShapes.cuboid(0.125, 0.125, 0.125, 0.875F, 1.0, 0.875F));
		BOUNDING_SHAPES.put(Direction.NORTH, VoxelShapes.cuboid(0.125, 0.125, 0.125, 0.875F, 0.875F, 1.0));
		BOUNDING_SHAPES.put(Direction.SOUTH, VoxelShapes.cuboid(0.125, 0.125, 0.0, 0.875F, 0.875F, 0.875F));
		BOUNDING_SHAPES.put(Direction.WEST, VoxelShapes.cuboid(0.125, 0.125, 0.125, 1.0, 0.875F, 0.875F));
		BOUNDING_SHAPES.put(Direction.EAST, VoxelShapes.cuboid(0.0, 0.125, 0.125, 0.875F, 0.875F, 0.875F));
	}
}
