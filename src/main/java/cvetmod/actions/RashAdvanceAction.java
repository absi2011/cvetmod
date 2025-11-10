package cvetmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.sun.org.apache.xpath.internal.operations.Or;
import cvetmod.cards.special.Originium;

public class RashAdvanceAction extends AbstractGameAction {
    public RashAdvanceAction() {
        actionType = ActionType.SPECIAL;
        duration = startDuration = Settings.ACTION_DUR_XFAST;
    }

    @Override
    public void update() {
        boolean hasOriginium = false;
        for (AbstractCard c : DrawCardAction.drawnCards)
            if (c instanceof Originium) {
                hasOriginium = true;
                break;
            }
        if (hasOriginium) {
            for (AbstractCard c : DrawCardAction.drawnCards)
                if (!(c instanceof Originium))
                    Originium.addToOriginium(AbstractDungeon.player.hand, c);
        }
        isDone = true;
    }
}
