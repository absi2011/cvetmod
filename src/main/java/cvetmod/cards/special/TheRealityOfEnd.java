package cvetmod.cards.special;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import cvetmod.cards.AbstractCvetCard;
import cvetmod.patches.CvetTags;

public class TheRealityOfEnd extends AbstractCvetCard {
    public static final String ID = "cvetmod:TheRealityOfEnd";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/CvetStrikeA.png";
    public static final int COST = 4;
    public static final int SECOND_COST = 0;
    public static final int HP_LOSS = 20;
    public static final int UPG_LOSS = -5;
    public TheRealityOfEnd() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.SKILL, CardRarity.SPECIAL, CardTarget.ALL_ENEMY);
        color = CardColor.COLORLESS;
        magicNumber = baseMagicNumber = HP_LOSS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(p, new DamageInfo(p, magicNumber, DamageInfo.DamageType.HP_LOSS)));
        for (AbstractMonster mo: AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!mo.isDeadOrEscaped()) {
                addToBot(new InstantKillAction(mo));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_LOSS);
            initializeDescription();
        }
    }
}
