package cvetmod.cards;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.OnObtainCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.DrawPileToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.green.Reflex;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.EnergyDownPower;
import cvetmod.actions.AddCardInHandAction;
import cvetmod.actions.DrawSpecificCardAction;
import cvetmod.actions.GainSecondEnergyAction;
import cvetmod.cards.special.Originium;
import cvetmod.powers.EnergyNextTurnPower;

import java.util.ArrayList;

public class Catastrophe extends AbstractCvetCard {
    public static final String ID = "cvetmod:Catastrophe";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/Analyse.png";
    public static final int COST = -2;
    public static final int SECOND_COST = -2;
    public static final int ENERGY_GAIN = 1;
    public static final int UPG_ENERGY_GAIN = 2;
    public Catastrophe() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        cantUseMessage = CardCrawlGame.languagePack.getCardStrings(Reflex.ID).EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (dontTriggerOnUseCard) {
            AbstractCard card = null;
            for (AbstractCard c : p.drawPile.group) {
                if (c instanceof Originium) {
                    card = c;
                    break;
                }
            }
            if (card != null) {
                card.retain = true;
                AbstractCard copy = card.makeStatEquivalentCopy();
                copy.retain = true;
                addToBot(new DrawSpecificCardAction(AbstractDungeon.player.drawPile, card));
                addToBot(new AddCardInHandAction(copy));
            }
            else {
                for (AbstractCard c : p.discardPile.group) {
                    if (c instanceof Originium) {
                        card = c;
                        break;
                    }
                }
                if (card != null) {
                    card.retain = true;
                    AbstractCard copy = card.makeStatEquivalentCopy();
                    copy.retain = true;
                    addToBot(new DrawSpecificCardAction(AbstractDungeon.player.discardPile, card));
                    addToBot(new AddCardInHandAction(copy));
                }
            }
            int energy = ENERGY_GAIN;
            if (upgraded) {
                energy = UPG_ENERGY_GAIN;
            }
            addToBot(new ApplyPowerAction(p, p, new EnergyNextTurnPower(p, energy)));
        }
    }

    public void triggerOnEndOfTurnForPlayingCard() {
        dontTriggerOnUseCard = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

}
