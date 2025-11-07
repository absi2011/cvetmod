package cvetmod.actions;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import cvetmod.monsters.AbstractCvetMonster;

public class AddCardInHandAction extends AbstractGameAction {
    AbstractCard c;
    public AddCardInHandAction(AbstractCard card) {
        this.actionType = ActionType.CARD_MANIPULATION;
        UnlockTracker.markCardAsSeen(card.cardID);
        c = card;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.hand.size() + 1 > BaseMod.MAX_HAND_SIZE) {
            AbstractDungeon.player.createHandIsFullDialog();
            AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(c));
        }
        else {
            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(c));
        }

        this.isDone = true;
    }
}
