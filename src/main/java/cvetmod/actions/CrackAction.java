package cvetmod.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import cvetmod.cards.AbstractCvetCard;
import cvetmod.cards.Crack;
import cvetmod.cards.special.Originium;

import java.util.ArrayList;

public class CrackAction extends AbstractGameAction {
    boolean upgraded;
    String[] TEXT = CardCrawlGame.languagePack.getUIString("cvetmod:CrackScreen").TEXT;

    public CrackAction(boolean upgraded) {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, 0);
        this.duration = Settings.ACTION_DUR_XFAST;
        this.upgraded = upgraded;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_XFAST) {
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c:Originium.originiumPile.group) {
                if (!(c instanceof Crack)) {
                    group.group.add(c);
                }
            }
            if (group.isEmpty()) {
                this.isDone = true;
                return;
            }
            if (group.size() == 1) {
                crack(group.group.get(0));
                isDone = true;
                return;
            }

            AbstractDungeon.gridSelectScreen.open(group, 1, false, TEXT[0]);
            this.tickDuration();
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            crack(card);
            this.isDone = true;
        } else {
            this.tickDuration();
        }
    }

    private void crack(AbstractCard card) {
        AbstractDungeon.player.hand.refreshHandLayout();
        AbstractCard copies = card.makeStatEquivalentCopy();
        copies.exhaust = true;
        if (!copies.name.startsWith(TEXT[1]))  {
            copies.name = TEXT[1] + copies.name;
            copies.rawDescription += TEXT[2];
            copies.initializeDescription();
        }
        if ((upgraded) && (copies instanceof AbstractCvetCard)) {
            ((AbstractCvetCard)copies).reduceSecondCost(1);
        }
        addToTop(new MakeTempCardInHandAction(copies, 2));
        addToTop(new ExhaustSpecificCardAction(card, Originium.originiumPile));
    }
}
