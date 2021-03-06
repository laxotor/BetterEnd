package ru.betterend.item.tool;

import net.fabricmc.fabric.api.tool.attribute.v1.DynamicAttributeTool;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import ru.betterend.patterns.Patterned;
import ru.betterend.patterns.Patterns;

public class EndSwordItem extends SwordItem implements DynamicAttributeTool, Patterned {
	public EndSwordItem(Tier material, int attackDamage, float attackSpeed, Properties settings) {
		super(material, attackDamage, attackSpeed, settings);
	}
	
	@Override
	public String getModelPattern(String name) {
		return Patterns.createJson(Patterns.ITEM_HANDHELD, name);
	}
}
