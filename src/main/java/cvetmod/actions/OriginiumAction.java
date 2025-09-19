package cvetmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import cvetmod.cards.special.Originium;

public class OriginiumAction extends AbstractGameAction {
    public final AbstractCard card;
    public OriginiumAction(AbstractCard card) {
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_FAST;
        this.card = card;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.player.drawPile.removeCard(card);
            AbstractDungeon.player.hand.removeCard(card);
            AbstractDungeon.player.discardPile.removeCard(card);
            Originium.originiumPile.addToTop(card);
        }
        tickDuration();
    }
}
