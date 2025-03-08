package cvetmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import cvetmod.cards.special.Originium;

import java.util.ArrayList;

public class GetRandomCardFromOriginium extends AbstractGameAction {
    int count;

    public GetRandomCardFromOriginium(int count) {
        this.setValues(AbstractDungeon.player, AbstractDungeon.player, 0);
        this.duration = Settings.ACTION_DUR_FAST;
        this.count = count;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (count > Originium.originiumPile.size()) {
                count = Originium.originiumPile.size();
            }
            for (int i = 0; i < count; i++) {
                AbstractCard card = Originium.originiumPile.getRandomCard(true);
                Originium.originiumPile.removeCard(card);
                addToTop(new MakeTempCardInHandAction(card, true, true));
            }
        }
        this.tickDuration();
    }

}
