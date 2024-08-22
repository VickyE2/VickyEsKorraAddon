package com.vickyeka.vickyeskorraaddon.abilityparents;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.ElementalAbility;
import com.vickyeka.vickyeskorraaddon.Elements;
import org.bukkit.entity.Player;


public abstract class TempestAbility extends ElementalAbility implements AddonAbility {

    public boolean isIgniteAbility() {
        return true;
    }

    public boolean isExplosiveAbility() {
        return false;
    }

    public static boolean canTempestGrief() {
        return getConfig().getBoolean("VickyEsKA.Tempest.CanTempestGrief");
}

    public TempestAbility(Player player){
        super(player);
    }

    public Element getElement() {
        return Elements.TEMPEST;
    }

}
