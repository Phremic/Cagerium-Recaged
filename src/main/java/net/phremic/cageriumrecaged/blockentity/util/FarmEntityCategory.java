package net.phremic.cageriumrecaged.blockentity.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.warden.Warden;

public enum FarmEntityCategory {
    PASSIVE,
    HOSTILE,
    BOSS;

    public boolean acceptsEntity(Entity pEntity) {

        // Override category for specific mobs
        if (pEntity instanceof ElderGuardian) return this == BOSS;
        if (pEntity instanceof EnderDragon) return this == BOSS;
        if (pEntity instanceof Evoker) return this == BOSS;
        if (pEntity instanceof IronGolem) return this == HOSTILE;
        if (pEntity instanceof Warden) return this == BOSS;
        if (pEntity instanceof WitherBoss) return this == BOSS;

        // Determine mob category
        if (pEntity.getType().getCategory().isFriendly()) return this == PASSIVE;
        if (pEntity.getType().getCategory() == MobCategory.MONSTER) return this == HOSTILE;
        return this == BOSS;
    }
}
