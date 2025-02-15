package cvetmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import cvetmod.cards.special.Originium;

import java.util.ArrayList;

public class TerminateAction extends AbstractGameAction {
    AbstractCard card;

    public TerminateAction(AbstractCard c) {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, 0);
        this.duration = Settings.ACTION_DUR_FAST;
        this.card = c;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            AbstractPlayer p = AbstractDungeon.player;
            checkDeck(p.discardPile);
            checkDeck(p.hand);
            checkDeck(p.drawPile);
        }
        this.tickDuration();
    }

    void checkDeck(CardGroup g) {
        ArrayList<AbstractCard> list = new ArrayList<>();
        for (AbstractCard c : g.group) {
            if ((!(c instanceof Originium)) && (c != card)) {
                list.add(c);
            }
        }
        for (AbstractCard c : list) {
            Originium.addToOriginium(g, c);
        }
    }
}
