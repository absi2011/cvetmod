package cvetmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import cvetmod.cards.Seal;
import cvetmod.cards.special.Originium;

import java.util.ArrayList;

public class ConvertAction extends AbstractGameAction {
    String[] TEXT = CardCrawlGame.languagePack.getUIString("cvetmod:ConvertScreen").TEXT;
    public final ArrayList<AbstractCard> cannotConvert = new ArrayList<>();

    public ConvertAction(int amount) {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, 0);
        this.duration = Settings.ACTION_DUR_XFAST;
        this.amount = amount;
    }

    public ConvertAction() {
        this(1);
    }

    public void update() {
        CardGroup group = AbstractDungeon.player.hand;
        if (duration == Settings.ACTION_DUR_XFAST) {
            for (AbstractCard c : group.group)
                if (c instanceof Originium) {
                    cannotConvert.add(c);
                }
            if (cannotConvert.size() == group.group.size()) {
                isDone = true;
                return;
            }
            if (amount > group.group.size()) {
                amount = group.group.size();
            }
            if (group.group.size() == amount) {
                ArrayList<AbstractCard> list = new ArrayList<>();
                for (AbstractCard c : group.group) {
                    list.add(c);
                }
                for (AbstractCard c : list) {
                    Originium.addToOriginium(group, c);
                }
                group.refreshHandLayout();
                isDone = true;
                return;
            }
            group.group.removeAll(cannotConvert);
            AbstractDungeon.handCardSelectScreen.open(TEXT[0] + amount + TEXT[1], amount, false, false, false);
            tickDuration();
            return;
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                Originium.addToOriginium(group, c);
                c.superFlash();
            }
            for (AbstractCard c : cannotConvert)
                group.addToTop(c);
            group.refreshHandLayout();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            isDone = true;
        }

    }
}
