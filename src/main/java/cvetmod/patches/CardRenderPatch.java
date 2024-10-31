package cvetmod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import cvetmod.cards.AbstractCvetCard;

public class CardRenderPatch {
    private static final Texture portraitOrb = new Texture("cvetmod/images/1024/card_cvet_orb.png");
    private static final Color ENERGY_COST_RESTRICTED_COLOR = new Color(1.0F, 0.3F, 0.3F, 1.0F);
    private static final Color ENERGY_COST_MODIFIED_COLOR = new Color(0.4F, 1.0F, 0.4F, 1.0F);
    @SpirePatch(clz = AbstractCard.class, method = "renderEnergy")
    public static class RenderPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractCard _inst, SpriteBatch sb, boolean ___darken, Color ___renderColor) {
            if (!___darken && !_inst.isLocked && _inst.isSeen && _inst instanceof AbstractCvetCard && ((AbstractCvetCard) _inst).secondCostForTurn != -2) {
                Color costColor = Color.WHITE.cpy();
                if (AbstractDungeon.player != null && AbstractDungeon.player.hand.contains(_inst) && !_inst.hasEnoughEnergy()) {
                    costColor = ENERGY_COST_RESTRICTED_COLOR;
                } else if (((AbstractCvetCard) _inst).isSecondCostModified || ((AbstractCvetCard) _inst).isSecondCostModifiedForTurn || _inst.freeToPlay()) {
                    costColor = ENERGY_COST_MODIFIED_COLOR;
                }

                costColor.a = _inst.transparency;
                String text = ((AbstractCvetCard) _inst).secondCost == -1? "X" : (_inst.freeToPlay()? "0" : Integer.toString(((AbstractCvetCard) _inst).secondCostForTurn));
                FontHelper.cardEnergyFont_L.getData().setScale(_inst.drawScale);
                BitmapFont font = FontHelper.cardEnergyFont_L;
                FontHelper.renderRotatedText(sb, font, text, _inst.current_x, _inst.current_y, 132.0F * _inst.drawScale * Settings.scale, 192.0F * _inst.drawScale * Settings.scale, _inst.angle, false, costColor);
            }
        }
    }

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderCost")
    public static class SingleRenderPatch {
        @SpirePrefixPatch
        public static SpireReturn<?> Prefix(SingleCardViewPopup _inst, SpriteBatch sb, AbstractCard ___card) {
            if (!___card.isLocked && ___card.isSeen && ___card instanceof AbstractCvetCard) {
                sb.draw(portraitOrb,
                        (float) Settings.WIDTH / 2.0F - 512.0F, (float)Settings.HEIGHT / 2.0F - 512.0F,
                        512.0F, 512.0F, 1024.0F, 1024.0F, Settings.scale, Settings.scale,
                        0.0F, 0, 0, 1024, 1024, false, false);

                Color c;
                if (___card.isCostModified) {
                    c = Settings.GREEN_TEXT_COLOR;
                } else {
                    c = Settings.CREAM_COLOR;
                }
                switch (___card.cost) {
                    case -2:
                        break;
                    case -1:
                        FontHelper.renderFont(sb, FontHelper.SCP_cardEnergyFont, "X", (float)Settings.WIDTH / 2.0F - 292.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F + 404.0F * Settings.scale, c);
                        break;
                    case 1:
                        FontHelper.renderFont(sb, FontHelper.SCP_cardEnergyFont, Integer.toString(___card.cost), (float)Settings.WIDTH / 2.0F - 284.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F + 404.0F * Settings.scale, c);
                    default:
                        FontHelper.renderFont(sb, FontHelper.SCP_cardEnergyFont, Integer.toString(___card.cost), (float)Settings.WIDTH / 2.0F - 292.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F + 404.0F * Settings.scale, c);
                        break;
                }

                if (((AbstractCvetCard) ___card).isSecondCostModified) {
                    c = Settings.GREEN_TEXT_COLOR;
                } else {
                    c = Settings.CREAM_COLOR;
                }
                switch (((AbstractCvetCard) ___card).secondCost) {
                    case -2:
                        break;
                    case -1:
                        FontHelper.renderFont(sb, FontHelper.SCP_cardEnergyFont, "X", (float)Settings.WIDTH / 2.0F + 292.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F + 404.0F * Settings.scale, c);
                        break;
                    case 1:
                        FontHelper.renderFont(sb, FontHelper.SCP_cardEnergyFont, Integer.toString(___card.cost), (float)Settings.WIDTH / 2.0F + 300.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F + 404.0F * Settings.scale, c);
                    default:
                        FontHelper.renderFont(sb, FontHelper.SCP_cardEnergyFont, Integer.toString(___card.cost), (float)Settings.WIDTH / 2.0F + 292.0F * Settings.scale, (float)Settings.HEIGHT / 2.0F + 404.0F * Settings.scale, c);
                        break;
                }
                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }
    }
}
