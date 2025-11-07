package cvetmod.actions;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import cvetmod.CvetMod;
import cvetmod.monsters.AbstractCvetMonster;

public class RefreshStringCountAction extends AbstractGameAction {
    public RefreshStringCountAction() {
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        CvetMod.stringCountBeforePlay = CvetMod.stringCount;
        AbstractDungeon.player.hand.applyPowers();
        isDone = true;
    }
}
