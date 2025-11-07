package cvetmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import cvetmod.actions.HaltAttackAction;
import cvetmod.patches.CvetTags;
import cvetmod.powers.ChimeraPower;

public class HaltAttack extends AbstractCvetCard {
    public static final String ID = "cvetmod:HaltAttack";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG = "cvetmod/images/cards/Chimera.png";
    public static final int COST = 2;
    public static final int SECOND_COST = 1;
    public static final int DAMAGE_AMT = 18;
    public static final int UPG_AMT = 6;
    public static final int ADD_DMG = 60;
    public static final int ADD_UPG_DMG = 15;
    public int[] extraMultiDamage;
    public HaltAttack() {
        super(ID, NAME, IMG, COST, SECOND_COST, DESCRIPTION, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        isMultiDamage = true;
        damage = baseDamage = DAMAGE_AMT;
        magicNumber = baseMagicNumber = ADD_DMG;
        tags.add(CvetTags.IS_ORIGINIUM_ARTS);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAllEnemiesAction(p, multiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        if (extraTriggered()) {
            addToBot(new HaltAttackAction(p, extraMultiDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public void applyPowers() {
        int temp = baseDamage;
        baseDamage = baseMagicNumber;
        super.applyPowers();
        magicNumber = damage;
        isMagicNumberModified = isDamageModified;
        extraMultiDamage = multiDamage;
        baseDamage = temp;
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int temp = baseDamage;
        baseDamage = baseMagicNumber;
        super.calculateCardDamage(mo);
        magicNumber = damage;
        isMagicNumberModified = isDamageModified;
        extraMultiDamage = multiDamage;
        baseDamage = temp;
        super.calculateCardDamage(mo);
    }

    @Override
    public boolean extraTriggered() {
        return OriginiumArts();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_AMT);
            upgradeMagicNumber(ADD_UPG_DMG);
            initializeDescription();
        }
    }
}
