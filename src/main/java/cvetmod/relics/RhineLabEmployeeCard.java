package cvetmod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class RhineLabEmployeeCard extends AbstractCvetRelic {

    public static final String ID = "cvetmod:RhineLabEmployeeCard";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("cvetmod/images/relics/RhineLabEmployeeCard.png");
    public static final Texture IMG_OUTLINE = new Texture("cvetmod/images/relics/RhineLabEmployeeCard_p.png");
    public RhineLabEmployeeCard() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.BOSS, LandingSound.FLAT);
        counter = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RhineLabEmployeeCard();
    }

    @Override
    public boolean canSpawn() {
//        return AbstractDungeon.player.hasRelic(TITStudentIdCard.ID);
        return true;
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
