package ru.betterend.item;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.SpawnEggItem;
import ru.betterend.patterns.Patterned;
import ru.betterend.patterns.Patterns;

public class EndSpawnEggItem extends SpawnEggItem implements Patterned {
	public EndSpawnEggItem(EntityType<?> type, int primaryColor, int secondaryColor, Properties settings) {
		super(type, primaryColor, secondaryColor, settings);
	}
	
	@Override
	public String getModelPattern(String name) {
		return Patterns.createJson(Patterns.ITEM_SPAWN_EGG, name);
	}
}
