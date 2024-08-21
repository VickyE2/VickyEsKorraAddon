package com.vickyeka.vickyeskorraaddon.ability;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ability.ElementalAbility;
import com.vickyeka.vickyeskorraaddon.Elements;
import com.vickyeka.vickyeskorraaddon.VickyEsPKA;
import org.bukkit.entity.Player;

public abstract class AbilityClass extends ElementalAbility{

    public AbilityClass(Player player){
        super(player);
    }

    @Override
    public Element getElement()
    {
        return Elements.TEMPEST;
    }

}
