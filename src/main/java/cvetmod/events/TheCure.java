package cvetmod.events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.FairyPotion;
import com.megacrit.cardcrawl.vfx.ObtainPotionEffect;

import java.util.ArrayList;
import java.util.List;

public class TheCure extends AbstractImageEvent {
    public static final String ID = "cvetmod:TheCure";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    public static final String NAME = eventStrings.NAME;
    public static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private CurScreen screen = CurScreen.INTRO;
    private enum CurScreen {
        INTRO, LEAVE
    }
    public TheCure() {
        super(NAME, DESCRIPTIONS[0], "cvetmod/images/event/TheCure.png");
        imageEventText.setDialogOption(OPTIONS[1]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch (this.screen) {
            case INTRO:
                List<String> tempList = new ArrayList<>();
                switch (buttonPressed) {
                    case 0:
                        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        this.screen = CurScreen.LEAVE;
                        AbstractPotion potion = new FairyPotion();
                        tempList.add(potion.ID);
                        logMetric(ID, "DELIBERATION", null, null, null, null, null, tempList, null, 0, 0, 0, 0, 0, 0);
                        AbstractDungeon.effectList.add(new ObtainPotionEffect(potion));
                        break;
                }
                this.imageEventText.updateDialogOption(0, OPTIONS[3]);
                this.imageEventText.clearRemainingOptions();
                return;
            default:
                openMap();
        }
        openMap();
    }
}
