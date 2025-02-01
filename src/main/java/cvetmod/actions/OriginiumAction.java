package cvetmod.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import cvetmod.cards.special.Originium;
import cvetmod.monsters.AbstractCvetMonster;

public class OriginiumAction extends AbstractGameAction {
    public final AbstractCard card;
    public OriginiumAction(AbstractCard card) {
        actionType = ActionType.SPECIAL;
        duration = Settings.ACTION_DUR_FAST;
        this.card = card;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            AbstractDungeon.player.drawPile.removeCard(card);
            AbstractDungeon.player.hand.removeCard(card);
            AbstractDungeon.player.discardPile.removeCard(card);
            Originium.originium.addToTop(card);
        }
        tickDuration();
    }
}
