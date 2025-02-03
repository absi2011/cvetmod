package cvetmod.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class SpiritBurstPower extends AbstractCvetPower {
    public static final String POWER_ID = "cvetmod:SpiritBurstPower";
    public static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    boolean nextTurn = false;
    boolean thisTurn = false;
    public SpiritBurstPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.DEBUFF;
        this.owner = owner;
        //TODO: 找个机会创建个模板吧，别非要让我用cards的
        region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("cvetmod/images/cards/CvetStrikeA.png"), 0, 0, 128, 128);
        region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("cvetmod/images/cards/CvetStrikeA.png"), 0, 0, 48, 48);
        updateDescription();
        nextTurn = true;
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        nextTurn = true;
        updateDescription();
    }

    @Override
    public boolean canPlayCard(AbstractCard card) {
        if (thisTurn && card.costForTurn > 0) {
            return false;
        }
        return super.canPlayCard(card);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        thisTurn = nextTurn;
        nextTurn = false;
        updateDescription();
        if (!thisTurn) {
            addToBot(new RemoveSpecificPowerAction(owner, owner,this));
        }
    }

    @Override
    public void updateDescription() {
        if (thisTurn && nextTurn) {
            description = DESCRIPTIONS[2];
        }
        else if (thisTurn) {
            description = DESCRIPTIONS[0];
        }
        else if (nextTurn) {
            description = DESCRIPTIONS[1];
        }
        else {
            description = DESCRIPTIONS[0];
        }
    }
}
