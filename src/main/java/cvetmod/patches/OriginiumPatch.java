package cvetmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.shrines.FountainOfCurseRemoval;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;
import java.util.List;

public class OriginiumPatch {
    public static ExprEditor originiumEditor() { // cardID.equals("") version
        return new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException {
                if (m.getClassName().equals(String.class.getName()) && m.getMethodName().equals("equals")) {
                    m.replace("$_ = (!$0.equals(\"cvetmod:Originium\") && $proceed($$));");
                }
            }
        };
    }

    public static ExprEditor originiumEditor2() { // Object.equals(cardID, "") version
        return new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException {
                if (m.getClassName().equals(Object.class.getName()) && m.getMethodName().equals("equals")) {
                    m.replace("$_ = (!$1.equals(\"cvetmod:Originium\") && $proceed($$));");
                }
            }
        };
    }

    @SpirePatch(clz = CardGroup.class, method = "getPurgeableCards")
    public static class CardGroupGetPurgeableCardsPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return originiumEditor();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "isCursed")
    public static class AbstractPlayerIsCursedPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(AbstractPlayer _inst) {
            boolean cursed = false;
            for (AbstractCard c : _inst.masterDeck.group) {
                if (c.type == AbstractCard.CardType.CURSE && !c.cardID.equals("cvetmod:Originium") && !c.cardID.equals("Necronomicurse") && !c.cardID.equals("CurseOfTheBell") && !c.cardID.equals("AscendersBane")) {
                    cursed = true;
                    break;
                }
            }
            return SpireReturn.Return(cursed);
        }
    }

    @SpirePatch(clz = FountainOfCurseRemoval.class, method = "buttonEffect")
    public static class FountainOfCurseRemovalButtonEffectPatch {
        static EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("Fountain of Cleansing");
        static String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        static String[] OPTIONS = eventStrings.OPTIONS;
        static String DIALOG_2 = DESCRIPTIONS[1];

        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(FountainOfCurseRemoval _inst, int buttonPressed, @ByRef int[] ___screenNum) {
            if (___screenNum[0] == 0 && buttonPressed == 0) {
                _inst.imageEventText.updateBodyText(DIALOG_2);
                List<String> curses = new ArrayList<>();
                ___screenNum[0] = 1;

                for (int i = AbstractDungeon.player.masterDeck.group.size() - 1; i >= 0; --i) {
                    if ((AbstractDungeon.player.masterDeck.group.get(i)).type == AbstractCard.CardType.CURSE &&
                            !(AbstractDungeon.player.masterDeck.group.get(i)).inBottleFlame &&
                            !(AbstractDungeon.player.masterDeck.group.get(i)).inBottleLightning &&
                            !(AbstractDungeon.player.masterDeck.group.get(i)).cardID.equals("cvetmod:Originium") &&
                            !(AbstractDungeon.player.masterDeck.group.get(i)).cardID.equals("AscendersBane") &&
                            !(AbstractDungeon.player.masterDeck.group.get(i)).cardID.equals("CurseOfTheBell") &&
                            !(AbstractDungeon.player.masterDeck.group.get(i)).cardID.equals("Necronomicurse")) {
                        AbstractDungeon.effectList.add(new PurgeCardEffect(AbstractDungeon.player.masterDeck.group.get(i)));
                        curses.add((AbstractDungeon.player.masterDeck.group.get(i)).cardID);
                        AbstractDungeon.player.masterDeck.removeCard(AbstractDungeon.player.masterDeck.group.get(i));
                    }
                }

                AbstractEvent.logMetricRemoveCards("Fountain of Cleansing", "Removed Curses", curses);
                _inst.imageEventText.updateDialogOption(0, OPTIONS[1]);
                _inst.imageEventText.clearRemainingOptions();
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = CardLibrary.class, method = "getCurse", paramtypez = {})
    public static class CardLibraryGetCursePatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return originiumEditor();
        }
    }

    @SpirePatch(clz = CardLibrary.class, method = "getCurse", paramtypez = {AbstractCard.class, Random.class})
    public static class CardLibraryGetCursePatch2 {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return originiumEditor2();
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "addCurseCards")
    public static class AddCurseCardsPatch {
        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return originiumEditor2();
        }
    }
}
