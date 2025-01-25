package cvetmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import cvetmod.characters.CivilightEterna;
import cvetmod.util.CostReserves;
import cvetmod.util.CvetEnergyManager;

import java.util.Iterator;

public class GainSecondEnergyAction extends AbstractGameAction {
    private final int energyGain;

    public GainSecondEnergyAction(int amount) {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, 0);
        this.duration = Settings.ACTION_DUR_FAST;
        this.energyGain = amount;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (AbstractDungeon.player instanceof CivilightEterna) {
                CostReserves.addReserves(energyGain);
            }
            AbstractDungeon.player.hand.glowCheck();
            for (AbstractCard c:AbstractDungeon.player.hand.group) {
                c.triggerOnGainEnergy(this.energyGain, true);
            }
        }

        this.tickDuration();
    }
}
