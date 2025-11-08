package cvetmod.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import cvetmod.cards.Growth;
import cvetmod.cards.special.Originium;
import cvetmod.monsters.AbstractCvetMonster;

public class GrowthAction extends AbstractGameAction {
    Growth card;
    int delay;
    public GrowthAction(Growth growth, int delay) {
        actionType = ActionType.SPECIAL;
        card = growth;
        this.delay = delay;
    }

    @Override
    public void update() {
        if (delay > 0) {
            addToBot(new GrowthAction(card, delay - 1));
            isDone = true;
            return;
        }
        card.updateCost(1);
        card.isCostModified = (card.cost != card.costForTurn);
        card.baseDamage *= card.magicNumber;
        if (Originium.originiumPile.contains(card)) {
            Originium.originiumPile.removeCard(card);
            addToTop(new AddCardInHandAction(card));
        }
        else if (AbstractDungeon.player.discardPile.contains(card)) {
            AbstractDungeon.player.discardPile.removeCard(card);
            addToTop(new AddCardInHandAction(card));
        }
        else if (AbstractDungeon.player.drawPile.contains(card)) {
            AbstractDungeon.player.drawPile.removeCard(card);
            addToTop(new AddCardInHandAction(card));
        }
        else if (AbstractDungeon.player.exhaustPile.contains(card)) {
            AbstractDungeon.player.exhaustPile.removeCard(card);
            addToTop(new AddCardInHandAction(card));
        }
        isDone = true;
    }
}
