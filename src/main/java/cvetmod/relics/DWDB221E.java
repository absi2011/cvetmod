package cvetmod.relics;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class DWDB221E extends AbstractCvetRelic {

    public static final String ID = "cvetmod:DWDB221E";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("cvetmod/images/ManaAmiya.png");  // TODO
    public static final Texture IMG_OUTLINE = new Texture("cvetmod/images/ManaAmiya.png"); // TODO
    public DWDB221E() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.STARTER, LandingSound.FLAT);
        counter = 0;
    }

    @Override
    public void atBattleStartPreDraw() {
        BaseMod.MAX_HAND_SIZE = 12;
    }

    @Override
    public void onVictory() {
        BaseMod.MAX_HAND_SIZE = 10;
    }

    @Override
    public void atTurnStart() {
        addToBot(new DrawCardAction(1));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DWDB221E();
    }

    @Override
    public void obtain() {
//        if (AbstractDungeon.player.hasRelic(TITStudentIdCard.ID)) {
//            for (int i = 0; i < AbstractDungeon.player.relics.size(); i++)
//                if (AbstractDungeon.player.relics.get(i).relicId.equals(TITStudentIdCard.ID)) {
//                    instantObtain(AbstractDungeon.player, i, true);
//                    break;
//                }
//        } else {
//            super.obtain();
//        }
    }
}
