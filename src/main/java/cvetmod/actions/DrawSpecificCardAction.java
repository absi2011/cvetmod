package cvetmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import cvetmod.cards.Struggle;

import java.util.ArrayList;

public class DrawSpecificCardAction extends AbstractGameAction {
    CardGroup group;
    String id = "";
    AbstractCard card = null;

    public DrawSpecificCardAction(CardGroup g, String id) {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, 0);
        this.duration = Settings.ACTION_DUR_FAST;
        this.group = g;
        this.id = id;
    }
    public DrawSpecificCardAction(CardGroup g, AbstractCard card) {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, 0);
        this.duration = Settings.ACTION_DUR_FAST;
        this.group = g;
        this.card = card;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            ArrayList<AbstractCard> cards = new ArrayList<AbstractCard>();
            for (AbstractCard c: group.group) {
                if (c.cardID.equals(id)) {
                    cards.add(c);
                }
                if (c == card) {
                    cards.add(c);
                }
            }
            if (!cards.isEmpty()) {
                AbstractCard card = cards.get(0);
                group.removeCard(card);
                AbstractDungeon.player.drawPile.addToTop(card);
                addToTop(new DrawCardAction(1));
            }
        }
        this.tickDuration();
    }

}
