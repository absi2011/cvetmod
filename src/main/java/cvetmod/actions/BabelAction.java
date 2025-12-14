package cvetmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import cvetmod.cards.AbstractCvetCard;
import cvetmod.cards.Growth;
import cvetmod.cards.special.Originium;
import jdk.internal.net.http.common.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class BabelAction extends AbstractGameAction {
    public BabelAction() {
        actionType = ActionType.DRAW;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        HashMap<Integer, ArrayList<AbstractCard>> map = new HashMap<>();
        for (AbstractCard c: AbstractDungeon.player.drawPile.group) {
            int firstCost, secondCost;
            if (c instanceof AbstractCvetCard) {
                firstCost = c.cost;
                secondCost = ((AbstractCvetCard) c).secondCost;
            }
            else {
                firstCost = c.cost;
                secondCost = 0;
            }
            if (!map.containsKey(firstCost*10000 + secondCost)) {
                map.put(firstCost*10000 + secondCost, new ArrayList<>());
            }
            map.get(firstCost*10000 + secondCost).add(c);
        }
        ArrayList<AbstractCard> selectedCards = new ArrayList<>();
        for (ArrayList<AbstractCard> groups : map.values()) {
            selectedCards.add(groups.get(AbstractDungeon.cardRng.random(0, groups.size() - 1)));
        }
        for (AbstractCard c : selectedCards) {
            AbstractDungeon.player.drawPile.removeCard(c);
        }
        for (AbstractCard c : selectedCards) {
            AbstractDungeon.player.drawPile.addToTop(c);
        }
        AbstractDungeon.actionManager.addToTop(new DrawCardAction(selectedCards.size()));
        isDone = true;
    }
}
