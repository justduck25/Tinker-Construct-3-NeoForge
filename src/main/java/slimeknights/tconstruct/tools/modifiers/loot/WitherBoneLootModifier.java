package slimeknights.tconstruct.tools.modifiers.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import slimeknights.mantle.loot.AbstractLootModifierBuilder.GenericLootModifierBuilder;
import slimeknights.tconstruct.shared.TinkerMaterials;

import javax.annotation.Nonnull;

/** Replaces wither skeleton bones with necrotic bones and compacts the result into normal stacks. */
public class WitherBoneLootModifier extends LootModifier {
  public static final MapCodec<WitherBoneLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> codecStart(inst).apply(inst, WitherBoneLootModifier::new));

  protected WitherBoneLootModifier(LootItemCondition[] conditionsIn) {
    this(conditionsIn, IGlobalLootModifier.DEFAULT_PRIORITY);
  }

  protected WitherBoneLootModifier(LootItemCondition[] conditionsIn, int priority) {
    super(conditionsIn, priority);
  }

  /** Creates a builder for datagen. */
  public static GenericLootModifierBuilder<WitherBoneLootModifier> builder() {
    return new GenericLootModifierBuilder<>(WitherBoneLootModifier::new);
  }

  @Nonnull
  @Override
  protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
    int bones = 0;
    for (int i = generatedLoot.size() - 1; i >= 0; i--) {
      ItemStack stack = generatedLoot.get(i);
      if (stack.is(Items.BONE) || stack.is(TinkerMaterials.necroticBone.get())) {
        bones += stack.getCount();
        generatedLoot.remove(i);
      }
    }

    int maxStackSize = TinkerMaterials.necroticBone.get().getDefaultMaxStackSize();
    while (bones > 0) {
      int count = Math.min(maxStackSize, bones);
      generatedLoot.add(new ItemStack(TinkerMaterials.necroticBone.get(), count));
      bones -= count;
    }
    return generatedLoot;
  }

  @Override
  public MapCodec<? extends IGlobalLootModifier> codec() {
    return CODEC;
  }
}
