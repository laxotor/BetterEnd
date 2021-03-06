package ru.betterend.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import ru.betterend.registry.EndBlockEntities;
import ru.betterend.rituals.InfusionRitual;

public class InfusionPedestalEntity extends PedestalBlockEntity {

	private InfusionRitual linkedRitual;
	
	public InfusionPedestalEntity() {
		super(EndBlockEntities.INFUSION_PEDESTAL);
	}
	
	@Override
	public void setLevelAndPosition(Level world, BlockPos pos) {
		super.setLevelAndPosition(world, pos);
		if (hasRitual()) {
			linkedRitual.setLocation(world, pos);
		}
	}
	
	public void linkRitual(InfusionRitual ritual) {
		linkedRitual = ritual;
	}
	
	public InfusionRitual getRitual() {
		return linkedRitual;
	}
	
	public boolean hasRitual() {
		return linkedRitual != null;
	}
	
	@Override
	public void tick() {
		if (hasRitual()) {
			linkedRitual.tick();
		}
		super.tick();
	}

	@Override
	public CompoundTag save(CompoundTag tag) {
		if (hasRitual()) {
			tag.put("ritual", linkedRitual.toTag(new CompoundTag()));
		}
		return super.save(tag);
	}

	@Override
	protected void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		if (tag.contains("ritual")) {
			linkedRitual = new InfusionRitual(level, worldPosition);
			linkedRitual.fromTag(tag.getCompound("ritual"));
		}
	}
}
