package cvetmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import cvetmod.cards.Struggle;
import cvetmod.cards.special.Originium;
import cvetmod.characters.CivilightEterna;
import cvetmod.util.CostReserves;

public class StruggleAction extends AbstractGameAction {
    private final AbstractCard card;

    public StruggleAction(AbstractCard c) {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, 0);
        this.duration = Settings.ACTION_DUR_XFAST;
        card = c;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_XFAST) {
            AbstractPlayer p = AbstractDungeon.player;
            checkDeck(p.discardPile);
            checkDeck(p.hand);
            checkDeck(p.drawPile);
            checkDeck(Originium.originiumPile);
            checkDeck(p.exhaustPile);
            card.baseDamage += card.magicNumber;
        }
        this.tickDuration();
    }

    void checkDeck(CardGroup g) {
        for (AbstractCard c : g.group) {
            if (c instanceof Struggle) {
                c.baseDamage += card.magicNumber;
            }
        }
    }
}
