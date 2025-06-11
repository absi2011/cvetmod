package cvetmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import cvetmod.cards.AbstractCvetCard;
import cvetmod.cards.Crack;
import cvetmod.cards.Seal;
import cvetmod.cards.special.Originium;

public class SealAction extends AbstractGameAction {
    String[] TEXT = CardCrawlGame.languagePack.getUIString("cvetmod:SealScreen").TEXT;

    public SealAction(int amount) {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, 0);
        this.duration = Settings.ACTION_DUR_XFAST;
        this.amount = amount;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_XFAST) {
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            group.group.addAll(AbstractDungeon.player.exhaustPile.group);
            if (group.isEmpty()) {
                this.isDone = true;
                return;
            }
            if (group.size() < amount) {
                amount = group.size();
            }
            if (group.size() == amount) {
                for (int i = 0; i < amount; i++) {
                    AbstractCard card = group.group.get(i);
                    Originium.addToOriginium(AbstractDungeon.player.exhaustPile, card);
                }
                isDone = true;
                return;
            }

            AbstractDungeon.gridSelectScreen.open(group, amount, false, TEXT[0]);
            this.tickDuration();
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (int i = 0; i < amount; i++) {
                AbstractCard card = AbstractDungeon.gridSelectScreen.selectedCards.get(i);
                Originium.addToOriginium(AbstractDungeon.player.exhaustPile, card);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.player.hand.refreshHandLayout();
            this.isDone = true;
        } else {
            this.tickDuration();
        }
    }
}
