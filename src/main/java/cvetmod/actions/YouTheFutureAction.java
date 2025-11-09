package cvetmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import cvetmod.cards.Inhibitor;
import cvetmod.cards.special.Originium;
import cvetmod.cards.special.TempInhibitor;

import java.util.ArrayList;

public class YouTheFutureAction extends AbstractGameAction {

    public YouTheFutureAction() {
        this.duration = Settings.ACTION_DUR_LONG;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_LONG) {
            AbstractPlayer p = AbstractDungeon.player;
            ArrayList<AbstractCard> g = new ArrayList<>(Originium.originiumPile.group);
            Originium.originiumPile.group.clear();
            for (AbstractCard c : g) {
                c.exhaustOnUseOnce = true;
                AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(c, AbstractDungeon.getRandomMonster(), c.energyOnUse, true, true), true);
            }
            p.hand.refreshHandLayout();
            tickDuration();
        }
        isDone = true;
    }
}
