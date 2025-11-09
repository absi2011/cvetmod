package cvetmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import cvetmod.cards.special.Originium;

public class DustOfThePastAction extends AbstractGameAction {
    public DustOfThePastAction(int amount) {
        this.amount = amount;
        actionType = ActionType.SPECIAL;
        duration = startDuration = Settings.ACTION_DUR_XFAST;
    }

    @Override
    public void update() {
        for (AbstractCard c : DrawCardAction.drawnCards)
            if (AbstractDungeon.actionManager.cardsPlayedThisCombat.contains(c)) {
                addToBot(new GainBlockAction(AbstractDungeon.player, amount));
            }
        isDone = true;
    }
}
