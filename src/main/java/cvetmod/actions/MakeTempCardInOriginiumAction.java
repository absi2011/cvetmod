package cvetmod.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import cvetmod.cards.special.Originium;
import cvetmod.vfx.ShowCardAndAddToOriginiumEffect;

import java.util.ArrayList;
import java.util.Arrays;

public class MakeTempCardInOriginiumAction extends AbstractGameAction {
    public static final float PADDING = 25.0F * Settings.scale;
    public ArrayList<AbstractCard> cardList;
    public MakeTempCardInOriginiumAction(AbstractCard card) {
        this(new ArrayList<>(Arrays.asList(card)));
    }

    public MakeTempCardInOriginiumAction(ArrayList<AbstractCard> lst) {
        cardList = lst;
        actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (cardList.size() == 1) {
            AbstractDungeon.effectList.add(new ShowCardAndAddToOriginiumEffect(cardList.get(0), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
        } else if (cardList.size() == 2) {
            AbstractDungeon.effectList.add(new ShowCardAndAddToOriginiumEffect(cardList.get(0), Settings.WIDTH / 2.0F + PADDING + AbstractCard.IMG_WIDTH, Settings.HEIGHT / 2.0F));
            AbstractDungeon.effectList.add(new ShowCardAndAddToOriginiumEffect(cardList.get(1), Settings.WIDTH / 2.0F - (PADDING + AbstractCard.IMG_WIDTH), Settings.HEIGHT / 2.0F));
        } else if (cardList.size() == 3) {
            AbstractDungeon.effectList.add(new ShowCardAndAddToOriginiumEffect(cardList.get(0), Settings.WIDTH / 2.0F + PADDING + AbstractCard.IMG_WIDTH, Settings.HEIGHT / 2.0F));
            AbstractDungeon.effectList.add(new ShowCardAndAddToOriginiumEffect(cardList.get(1), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
            AbstractDungeon.effectList.add(new ShowCardAndAddToOriginiumEffect(cardList.get(2), Settings.WIDTH / 2.0F - (PADDING + AbstractCard.IMG_WIDTH), Settings.HEIGHT / 2.0F));
        } else {
            for (AbstractCard card : cardList) {
                AbstractDungeon.effectList.add(new ShowCardAndAddToOriginiumEffect(card, MathUtils.random(Settings.WIDTH * 0.2F, Settings.WIDTH * 0.8F), MathUtils.random(Settings.HEIGHT * 0.3F, Settings.HEIGHT * 0.7F)));
            }
        }
        isDone = true;
    }
}
