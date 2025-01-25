package cvetmod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import cvetmod.cards.AbstractCvetCard;
import cvetmod.characters.CivilightEterna;
import cvetmod.util.CostReserves;
import cvetmod.util.CvetEnergyManager;
import cvetmod.util.SecondCostEnergyOrb;
import javassist.CtBehavior;

import static com.megacrit.cardcrawl.core.CardCrawlGame.saveFile;

public class EnergyPatch {
    private static final Texture orbTexture = new Texture("cvetmod/images/ManaTheresa.png");
    private static final TextureAtlas.AtlasRegion secondOrb = new TextureAtlas.AtlasRegion(orbTexture, 0, 0, orbTexture.getWidth(), orbTexture.getHeight());
    @SpirePatch(clz = AbstractCard.class, method = "hasEnoughEnergy")
    public static class HasEnoughEnergyPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> Insert(AbstractCard _inst) {
            if (_inst instanceof AbstractCvetCard) {
                if ((EnergyPanel.getCurrentEnergy() >= _inst.costForTurn && CostReserves.reserveCount() >= ((AbstractCvetCard) _inst).secondCostForTurn) ||
                        _inst.freeToPlay() || _inst.isInAutoplay) {
                    return SpireReturn.Return(true);
                }
                _inst.cantUseMessage = AbstractCard.TEXT[11];
                return SpireReturn.Return(false);
            } else {
                return SpireReturn.Continue();
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.FieldAccessMatcher fieldAccessMatcher = new Matcher.FieldAccessMatcher(EnergyPanel.class, "totalCount");
                return LineFinder.findInOrder(ctBehavior, fieldAccessMatcher);
            }
        }
    }

    @SpirePatch(clz = EnergyPanel.class, method = "render")
    public static class ShowTextForReserves {
        @SpirePostfixPatch
        public static void Postfix(EnergyPanel __instance, SpriteBatch sb) {
            if (AbstractDungeon.player instanceof CivilightEterna) {
                String toShow = CostReserves.reserveCount() + "/" + ((CvetEnergyManager) AbstractDungeon.player.energy).secondEnergy;
                AbstractDungeon.player.getEnergyNumFont().getData().setScale(SecondCostEnergyOrb.fontScale);
                FontHelper.renderFontCentered(sb, AbstractDungeon.player.getEnergyNumFont(), toShow, __instance.current_x + SecondCostEnergyOrb.X_OFFSET, __instance.current_y + SecondCostEnergyOrb.Y_OFFSET, Color.WHITE);
            }
        }
    }

    @SpirePatch(clz = CardCrawlGame.class, method = "loadPlayerSave")
    public static class EnergyManagerPatch {
        @SpirePostfixPatch
        public static void Postfix(CardCrawlGame __instance, AbstractPlayer p) {
            if (p instanceof CivilightEterna) {
                p.energy = new CvetEnergyManager(saveFile.red, saveFile.red);
            }
        }
    }

    @SpirePatch(clz = FontHelper.class, method = "identifyOrb")
    public static class IdentifyOrbPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(String word) {
            if (word.equals("[cvetmod:E2]")) {
                return SpireReturn.Return(secondOrb);
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(clz = TipHelper.class, method = "renderBox")
    public static class RenderBoxPatch {
        private static final Color BASE_COLOR = new Color(1.0F, 0.9725F, 0.8745F, 1.0F);
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn<?> Insert(SpriteBatch sb, String word, float x, float y) {
            if (word.equals("[cvetmod:E2]")) {
                TipHelper.renderTipEnergy(sb, secondOrb, x + 22.0F * Settings.scale, y - 8.0F * Settings.scale);
                FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, TipHelper.capitalize(SecondCostEnergyOrb.TEXT[0]), x + 22.0F * Settings.scale * 2.5F, y + 12.0F * Settings.scale, Settings.GOLD_COLOR);
                FontHelper.renderSmartText(sb, FontHelper.tipBodyFont, GameDictionary.TEXT[0], x + 22.0F * Settings.scale, y - 20.0F * Settings.scale, 280.0F * Settings.scale, 26.0F * Settings.scale, BASE_COLOR);
                return SpireReturn.Return();
            } else if (word.equals("[E]") && AbstractDungeon.player instanceof CivilightEterna) {
                TipHelper.renderTipEnergy(sb, AbstractDungeon.player.getOrb(), x + 22.0F * Settings.scale, y - 8.0F * Settings.scale);
                FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, TipHelper.capitalize(SecondCostEnergyOrb.TEXT[2]), x + 22.0F * Settings.scale * 2.5F, y + 12.0F * Settings.scale, Settings.GOLD_COLOR);
                FontHelper.renderSmartText(sb, FontHelper.tipBodyFont, GameDictionary.TEXT[0], x + 22.0F * Settings.scale, y - 20.0F * Settings.scale, 280.0F * Settings.scale, 26.0F * Settings.scale, BASE_COLOR);
                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(String.class, "equals");
                return LineFinder.findInOrder(ctBehavior, methodCallMatcher);
            }
        }
    }
}
