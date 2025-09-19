package cvetmod.relics;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import cvetmod.actions.MakeTempCardInOriginiumAction;
import cvetmod.cards.special.Patriot;
import cvetmod.cards.special.SanguinarchOfVampires;
import cvetmod.cards.special.SarkazCenturion;

import java.util.ArrayList;
import java.util.Arrays;

public class ProcessionOfKings extends AbstractCvetRelic {
    public static final String ID = "cvetmod:ProcessionOfKings";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("cvetmod/images/ManaAmiya.png");  // TODO
    public static final Texture IMG_OUTLINE = new Texture("cvetmod/images/ManaAmiya.png"); // TODO
    public ProcessionOfKings() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.RARE, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ProcessionOfKings();
    }

    @Override
    public void atBattleStart() {
        addToBot(new MakeTempCardInOriginiumAction(new ArrayList<>(Arrays.asList(new Patriot(), new SarkazCenturion(), new SanguinarchOfVampires()))));
    }
}
