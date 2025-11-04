package cvetmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import cvetmod.actions.GainSecondEnergyAction;

public class EnergyNextTurnPower extends AbstractCvetPower {
    public static final String POWER_ID = "cvetmod:EnergyNextTurnPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public EnergyNextTurnPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.BUFF;
        this.owner = owner;
        this.amount = amount;
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("cvetmod/images/cards/CvetStrikeA.png"), 0, 0, 128, 128);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("cvetmod/images/cards/CvetStrikeA.png"), 0, 0, 48, 48);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
        for (int i = 0; i < amount; i++) {
            description += "[E] ";
        }
        for (int i = 0; i < amount; i++) {
            description += "[cvetmod:E2Icon] ";
        }
        description += DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new GainEnergyAction(amount));
        addToBot(new GainSecondEnergyAction(amount));
        flash();
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }
}
