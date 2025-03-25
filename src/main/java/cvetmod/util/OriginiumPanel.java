package cvetmod.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.AbstractPanel;
import com.megacrit.cardcrawl.vfx.ExhaustPileParticle;
import cvetmod.CvetMod;
import cvetmod.cards.special.Originium;

public class OriginiumPanel extends AbstractPanel {
    private static final float COUNT_CIRCLE_W = 128.0F * Settings.scale;
    private Hitbox hb = new Hitbox(0.0F, 0.0F, 100.0F * Settings.scale, 100.0F * Settings.scale);
    public static float energyVfxTimer = 0.0F;

    public static OriginiumPileViewScreen originiumPileViewScreen = new OriginiumPileViewScreen();

    public OriginiumPanel() {
        super(Settings.WIDTH - 70.0F * Settings.scale, 300.0F * Settings.scale, Settings.WIDTH + 100.0F * Settings.scale, 300.0F * Settings.scale, 0.0F, 0.0F, null, false);
    }

    @Override
    public void updatePositions() {
        super.updatePositions();
        if (!isHidden && !Originium.originiumPile.isEmpty()) {
            hb.update();
            updateVfx();
        }

        if (hb.hovered &&
                (!AbstractDungeon.isScreenUp ||
                AbstractDungeon.screen == OriginiumPileViewScreen.ORIGINIUM_VIEW ||
                AbstractDungeon.screen == AbstractDungeon.CurrentScreen.HAND_SELECT ||
                (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.CARD_REWARD && AbstractDungeon.overlayMenu.combatPanelsShown))) {
            AbstractDungeon.overlayMenu.hoveredTip = true;
            if (InputHelper.justClickedLeft) {
                hb.clickStarted = true;
            }
        }

        if (hb.clicked && AbstractDungeon.screen == OriginiumPileViewScreen.ORIGINIUM_VIEW) {
            hb.clicked = false;
            hb.hovered = false;
            CardCrawlGame.sound.play("DECK_CLOSE");
            AbstractDungeon.closeCurrentScreen();
            return;
        }

        if (hb.clicked && AbstractDungeon.overlayMenu.combatPanelsShown && AbstractDungeon.getMonsters() != null && !AbstractDungeon.getMonsters().areMonstersDead() && !AbstractDungeon.player.isDead && !Originium.originiumPile.isEmpty()) {
            hb.clicked = false;
            hb.hovered = false;
            if (AbstractDungeon.isScreenUp) {
                if (AbstractDungeon.previousScreen == null) {
                    AbstractDungeon.previousScreen = AbstractDungeon.screen;
                }
            } else {
                AbstractDungeon.previousScreen = null;
            }
            openOriginiumPile();
        }
    }

    private void openOriginiumPile() {
        if (AbstractDungeon.player.hoveredCard != null) {
            AbstractDungeon.player.releaseCard();
        }
        AbstractDungeon.dynamicBanner.hide();
        originiumPileViewScreen.open();
        hb.hovered = false;
        InputHelper.justClickedLeft = false;
    }

    private void updateVfx() {
        energyVfxTimer -= Gdx.graphics.getDeltaTime();
        if (energyVfxTimer < 0.0F && !Settings.hideLowerElements) {
            AbstractDungeon.effectList.add(new ExhaustPileParticle(current_x, current_y));
            energyVfxTimer = 0.05F;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!Originium.originiumPile.isEmpty()) {
            hb.move(current_x, current_y);
            String msg = Integer.toString(Originium.originiumPile.size());
            sb.setColor(Settings.TWO_THIRDS_TRANSPARENT_BLACK_COLOR);
            sb.draw(ImageMaster.DECK_COUNT_CIRCLE, current_x - COUNT_CIRCLE_W / 2.0F, current_y - COUNT_CIRCLE_W / 2.0F, COUNT_CIRCLE_W, COUNT_CIRCLE_W);
            FontHelper.renderFontCentered(sb, FontHelper.turnNumFont, msg, current_x, current_y + 2.0F * Settings.scale, Settings.PURPLE_COLOR);
        }

        hb.render(sb);
        if (hb.hovered && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.isScreenUp) {
            TipHelper.renderGenericTip(1550.0F * Settings.scale, 450.0F * Settings.scale, CvetMod.ORIGINIUM_TIP_LABEL[0], CvetMod.ORIGINIUM_TIP_TEXT[0]);
        }
    }
}