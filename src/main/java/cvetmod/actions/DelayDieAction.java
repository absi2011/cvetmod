package cvetmod.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import cvetmod.monsters.AbstractCvetMonster;

public class DelayDieAction extends AbstractGameAction {
    public final AbstractCvetMonster m;
    float maxDuration;
    public DelayDieAction(AbstractCvetMonster m, float time) {
        actionType = ActionType.SPECIAL;
        maxDuration = duration = time;
        this.m = m;
    }

    @Override
    public void update() {
        if (duration == maxDuration) {
            CardCrawlGame.sound.play("TURNPIKE_BOOM");
            m.dieAnimation();
        }
        duration -= Gdx.graphics.getDeltaTime();
        if (duration < 0.0F) {
            m.realDie();
            isDone = true;
        }
    }
}
