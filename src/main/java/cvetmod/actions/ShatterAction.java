package cvetmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import cvetmod.cards.AbstractCvetCard;
import cvetmod.cards.Crack;
import cvetmod.cards.Inhibitor;
import cvetmod.cards.Struggle;
import cvetmod.cards.special.Originium;
import cvetmod.cards.special.TempInhibitor;

import java.util.ArrayList;

public class ShatterAction extends AbstractGameAction {

    public ShatterAction() {
        this.duration = Settings.ACTION_DUR_LONG;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_LONG) {
            AbstractPlayer p = AbstractDungeon.player;
            checkDeck(p.discardPile);
            checkDeck(p.hand);
            checkDeck(p.drawPile);
            checkDeck(Originium.originiumPile);
            checkDeck(p.exhaustPile);
            //TODO: 如果可以的话，来个华丽的特效，瓦塔西——
            p.hand.refreshHandLayout();
            tickDuration();
        }
        isDone = true;
    }

    void checkDeck(CardGroup g) {
        for (;g.removeCard(Originium.ID);) ;
        for (;g.removeCard(Inhibitor.ID);) ;
        for (;g.removeCard(TempInhibitor.ID);) ;
    }
}
