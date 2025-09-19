package cvetmod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import cvetmod.cards.special.Originium;

public class ShowCardAndAddToOriginiumEffect extends AbstractGameEffect {
    private final AbstractCard card;
    public ShowCardAndAddToOriginiumEffect(AbstractCard card, float offsetX, float offsetY) {
        this.card = card;
        card.current_x = Settings.WIDTH / 2.0F;
        card.current_y = Settings.HEIGHT / 2.0F;
        card.target_x = offsetX;
        card.target_y = offsetY;
        this.duration = 1.2F;
        card.drawScale = 0.75F;
        card.targetDrawScale = 0.75F;
        card.transparency = 0.01F;
        card.targetTransparency = 1.0F;
        card.fadingOut = false;
        this.playCardObtainSfx();
        if (card.type != AbstractCard.CardType.CURSE && card.type != AbstractCard.CardType.STATUS && AbstractDungeon.player.hasPower("MasterRealityPower")) {
            card.upgrade();
        }

        Originium.originiumPile.addToTop(card);
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        this.card.update();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        if (!this.isDone) {
            this.card.render(sb);
        }
    }

    @Override
    public void dispose() {}

    private void playCardObtainSfx() {
        if (AbstractDungeon.effectList.stream()
                .filter(e -> e instanceof ShowCardAndAddToHandEffect)
                .count() < 2) {
            CardCrawlGame.sound.play("CARD_OBTAIN");
        }
    }
}
